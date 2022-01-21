import React ,{useContext , useState ,useEffect}from 'react';
import {auth} from '../firebase';
import {db} from '../firebase'
const AuthContext = React.createContext();

export const useAuth =()=>{
return useContext(AuthContext)
}


 export const Authprovider = ({children}) => {
    const [currentUser,setCurrentUser]=useState();
    const [loading,setLoading] =useState(true)
    const [userInfo,setUserInfo]=useState()
    const signup =(email,password,name,role)=>{
       return auth.createUserWithEmailAndPassword(email,password)
       .then(user=>{
        
          db.users.doc(user.user.uid).set({
               name:name,
               role:role,
               userId:user.user.uid,
               email:email,
           })
           
       });
    }
    
    const login =(email,password)=>{
        return auth.signInWithEmailAndPassword(email,password);
    }
    const logout =()=>{
        return auth.signOut();
    }
    const resetPassword =(email)=>{
        return auth.sendPasswordResetEmail(email);
    }
    const updateEmail =(email)=>{
        return currentUser.updateEmail(email);
        
    }
    useEffect(()=>{
        const unsubscribe=auth.onAuthStateChanged(user=>{
            setCurrentUser(user);
            console.log(user)
            if(user){
            const data= db.users.doc(user.uid).get().then(doc=>{
                setUserInfo(db.formatedDoc(doc))
             })
             console.log(userInfo)
            }
            setLoading(false)
        });

        return unsubscribe;
    },[currentUser]);


    const value = {
        currentUser,
        userInfo,
        signup,
        login,
        logout,
        resetPassword,
        updateEmail,
    }

    return (
        <AuthContext.Provider value={value}>
            {!loading&&children}
        </AuthContext.Provider>
    )
}

