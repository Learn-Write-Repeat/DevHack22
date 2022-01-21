import { store } from 'quasar/wrappers'
import { createStore } from 'vuex'

import axios from 'axios'
import { api } from 'boot/axios'
import { prefix } from '../apiConfig'

// import example from './module-example'

/*
 * If not building with SSR mode, you can
 * directly export the Store instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Store instance.
 */

export default store(function (/* { ssrContext } */) {
  const Store = createStore({
    state() {
      return {
        msgsData: [],
        userData: {
          id: '',
          name: '',
          email: '',
          photoURL: '',
          status: 'online',
          rollNo: '19fh1a0546',
          regulation: 'R19',
          year: 'III',
          sem: 'I',
        },
        tab: 'home',
        socket: null,
        typingUsers: [
        ],
      }
    },
    mutations: {
      setUserStatus(state, status) {
        console.log('setUserStatus', status)
        state.userData.status = status
      },
      setUserData(state, payload) {
        // console.log('setUserData', payload)
        state.userData = payload
        localStorage.setItem('userData', JSON.stringify(state.userData))
      },
      setCustomPhotoURL(state, payload) {
        //gets called when user uploads a profile photo for the first
        state.userData.customProfilePic = payload.newCustomPhotoURL
        state.userData.photoURL = prefix + '/user/photoURL/' + state.userData.uid
      },
      addPhotoUrlParams(state, payload) {
        //force reload <img/> by adding params to src
        state.userData.photoURL += '?'
      },
      updateMsgsData(state, payload) {
        state.msgsData = payload
      },
      appendNewMsgData(state, payload) {
        state.msgsData.push(payload)
      },
      updateSocket(state, payload) {
        state.socket = payload
      },
      updateTab(state, payload) {
        // console.log("go home", payload);
        state.tab = payload
      },
      addToTypingUsers(state, payload) {
        state.typingUsers.push(payload)
      },
      removeFromTypingUsers(state, payload) {
        state.typingUsers = state.typingUsers.filter(user => user.id != payload.id)
      },
      setMaterials(state, payload) {
        state.materials = payload
      }
    },
    actions: {
      getUserData({ commit, state }, user) {
        // console.log('getuserdata', user)
        //to get extra user data from my db
        api.post('/user/userData', { uid: user.uid })
          .then(res => {
            if (res.data.customProfilePic)
              res.data.photoURL = prefix + '/user/photoURL/' + res.data.uid
            commit('setUserData', res.data)
          })
      },
      updateTypingUsers({ commit }, payload) {
        // console.log("updateTypingUsers", payload);
        if (payload.typing == true) {
          commit('addToTypingUsers', payload)
          //timeout to remove user from typingUsers
          setTimeout(() => {
            commit('removeFromTypingUsers', payload)
          }, 50000)
        }
        else
          commit('removeFromTypingUsers', payload)
      },
      getMaterials({ commit }, classroomID) {
        api.get('/uploads/materials/' + classroomID)
          .then(res => {
            commit('setMaterials', res.data)
            // console.log(res.data)
          })
          .catch(err => {
            // console.log('couldnt get the materials', err)
          })
      }
    },
    modules: {
      // example
    },

    // enable strict mode (adds overhead!)
    // for dev mode and --debug builds only
    strict: process.env.DEBUGGING
  })

  return Store
})
