package ru.awac.mvvm_homework.data

class UserRepository private constructor(private val userDao: UserDao){

    fun addUser(user: User){
        userDao.insert(user)
    }

    fun getAllUsers() = userDao.getAll()

    companion object {
        // Singleton instantiation you already know and love
        @Volatile private var instance: UserRepository? = null

        fun getInstance(quoteDao: UserDao) =
                instance ?: synchronized(this) {
                    instance ?: UserRepository(quoteDao).also { instance = it }
                }
    }
}