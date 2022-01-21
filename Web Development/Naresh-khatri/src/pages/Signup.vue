<template>
  <q-page padding class="bg-dak">
    <div class="q-my-xl">
      <div class="row text-white q-mb-lg">
        <div class="col flex flex-center column">
          <q-btn flat @click="googleLogin">
            <img src="../assets/login/google.png" width="120" />
          </q-btn>
          <q-img
            class="q-mr-xl q-mt-sm"
            v-if="!showFileUploadDrawer"
            width="50px"
            src="https://www.gstatic.com/classroom/web/home/dark_create_class_arrow.svg"
          />
          <div class="text-grey text-center q-ma-sm">More options coming in future....</div>
        </div>
        <!-- <div class="col flex flex-center">
          <q-btn flat>
            <img src="../assets/login/facebook.png" width="120" />
          </q-btn>
        </div>-->
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref } from "vue";
import { useQuasar } from "quasar";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

// import axios from "axios";
import { api } from "boot/axios";

import { getAuth, signInWithPopup, GoogleAuthProvider } from "firebase/auth";

// import { registration } from "../apiendpoints.js";
export default {
  setup() {
    const $q = useQuasar();
    const store = useStore();
    const $router = useRouter();

    const firstName = ref("");
    const lastName = ref("");
    const email = ref("");
    const password = ref("");
    const googleLogin = () => {
      const provider = new GoogleAuthProvider();
      const auth = getAuth();
      signInWithPopup(auth, provider)
        .then((res) => {
          // console.log(res);
          saveUser(res);
          // This gives you a Google Access Token. You can use it to access the Google API.
          const credential = GoogleAuthProvider.credentialFromResult(res);
          // console.log(credential);
          const user = res.user;
          const userInfo = {
            uid: user.uid,
            email: user.email,
            phone: user.phoneNumber,
            name: user.displayName,
            emailVerified: user.emailVerified,
            photoURL: user.photoURL,
            metadata: user.metadata,
            reloadUserInfo: user.reloadUserInfo,
          };
          //save to vuex
          store.commit("setUserData", userInfo);
          $q.notify({
            color: "positive",
            message: "Login successful🍭",
          });
          //store userInfo obj in localstorage
          // $q.localStorage.set("loggedUser", userInfo);
          $router.push("/chat");
          //TODO: store userInfo obj in backend
        })
        .catch((error) => {
          // console.log(error);
          $q.notify({
            color: "negative",
            message: "Couldn't login 👀",
          });
        });
    };
    const saveUser = async (user) => {
      const userInfo = {
        uid: user.user.uid,
        name: user.user.displayName,
        email: user.user.email,
        emailVerified: user.user.emailVerified,
        phone: user.user.phoneNumber,
        photoURL: user.user.photoURL,
        disabled: user.user.disabled,
        providerData: user.user.providerData[0],
        reloadUserInfo: user.user.reloadUserInfo,
        metadata: user.user.metadata,
        localId: user.user.localId,
        status: "online",
        badges: [],
        interests: [],
      };
      // console.log(user);
      // console.log(userInfo);
      try {
        const res = await api.post("/user/register", userInfo);
        const uid = JSON.parse(res.config.data).uid
        //store accepts userInfo with uid to search user in db
        store.dispatch("getUserData", { uid });
        // console.log(res);
        // console.log();
      } catch (err) {
        // console.log(err);
      }
    };
    return {
      firstName,
      lastName,
      email,
      password,
      googleLogin,
    };
  },
};
</script>
<style scoped></style>
