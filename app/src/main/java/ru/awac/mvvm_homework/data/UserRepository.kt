package ru.awac.mvvm_homework.data

class UserRepository private constructor(private val userDao: UserDao) {

    fun addUser(user: User) {
        userDao.insert(user)
    }

    fun getAllUsers() = userDao.getAll()

    companion object {

        private var instance: UserRepository? = null

        @Synchronized
        fun getInstance(quoteDao: UserDao) =
            instance ?: UserRepository(quoteDao).also { instance = it }
    }
}