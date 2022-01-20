import React,{useState}from 'react'
import {Button,Alert} from 'react-bootstrap'
import {Link,useHistory} from 'react-router-dom'
import {useAuth} from '../Context/AuthContext';
import CenterContainer from './Styles/CenterContainer'
const Dashboard = () => {
    const [err,setErr]=useState('');
    const {logout,currentUser} =useAuth();
    const history = useHistory();
    const handleLogOut =async()=>{
      try {
          await logout();
          history.push('/login')
      } catch (error) {
          setErr('Failed to logout')
      }
    }
    return (
        <CenterContainer>
        <div>
            <h1>Dashborad</h1>
            <Alert>{err&&err}</Alert>
            <Link to='/updateProfile' className="btn btn-primary w-100 my-3">Update Profile</Link>
            <Button onClick={handleLogOut}>Logout</Button>
        </div>
        </CenterContainer>
    )
}

export default Dashboard
