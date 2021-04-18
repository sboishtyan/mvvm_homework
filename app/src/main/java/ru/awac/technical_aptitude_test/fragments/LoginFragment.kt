package ru.awac.technical_aptitude_test.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.awac.technical_aptitude_test.R
import ru.awac.technical_aptitude_test.utils.fadeTo
import ru.awac.technical_aptitude_test.utils.retrofit.Common
import ru.awac.technical_aptitude_test.utils.retrofit.RetrofitServices

class LoginFragment : Fragment(R.layout.fragment_login) {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flSignInButton.setOnClickListener {
            hideMessages()
            checkLogin()
            checkPassword()
            if (isLoginCorrect && isPasswordCorrect) {
                fpProgressBar.fadeTo(true)
                GlobalScope.launch(Dispatchers.Main) {
                    login(enteredLogin, enteredPassword)
                }
            }
        }
    }

    private fun hideMessages() {
        flPassErrorTextView.fadeTo(false)
        flLoginErrorTextView.fadeTo(false)
        flLoginProcessErrorTextView.fadeTo(false)
    }

    private fun checkLogin() {
        enteredLogin = flLoginEditText.text.toString()
        if (enteredLogin.length < MINIMAL_LOGIN_LENGTH)
            flLoginErrorTextView.fadeTo(true)
        else
            isLoginCorrect = true
    }

    private fun checkPassword() {
        enteredPassword = flPassEditText.text.toString()
        if (enteredPassword.length < MINIMAL_PASSWORD_LENGTH) {
            flPassErrorTextView.fadeTo(true)
        } else
            isPasswordCorrect = true
    }

    private suspend fun login(login: String, password: String) {
        val result = mService.login(login, password)

        if (result.response?.token != null) {
            token = mService.login(login, password).response?.token
            openPaymentsFragment(token)
        } else if (result.error?.error_msg != null) {
            flLoginProcessErrorTextView.text = mService.login(login, password).error?.error_msg
            flLoginProcessErrorTextView.fadeTo(true)
        } else {
            flLoginProcessErrorTextView.text = getString(R.string.retrofit_error)
        }
        fpProgressBar.fadeTo(false)
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