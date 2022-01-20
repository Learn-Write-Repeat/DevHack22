import { useReducer , useEffect} from "react"
import { db } from "../firebase"
import {useAuth} from '../Context/AuthContext';



const ACTIONS={
   SELECT_FOLDER:'select-folder',
   UPDATE_FOLDER:'update-folder',
   SET_CHILD_FOLDER:'set-child-folder',
   SET_CHILD_FILE:'set-child-file'

}

export const ROOT_FOLDER={name:'Root',id:null,path:[]}

const reducer=(state,{type,payload})=>{
  switch(type){
      case  ACTIONS.SELECT_FOLDER:
          return {folderId:payload.folderId,folder:payload.folder,childFiles:[],childfolders:[]}
      case ACTIONS.UPDATE_FOLDER:
          return {
              ...state,
              folder:payload.folder
          }    
      case ACTIONS.SET_CHILD_FOLDER:
          return {
              ...state,
              childfolders:payload.childfolders
          }
      case ACTIONS.SET_CHILD_FILE:
          return{
              ...state,
              childFiles:payload.childFiles
          }        
      default:
          return state    
  }
}

export const useFolder=(folderId=null,folder=null)=>{
const [state,dispatch]=useReducer(reducer,{
    folderId,
    folder,
    childfolders:[],
    childFiles:[]
});

const {currentUser}=useAuth();

useEffect(()=>{
 dispatch({type:ACTIONS.SELECT_FOLDER,payload:{folderId:folderId,folder:folder}})
},[folderId,folder])

useEffect(()=>{
if(folderId===null){
    return dispatch({
        type:ACTIONS.UPDATE_FOLDER,
        payload:{folder:ROOT_FOLDER}
    })
}
    db.folders.doc(folderId).get().then(doc=>{
        dispatch({
            type:ACTIONS.UPDATE_FOLDER,
            payload:{folder:db.formatedDoc(doc)}
        })
        console.log(db.formatedDoc(doc))
    })
    .catch((e)=>{
        console.error(e)
        dispatch({
            type:ACTIONS.UPDATE_FOLDER,
            payload:{folder:ROOT_FOLDER},
        })
    
    })

},[folderId])

useEffect(()=>{
 return db.folders.where("parentId","==",folderId)
 
 .onSnapshot(snapshot=>{
     dispatch({
         type: ACTIONS.SET_CHILD_FOLDER,
         payload:{childfolders:snapshot.docs.map(db.formatedDoc)}
     })
 })
},[folderId,currentUser]);

useEffect(()=>{
 return db.files.where("folderId","==",folderId)
 
 .onSnapshot(snapshot=>{
     dispatch({
         type: ACTIONS.SET_CHILD_FILE,
         payload:{childFiles:snapshot.docs.map(db.formatedDoc)}
     })
 })
 
},[folderId,currentUser]);
return state
}