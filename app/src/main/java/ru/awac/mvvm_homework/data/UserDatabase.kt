package ru.awac.mvvm_homework.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "database"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries() //FIXME: Yeah, it's bad, but I wanted to get app
                        // up and running and afterwards learn about coroutines or some other
                        // best practices
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}