import os
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'RichFamily.settings')
import django
django.setup()

from django.test import TestCase

from groups.models import Group
from RichFamily.wsgi import *

class GroupsTests(TestCase):
    def test_group_create(self):
        group = Group.objects.create(gr_name="gr_name")
        self.assertIsNotNone(group)
        self.assertEquals(group.gr_name, "gr_name")

    def test_group_update(self):
        group = Group.objects.create(gr_name="gr_name")
        self.assertIsNotNone(group)
        self.assertEquals(group.gr_name, "gr_name")
        group.gr_name = "new_gr_name"
        group.save()
        group2 = Group.objects.get(gr_name="new_gr_name")
        self.assertIsNotNone(group2)

    def test_group_delete(self):
        group = Group.objects.create(gr_name="gr_name")
        self.assertIsNotNone(group)
        group.delete()
        with self.assertRaises(Exception):
            group2 = Group.objects.get(gr_name="gr_name")
