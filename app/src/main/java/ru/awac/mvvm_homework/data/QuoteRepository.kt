package ru.awac.mvvm_homework.data

class QuoteRepository private constructor(private val userDao: UserDao){

    fun addUser(user: User){
        userDao.insert(user)
    }

    fun getAllUsers() = userDao.getAll()


    /*
    companion object {
        // Singleton instantiation you already know and love
        @Volatile private var instance: QuoteRepository? = null

        fun getInstance(quoteDao: FakeQuoteDao) =
                instance ?: synchronized(this) {
                    instance ?: QuoteRepository(quoteDao).also { instance = it }
                }
    }
}
     */
}