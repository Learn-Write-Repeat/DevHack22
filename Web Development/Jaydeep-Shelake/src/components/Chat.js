import React,{useState} from 'react'
import {db} from '../firebase'
import { useAuth } from '../Context/AuthContext'
import {useCollectionData} from 'react-firebase-hooks/firestore'
const Chat = () => {
    const query =db.chat.orderBy('createdAt').limit(25);
     const [message]=useCollectionData(query,{idField:'id'});
    const {currentUser,userInfo}=useAuth();
     const [value,setValue]=useState('')
  console.log(message)
     const sendMsg=async(e)=>{
           e.preventDefault();
           await db.chat.add({
               name:userInfo.name,
               userId:currentUser.uid,
               uid:currentUser.uid,
               text:value,
               createdAt:db.getCurrentTimeStamp()
           })
           setValue('')
     }
    return (
        <div className='chat'>
            <div className="chat-msg-area">
              {message&&message.map(msg=>(
                  <>
                  <div title={`sent by:${msg.name} at:${msg.createdAt?.toDate()}`} className={`msg-2 ${currentUser.uid===msg.userId?'send':'rechive'}`} key={msg.id} >
                      {msg.text}
                     
                  </div>
                  {/* <span>{msg.name}</span> */}
                  </>
              ))}
            </div>
            <div className="input">
                <form onSubmit={sendMsg} style={{width:'100%'}}>
                <input type="text" value={value} onChange={(e)=>setValue(e.target.value)} />
                <button type="submit" className='send-btn'>Send</button>
                </form>
            </div>
            
        </div>
    )
}

export default Chat
