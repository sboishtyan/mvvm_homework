package ru.awac.technical_aptitude_test.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout.view.*
import ru.awac.technical_aptitude_test.Model.ResponseModel
import ru.awac.technical_aptitude_test.R
import ru.awac.technical_aptitude_test.utils.checkFieldForNull
import ru.awac.technical_aptitude_test.utils.formatDateAndTime

class PaymentsAdapter(
    private val paymentList: MutableList<ResponseModel>
):RecyclerView.Adapter<PaymentsAdapter.PaymentViewHolder>() {

    class PaymentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val iCreatedTextView: TextView = itemView.iCreatedTextView
        val iCurrencyTextView: TextView = itemView.iCurrencyTextView
        val iAmountTextView: TextView = itemView.iAmountTextView
        val iDescriptionTextView: TextView = itemView.iDescriptionTextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent,
            false)
        return PaymentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.iCreatedTextView.text = paymentList[position].created.formatDateAndTime()
        holder.iCurrencyTextView.text = paymentList[position].currency.checkFieldForNull()
        holder.iAmountTextView.text = paymentList[position].amount.toString().checkFieldForNull()
        //TODO: проверить тип переменной, добавить названия для полей
        holder.iDescriptionTextView.text = paymentList[position].desc.checkFieldForNull()
    }

    override fun getItemCount() = paymentList.size
}