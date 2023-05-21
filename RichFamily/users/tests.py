from django.test import TestCase
from django.contrib.auth.models import User

from .models import AppUserProfile


class UsersTests(TestCase):
    def setUp(self):
        self.user_account = User.objects.create_user("user1", None, "pass1")

    def test_app_user_profile_create(self):
        appUserProfile = AppUserProfile.objects.create(user=self.user_account, secret_word="secret")
        self.assertIsNotNone(appUserProfile)
        self.assertEquals(appUserProfile.user, self.user_account)
        self.assertEquals(appUserProfile.secret_word, "secret")

    def test_app_user_profile_update(self):
        appUserProfile = AppUserProfile.objects.create(user=self.user_account, secret_word="secret")
        self.assertIsNotNone(appUserProfile)
        self.assertEquals(appUserProfile.secret_word, "secret")
        appUserProfile.secret_word = "new_secret"
        appUserProfile.save()
        appUserProfile2 = AppUserProfile.objects.get(secret_word="new_secret")
        self.assertIsNotNone(appUserProfile2)

    def test_app_user_profile_delete(self):
        appUserProfile = AppUserProfile.objects.create(user=self.user_account, secret_word="secret")
        self.assertIsNotNone(appUserProfile)
        appUserProfile.delete()
        with self.assertRaises(Exception):
            appUserProfile2 = AppUserProfile.objects.get(secret_word="secret")