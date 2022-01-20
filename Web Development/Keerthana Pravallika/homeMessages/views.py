from datetime import date, datetime
from django.shortcuts import render
from django.contrib.auth.models import User,auth
from .models import sendMsg,uploadDoc
from django.http import FileResponse, Http404 ,HttpResponse
# Create your views here.

def home(request):
    return render(request,"index.html")

def showMsg(request):
    values = sendMsg.objects.all()
    return render(request, 'messages.html' , {"values":values})

def sendMsgs(request):
    msg = request.POST['msg']
    name = request.user.username

    cDate = date.today()

    currentDate = cDate.strftime("%Y-%m-%d")
    now = datetime.now()

    current_time = now.strftime("%H:%M:%S")

    obj = sendMsg(name=name,msg=msg,cdate=currentDate,ctime=current_time)
    obj.save()

    values = sendMsg.objects.all()
    return render(request, 'messages.html' , {"values":values}) 

def searching(request):
    givenDate = request.POST['search']

    values = sendMsg.objects.filter(cdate = givenDate )
    return render(request, 'messages.html' , {"values":values})

def account(request):
    return render(request,"account.html")
    
def upload(request):
    return render(request,"upload.html")

def uploadDocForm(request):
    return render(request,"uploadDoc.html")

def uploadDocview(request):
    title = request.POST['titles']
    tag = request.POST['tag']
    docfile = request.POST['docFile']

    name = request.user.username

    cDate = date.today()

    currentDate = cDate.strftime("%Y-%m-%d")
    now = datetime.now()

    current_time = now.strftime("%H:%M:%S")

    obj = uploadDoc(title=title,tag=tag,name=name,cdate=currentDate,ctime=current_time,docfile=docfile)
    obj.save()

    return render(request,"upload.html")

def documents(request):
    values = uploadDoc.objects.all()
    return render(request, 'documents.html' , {"values":values}) 

def pdf_view(request,value):

    with open(value, 'rb') as pdf:
        response = HttpResponse(pdf.read(),content_type='application/pdf')
        response['Content-Disposition'] = 'filename=some_file.pdf'
        return response

def searchingDoc(request):
    tag = request.POST['tag']

    values = uploadDoc.objects.filter(tag = tag )

    return render(request, 'documents.html' , {"values":values}) 



   
    



