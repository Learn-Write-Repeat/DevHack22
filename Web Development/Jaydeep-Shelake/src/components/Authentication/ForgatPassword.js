import React,{useState,useRef}from 'react'
import {useAuth} from '../../Context/AuthContext';
import {Card,Form,Button,Alert} from 'react-bootstrap';
import {Link, useHistory} from 'react-router-dom';
import CenterContainer from '../Styles/CenterContainer'
const ForgatPassword = () => {
    const [err,setErr]=useState('');
    const [loading ,setLoading]=useState(false);
    const [msg,setMsg]=useState('');
    const emailRef=useRef();
 
   const {updateEmail,currentUser} =useAuth();

   const history = useHistory();
 
   const handleSubmit=(e)=>{
    const promises=[];
    setErr('');
    setLoading(true);
    if(emailRef.current.value!==currentUser.email){
       promises.push(updateEmail(emailRef.current.value))
    }

    Promise.all(promises).then(()=>{
         history.push('/user');
    })
    .catch(()=>{
       setErr('Failed to upadte ')
    })
    .finally(()=>{
        setLoading(false)
    })

     e.preventDefault()
    
     setLoading(false);
   }
 
     return (
         <CenterContainer>
           <Card>
             <Card.Body>
               <h2 className="text-center mb-4">Reset Password</h2>
               {err&&<Alert variant="danger">{err}</Alert>}
               {msg&&<Alert variant="success">{msg}</Alert>}
               <Form onSubmit={handleSubmit} >
               
                 <Form.Group id="email">
                   <Form.Label>Email</Form.Label>
                   <Form.Control type="email" required ref={emailRef} ></Form.Control>
                 </Form.Group>
                 <Button disabled={loading} type="submit" className="w-100">Reset Password</Button>
               </Form>
            </Card.Body>
           </Card>

         </CenterContainer>
     )
}

export default ForgatPassword
