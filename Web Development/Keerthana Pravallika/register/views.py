from django.shortcuts import render,redirect
from django.contrib.auth.models import User,auth
from django.contrib import messages
import math, random 
import os
from twilio.rest import Client

# Create your views here.

account_sid = 'AC6e85aac2ca9db2127e4552ef26a03a78'
auth_token = '333d42b723cecf95b45158b411e02f73'
client = Client(account_sid, auth_token)

otp = 0

def generateOTP() :
    digits = "0123456789"
    OTP = ""
    for i in range(4) :
        OTP += digits[math.floor(random.random() * 10)]
    return OTP

def login(request):
    return render(request,'login.html')

def signin(request):

    if request.method == 'POST':
        global name
        name = request.POST['name']
        global username 
        username = request.POST['idno']
        global email
        email = request.POST['email']
        global phno
        phno = request.POST['phno']
        global password1
        password1  = request.POST['password1']
        password2 = request.POST['password2']

        if password1 == password2 : 

            if User.objects.filter(email=email).exists():
                messages.info(request, "Email already registered")
                return redirect('/register/signup')
            else:
                '''
                user = User.objects.create_user(first_name=name,email=email,username=username,last_name=phno,password=password1)
                user.save()
                print("User created")   
                return redirect('/register/login') 
                '''
                o=generateOTP()
                global otp 
                otp = o
                print(otp)
                message = client.messages \
                .create(
                    body='Your OTP : '+o,
                    from_='+18509203345',
                    to=phno
                ) 

                return render(request, "otp.html")      
        else:
            print("Password not matching....")
            return redirect('/register/signup')
        return redirect('/')

        
def signup(request):
    return render(request,'signup.html') 

def login_action(request):
    if request.method == "POST":
        username = request.POST['username']
        password = request.POST['password']

        user = auth.authenticate( username=username,password=password)

        if user is not None :
            auth.login(request,user)
            return redirect('/')
        else:
            messages.info(request,"Invalid credentials")
            return redirect('/register/login')

def logout(request):
    auth.logout(request)
    return redirect('/') 

def validate(request):
    givenOTP = request.POST['opnum']
    print(givenOTP)
    
    if otp == givenOTP:
        user = User.objects.create_user(first_name=name,email=email,username=username,last_name=phno,password=password1)
        user.save()
        print("User created")   
        return redirect('/register/login')        
    else:
        return render(request, "otp.html",{'value':"Incorrect OTP , Please try again !"})   

    


