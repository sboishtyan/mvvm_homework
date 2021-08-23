package ru.awac.mvvm_homework.utils

import android.content.Context
import ru.awac.mvvm_homework.data.UserDatabase
import ru.awac.mvvm_homework.data.UserRepository
import ru.awac.mvvm_homework.ui.login.LoginViewModelFactory

class ServiceLocator(
    private val context: Context
) {

    val loginViewModelFactory by lazy {
        val userRepository = UserRepository.getInstance(UserDatabase.getInstance(context).userDao)
        LoginViewModelFactory(userRepository)
    }
}