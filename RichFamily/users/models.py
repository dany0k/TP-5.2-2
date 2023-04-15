from django.db import models
from django.db.models.signals import post_save
from django.contrib.auth.models import User
from django.dispatch import receiver

from groups.models import Group


class AppUserProfile(models.Model):
    # В качестве модели пользователя будет использоваться расширенная встроенная модель
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    secret_word = models.CharField(max_length=255)
    groups = models.ManyToManyField(Group, related_name='users', blank=True)


@receiver(post_save, sender=User)
def create_user_profile(sender, instance, created, **kwargs):
    if created:
        AppUserProfile.objects.create(user=instance, secret_word="")


@receiver(post_save, sender=User)
def save_user_profile(sender, instance, **kwargs):
    instance.appuserprofile.save()


class Leaders(models.Model):
    group = models.OneToOneField(Group, on_delete=models.CASCADE)
    leader = models.OneToOneField(AppUserProfile, on_delete=models.CASCADE)

    class Meta:
        db_table = 'leaders'
