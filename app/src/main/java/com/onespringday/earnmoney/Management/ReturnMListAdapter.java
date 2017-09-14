package com.onespringday.earnmoney.Management;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onespringday.earnmoney.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by yoseong on 2017-08-27.
 */

public class ReturnMListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<ReturnMItem> data;

    private SharedPreferences pref_member;
    private String memberId;

    public ReturnMListAdapter (Context context, List<ReturnMItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler view 에 반복될 아이템 레이아웃을 연결
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.return_management_list_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // data 설정과 레이아웃 설정
        final ReturnMItem returnMItem = data.get(position);
        final CardViewHolder cardViewHolder =  (CardViewHolder) holder;

        SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance(); // 오늘날짜
        final String todayDate = today.format(calendar.getTime());

        pref_member = context.getSharedPreferences("pref_member", 0);
        memberId = pref_member.getString("id", "");

        // 상세 정보 설정
        cardViewHolder.returnName.setText(returnMItem.getReturnName());
        cardViewHolder.returnColor.setText(returnMItem.getReturnColor());
        cardViewHolder.returnSize.setText(returnMItem.getReturnSize());
        cardViewHolder.returnCount.setText(returnMItem.getReturnCount());
        cardViewHolder.orderDate.setText(returnMItem.getOrderDate());
        cardViewHolder.returnDate.setText(returnMItem.getReturnDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {

    }

    private class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView returnName;
        private TextView returnColor;
        private TextView returnSize;
        private TextView returnCount;
        private TextView orderDate;
        private TextView returnDate;

        public CardViewHolder(View itemView) {
            super(itemView);
            returnName = (TextView) itemView.findViewById(R.id.return_management_list_name);
            returnColor = (TextView) itemView.findViewById(R.id.return_management_list_color);
            returnSize = (TextView) itemView.findViewById(R.id.return_management_list_size);
            returnCount = (TextView) itemView.findViewById(R.id.return_management_list_count);
            orderDate = (TextView) itemView.findViewById(R.id.return_management_list_order_date);
            returnDate = (TextView) itemView.findViewById(R.id.return_management_list_return_date);
        }
    }
}

