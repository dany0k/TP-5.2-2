from django.db import models


class OnboardScreen(models.Model):
    """ Модель отдельного onboard screen """
    title = models.CharField(max_length=100)
    description = models.CharField(max_length=255)
    onboard_type = models.CharField(max_length=20)
