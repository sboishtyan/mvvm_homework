package ru.awac.mvvm_homework.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import mvvm_homework.R
import mvvm_homework.databinding.FragmentLoginBinding
import ru.awac.mvvm_homework.data.User
import ru.awac.mvvm_homework.utils.ServiceLocator
import ru.awac.mvvm_homework.utils.fadeTo

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var factory: LoginViewModelFactory? = null

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var isLoginEntered = false
    private var isPasswordEntered = false
    private var isLoginCorrect: Boolean = false
    private var isPasswordCorrect: Boolean = false
    private var enteredLogin: String = ""
    private var enteredPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeUi()
    }

    private fun initializeUi() {
        factory = context?.let { ServiceLocator(it).loginViewModelFactory }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginViewModel = factory?.let { ViewModelProvider(this, it).get(LoginViewModel::class.java) }
        
        loginViewModel.users.observe { users ->
            
        }
        loginViewModel.isSignEnabled.observe { isEnabled
            binding.flSignInButton.isEnabled = isEnabled
        }
        // тоже самое для isRegisterEnabled
        
        binding.flLoginEditText.doAfterTextChanged {
            loginViewModel.login.set(it.toString())
        }

        binding.flPassEditText.doAfterTextChanged {
            loginViewModel.password.set(it.toString())
        }
        
        loginViewModel.showMessages.observe { show -> 
            
        }
        
        loginViewModel.operationResult.observe { (fade, text) -> 
            binding.flOperationResultTextView.fadeTo(fade)
            binding.flOperationResultTextView.text = text
        }

        binding.flRegisterButton.setOnClickListener {
            loginViewModel.onRegisterClick()
            
            hideMessages()
            checkLogin()
            checkPassword()
            if (isLoginCorrect && isPasswordCorrect) {
                enableActionButtons(false)
                var isAlreadyRegistered = false
                allUsers?.forEach{ user ->
                    if (user.email == enteredLogin)
                        isAlreadyRegistered = true
                }
                
                if (isAlreadyRegistered)
                    binding.flOperationResultTextView.text = getString(R.string.register_fail)
                else {
                    val user = User(enteredLogin, enteredPassword)
                    factory?.let { ViewModelProvider(this, it).get(LoginViewModel::class.java) }
                        ?.addUser(user)
                    binding.flOperationResultTextView.text = getString(R.string.register_success)
                }
                binding.flOperationResultTextView.fadeTo(true)
                enableActionButtons()
            }
        }

        binding.flSignInButton.setOnClickListener {
            hideMessages()
            checkLogin()
            checkPassword()
            if (isLoginCorrect && isPasswordCorrect) {
                enableActionButtons(false)
                val user = User(enteredLogin, enteredPassword)
                if (allUsers?.contains(user) != true)
                    binding.flOperationResultTextView.text = getString(R.string.login_error)

                else
                    binding.flOperationResultTextView.text = getString(R.string.login_success)
                binding.flOperationResultTextView.fadeTo(true)
                enableActionButtons()
            }
        }
    }

    private fun enableActionButtons(isEnabled: Boolean = true) {
        val isDataEntered = isLoginEntered && isPasswordEntered
        binding.flRegisterButton.isEnabled = isDataEntered && isEnabled
        binding.flSignInButton.isEnabled = isDataEntered && isEnabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideMessages() {
        binding.flPassErrorTextView.fadeTo(false)
        binding.flLoginNoteTextView.fadeTo(false)
        binding.flOperationResultTextView.fadeTo(false)
    }

    private fun checkLogin() {
        enteredLogin = binding.flLoginEditText.text.toString()
        val isOnlyCorrectSymbols: Boolean = Patterns.EMAIL_ADDRESS.matcher(enteredLogin).matches()

        if ( isOnlyCorrectSymbols )
            isLoginCorrect = true
        else
            binding.flLoginNoteTextView.fadeTo(true)
    }

    private fun checkPassword() {
        enteredPassword = binding.flPassEditText.text.toString()
        if (enteredPassword.length < MINIMAL_PASSWORD_LENGTH) {
            binding.flPassErrorTextView.fadeTo(true)
        } else
            isPasswordCorrect = true
    }

    companion object {
        const val MINIMAL_PASSWORD_LENGTH = 8
    }
}
