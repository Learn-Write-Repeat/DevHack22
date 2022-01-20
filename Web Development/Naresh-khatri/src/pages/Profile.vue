<template>
  <q-page v-if="user" class="bg-white q-pa-md q-mb-md flex column items-center">
    <div style="width:750px">
      <div class="text-h4 text-bold">Profile</div>
      <q-card-section>
        <q-img
          class="shadow-10"
          :src="user.photoURL"
          style="border-radius: 50%; width: 200px; height: 200px"
        />
        <q-btn class="q-ml-xl" label="change" @click="editProfile" />
        <!-- <q-btn class="q-ml-xl" label="remove" /> -->
      </q-card-section>

      <div class="q-mt-xl text-h4 text-bold">Personal Stuff</div>
      <q-card-section class="q-pt-xl">
        <div class="text-grey-7 text-subtitle">My status</div>
        <q-scroll-area style="height: 60px; max-width: 90vw; white-space: nowrap">
          <span @click="setActiveStatus(status.id)" v-for="(status, i) in statuses" :key="i">
            <transition appear enter-active-class="animated bounceIn">
              <q-chip
                v-if="status.id == user.status"
                :label="status.text"
                :color="status.color"
                :text-color="status.textColor"
                size="17px"
                :style="{ background: status.color, color: status.textColor }"
                class="q-mr-sm"
                :class="user.status == status.id ? 'shadow-10' : 'disabled'"
              />
            </transition>
          </span>
          <span @click="setActiveStatus(status.id)" v-for="(status, i) in statuses" :key="i">
            <q-chip
              v-if="status.id !== user.status"
              :label="status.text"
              :color="status.color"
              :text-color="status.textColor"
              size="17px"
              class="q-mr-sm shadow-10"
              style="opacity: 0.6; cursor: pointer;"
              :style="`background: ${status.color}; color: ${status.textColor}`"
            />
          </span>
        </q-scroll-area>
        <div class="text-grey-7 text-subtitle">My interests</div>
        <div class="q-gutter-xs text-black">
          <q-chip
            v-for="(interest, i) in getRandomInterests"
            :key="i"
            :label="interest.text"
            :color="interest.color"
            :text-color="interest.textColor"
            size="17px"
            :style="{ background: interest.color, color: interest.textColor }"
            class="q-mr-sm shadow-10"
          />
        </div>
      </q-card-section>
      <div class="q-mt-xl text-h4 text-bold">Social accouts</div>
      <q-card-section class="q-pt-lg">
        <q-icon
          v-for="(site, i) in socialSites"
          :key="i"
          style="opacity: 1;"
          :style="`color: ${site.color};opacity: ${editSocialSiteName &&
          editSocialSiteName == site.name ? 1 : 0.5}`"
          :name="site.icon"
          size="xl"
          class="q-mr-sm q-pa-sm q-mt-lg"
          @click="editSocialSiteName = site.name"
        >
          <q-badge rounded color="negative" floating label="❌" />
        </q-icon>
        <span class="text-subtitle" v-if="!editSocialSiteName">click to add</span>
        <transition
          appear
          enter-active-class="animated zoomInDown"
          enter-leave-class="animated zoomOutDown"
        >
          <div v-if="editSocialSiteName">
            <q-input
              class="q-ma-md"
              v-model="newSocialSiteName"
              :label="`Add ${editSocialSiteName} profile URL`"
            >
              <template v-slot:append>
                <q-icon
                  v-if="newSocialSiteName !== ''"
                  name="close"
                  @click="editSocialSiteName = ''"
                  class="cursor-pointer"
                />
              </template>
            </q-input>
            <q-btn style="float: right" class="q-my-sm" label="Save" @click="addSocialMedia" />
          </div>
        </transition>
      </q-card-section>
      <div class="q-mt-xl text-h4 text-bold">General</div>
      <q-card-section style="margin-bottom:400px">
        <div class="row">
          <q-input class="col q-mr-md q-my-xl" label="Full name" v-model="userName" />
          <!-- <q-input class="col q-ml-md q-my-sm" label="Last name" v-model="lastName" /> -->
        </div>
        <q-input label="Email" class="q-my-sm" disable v-model="user.email" />
        <q-btn
          style="float: right"
          v-if="user.name != userName"
          class="q-my-sm"
          label="Save"
          @click="changeUserName"
        />
      </q-card-section>
    </div>
  </q-page>
</template>

<script setup>
import axios from "axios";
import { api } from "boot/axios";


import ProfileEdit from "../components/ProfileEdit.vue";

import statusesJSON from "../data/profileStatuses";
import interestsJSON from "../data/profileInterests";
import socialSites from "../data/socialSites";

import { ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import { useQuasar } from "quasar";

const store = useStore();
const $q = useQuasar();

const statuses = ref(statusesJSON);
const interests = ref(interestsJSON);
const activeStatus = ref("online");


const editSocialSiteName = ref('');
const newSocialSiteName = ref('https://');

// const badges = ref([
//   require("../assets/badges/new.svg"),
//   require("../assets/badges/best.svg"),
//   require("../assets/badges/diamond.svg"),
//   require("../assets/badges/fast.svg"),
//   require("../assets/badges/heart.svg"),
//   require("../assets/badges/mostLiked.svg"),
//   require("../assets/badges/verified.svg"),
// ]);

// const statuses = store.state.profileStatuses;
// const interests = store.state.profileInterests;

const userName = ref('John');
const user = computed(() => store.state.userData);
setTimeout(() => {
  userName.value = user.value.name
}, 2000);
// const photoURL = computed(()=> store.state.userData.photoURL);
// const photoURL = ref("");
// photoURL.value = "http://localhost:4000/user/getPhotoURL/ipZVQgDR5Wdixl5EfR48I6rPtkG3"
// photoURL.value = computed(() =>
// store.state.userData.customProfilePic
//   ? "http://localhost:4000/user/getPhotoURL/ipZVQgDR5Wdixl5EfR48I6rPtkG3"
//   : store.state.userData.photoURL
// );

const getRandomInterests = computed(() => {
  const interestsCopy = interests;
  return interestsCopy.value.sort(() => 0.5 - Math.random()).slice(0, 4);
});

onMounted(async () => {
  // setInterval(() => {
  // photoURL.value += "?";
  // console.log("updating profile pic", photoURL.value);
  // }, 1000);
  try {
    setTimeout(() => {
    }, 0);
    // const res = await axios.get(
    //   "https://jntua.plasmatch.in/singleResultv2/19fh1a0546/R19/B.TECH/II/II"
    // );
  } catch (e) {
    console.log(e);
  }
});
const editProfile = () => {
  $q.dialog({
    component: ProfileEdit,
  })
    .onOk(() => {
      // console.log('OK')
    })
    .onCancel(() => {
      // console.log('Cancel')
    })
    .onDismiss(() => {
      // console.log('I am triggered on both OK and Cancel')
    });
  // $q.dialog({
  //   title: "Edit Profile",
  //   component: "edit-profile",
  //   width: "500px",
  // });
};
const addSocialMedia = () => {
  // console.log('add social media', editSocialSiteName.value, newSocialSiteName.value);
  $q.notify({
    color: "positive",
    textColor: "white",
    position: "top-right",
    message: `Added ${editSocialSiteName.value}`,
  });

  // api.patch('/user/addSocialLink',{
  //   uid: user.value.uid,
  //   siteId: editSocialSiteName.value,
  //   link: newSocialSiteName.value
  // })
  // .then(res => {
  //   console.log(res);
  // })
  // .catch(err => {
  //   console.log(err);
  // })

}

const changeUserName = () => {
  // console.log("changing user name", userName.value);
  api.put("/user/changeUserName", {
    uid: user.value.uid,
    newName: userName.value
  }).then(res => {
    // console.log("user name changed", res.data);
  }).catch(err => {
    // console.log("error changing user name", err);
  })
}

const setActiveStatus = (id) => {
  console.log("setting");
  activeStatus.value = id;
  store.commit("setUserStatus", id);
  api
    .post("/user/changeStatus", {
      status: id,
      uid: user.value.uid,
    })
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
};
const editInterests = () => {
  $q.dialog({
    title: "Edit Interests",
    message: "This feature is not implemented yet 😢",
    width: "500px",
  });
};
</script>

<style></style>
