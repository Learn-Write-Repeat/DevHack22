<template>
  <q-dialog ref="dialog" @hide="onDialogHide">
    <q-card class="q-dialog-plugin">
      <q-card-section>
        <div class="text-h5 text-bold q-mb-md">Edit Profile</div>
        <q-card-actions align="right"  v-if="newProfilePic != ''">
          <!-- <q-btn flat color="black" icon="upload" @click="uploadProfilePic" /> -->
          <q-btn color="primary" icon="upload" label="upload" @click="uploadProfilePic" />
        </q-card-actions>
      </q-card-section>
      <q-card-section>
        <div class="flex flex-center" v-if="newProfilePic == ''">
          <input
            style="opacity: 0; position: absolute; height: 0.1px; width: 0.1px"
            type="file"
            id="file"
            name="profilePic"
            accept="image/*"
            @change="onFileChange"
          />
          <label
            for="file"
            style="
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: center;
              position: relative;
              height: 150px;
              width: 100%;
              border: 2px dashed black;
            "
          >
            <q-icon name="fas fa-image" color="primary" size="xl" />
            <p style="font-size: 20px" class="text-caption">Upload a profile pic 😎👌</p>
          </label>
        </div>
        <div
          v-if="result.image"
          style="width: 100%; justify-content: center"
          class="flex items-start"
          @click="removeImg"
        >
          <preview
            :width="120"
            :height="120"
            :image="result.image"
            :coordinates="result.coordinates"
          />
          <q-btn icon="fas fa-trash" round color="negative" />
        </div>
        <!-- <img :src='croppedImg' /> -->
        <cropper
          ref="cropper"
          class="cropper"
          :src="newProfilePic"
          :debounce="false"
          :stencil-props="{
            aspectRatio: 1 / 1,
          }"
          @change="onChange"
        ></cropper>
      </q-card-section>
      
    </q-card>
  </q-dialog>
</template>

<script>
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ref, computed } from "vue";
import { useQuasar } from "quasar";

import { Cropper, Preview } from "vue-advanced-cropper";
import "vue-advanced-cropper/dist/style.css";
// import axios from "axios";
import { api } from "boot/axios";

export default {
  components: {
    Cropper,
    Preview,
  },
  emits: [
    // REQUIRED
    "ok",
    "hide",
  ],
  setup(props, { emit }) {
    const $q = useQuasar();
    const store = useStore();
    const $router = useRouter();

    const dialog = ref(null);
    const newProfilePic = ref("");
    const imgName = ref("");
    const cropper = ref(null);
    const result = ref({
      image: "",
      coordinates: {
        x: 0,
        y: 0,
        width: 0,
        height: 0,
      },
    });
    const user = computed(() => store.state.userData);

    const show = () => {
      dialog.value.show();
    };
    const onChange = (e) => {
      result.value = { image: e.image, coordinates: e.coordinates };
    };
    const compressImg = (imgFile, MAX_WIDTH, MAX_HEIGHT, compressionRatio) => {
      return new Promise((resolve, reject) => {
        const canvas = document.createElement("canvas");
        const image = new Image();
        image.src = imgFile;
        image.onload = () => {
          const ctx = canvas.getContext("2d");
          ctx.drawImage(image, 0, 0);
          let width = image.width;
          let height = image.height;

          if (width > height) {
            if (width > MAX_WIDTH) {
              height *= MAX_WIDTH / width;
              width = MAX_WIDTH;
            }
          } else {
            if (height > MAX_HEIGHT) {
              width *= MAX_HEIGHT / height;
              height = MAX_HEIGHT;
            }
          }
          canvas.width = width;
          canvas.height = height;
          ctx.drawImage(image, 0, 0, width, height);
          const dataUrl = canvas.toDataURL("image/jpeg", compressionRatio);
          resolve(dataUrl);
        };
      });
    };
    const onFileChange = (e) => {
      console.log("load started");

      $q.loading.show({
        delay: 400, // ms
        message: "Uploading your image... 😎",
        boxClass: "bg-grey-2 text-grey-9",
      });
      // console.log("file changed", e.target.files[0]);
      imgName.value = e.target.files[0].name;
      const reader = new FileReader();
      reader.readAsDataURL(e.target.files[0]);
      console.log("load started");

      reader.onload = async (e) => {
        //create a canvas to resize the image
        const dataUrl = await compressImg(e.target.result, 1200, 1200, 1);

        newProfilePic.value = dataUrl;
        $q.loading.hide();
      };
    };
    const removeImg = () => {
      imgName.value = "";
      newProfilePic.value = "";
      result.value = {};
    };

    const uploadProfilePic = () => {
      const { canvas } = cropper.value.getResult();
      // console.log(canvas);
      canvas.toBlob(async (blob) => {
        console.log("blob", (blob.size / 1048576).toFixed(2) + " MB");
        const formData = new FormData();
        formData.append("uid", user.value.uid);
        formData.append("imgName", imgName.value);
        formData.append("profilePic", blob);
        // formData.append("mul")
        // console.log("sendin", formData);

        $q.loading.show({
          delay: 400, // ms
          message: "Uploading your image... 😎",
          boxClass: "bg-grey-2 text-grey-9",
        });
        try {
          const res = await api.post("/user/uploadProfilePic", formData);
          console.log(res);
          //check if first time uploading profile pic
          if (!user.value.customProfilePic) {
            console.log("new user");
            store.commit("setCustomPhotoURL", {
              newCustomProfilePic: true,
              user: user,
            });
            // store.dispatch("getUserData", user);
          }
          store.commit("addPhotoUrlParams", {});
          // store.dispatch('getUserData', user);
          $q.loading.hide();
          hide();
        } catch (err) {
          console.log(err);
        }
      });
    };

    //dialog stuff
    const hide = () => {
      dialog.value.hide();
    };
    const onDialogHide = () => {
      emit("hide");
    };
    const onOKClick = () => {
      emit("ok");
      hide();
    };
    const onCancelClick = () => {
      hide();
    };
    return {
      show,
      hide,
      user,
      dialog,
      onDialogHide,
      onOKClick,
      onCancelClick,
      onChange,
      onFileChange,
      cropper,
      uploadProfilePic,
      newProfilePic,
      result,
      removeImg,
    };
  },
};
</script>
