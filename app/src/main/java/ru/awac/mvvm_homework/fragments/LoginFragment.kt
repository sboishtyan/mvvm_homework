package ru.awac.mvvm_homework.fragments

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
import ru.awac.mvvm_homework.ui.login.LoginViewModel
import ru.awac.mvvm_homework.ui.login.LoginViewModelFactory
import ru.awac.mvvm_homework.utils.InjectorUtils
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
        factory = context?.let { context ->
            activity?.let { activity ->
                InjectorUtils(
                    context,
                    activity.application
                ).provideLoginViewModelFactory()
            }
        }

        factory?.let { ViewModelProvider(this, it).get(LoginViewModel::class.java) }?.getAllUsers()
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
        binding.flLoginEditText.doAfterTextChanged {
            isLoginEntered = it.toString() != ""
            enableActionButtons()
        }

        binding.flPassEditText.doAfterTextChanged {
            isPasswordEntered = it.toString() != ""
            enableActionButtons()
        }

        binding.flSignInButton.setOnClickListener {
            hideMessages()
            checkLogin()
            checkPassword()
            if (isLoginCorrect && isPasswordCorrect) {
                val user = User(1, enteredLogin, enteredPassword)
                val allUsers =
                    factory?.let { ViewModelProvider(this, it).get(LoginViewModel::class.java) }
                        ?.getAllUsers()
                if (allUsers?.contains(user) != true)
                    binding.flLoginSuccessTextView.text =
                        "Ошибочка вышла, нет такого пользователя в базе"
                binding.flLoginSuccessTextView.fadeTo(true)
            }
        }
    }

    private fun enableActionButtons() {
        val isDataEntered = isLoginEntered && isPasswordEntered
        binding.flRegisterButton.isEnabled = isDataEntered
        binding.flSignInButton.isEnabled = isDataEntered
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideMessages() {
        binding.flPassErrorTextView.fadeTo(false)
        binding.flLoginErrorTextView.fadeTo(false)
        binding.flLoginSuccessTextView.fadeTo(false)
    }

    private fun checkLogin() {
        enteredLogin = binding.flLoginEditText.text.toString()
        val isUsernameCorrect: Boolean
        val isTopLevelDomainCorrect: Boolean
        val atSignSplitEmailList = enteredLogin.split("@")
        val isOnlyCorrectSymbols: Boolean = Patterns.EMAIL_ADDRESS.matcher(enteredLogin).matches()

        if (atSignSplitEmailList.size != 2) {
            isUsernameCorrect = false
            isTopLevelDomainCorrect = false
        } else {
            isUsernameCorrect = atSignSplitEmailList[0] != ""
            isTopLevelDomainCorrect =
                atSignSplitEmailList[1].split(".").last() != ""
        }

        if (enteredLogin.contains("@") &&
            enteredLogin.contains(".") &&
            isOnlyCorrectSymbols &&
            isUsernameCorrect &&
            isTopLevelDomainCorrect
        )
            isLoginCorrect = true
        else
            binding.flLoginErrorTextView.fadeTo(true)
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