from django.test import TestCase
from django.contrib.auth.models import User

from .models import AppUserProfile


class UsersTests(TestCase):
    def setUp(self):
        self.user_account = User.objects.create_user("user1", None, "pass1")

    def test_app_user_profile_create(self):
        self.user_account.appuserprofile.secret_word = "secret"
        self.user_account.appuserprofile.save()
        appUserProfile = self.user_account.appuserprofile
        self.assertIsNotNone(appUserProfile)
        self.assertEquals(appUserProfile.user, self.user_account)
        self.assertEquals(appUserProfile.secret_word, "secret")

    def test_app_user_profile_update(self):
        self.user_account.appuserprofile.secret_word="secret"
        self.user_account.appuserprofile.save()
        appUserProfile = self.user_account.appuserprofile
        self.assertIsNotNone(appUserProfile)
        self.assertEquals(appUserProfile.secret_word, "secret")
        appUserProfile.secret_word = "new_secret"
        appUserProfile.save()
        appUserProfile2 = AppUserProfile.objects.get(secret_word="new_secret")
        self.assertIsNotNone(appUserProfile2)

    def test_app_user_profile_delete(self):
        self.user_account.appuserprofile.secret_word = "secret"
        self.user_account.appuserprofile.save()
        appUserProfile = self.user_account.appuserprofile
        self.assertIsNotNone(appUserProfile)
        appUserProfile.delete()
        with self.assertRaises(Exception):
            appUserProfile2 = AppUserProfile.objects.get(secret_word="secret")