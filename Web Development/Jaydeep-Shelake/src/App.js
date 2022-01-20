import React from 'react';
import SignUp from './Components/Authentication/SignUp';
import {BrowserRouter as Router, Route,Switch} from 'react-router-dom';
import Dashboard from './Components/Dashboard';
import Login from './Components/Authentication/Login';
import PrivateRoute from './Components/PrivateRoute';
import ForgatPassword from './Components/Authentication/ForgatPassword';
import UpdateProfile from './Components/Authentication/UpdateProfile';
import MianDashboard from './Components/PrintX/Dashboard';
import {Authprovider} from './Context/AuthContext';
import './styles/index.css'
import ChatRoom from './Components/ChatRoom';
const App = () => {
    return (
              <Router>
                  <Authprovider>
                  <Switch>
                 
                     {/*Print Routes*/}
                      <PrivateRoute exact path="/" component={MianDashboard}/>
                      <PrivateRoute exact path="/folder/:folderId" component={MianDashboard}/>
                     {/* Profile Routes */}

                     <PrivateRoute  path="/user" component={Dashboard}/>
                     <PrivateRoute  path="/updateProfile" component={UpdateProfile}/>

                        {/* Auth Routes */}
                      <Route path="/signup" component={SignUp}/>
                      <Route path="/login" component={Login}/>
                      <Route path="/resetPassword" component={ForgatPassword}/>
                      <Route path="/chat" component={ChatRoom}/>

                  </Switch>
                  </Authprovider> 
              </Router>
    )
}

export default App
