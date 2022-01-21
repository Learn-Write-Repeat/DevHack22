import React from 'react'
import {Route,Redirect} from 'react-router-dom';
import {useAuth} from '../Context/AuthContext';
const AdminRoute = ({component:Component,...rest}) => {
    const {currentUser,userInfo}=useAuth();
    return (
        <Route {...rest} render={props=>{
         return currentUser && (userInfo?.role==="Admin" ||userInfo?.role==="Teacher")? <Component {...props}/>:<Redirect to="/"/>
        }}>
        </Route>
    )
}

export default AdminRoute