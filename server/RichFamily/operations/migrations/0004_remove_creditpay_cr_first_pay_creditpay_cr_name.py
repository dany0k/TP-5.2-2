# Generated by Django 4.2 on 2023-05-10 08:29

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('operations', '0003_operationcategory_user'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='creditpay',
            name='cr_first_pay',
        ),
        migrations.AddField(
            model_name='creditpay',
            name='cr_name',
            field=models.CharField(default='Кредит', max_length=255),
        ),
    ]
