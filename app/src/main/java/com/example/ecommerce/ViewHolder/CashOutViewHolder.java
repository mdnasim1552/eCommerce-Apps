package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;


public class CashOutViewHolder extends RecyclerView.ViewHolder{
    public TextView seeProfileCashOut,amountCashOut,dateTimeCashOut;
    public EditText trsnIdCashOut;
    public Button submitBtnCashOut;
    public CashOutViewHolder(View itemView) {
        super(itemView);
        seeProfileCashOut=itemView.findViewById(R.id.see_profile_cash_out);
        amountCashOut=itemView.findViewById(R.id.amount_cash_out);
        dateTimeCashOut=itemView.findViewById(R.id.date_time_cash_out);
        trsnIdCashOut=itemView.findViewById(R.id.trsn_id_cash_out);
        submitBtnCashOut=itemView.findViewById(R.id.submit_btn_cash_out);
    }
}
