import React,{useState,useRef} from 'react'
import {useAuth} from '../../Context/AuthContext';
import {Link} from 'react-router-dom'
import {Card,Form,Button,Alert} from 'react-bootstrap';
import CenterContainer from '../Styles/CenterContainer';
const UpdateProfile = () => {
    const [err,setErr]=useState('');
    const [loading ,setLoading]=useState(false);
    const emailRef=useRef();
    const nameref=useRef()
    const {signup,currentUser,userInfo}=useAuth();
 
  const handleSubmit=async(e)=>{
    e.preventDefault()
    try{
      setLoading(true)
      setErr('');
      await signup(emailRef.current.value);
    }
    catch{
     setErr('Failed to create account');
    }
    setLoading(false);
  }

    return (
        <CenterContainer>
          <Card>
            <Card.Body>
              <h2 className="text-center mb-4">Update Profile</h2>
              {err&&<Alert variant="danger">{err}</Alert>}
              <Form onSubmit={handleSubmit} >
              <Form.Group id="name">
                  <Form.Label>Name</Form.Label>
                  <Form.Control type="text" required ref={nameref} defaultValue={userInfo?.name} ></Form.Control>
                </Form.Group>
             
                <Form.Group id="email">
                  <Form.Label>Email</Form.Label>
                  <Form.Control type="email" required ref={emailRef} defaultValue={currentUser?.email} ></Form.Control>
                </Form.Group>
                <Button disabled={loading} type="submit" className="w-100">Upadte Profile</Button>
              </Form>
            </Card.Body>
          </Card>
          <div className="w-100 text-center mt-2">
            <Link to="/user">Cancel</Link>
            </div>
        </CenterContainer>
    )
}

export default UpdateProfile
