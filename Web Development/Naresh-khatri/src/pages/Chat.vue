<template >
  <div class="bg-gr ey-3">
    <q-page class="flex justify-center bg-white" style="width:60%; border-radius:40px; margin: 0 auto">
      <div class="top-left-btn">
        <q-btn
          class="absolute"
          color="primary"
          style="background: white; width: 60px; height: 60px; z-index: 10"
          flat
          dense
          round
          icon="arrow_back_ios"
          aria-label="Menu"
          @click="$router.push('/')"
        />
      </div>
      <!-- <q-btn
      color="primary"
      style="
        background: white;
        width: 60px;
        height: 60px;
        z-index: 10;
        left: 200px;
      "
      flat
      dense
      round
      icon="download"
      aria-label="Menu"
      @click="scrollBottom"
      />-->
      <div
        class="date"
        style="
        width: 100vw;
        position: absolute;
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 10;
        margin-top: 15px;
      "
      >
        <transition name="slide-fade">
          <div v-if="canShowTopDate">
            <q-chip color="secondary" ref="DateBox" text-color="white" size="17px">{{ topMsgDate }}</q-chip>
          </div>
        </transition>
      </div>
      <q-scroll-area
        @scroll="showTopDate"
        ref="scrollArea"
        class="absolute-full"
        style="height: 90%"
      >
        <!--       
      <q-chat-message
        bg-color="primary"
        text-color="white"
        class="q-mx-md text-white"
        name="Server"
        :text="['hey', 'how are you?']"
        />-->
        <!-- Chat msgs -->
        <div v-for="(msg, index) in getMsgsData" :key="index">
          <div>
            <q-chat-message v-if="index === 0" :label="getDatestamp(msg.timestamp)" />
            <q-chat-message
              v-else-if="
                getDate(msg.timestamp) !=
                getDate(getMsgsData[index == 0 ? 0 : index - 1].timestamp)
              "
              :label="getDatestamp(msg.timestamp)"
            />
          </div>
          <!-- {{msg.timestamp}} -->
          <!-- {{getMsgsData[index].timestamp}} -->
          <!-- {{ getMsgsData[index].user }} -->
          <!-- {{ msg.uid }} -->
          <transition
            appear
            :enter-active-class="`animated ${isOwner(msg.user) ? 'slideInRight' : 'slideInLeft'
            } `"
            v-if="index != 0"
          >
            <!-- dont show name if last msg is from same user
            and show if date has passed-->

            <q-chat-message
              v-intersection="onChatMessageIntersection"
              :key="index"
              :data-id="index"
              :sent="isOwner(msg.user)"
              class="q-mx-sm text-white"
              :class="msg.text.length <= 2 ? 'text-h6' : 'text-h6'"
              :name="
                getMsgsData[index - 1].user != msg.user ||
                  getDate(msg.timestamp) !=
                  getDate(getMsgsData[index == 0 ? 0 : index - 1].timestamp)
                  ? `<span class='text-black'>${msg.name}</span>`
                  : ''
              "
              name-html
              :text="[msg.text]"
              :stamp="getTimestamp(msg.timestamp)"
            >
              <template v-slot:avatar>
                <img
                  @click="handleProfileShow(msg.user)"
                  style="cursor: pointer;"
                  class="q-mx-sm q-message-avatar q-message-avatar--sent"
                  :src="
                    getMsgPhotoURL(msg.user) ||
                    'https://cdn.quasar.dev/img/avatar1.jpg'
                  "
                />
              </template>
            </q-chat-message>
          </transition>
        </div>
        <!-- typing indicators -->
        <transition-group
          appear
          enter-active-class="animated slideInLeft"
          leave-active-class="animated fadeOut"
        >
          <q-chat-message
            v-for="(user, index) in getTypingUsers"
            :key="index"
            :name="user.username"
            :avatar="user.photoURL || 'https://cdn.quasar.dev/img/avatar1.jpg'"
            bg-color="amber"
          >
            <q-spinner-dots size="2rem"></q-spinner-dots>
          </q-chat-message>
        </transition-group>
        <!-- typing users: {{ getTypingUsers }} -->
      </q-scroll-area>
      <!-- Scroll bottom button -->
      <q-page-sticky position="bottom-right" :offset="[18, 88]">
        <transition appear enter-active-class="animated slideInUp">
          <q-btn
            v-if="unreadChatCount"
            @click="scrollBottom"
            round
            icon="expand_more"
            color="primary"
          >
            <q-badge rounded color="orange" floating>
              {{
                unreadChatCount
              }}
            </q-badge>
          </q-btn>
        </transition>
      </q-page-sticky>
      <!-- Text Input box -->
      <q-input
        filled
        rounded
        v-model="msgText"
        placeholder="Type a message"
        @keydown.enter="sendMsg"
        @keydown="debounceSendMsg"
        class="absolute-bottom q-ma-md"
      >
        <!-- isTyping: {{ typing }} -->
        <!-- v-for="(user, index) in getTypingUsers"
        :key="index"-->
        <!-- <template v-slot:before>
        <q-avatar>
          <img src="https://cdn.quasar.dev/img/avatar5.jpg" />
        </q-avatar>
        </template>-->

        <template v-slot:after>
          <q-btn round dense flat color="green" icon="send" @click="sendMsg" />
        </template>
      </q-input>
    </q-page>
  </div>
</template>

<script>
import io from "socket.io-client";
import { api } from 'boot/axios'
import { ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from 'vue-router'
import { QSpinnerIos, QSpinnerOrbit, useQuasar } from "quasar";

import { prefix, prefixWs } from "../apiConfig";

export default {
  setup() {
    const store = useStore();
    const router = useRouter()
    const $q = useQuasar();

    const msgText = ref("");
    const unreadChatCount = ref(3);
    const socket = ref(null);
    const topMsgDate = ref(null);
    const canShowTopDate = ref(false);
    const timer = ref(null);
    const scrollArea = ref(null);
    const chatsInView = ref([]);
    const debounce = ref(null);
    const typing = ref(false);

    const getMsgsData = computed(() => store.state.msgsData);
    const getTypingUsers = computed(() => store.state.typingUsers);
    const getUserId = computed(() => store.state.userData.uid);
    const getUsername = computed(() => store.state.userData.name);
    const getPhotoURL = computed(() => store.state.userData.photoURL);

    onMounted(() => {
      canShowTopDate.value = true;
      // socket.value = io("ws://localhost:4000", { transports: ["websocket"] });
      // socket.value = io("wss://classroomchat.plasmatch.in");
      socket.value = io(prefixWs);
      socket.value.on("connect", () => {
        console.log("conneted to ws - " + socket.value.id);
        // store.commit("updateSocket", socket.value);
        if ($q.loading.isActive) {
          $q.notify({
            message: "✅ Connected to server 😎",
            color: "primary",
            position: "top",
            timeout: 2000
          })
          $q.loading.hide()
        }
      });
      socket.value.on("disconnect", () => {
        console.log("disconneted from ws");
        $q.loading.show({
          message: 'Reconeccting to server...',
          boxClass: 'bg-grey-2 text-grey-9',
          spinner: QSpinnerOrbit,
          spinnerColor: 'primary',
        })
        // store.commit("updateSocket", null);
      });
      socket.value.on("receivePrevMsgsData", (data) => {
        // console.log(data);
        store.commit("updateMsgsData", data);
        setTimeout(() => {
          scrollBottom();
        }, 500);
      });
      socket.value.on("typing", (data) => {
        store.dispatch("updateTypingUsers", data);
        scrollBottom();
      });
      socket.value.on("receiveMsg", (data) => {
        store.dispatch("updateTypingUsers", { uid: data.user, typing: false });
        // console.log(data);
        store.commit("appendNewMsgData", data);
        unreadChatCount.value++;
        //741 is the diff btw size and pos
        const scrollDelta =
          scrollArea.value.getScroll().verticalSize -
          scrollArea.value.getScroll().verticalPosition;
        if (scrollDelta < 800) scrollBottom();
        // scrollArea.value.get
        // setTimeout(() => {
        //   scrollBottom();
        // }, 0);
        // animateScroll();
      });
    });
    const getDate = (time) => new Date(time).getDate();

    const debounceSendMsg = () => {
      const user = {
        uid: getUserId.value,
        username: getUsername.value,
        photoURL: getPhotoURL.value,
      };
      if (!typing.value) {
        console.log("emitting typing");
        socket.value.emit("typing", Object.assign(user, { typing: true }));
      }
      typing.value = true;
      clearTimeout(debounce.value);
      debounce.value = setTimeout(() => {
        typing.value = false;
        socket.value.emit("typing", Object.assign(user, { typing: false }));
        // socket.value.emit("typing", Object.assign(user, { typing: false }));
      }, 2000);
    };
    const isOwner = (uid) => {
      return uid == getUserId.value;
    };
    const getMsgPhotoURL = (uid) => {
      return prefix + "/user/photoURL/" + uid;
    };
    const handleProfileShow = async (uid) => {
      try {
        const user = await api.post('/user/userData', { uid })
        // console.log(user)
        router.push({ name: 'profile-viewer', query: { uid: uid } })
      }
      catch (err) {
        // console.log('error', err)
        $q.notify({
          message: "❌ Error fetching user data",
          color: "red",
          position: "top-right",
          timeout: 2000
        })
      }

    }
    const sendMsg = () => {
      //check login first 
      if (!getUserId.value) {
        $q.dialog({
          title: "Login Required",
          message: "Please login to send message",
          ok: "Login",
          cancel: "Cancel",
          persistent: true,
        })
          .onOk(() => {
            router.push('/signup')
          })
          .onCancel(() => {
            router.push('/')
          })
        return
      }
      if (msgText.value == "") return;
      canShowTopDate.value = !canShowTopDate.value;
      // console.log(getUserId.value);
      const payload = {
        user: getUserId.value,
        name: getUsername.value,
        photoURL: getPhotoURL.value,
        text: [msgText.value],
      };
      //server broadcasts payloads
      socket.value.emit("sendMsg", payload);
      //add iso date to payload since not getting from server
      store.commit(
        "appendNewMsgData",
        Object.assign(payload, { timestamp: new Date().toISOString() })
      );
      scrollBottom();
      msgText.value = "";
    };
    const scrollBottom = () => {
      // console.log(scrollArea.value.);
      if (!scrollArea.value) return;
      scrollArea.value.setScrollPosition(
        "vertical",
        scrollArea.value.getScroll().verticalSize,
        400
      );
      unreadChatCount.value = 0;
    };
    const animateScroll = () => {
      //when scroll is not in bottom
      if (
        // scrollArea.value.getScrollPosition() +
        getMsgsData.value.length * 71.9 <
        scrollArea.value.getScroll().verticalSize
      ) {
        unreadChatCount.value++;
        return;
      }
      // console.log(
      //   scrollArea.value.getScrollPosition() +
      //     scrollArea.value.containerHeight,
      //   scrollArea.value.scrollSize
      // );
      scrollArea.value.setScrollPosition(
        "vertical",
        scrollArea.value.scrollSize,
        400
      );
    };
    const showTopDate = () => {
      //make a disappearing logic when idle and appear when scrolled up
      //debouce method since scroll
      canShowTopDate.value = true;
      clearTimeout(timer.value);
      timer.value = setTimeout(() => {
        canShowTopDate.value = false;
      }, 1500);
    };
    const onChatMessageIntersection = (e) => {
      if (e.isIntersecting) {
        chatsInView.value.push(e.target.dataset.id);
      } else {
        chatsInView.value.splice(
          chatsInView.value.indexOf(e.target.dataset.id),
          1
        );
      }
      chatsInView.value.sort();
      const topMostMsgId = chatsInView.value[0];
      if (!getMsgsData.value[topMostMsgId]) return;
      // console.log('topMostMsgId', topMostMsgId);
      // console.log(chatsInView.value);
      // console.log(e.target);
      // DateBox.style
      const monthNames = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
      ];
      topMsgDate.value =
        new Date(getMsgsData.value[topMostMsgId].timestamp).getDate() +
        " " +
        monthNames[
        new Date(getMsgsData.value[topMostMsgId].timestamp).getMonth()
        ];

      // console.log(new Date(getMsgsData.value[e.target.dataset.id].timestamp).getDate(),
      //   monthNames[
      //     new Date(getMsgsData.value[e.target.dataset.id].timestamp).getMonth()
      //   ]
      // );
    };
    const getDatestamp = (time) => {
      const date = new Date(time);
      const monthNames = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
      ];
      return (
        date.getDate() +
        " " +
        monthNames[date.getMonth()] +
        " " +
        date.getFullYear()
      );
    };
    const getTimestamp = (time) => {
      const currentTime = Date.now();
      const unixTime = new Date(time).getTime();
      const timeDiff = currentTime - unixTime;
      const secs = Math.ceil(timeDiff / 1000);
      // console.log(new Date(unixTime).getHours())
      const mins = new Date(unixTime).getMinutes();
      const hours = new Date(unixTime).getHours();

      return `${formatNumber(hours > 12 ? hours - 12 : hours)}:${formatNumber(
        mins
      )} ${hours >= 12 ? "PM" : "AM"}`;
      // if (secs > 86400) return `${Math.ceil(secs / 86400)}d ago`;
      // else if (secs > 3600) return `${Math.ceil(secs / 3600)}h ago`;
      // else if (secs > 60) return `${Math.ceil(secs / 60)}m ago`;
      // else return `${secs}s ago`;
    };
    const formatNumber = (str) => {
      return `${String(str).length == 1 ? "0" : ""}${String(str)}`;
    };
    return {
      msgText,
      getMsgPhotoURL,
      typing,
      debounceSendMsg,
      getTypingUsers,
      unreadChatCount,
      handleProfileShow,
      socket,
      topMsgDate,
      canShowTopDate,
      getMsgsData,
      scrollArea,
      isOwner,
      sendMsg,
      scrollBottom,
      animateScroll,
      showTopDate,
      onChatMessageIntersection,
      getTimestamp,
      getDatestamp,
      formatNumber,
      getDate,
    };
  },
};
</script>
<style scoped>
.slide-fade-enter-from {
  transform: translateY(-30px);
  opacity: 0;
}
.slide-fade-enter-to {
  transform: translateY(0px);
  opacity: 1;
}
.slide-fade-leave-from {
  transform: translateY(0px);
  opacity: 1;
}
.slide-fade-leave-to {
  transform: translateY(-30px);
  opacity: 0;
}
.slide-fade-leave-active {
  transition: all 1s ease;
}
.slide-fade-enter-active {
  transition: all 1s ease;
}
.top-left-btn {
  position: fixed;
  color: white;
  z-index: 10;
  top: 25px;
  left: 25px;
}
</style>
