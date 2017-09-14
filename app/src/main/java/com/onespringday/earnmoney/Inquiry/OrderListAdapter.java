package com.onespringday.earnmoney.Inquiry;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onespringday.earnmoney.Management.OrderMItem;
import com.onespringday.earnmoney.PHPUpload;
import com.onespringday.earnmoney.R;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by yoseong on 2017-08-22.
 */

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<OrderMItem> data;

    private SharedPreferences pref_member;
    private String memberId;

    public OrderListAdapter (Context context, List<OrderMItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler view 에 반복될 아이템 레이아웃을 연결
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.order_date_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // data 설정과 레이아웃 설정
        final OrderMItem orderMItem = data.get(position);
        final CardViewHolder cardViewHolder =  (CardViewHolder) holder;
/*
        pref_order_temp_save = context.getSharedPreferences("order_temp_save", 0);
        editor_order_temp_save = pref_order_temp_save.edit();
        editor_order_temp_save.putString("count", String.valueOf(count[0]));
*/
        SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance(); // 오늘날짜
        final String todayDate = today.format(calendar.getTime());

        pref_member = context.getSharedPreferences("pref_member", 0);
        memberId = pref_member.getString("id", "");

        // 상세 정보 설정
        cardViewHolder.orderName.setText(orderMItem.getOrderName());
        cardViewHolder.orderColor.setText(orderMItem.getOrderColor()+",");
        cardViewHolder.orderSize.setText(orderMItem.getOrderSize());
        cardViewHolder.orderCount.setText(String.valueOf(orderMItem.getOrderCount()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {

    }

    private class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView orderName;
        private TextView orderColor;
        private TextView orderSize;
        private TextView orderCount;

        public CardViewHolder(View itemView) {
            super(itemView);
            orderName = (TextView) itemView.findViewById(R.id.order_date_name);
            orderColor = (TextView) itemView.findViewById(R.id.order_date_color);
            orderSize = (TextView) itemView.findViewById(R.id.order_date_size);
            orderCount = (TextView) itemView.findViewById(R.id.order_date_count);
        }
    }
}
