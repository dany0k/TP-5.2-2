from django.db import models
from users.models import AppUserProfile

class Group(models.Model):
    """ Модель группы пользователей приложения """
    gr_name = models.CharField(max_length=100)

    class Meta:
        db_table = 'groups'


class Groups_Users(models.Model):
    """ Связующая модель групп и пользователей """
    group = models.ForeignKey(Group, on_delete=models.CASCADE)
    user = models.ForeignKey(AppUserProfile, on_delete=models.CASCADE)
    is_leader = models.BooleanField()

    class Meta:
        db_table = 'groups_users'


