package ru.awac.mvvm_homework.utils

import android.app.Application
import android.content.Context
import ru.awac.mvvm_homework.data.UserDatabase
import ru.awac.mvvm_homework.data.UserRepository
import ru.awac.mvvm_homework.ui.login.LoginViewModelFactory

class InjectorUtils(
    private val context: Context,
    private val application: Application
    ) {

    fun provideLoginViewModelFactory(): LoginViewModelFactory {

        val userRepository = UserRepository.getInstance(UserDatabase.getInstance(context).userDao)

        return LoginViewModelFactory(userRepository, application)
    }
}