package ru.awac.technical_aptitude_test.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.awac.technical_aptitude_test.Common.Common
import ru.awac.technical_aptitude_test.Interface.RetrofitServices
import ru.awac.technical_aptitude_test.Model.ApiResponse
import ru.awac.technical_aptitude_test.R
import ru.awac.technical_aptitude_test.utils.fadeTo

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var isLoginCorrect: Boolean = false
    private var isPasswordCorrect: Boolean = false
    private var enteredLogin: String = ""
    private var enteredPassword: String = ""
    private var token: String = ""

    lateinit var mService: RetrofitServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mService = Common.retrofitService
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInButton.setOnClickListener {
            passwordNotLongEnoughTextView.fadeTo(false)
            loginNotLongEnoughTextView.fadeTo(false)
            tokenTextView.fadeTo(false)
            checkLogin()
            checkPassword()
            if (isLoginCorrect && isPasswordCorrect)
                login(enteredLogin, enteredPassword)
        }
    }

    private fun checkLogin() {
        enteredLogin = loginEditText.text.toString()
        if (enteredLogin.length < MINIMAL_LOGIN_LENGTH)
            loginNotLongEnoughTextView.fadeTo(true)
        else
            isLoginCorrect = true
    }

    private fun checkPassword() {
        enteredPassword = passwordEditText.text.toString()
        if (enteredPassword.length < MINIMAL_PASSWORD_LENGTH) {
            passwordNotLongEnoughTextView.fadeTo(true)
        } else
            isPasswordCorrect = true
    }

    private fun login(login: String, password: String) {

        mService.login(login, password).enqueue(object : Callback<ApiResponse> {

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                tokenTextView.text = "что-то пошло не так"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.body()?.success == true)
                    tokenTextView.text = response.body()?.response?.token
                else
                    tokenTextView.text = response.body()?.error?.error_msg
                tokenTextView.fadeTo(true)
            }
        })
    }

    companion object {
        const val MINIMAL_LOGIN_LENGTH = 4
        const val MINIMAL_PASSWORD_LENGTH = 5
    }
}