import React,{useState,useRef} from 'react'
import {useAuth} from '../../Context/AuthContext';
import {Card,Form,Button,Alert} from 'react-bootstrap';
import {Link,useHistory} from 'react-router-dom';
import CenterContainer from '../Styles/CenterContainer'
import 'bootstrap/dist/css/bootstrap.min.css';
const Login = () => {
    const [err,setErr]=useState('');
    const [loading ,setLoading]=useState(false);
    const history = useHistory();
   const emailRef=useRef();
   const passwordRef=useRef();
 
   const {login,currentUser} =useAuth();
 
   const handleSubmit=async(e)=>{
     e.preventDefault()
     try{
       setLoading(true)
       setErr('');
       await login(emailRef.current.value,passwordRef.current.value);
       console.log(currentUser&&currentUser);
       history.push('/');
     }
     catch{
      setErr('Failed to log in');
     }
     setLoading(false);
   }
 
     return (
         <CenterContainer>
           <Card>
             <Card.Body>
               <h2 className="text-center mb-4">LogIn</h2>
               {err&&<Alert variant="danger">{err}</Alert>}
               <Form onSubmit={handleSubmit} >
               
                 <Form.Group id="email">
                   <Form.Label>Email</Form.Label>
                   <Form.Control type="email" required ref={emailRef} ></Form.Control>
                 </Form.Group>
                 <Form.Group id="password">
                   <Form.Label>Password</Form.Label>
                   <Form.Control type="password" required ref={passwordRef} ></Form.Control>
                 </Form.Group>
                 <Button disabled={loading} type="submit" className="w-100">Login</Button>
               </Form>
               <div className="w-100 text-center mt-3"><Link to="/resetPassword">Forgat Password?</Link></div>
             </Card.Body>
           </Card>
           <div className="w-100 text-center mt-2">
               new User ? <Link to="/signup">create an account</Link>
             </div>
         </CenterContainer>
     )
}

export default Login
