package ru.awac.technical_aptitude_test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.awac.technical_aptitude_test.Model.ResponseModel
import ru.awac.technical_aptitude_test.R
import ru.awac.technical_aptitude_test.databinding.FragmentPaymentsBinding
import ru.awac.technical_aptitude_test.utils.adapter.PaymentsAdapter
import ru.awac.technical_aptitude_test.utils.fadeTo
import ru.awac.technical_aptitude_test.utils.retrofit.Common
import ru.awac.technical_aptitude_test.utils.retrofit.RetrofitServices

class PaymentsFragment : Fragment(R.layout.fragment_payments) {
    private var token: String? = null

    private var _binding: FragmentPaymentsBinding? = null
    private  val binding get() = _binding!!

    private lateinit var mService: RetrofitServices
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: PaymentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(TOKEN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mService = Common.retrofitService
        layoutManager = LinearLayoutManager(activity)
        binding.fpRecyclerView.layoutManager = layoutManager

        GlobalScope.launch(Dispatchers.Main) {
            token?.let { loadPayments(it) }
        }

        binding.fpLogOutButton.setOnClickListener {
            logOut()
        }
    }


    private suspend fun loadPayments(token: String) {
        val result = mService.getPayments(token).response
        binding.fpProgressBar.fadeTo(false)
        if (result != null) {
            adapter = PaymentsAdapter(
                mService.getPayments(token).response as MutableList<ResponseModel>
            )
            adapter.notifyDataSetChanged()
            binding.fpRecyclerView.adapter = adapter
        } else {
            binding.fpErrorTextView.text = getString(R.string.retrofit_error)
            binding.fpErrorTextView.fadeTo(true)
        }
    }

    private fun logOut() {
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