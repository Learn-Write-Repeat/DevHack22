import React ,{useState}from 'react';
import ReactDOM from 'react-dom';
import InsertDriveFileIcon from '@material-ui/icons/InsertDriveFile';
import {v4 as uuidV4} from 'uuid'
import { storage ,db} from '../../firebase';
import {useAuth} from '../../Context/AuthContext'
import { ROOT_FOLDER } from '../../hooks/useFolder';
import { ProgressBar, Toast } from 'react-bootstrap';
const AddFileButton = ({currentFolder,big}) => {

    const [uploadingFile,setUploadingFiles]=useState([]);
    const {currentUser}=useAuth();

    const handleFileChange =(e)=>{
       const file= e.target.files[0];
       if(currentFolder===null || file===null) return;
       const id =uuidV4();

       setUploadingFiles(prevFiles=>[...prevFiles,{id:id,name:file.name,progress:0,error:false}])
       const filePath =
      currentFolder === ROOT_FOLDER
        ? `${currentFolder.path.join("/")}/${file.name}`
        : `${currentFolder.path.join("/")}/${currentFolder.name}/${file.name}`

        const uploadTasks = storage.ref(`/files/${currentUser.uid}/${filePath}`)
       .put(file)

       //saving files to the database
       uploadTasks.on("state_changed",snapshot=>{
         const progress = snapshot.bytesTransferred/snapshot.totalBytes
         setUploadingFiles(prevFiles=>{
             return prevFiles.map(file=>{
                 if(file.id===id){
                     return{...file,progress:progress}
                 }
                 return file
             })
         })
       },()=>{
        setUploadingFiles(prevFiles=>{
            return prevFiles.map(file=>{
                if(file.id===id){
                    return {...file,error:true}
                }
                return file
            })
        })
       },()=>{
           setUploadingFiles(prevFiles=>{
               return prevFiles.filter(file=>{
                  return file.id!==id
               })
           })
           uploadTasks.snapshot.ref.getDownloadURL().then(url=>{
               db.files.where("file","==",file.name).where("userId","==",currentUser.uid)
               .where("folderId","==",currentFolder.id)
               .get()
               .then(extitingFiles=>{
                   const extingFile=extitingFiles.docs[0]
                   if(extingFile){
                          extingFile.ref.update({url:url})
                   }
                   else{
                    db.files.add({
                        url:url,
                        file:file.name,
                        createdAt:db.getCurrentTimeStamp(),
                        folderId:currentFolder.id,
                        userId:currentUser.uid,
                        size:file.size,
                    })
                   }
               })
               
           })
       })
    }
    return (
        <>
        <label className={`${big&&'bigBtn'}`}>
            <InsertDriveFileIcon/>
           <input  type="file"
            onChange={handleFileChange}
            style={{opacity:'0',position:'absolute',left:'-999px'}}
            /> 
        </label>
        {uploadingFile.length>0 && ReactDOM.createPortal(
            <div style={{position:'absolute',bottom:'1rem',right:'1rem',maxWidth:'250px'}}>
              {uploadingFile.map(file=>(
                <Toast key={file.id} onClose={()=>{
                    setUploadingFiles(prevFiles=>{
                        return prevFiles.filter(file=>{
                            return file.id!==file.id
                        })
                    })
                    }}>
                    <Toast.Header closeButton={file.error} className="text-truncate w-100 d-block">
                        {file.name}
                    </Toast.Header>
                    <Toast.Body>
                        <ProgressBar 
                        animated={!file.error} 
                        variant={file.error ? 'danger':'primary'}
                        now={file.error ? 100:file.progress*100}
                        label={file.error?'Error':`${Math.round(file.progress*100)}%`}
                        />
                    </Toast.Body>
                </Toast>
              ))}
            </div>,
            document.body
        )}
        </>
    )
}

export default AddFileButton
