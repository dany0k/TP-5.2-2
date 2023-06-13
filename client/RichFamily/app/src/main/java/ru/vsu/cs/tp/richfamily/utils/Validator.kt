package ru.vsu.cs.tp.richfamily.utils

import android.widget.EditText

object Validator {

    fun isValidFirstName(firstName: EditText): Boolean {
        val fName = firstName.text.toString().trim()
        if (fName.isBlank()) {
            firstName.error = Constants.COMP_FIELD
            return false
        } else if (fName.length >= 21) {
            firstName.error = Constants.MAX_LENGHT_ERR_20
            return false
        } else if (!fName.matches(regex = Regex("[a-zA-Zа-яА-Я]*"))) {
            firstName.error = Constants.ONLY_LETTERS
            return false
        }
        return true
    }

    fun isValidLastName(lastName: EditText): Boolean {
        val lName = lastName.text.toString().trim()
        if (lName.isBlank()) {
            lastName.error = Constants.COMP_FIELD
            return false
        } else if (lName.length >= 21) {
            lastName.error = Constants.MAX_LENGHT_ERR_20
            return false
        } else if (!lName.matches(regex = Regex("[a-zA-Zа-яА-Я]*"))) {
            lastName.error = Constants.ONLY_LETTERS
            return false
        }
        return true
    }

    fun isValidEmail(emailET: EditText): Boolean {
        val email = emailET.text.toString().trim()
        if (email.isBlank()) {
            emailET.error = Constants.COMP_FIELD
            return false
        } else if (email.length >= 46) {
            emailET.error = Constants.MAX_LENGHT_ERR_45
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.error = Constants.INVALID_EMAIL
            return false
        }
        return true
    }

    fun isValidPwd(pwdET: EditText): Boolean {
        val pwd = pwdET.text.toString().trim()
        if (pwd.isBlank()) {
            pwdET.error = Constants.COMP_FIELD
            return false
        } else if (pwd.length >= 46) {
            pwdET.error = Constants.MAX_LENGHT_ERR_45
            return false
        } else if (!pwd.matches(regex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}\$"))) {
            pwdET.error = Constants.PWD_INVALID
            return false
        }
        return true
    }

    fun isValidSecretWord(secretET: EditText): Boolean {
        val secret = secretET.text.toString().trim()
        if (secret.isBlank()) {
            secretET.error = Constants.COMP_FIELD
            return false
        } else if (secret.length >= 21) {
            secretET.error = Constants.MAX_LENGHT_ERR_20
            return false
        } else if (secret.length < 9) {
            secretET.error = Constants.MIN_LENGHT_ERR_8
            return false
        }
        return true
    }

    fun comparePwd(pwdET: EditText, subPwdET: EditText): Boolean {
        val pwd = pwdET.text.toString().trim()
        val subPwd = subPwdET.text.toString().trim()
        if (pwd != subPwd) {
            pwdET.error = Constants.PWD_NOT_COMPARE
            subPwdET.error = Constants.PWD_NOT_COMPARE
            return false
        }
        return true
    }
}