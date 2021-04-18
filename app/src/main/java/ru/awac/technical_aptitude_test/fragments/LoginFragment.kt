package ru.awac.technical_aptitude_test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.awac.technical_aptitude_test.R
import ru.awac.technical_aptitude_test.databinding.FragmentLoginBinding
import ru.awac.technical_aptitude_test.utils.fadeTo
import ru.awac.technical_aptitude_test.utils.retrofit.Common
import ru.awac.technical_aptitude_test.utils.retrofit.RetrofitServices

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var isLoginCorrect: Boolean = false
    private var isPasswordCorrect: Boolean = false
    private var enteredLogin: String = ""
    private var enteredPassword: String = ""
    private var token: String? = null

    private lateinit var mService: RetrofitServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mService = Common.retrofitService
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

        binding.flSignInButton.setOnClickListener {
            hideMessages()
            checkLogin()
            checkPassword()
            if (isLoginCorrect && isPasswordCorrect) {
                binding.fpProgressBar.fadeTo(true)
                GlobalScope.launch(Dispatchers.Main) {
                    login(enteredLogin, enteredPassword)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideMessages() {
        binding.flPassErrorTextView.fadeTo(false)
        binding.flLoginErrorTextView.fadeTo(false)
        binding.flLoginProcessErrorTextView.fadeTo(false)
    }

    private fun checkLogin() {
        enteredLogin = binding.flLoginEditText.text.toString()
        if (enteredLogin.length < MINIMAL_LOGIN_LENGTH)
            binding.flLoginErrorTextView.fadeTo(true)
        else
            isLoginCorrect = true
    }

    private fun checkPassword() {
        enteredPassword = binding.flPassEditText.text.toString()
        if (enteredPassword.length < MINIMAL_PASSWORD_LENGTH) {
            binding.flPassErrorTextView.fadeTo(true)
        } else
            isPasswordCorrect = true
    }

    private suspend fun login(login: String, password: String) {
        val result = mService.login(login, password)

        if (result.response?.token != null) {
            token = mService.login(login, password).response?.token
            openPaymentsFragment(token)
        } else if (result.error?.error_msg != null) {
            binding.flLoginProcessErrorTextView.text = mService.login(login, password).error?.error_msg
            binding.flLoginProcessErrorTextView.fadeTo(true)
        } else {
            binding.flLoginProcessErrorTextView.text = getString(R.string.retrofit_error)
        }
        binding.fpProgressBar.fadeTo(false)
    }

    private fun openPaymentsFragment(token: String?) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(
            R.id.fragment_container_view,
            PaymentsFragment.newInstance(token)
        )
        transaction?.commit()
    }

    companion object {
        const val MINIMAL_LOGIN_LENGTH = 4
        const val MINIMAL_PASSWORD_LENGTH = 5

        fun newInstance() = LoginFragment()
    }
}