# Generated by Django 4.2 on 2023-05-18 08:28

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('operations', '0005_operationtemplate_account_and_more'),
    ]

    operations = [
        migrations.AddField(
            model_name='creditpay',
            name='cr_first_pay',
            field=models.FloatField(default=0.0),
        ),
    ]
