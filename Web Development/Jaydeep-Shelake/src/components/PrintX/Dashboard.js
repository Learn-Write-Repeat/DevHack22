import React,{useState} from 'react'
import Navbar from './Navbar'
import AddFolderBtn from '../Buttons/AddFolderBtn';
import AddFileButton from '../Buttons/AddFileButton';
import { useFolder } from '../../hooks/useFolder';
import Folder from './Folder';
import File from './File';
import { Link ,useHistory} from 'react-router-dom';
import { useAuth } from '../../Context/AuthContext';
import {useLocation, useParams} from 'react-router-dom';
import FolderBreadCrum from './FolderBreadCrum';
import {MdDashboard} from 'react-icons/md'
import {ImProfile} from 'react-icons/im'
import {FaTimes} from 'react-icons/fa'
import {AiOutlineMenu} from 'react-icons/ai'
import {FiLogOut} from 'react-icons/fi'
import '../../styles/main.css'
import '../../styles/folder.css'
import Chat from '../Chat';
import user from './user.png'
const Dashboard = () => {
     const {folderId}=useParams();
     const {state={}}=useLocation();
    const {userInfo,logout}=useAuth();
    const [err,setErr]=useState('');
    const [showSidebar,setSidebar]=useState(false)
    const history = useHistory();
    const handleLogOut =async()=>{
        try {
            await logout();
            history.push('/login')
        } catch (error) {
            setErr('Failed to logout')
        }
      }

    const {folder,childfolders,childFiles} = useFolder(folderId,state.folder);
    return (
        <>
         
         <div className='main'>
             <div className={`side-bar ${showSidebar&&'show'}`}>
             <Navbar/> 
             <div className="close" onClick={()=>setSidebar(false)}>
                <FaTimes/>
             </div>
             <div className="menu-area">
             <Link to="/"><div className="menu">
                  <MdDashboard/>
                  <p>Dashboard</p>
                 </div></Link>
                 <Link to="/updateProfile"><div className="menu">
                  <ImProfile/>
                  <p>Profile</p>
                 </div></Link>
                 <Link to="/chat"><div className="menu chat">
                  <ImProfile/>
                  <p>ChatRoom</p>
                 </div></Link>
                 
                 <div className="menu logout" onClick={handleLogOut}>
                  <FiLogOut/>
                  <p>Logout</p>
                 </div>
             </div>
             </div>
             <div className="folder-container">

             
             <div className="folder-header">
                 <h4>Folders</h4>
                 <div className="btns">
                 <AddFolderBtn currentFolder={folder}/>
               <AddFileButton currentFolder={folder}/>
                 </div>
               
             </div> 
             
             {childfolders.length > 0 && (
                 <div className="folder-area">
                     {
                         childfolders.map(ChildFolder=>{
                             return(<div key={ChildFolder.id} className="folder" >
                               <Folder folder={ChildFolder}/>
                             </div>)
                         })
                     }
                     <div className="meun-btn"  onClick={()=>setSidebar(true)}>
                        <AiOutlineMenu/>
                     </div>
                 </div>
             )}
             </div>
             {childFiles.length > 0 && (
                 <div className="files-area">
                     <div className="routes">
                  <FolderBreadCrum currentFolder={folder}/>  
                     </div>
                     <h3 style={{marginTop:'20px',color:'#3656db'}}>Files</h3>
                     <div className="all-files">
                        
                     {
                         childFiles.map(ChildFile=>(
                               <File file={ChildFile}/>
                             
                         ))
                     }
                 </div>
               
                 </div>
             )}
             <div className="chat-area">
               <div className="user-info">
                   <div className="profile">
                     <img src={user} alt="" />
                   </div>
                   <div className="name">
                    {userInfo?.name}
                   </div>
               </div>
               <Chat/>
             </div>
         </div>
         
        </>
    )
}

export default Dashboard
