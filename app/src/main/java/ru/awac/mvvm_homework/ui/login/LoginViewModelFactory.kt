package ru.awac.mvvm_homework.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.awac.mvvm_homework.data.UserDatabase
import ru.awac.mvvm_homework.data.UserRepository

class LoginViewModelFactory (private val userRepository: UserRepository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(userRepository) as T
    }

    fun provideLoginViewModelFactory() : LoginViewModelFactory {
        val userRepository = UserRepository.getInstance(UserDatabase.getInstance().userDao)
        return LoginViewModelFactory(userRepository)
    }
}