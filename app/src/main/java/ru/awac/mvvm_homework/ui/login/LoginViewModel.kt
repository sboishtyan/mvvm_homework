package ru.awac.mvvm_homework.ui.login

import androidx.lifecycle.ViewModel
import ru.awac.mvvm_homework.data.User
import ru.awac.mvvm_homework.data.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    
    private val user: LiveData<User> = TODO()
    
    val showMessages: LiveData<Boolean> = TODO()
    val login: LiveData<String> = TODO()
    val password: LiveData<String> = TODO()

    fun getAllUsers() = userRepository.getAllUsers()

    fun addUser(user: User) = userRepository.addUser(user)
    
    fun onRegisterClick() {
        showMessages.set(false)            
    }
}
