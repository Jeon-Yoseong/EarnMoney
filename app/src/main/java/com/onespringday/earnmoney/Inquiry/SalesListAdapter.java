package com.onespringday.earnmoney.Inquiry;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onespringday.earnmoney.R;

import java.util.List;

/**
 * Created by yoseong on 2017-08-07.
 */

public class SalesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SalesItem> data;

    public SalesListAdapter (Context context, List<SalesItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler view 에 반복될 아이템 레이아웃을 연결
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sales_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // data 설정과 레이아웃 설정
        SalesItem salesItem = data.get(position);
        CardViewHolder cardViewHolder =  (CardViewHolder) holder;

        // 상세 정보 설정
        cardViewHolder.salesDate.setText(salesItem.getSalesDate());
        cardViewHolder.salesCount.setText("판매수량: "+salesItem.getSalesCount()+"개");
        cardViewHolder.salesTotalMoney.setText("매출액: "+salesItem.getSalesTotalMoney()+"원");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView salesDate;
        private TextView salesCount;
        private TextView salesTotalMoney;

        public CardViewHolder(View itemView) {
            super(itemView);
            salesDate = (TextView) itemView.findViewById(R.id.sales_date);
            salesCount = (TextView) itemView.findViewById(R.id.sales_count);
            salesTotalMoney = (TextView) itemView.findViewById(R.id.sales_total_money);
        }
    }
}
