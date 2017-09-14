package com.onespringday.earnmoney.Management;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onespringday.earnmoney.PHPUpload;
import com.onespringday.earnmoney.R;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by yoseong on 2017-08-15.
 */

public class OrderMListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<OrderMItem> data;

    private SharedPreferences pref_member;
    private String memberId;

    public OrderMListAdapter (Context context, List<OrderMItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler view 에 반복될 아이템 레이아웃을 연결
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.order_manage_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // data 설정과 레이아웃 설정
        final OrderMItem orderMItem = data.get(position);
        final CardViewHolder cardViewHolder =  (CardViewHolder) holder;

        // 주문 수량 변수
        final int[] count = {Integer.parseInt(orderMItem.getOrderCount())};
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
        cardViewHolder.orderCount.setText(String.valueOf(count[0]));
        cardViewHolder.orderMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]--;
                cardViewHolder.orderCount.setText(String.valueOf(count[0]));
              //  editor_order_temp_save.putString("count", String.valueOf(count[0]));
                try {
                    // flag를 보내서 +인지 -인지 php문에서 판단할 수 있게 해준다. 그래서 +일 경우 재고를 -하고 -일경우 재고를 +해준다.
                    // 주문 테이블에도 마찬가지로 flag를 통해 +일 경우 주문수량을 +해주고 -일경우 주문수량을 -해준다.
                    // 주문수량 - 클릭 시 플래그 0 보내줌
                    PHPUpload upload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/save_order_info.php");
                    String result = upload.PHPRequest(String.valueOf(orderMItem.getOrderName()), String.valueOf(orderMItem.getOrderColor()), String.valueOf(orderMItem.getOrderSize()), count[0], todayDate, memberId, 0);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        cardViewHolder.orderPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]++;
                cardViewHolder.orderCount.setText(String.valueOf(count[0]));
                try {
                    // 주문수량 + 클릭 시 플래그 1 보내줌
                    PHPUpload upload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/save_order_info.php");
                    String result = upload.PHPRequest(String.valueOf(orderMItem.getOrderName()), String.valueOf(orderMItem.getOrderColor()), String.valueOf(orderMItem.getOrderSize()), count[0], todayDate, memberId, 1);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
               // editor_order_temp_save.putString("count", String.valueOf(count[0]));
/*                try {
                    PHPUpload upload = new PHPUpload("http://");
                    String result = upload.PHPRequest();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }*/
            }
        });


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
        private ImageView orderMinus;
        private ImageView orderPlus;
        private ConstraintLayout orderItem;

        public CardViewHolder(View itemView) {
            super(itemView);
            orderName = (TextView) itemView.findViewById(R.id.order_management_name);
            orderColor = (TextView) itemView.findViewById(R.id.order_management_color);
            orderSize = (TextView) itemView.findViewById(R.id.order_management_size);
            orderCount = (TextView) itemView.findViewById(R.id.order_management_count);
            orderMinus = (ImageView) itemView.findViewById(R.id.order_management_minus);
            orderPlus = (ImageView) itemView.findViewById(R.id.order_management_plus);
            orderItem = (ConstraintLayout) itemView.findViewById(R.id.order_management_item_layout);
        }
    }
}
