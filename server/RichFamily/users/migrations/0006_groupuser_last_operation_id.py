# Generated by Django 4.2 on 2023-05-19 09:16

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0005_groupuser_remove_appuserprofile_groups_and_more'),
    ]

    operations = [
        migrations.AddField(
            model_name='groupuser',
            name='last_operation_id',
            field=models.IntegerField(default=1),
        ),
    ]