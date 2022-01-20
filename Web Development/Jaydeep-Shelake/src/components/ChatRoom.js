import React from 'react';
import Chat from './Chat'
import { useAuth } from '../Context/AuthContext';
const ChatRoom = () => {
    const {userInfo}=useAuth();

  return (
    <div className="chat-area chat-page">
    <div className="user-info">
        <div className="profile">
          <img src="https://cdn-icons.flaticon.com/png/512/3177/premium/3177440.png?token=exp=1642531917~hmac=e4130c30b1c7e7defb6faca673297dc8" alt="" />
        </div>
        <div className="name">
         {userInfo?.name}
        </div>
    </div>
    <Chat/>
  </div>
  )
};

export default ChatRoom;
