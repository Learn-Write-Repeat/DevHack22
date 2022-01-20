import { Button ,Modal,Form} from 'react-bootstrap'
import React ,{useState}from 'react'
import CreateNewFolderIcon from '@material-ui/icons/CreateNewFolder';
import CancelIcon from '@material-ui/icons/Cancel';
import AddCircleIcon from '@material-ui/icons/AddCircle';
import {db} from '../../firebase'
import {useAuth} from '../../Context/AuthContext';
import {ROOT_FOLDER} from '../../hooks/useFolder'
const AddFolderBtn = ({currentFolder}) => {
 
    const {currentUser}=useAuth()
     const [open,setOpen]=useState(false);
     const [folderName,setFolderName]=useState('');

    const AddNewFolder=()=>{
        setOpen(true);
    }
    const CloseModal=()=>{
        setOpen(false);
    }

    const handleSubmit=(e)=>{
        e.preventDefault();
        if(currentFolder===null) return;

        const  path =[...currentFolder.path]
        if(currentFolder!==ROOT_FOLDER){
            path.push({name:currentFolder.name,id:currentFolder.id})
        }
        //create the folder in the firebase
        db.folders.add({
            name:folderName,
            parentId:currentFolder.id,
            userId:currentUser.uid,
            path:path,
            createdAt:db.getCurrentTimeStamp(),
        })
        setFolderName('');
        CloseModal();
    }

    return (
        <>
        <Button onClick={AddNewFolder} variant="outline-success" size="sm">
            <CreateNewFolderIcon/>
         </Button>

           <Modal show={open} onHide={CloseModal}>
            <Form onSubmit={handleSubmit}>
                <Modal.Body>
                    <Form.Group>
                        <Form.Label>Folder Name</Form.Label>
                        <Form.Control type="text" 
                        required 
                        value={folderName}
                        onChange={(e)=>setFolderName(e.target.value)}
                        />
                    </Form.Group>
                </Modal.Body>

                <Modal.Footer>
                  <Button variant="danger" onClick={CloseModal}><CancelIcon/> Cancle</Button>
                  <Button variant="success" type="submit"><AddCircleIcon/> Add Folder</Button>
                </Modal.Footer>
            </Form>
           </Modal>
        </>
    )
}

export default AddFolderBtn
