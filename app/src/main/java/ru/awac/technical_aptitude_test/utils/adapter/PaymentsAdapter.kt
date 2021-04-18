package ru.awac.technical_aptitude_test.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.awac.technical_aptitude_test.Model.ResponseModel
import ru.awac.technical_aptitude_test.databinding.ItemLayoutBinding
import ru.awac.technical_aptitude_test.utils.checkFieldForNull
import ru.awac.technical_aptitude_test.utils.formatDateAndTime

class PaymentsAdapter(
    private val paymentList: MutableList<ResponseModel>
):RecyclerView.Adapter<PaymentsAdapter.PaymentViewHolder>() {

    inner class PaymentViewHolder(val binding:  ItemLayoutBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.binding.iCreatedTextView.text = paymentList[position].created.formatDateAndTime()
        holder.binding.iCurrencyTextView.text = paymentList[position].currency.checkFieldForNull()
        holder.binding.iAmountTextView.text = paymentList[position].amount.toString().checkFieldForNull()
        holder.binding.iDescriptionTextView.text = paymentList[position].desc.checkFieldForNull()
    }

    override fun getItemCount() = paymentList.size

}