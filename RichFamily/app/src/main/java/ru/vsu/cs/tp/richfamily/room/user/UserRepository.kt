package ru.vsu.cs.tp.richfamily.room.user

class UserRepository(private val userDao: UserDao) {
    val currentUser: UserDB = userDao.getUser()
    val isInDB = userDao.countUser() != 0

    fun insert(userDB: UserDB) {
        userDao.insert(userDB)
    }
}