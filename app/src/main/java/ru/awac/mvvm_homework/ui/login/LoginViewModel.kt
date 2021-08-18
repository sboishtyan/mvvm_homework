package ru.awac.mvvm_homework.ui.login

import androidx.lifecycle.ViewModel
import ru.awac.mvvm_homework.data.User
import ru.awac.mvvm_homework.data.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getAllUsers() = userRepository.getAllUsers()

    fun addUser(user: User) = userRepository.addUser(user)
}