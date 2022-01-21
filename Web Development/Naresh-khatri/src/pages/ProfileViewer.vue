<template>
  <q-page class="bg-white q-pa-md q-mb-md flex column items-center">
    <div style="width:1000px">
      <q-card-section>
        <div class="text-h4 text-bold">Profile</div>
        <div class="row" style="margin-top: 100px">
          <div class="col-3">
            <div>
              <q-img
                class="shadow-10"
                :src="user.photoURL"
                style="border-radius: 50%; width: 100%"
              />
            </div>
          </div>
          <div class="col-7">
            <div class="text-white q-pa-md">
              <div class="text-black text-bold text-h5">{{ user.name }}</div>
              <div class="text-grey-7 text-subtitle" v-if="user">{{ generateSecretEmail(user.email)}}</div>
            </div>
            <q-chip
              :label="statuses[0].text"
              :color="statuses[0].color"
              :text-color="statuses[0].textColor"
              size="17px"
              class="q-ml-lg shadow-10"
              style="opacity: 0.6; cursor: pointer;"
              :style="`background: ${statuses[0].color}; color: ${statuses[0].textColor}`"
            />
          </div>
        </div>
      </q-card-section>

      <q-card-section class="q-pt-xl">
        <q-scroll-area style="height: 60px; max-width: 90vw; white-space: nowrap"></q-scroll-area>
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
          :name="site.icon"
          size="xl"
          class="q-mr-sm q-pa-sm q-mt-lg"
        >
          <q-badge rounded color="negative" floating label="❌" />
        </q-icon>
      </q-card-section>
    </div>
  </q-page>
</template>

<script setup>
import { api } from "boot/axios";

import socialSites from "../data/socialSites";
import statusesJSON from "../data/profileStatuses";
import interestsJSON from "../data/profileInterests";

import { ref, computed, onMounted, watchEffect } from "vue";
import { useRoute } from "vue-router";
import { useQuasar } from "quasar";

const route = useRoute()
const $q = useQuasar();

const interests = ref(interestsJSON);
const statuses = ref(statusesJSON);

const getSelectedStatus = computed(() => {
  const status = statuses.value.filter(status => status.id == 'online');
  // console.log(statuses.value)
  // console.log(status)
  return status
})

const user = ref('')

const generateSecretEmail = (email) => {
  const emailParts = email.split('@')
  const emailName = emailParts[0].substring(0, 3)
  const emailDomain = emailParts[1]
  return `${emailName}*****@${emailDomain}`
}
onMounted(async () => {
  // console.log('route', route.query.uid);
  getUserData(route.query.uid);
});

const getRandomInterests = computed(() => {
  const interestsCopy = interests;
  return interestsCopy.value.sort(() => 0.5 - Math.random()).slice(0, 4);
});
const getUserData = (uid) => {
  api.post('/user/userData', { uid }).then(res => {
    // console.log('res', res);
    user.value = res.data;
    // console.log('user', user.value);

  });
}
</script>

<style></style>
