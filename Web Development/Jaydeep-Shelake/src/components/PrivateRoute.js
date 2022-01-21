import React from 'react'
import {Route,Redirect} from 'react-router-dom';
import {useAuth} from '../Context/AuthContext';
const PrivateRoute = ({component:Component,...rest}) => {
    const {currentUser}=useAuth();
    // console.log(userInfo&&userInfo)
    return (
        <Route {...rest} render={props=>{
         return currentUser? <Component {...props}/>:<Redirect to="/login"/>
        }}>
        </Route>
    )
}

export default PrivateRoute
