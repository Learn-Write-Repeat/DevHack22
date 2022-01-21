from django.contrib import admin
from . import views
from django.urls import path


urlpatterns = [
    path('',views.home,name='home'),
    path('msg',views.showMsg,name='showMsg'),
    path('sendMsgs',views.sendMsgs,name='sendMsgs'),
    path('searching',views.searching,name='searching'),
    path('account',views.account,name="account"),
    path('upload',views.upload,name="upload"),
    path('uploadDocForm',views.uploadDocForm,name="uploadDocForm"),
    path('uploadDocview',views.uploadDocview,name='uploadDocview'),
    path('documents',views.documents,name="documents"),
    path('pdf_view/<value>',views.pdf_view,name="pdf_view"),
    path('searchingDoc',views.searchingDoc,name="searchingDoc"),
   

]