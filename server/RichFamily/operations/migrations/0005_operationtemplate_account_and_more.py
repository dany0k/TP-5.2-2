# Generated by Django 4.2 on 2023-05-11 15:43

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('operations', '0004_remove_creditpay_cr_first_pay_creditpay_cr_name'),
    ]

    operations = [
        migrations.AddField(
            model_name='operationtemplate',
            name='account',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='operations.account'),
        ),
        migrations.AddField(
            model_name='operationtemplate',
            name='temp_comment',
            field=models.TextField(default=''),
        ),
    ]