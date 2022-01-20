from django.db import models
from django.core.validators import FileExtensionValidator

# Create your models here.

class sendMsg(models.Model):
    name = models.CharField(max_length=100)
    msg = models.CharField(max_length=600)
    cdate = models.DateField()
    ctime = models.TimeField()

class uploadDoc(models.Model):
    title = models.CharField(max_length=100)
    tag = models.CharField(max_length=100)
    name = models.CharField(max_length=100)
    cdate = models.DateField()
    ctime = models.TimeField()
    docfile = models.FileField(validators=[
        FileExtensionValidator(allowed_extensions=['pdf', 'doc', 'ppt', 'xlsx','jpg','png'])
    ])


