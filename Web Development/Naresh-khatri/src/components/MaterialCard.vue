<template>
  <transition appear enter-active-class="animated  slideInUp" leave-active-class="animated fadeOut">
    <div class="row inline card bg-dark shadow-15 q-ma-md" style="width: 350px;">
      <transition
        appear
        enter-active-class="animated backInRight"
        leave-active-class="animated fadeOut"
      >
        <div class="col-8 flex">
          <div class="text-bold line-clamp-3" style="font-size: 16px;">{{ title }}</div>
          <div class="text-subtitle text-grey" style="font-size: 12px">{{ description }}</div>
          <div class="q-my-md">
            <div v-if="link">
              <router-link :to="link">
                <q-btn :label="btnText" flat rounded color="black" style="background: white" />
              </router-link>
            </div>
            <div v-else>
              <q-btn
                :label="btnText"
                flat
                rounded
                color="black"
                style="background: white"
                @click="handleBtnPress"
              />
            </div>
          </div>
        </div>
      </transition>
      <div
        class="col-4"
        style="
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          align-items: flex-end;
        "
      >
        <transition-group
          appear
          enter-active-class="animated backInLeft"
          leave-active-class="animated fadeOut"
          :duration="5000"
        >
          <q-icon key="1" class="z-index:10" name="fas fa-trash" @click="emitDelete" />
          <!-- <div class="text-blue">{{imgsrc}} </div> -->
          <q-img key="2" class="q-mt-md preview-img" :src="prefix + '/' + image">
            <q-icon
              style="position:absolute; right:0; bottom:0"
              name="far fa-file-pdf"
              color="red"
              size="lg"
            />
          </q-img>
        </transition-group>
        <!-- <q-chip :label="fileType" size="sm" color="red" text-color="white" /> -->
      </div>
    </div>
  </transition>
</template>

<script setup>
import { useQuasar } from 'quasar';
import { prefix } from '../apiConfig.js'

const $q = useQuasar();

const props = defineProps({
  image: {
    type: String,
    default: "",
  },
  title: {
    type: String,
    default: "",
  },
  description: {
    type: String,
    default: "",
  },
  btnText: {
    type: String,
    default: "",
  },
  btnIcon: {
    type: String,
    default: "",
  },
  link: {
    type: String,
  },
  fileType: {
    type: String,
    default: "",
  }
});
// console.log(prefix)
// console.log(props.image)
const imgsrc = prefix + props.image

const emit = defineEmits(["onDelete", "onPressed"])

const emitDelete = async (id) => {
  try {
    $q.dialog({
      title: 'Delete this file?',
      message: 'Are you sure you want to delete this file?',
      color: 'negative',
      cancel: {
        label: 'Cancel',
        flat: true,
        color: 'primary',
      },
      ok: {
        label: 'Delete',
        color: 'negative',
      },
      style: 'border-radius:25px 25px 0 0;',
      position: 'bottom',
    })
      .onOk(() => {
        emit('onDelete', id)
      })
      .onCancel(() => {
        console.log('cancelled')
      })

  } catch (error) {
    console.log(error);
  }
};
const handleBtnPress = () => {
  emit('onPressed')
}

</script>

<style>
a {
  text-decoration: none;
}
.card {
  color: #fff;
  padding: 20px;
  border-radius: 25px;
}
/* .vignette {
  position: relative;
  display: inline-block;
}
.vignette > img {
  display: block;
}
.preview-img::after {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  box-shadow: inset 0 0 100px rgba(0, 0, 0, 0.5);

  z-index: 10;
} */
.preview-img {
  width: 100%;
  border-radius: 15px;
  box-shadow: inset 0 0 60px rgba(0, 0, 0, 1);
}
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
