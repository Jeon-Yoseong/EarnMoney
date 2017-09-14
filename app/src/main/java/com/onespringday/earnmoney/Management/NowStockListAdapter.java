package com.onespringday.earnmoney.Management;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onespringday.earnmoney.PHPUpload;
import com.onespringday.earnmoney.R;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by yoseong on 2017-08-23.
 */

public class NowStockListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<NowStockItem> data;

    private SharedPreferences pref_member;
    private String memberId;

    public NowStockListAdapter(Context context, List<NowStockItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler view 에 반복될 아이템 레이아웃을 연결
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.stock_list_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // data 설정과 레이아웃 설정
        final NowStockItem nowStockItem = data.get(position);
        final CardViewHolder cardViewHolder =  (CardViewHolder) holder;

        pref_member = context.getSharedPreferences("pref_member", 0);
        memberId = pref_member.getString("id", "");

        // 상세 정보 설정
        cardViewHolder.orderName.setText(nowStockItem.getStockName());
        cardViewHolder.orderColor.setText(nowStockItem.getStockColor()+",");
        cardViewHolder.orderSize.setText(nowStockItem.getStockSize());
        cardViewHolder.orderCount.setText(nowStockItem.getStockCount());

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
            orderName = (TextView) itemView.findViewById(R.id.now_stock_list_name);
            orderColor = (TextView) itemView.findViewById(R.id.now_stock_list_color);
            orderSize = (TextView) itemView.findViewById(R.id.now_stock_list_size);
            orderCount = (TextView) itemView.findViewById(R.id.now_stock_list_count);
        }
    }
}
