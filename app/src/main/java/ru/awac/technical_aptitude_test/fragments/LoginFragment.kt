package ru.awac.technical_aptitude_test.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.awac.technical_aptitude_test.utils.retrofit.Common
import ru.awac.technical_aptitude_test.utils.retrofit.RetrofitServices
import ru.awac.technical_aptitude_test.Model.LoginModel
import ru.awac.technical_aptitude_test.R
import ru.awac.technical_aptitude_test.utils.fadeTo

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
            if (isLoginCorrect && isPasswordCorrect)
                login(enteredLogin, enteredPassword)
        }
    }

    private fun hideMessages(){
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

    private fun login(login: String, password: String) {
        fpProgressBar.fadeTo(true)
        mService.login(login, password).enqueue(object : Callback<LoginModel> {

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                fpProgressBar.fadeTo(false)
                flLoginProcessErrorTextView.text = getString(R.string.retrofit_error)
            }

            override fun onResponse(call: Call<LoginModel>, responseLoginModel: Response<LoginModel>) {
                if (responseLoginModel.body()?.success == true) {
                    token = responseLoginModel.body()?.response?.token
                    openPaymentsFragment(token)
                } else {
                    flLoginProcessErrorTextView.text = responseLoginModel.body()?.error?.error_msg
                    flLoginProcessErrorTextView.fadeTo(true)
                }
            }
        })
    }

    private fun openPaymentsFragment (token: String?){
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