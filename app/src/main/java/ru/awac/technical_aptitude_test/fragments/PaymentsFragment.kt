package ru.awac.technical_aptitude_test.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_payments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.awac.technical_aptitude_test.Model.PaymentsModel
import ru.awac.technical_aptitude_test.Model.ResponseModel
import ru.awac.technical_aptitude_test.R
import ru.awac.technical_aptitude_test.utils.adapter.PaymentsAdapter
import ru.awac.technical_aptitude_test.utils.fadeTo
import ru.awac.technical_aptitude_test.utils.retrofit.Common
import ru.awac.technical_aptitude_test.utils.retrofit.RetrofitServices

class PaymentsFragment : Fragment(R.layout.fragment_payments) {
    private var token: String? = null

    private lateinit var mService: RetrofitServices
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: PaymentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(TOKEN)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mService = Common.retrofitService
        layoutManager = LinearLayoutManager(activity)
        fpRecyclerView.layoutManager = layoutManager

        loadPayments(token)


        fpLogOutButton.setOnClickListener {
            logOut()
        }

    }

    private fun loadPayments(token: String?){
        if (token != null) {
            mService.getPayments(token).enqueue(object : Callback<PaymentsModel> {
                override fun onFailure(call: Call<PaymentsModel>, t: Throwable) {
                    fpProgressBar.fadeTo(false)
                }
                override fun onResponse(
                    call: Call<PaymentsModel>,
                    responseLoginModel: Response<PaymentsModel>
                ) {
                    adapter = PaymentsAdapter(responseLoginModel.body()?.response
                            as MutableList<ResponseModel>)
                    adapter.notifyDataSetChanged()
                    fpRecyclerView.adapter = adapter
                    fpProgressBar.fadeTo(false)
                }



            })
        }

    }

    private fun logOut(){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(
            R.id.fragment_container_view,
            LoginFragment.newInstance()
        )
        transaction?.commit()
    }

    companion object {

        private const val TOKEN = "token"

        @JvmStatic
        fun newInstance(token: String?) =
            PaymentsFragment().apply {
                arguments = Bundle().apply {
                    putString(TOKEN, token)
                }
            }
    }
}