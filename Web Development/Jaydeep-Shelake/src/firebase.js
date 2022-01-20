import firebase from 'firebase/app';
import "firebase/auth";
import "firebase/firebase-firestore";
import "firebase/firebase-storage"
const app = firebase.initializeApp({
  apiKey: "AIzaSyB-z6Caiet3o2GecU5jNEwrG8GtOb7kfB8",
  authDomain: "auth-83ed4.firebaseapp.com",
  projectId: "auth-83ed4",
  storageBucket: "auth-83ed4.appspot.com",
  messagingSenderId: "340870157640",
  appId: "1:340870157640:web:5912ace7d546d1a7a6cf7d"
})
export const auth = app.auth();
export const storage = app.storage();
export const firestore = app.firestore();
export const db={
  folders:firestore.collection('folders'),
  files:firestore.collection('files'),
  users:firestore.collection('users'),
  chat:firestore.collection('chats'),
  formatedDoc:doc=>{
    return{id:doc.id,...doc.data()}
  },
  getCurrentTimeStamp:firebase.firestore.FieldValue.serverTimestamp,
}
export default app;

//firebase production mode
/*rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if false;
    }
  }
} */