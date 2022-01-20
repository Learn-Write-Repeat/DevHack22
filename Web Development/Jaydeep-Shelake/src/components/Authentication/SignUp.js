import React,{useState,useRef} from 'react'
import {useAuth} from '../../Context/AuthContext';
import {Card,Form,Button,Alert} from 'react-bootstrap';
import CenterContainer from '../Styles/CenterContainer'
import {useHistory} from 'react-router-dom';
import {auth} from '../../firebase'
// import 'bootstrap/dist/css/bootstrap.min.css';
const SignUp = () => {

   const [err,setErr]=useState('');
   const [loading ,setLoading]=useState(false);
   const [role,setRole]=useState('Student');

  const emailRef=useRef();
  const passwordRef=useRef();
  const nameRef=useRef();
  const {signup,userInfo} =useAuth();
  const history = useHistory();
  const handleSubmit=async(e)=>{
    e.preventDefault()
    try{
      setLoading(true)
      setErr('');
      await signup(emailRef.current.value,passwordRef.current.value,nameRef.current.value,role);
      history.push('/');
      
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
              <h2 className="text-center mb-4">Sign up</h2>
              {/* {JSON.stringify(currentUser?.email)} */}
              {err&&<Alert variant="danger">{err}</Alert>}
              <Form onSubmit={handleSubmit} >
              <Form.Group id="name">
                  <Form.Label>Name</Form.Label>
                  <Form.Control type="text" required ref={nameRef} ></Form.Control>
                </Form.Group>
                <Form.Group id="email">
                  <Form.Label>Email</Form.Label>
                  <Form.Control type="email" required ref={emailRef} ></Form.Control>
                </Form.Group>
                 { userInfo && userInfo?.role==="Admin"&&(
                <Form.Group id="select">
                <Form.Label>Role</Form.Label>
                   <Form.Control  as="select" onChange={(e)=>setRole(e.target.value)}>
                        <option>Student</option>
                        <option>Teacher</option>
                     </Form.Control>
                </Form.Group>
                 )
                  }
                <Form.Group id="password">
                  <Form.Label>Password</Form.Label>
                  <Form.Control type="password" required ref={passwordRef} ></Form.Control>
                </Form.Group>
                <Button disabled={loading} type="submit" className="w-100">Signup</Button>
              </Form>
            </Card.Body>
          </Card>
        </CenterContainer>
    )
}

export default SignUp;
