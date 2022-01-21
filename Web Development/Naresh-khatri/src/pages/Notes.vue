<template>
  <q-page padding class="f lex flex -center">
    <div class="row" style="width: 100%;">
      <div class="col">
        <div class="float-right absolute-right-top flex column items-end">
          <q-btn
            size="lg"
            :icon="showFileUploadDrawer ? 'close' : 'add'"
            :title="showFileUploadDrawer ? 'close' : 'add'"
            round
            @click="showFileUploadDrawer = !showFileUploadDrawer"
          />
          <transition-group appear enter-active-class="animated tada">
            <q-img
              class="q-mr-lg q-mt-sm"
              v-if="!showFileUploadDrawer"
              width="50px"
              src="https://www.gstatic.com/classroom/web/home/dark_create_class_arrow.svg"
            />
            <div class="text-subtitle" v-if="!showFileUploadDrawer">Add a new note</div>
          </transition-group>
        </div>
        <q-card-section class="flex justify-center" style="margin-bottom: 100px;">
          <div v-for="(material, i) in materials" :key="i">
            <MaterialCard
              class="q-my-sm align-strech"
              :title="material.title"
              :description="material.description"
              :image="material.previewPath"
              :link="material.link"
              fileType="PDF"
              btnText="view"
              btnIcons="download"
              @onPressed="getMaterial(material._id)"
              @onDelete="deleteMaterial(material._id)"
            />
            <!-- <q-btn @click="getMaterial(material._id)" label="view" /> -->
          </div>
        </q-card-section>
      </div>
      <div class="col-3" style="width:70%; border: 2px dashed black;" v-if="showFileUploadDrawer">
        <transition-group appear enter-active-class="animated fadeIn">
          <div v-if="showFileUploadDrawer">
            <!-- input for a pdf document -->
            <!-- <input
        ref="fileInputEl"
        type="file"
        accept="application/pdf,application/vnd.ms-excel"
        @change="onFileChange"
            />-->
            <div style="width: 100%;" class="q-mt-md flex flex-center">
              <q-file
                v-model="fileToUpload"
                label="Pick a file"
                filled
                counter
                accept=".pdf, doc/*"
                style="width: 300px"
                @input="onFileChange"
              >
                <template v-slot:prepend>
                  <q-icon name="attach_file" />
                </template>
              </q-file>
            </div>
            <q-card-section v-if="fileToUpload" style="width:100%">
              <q-input class="q-my-md" label="Title" v-model="titleInput" />
              <div class="q-mt-xl text-h5 text-subtitle">Description</div>
              <q-input filled type="textarea" v-model="descInput" />
              <div class="q-mt-xl text-h5 text-subtitle">Select a thumbnail</div>
              <q-scroll-area
                style="height: 250px; border:1px dashed black"
                class="q-ma-sm flex flex-center"
              >
                <q-img
                  v-for="(img, i) in pdfImagePreviews"
                  :key="i"
                  :style="pdfImagePreviewsSelected == i ? 'border: 2px solid #2196f3;' : ''"
                  :src="img"
                  style="width:100px"
                  @click="pdfImagePreviewsSelected = i"
                >
                  <q-chip
                    v-if="pdfImagePreviewsSelected == i"
                    :label="pdfImagePreviewsSelected + 1"
                  />
                </q-img>
              </q-scroll-area>
              <q-btn icon="upload" label="Upload" style="float: right;" color="positive" @click="uploadFile" />
            </q-card-section>
          </div>
        </transition-group>
      </div>
    </div>
  </q-page>
</template>

<script setup>
import { ref, computed, onMounted, watchEffect } from "vue";
import { prefix } from '../apiConfig'
import { useQuasar } from "quasar";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { api } from "boot/axios";

import * as pdfjsLib from 'pdfjs-dist';
// import { pdfjsWorker } from 'pdfjs-dist/webpack'
import { pdfjsWorker } from 'pdfjs-dist/webpack'

import MaterialCard from "../components/MaterialCard.vue";

const store = useStore();
const router = useRouter();
const $q = useQuasar();

const user = computed(() => store.state.userData);

const materials = computed(() => store.state.materials);


const titleInput = ref("");
const descInput = ref("lorem ipsum dolor sit amet consectetur adipisicing elit. Quisquam, quidem. ");

const showFileUploadDrawer = ref(false);
const fileInputEl = ref(null);
const pdfImagePreviews = ref('');
const pdfImagePreviewsSelected = ref('');
const fileToUpload = ref(null);

onMounted(() => {
  console.log(pdfjsLib)
  store.dispatch("getMaterials", "1234");
});

const onFileChange = (e) => {
  console.log('changed')
  const inputFile = e.target.files[0];
  if (inputFile.size > 30_000_000) {
    $q.dialog({
      title: "File too large 😳",
      message: "File size should be less than 30MB",
    });
    resetFileInfo()
    return;
  }
  generatePreviews(inputFile);
  titleInput.value = inputFile.name;
  fileToUpload.value = inputFile;
};

const generatePreviews = async (pdfFile) => {
  console.log(pdfFile)
  const reader = new FileReader();
  reader.readAsArrayBuffer(pdfFile);
  const imgsArray = [];
  reader.onload = async () => {
    const buffer = reader.result;
    try {
      pdfImagePreviewsSelected.value = 0;
      pdfImagePreviews.value = await getPdfImgsArray(buffer, 10);
    }
    catch (e) {
      $q.notify({
        message: "Error: " + e.message + '😭',
        color: "negative",
      });
      resetFileInfo()
    }
  }
}
const resetFileInfo = () => {
  titleInput.value = "";
  descInput.value = "";
  fileInputEl.value = "";
  pdfImagePreviews.value = "";
  pdfImagePreviewsSelected.value = "";
  fileToUpload.value = null;
}
const getPdfImgsArray = async (pdfFile, maxCount) => {
  const pdf = await pdfjsLib.getDocument(pdfFile).promise;
  const pdfImgsArray = [];
  const pagesCount = pdf.numPages;
  maxCount = pagesCount < maxCount ? pagesCount : maxCount;
  return new Promise(async (resolve, reject) => {
    for (let i = 1; i <= maxCount; i++) {
      const page = await pdf.getPage(i);
      const viewport = page.getViewport({ scale: 1 });
      const canvas = document.createElement('canvas');
      const context = canvas.getContext('2d');
      canvas.height = viewport.height;
      canvas.width = viewport.width;
      const renderContext = {
        canvasContext: context,
        viewport: viewport
      };
      await page.render(renderContext).promise
      pdfImgsArray.push(canvas.toDataURL());
    }
    // console.log('returning', pdfImgsArray)
    resolve(pdfImgsArray)
  })
}
const uploadFile = async () => {
  //conver preview img to blob and upload
  const previewBlob = await (await fetch(pdfImagePreviews.value[pdfImagePreviewsSelected.value])).blob();
  // console.log(previewBlob)
  const formData = new FormData();
  formData.append("uid", user.value.uid);
  formData.append("classroomID", "1234");
  formData.append("title", titleInput.value);
  formData.append("description", descInput.value);
  formData.append("subject", "Data Structures and Algorithms");

  formData.append("fileName", fileToUpload.value.name);
  formData.append("file", fileToUpload.value,);
  formData.append("preview", previewBlob, fileToUpload.value.name);

  // formData.append("preview", pdfImagePreviews.value[pdfImagePreviewsSelected.value]);
  try {
    const res = await api.post("/uploads/pdf", formData);
    $q.notify({
      position: "top",
      color: "positive",
      textColor: "white",
      message: "File uploaded successfully",
    });
    store.dispatch("getMaterials", "1234");
    fileToUpload.value = ''
    showFileUploadDrawer.value = false
  }
  catch (err) {
    // console.log('could not upload file', err);
    $q.notify({
      position: "top",
      color: "negative",
      textColor: "white",
      message: "Could not upload file",
    });
  }
};

const getMaterial = async (id) => {
  window.location.href = `${prefix}/uploads/material/${id}?download=false`
  // const res = await api.get(`/uploads/material/${id}`, { responseType: "blob" });
  // console.log(res);
  // const url = window.URL.createObjectURL(new Blob([res.data]));
  // const link = document.createElement("a");
  // link.href = url;
  // link.setAttribute("download", "file.pdf"); //or any other extension
  // document.body.appendChild(link);
  // link.click();
};

const deleteMaterial = (id) => {
  console.log('deleting this shit')

  api.delete('/uploads/material/' + id)
    .then(res => {
      console.log(res)
      $q.notify({
        position: "top",
        color: "positive",
        textColor: "white",
        message: "File deleted successfully",
      });
      store.dispatch("getMaterials", "1234");
    })
    .catch(err => {
      console.log(err)
      $q.notify({
        position: "top",
        color: "negative",
        textColor: "white",
        message: "File not deleted",
      });
    })
}

</script>
<style>
.textarea {
}
</style>