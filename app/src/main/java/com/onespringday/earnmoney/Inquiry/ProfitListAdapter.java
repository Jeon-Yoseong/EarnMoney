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
 * Created by yoseong on 2017-08-09.
 */

public class ProfitListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ProfitItem> data;

    public ProfitListAdapter (Context context, List<ProfitItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.profit_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // data 설정과 레이아웃 설정
        ProfitItem profitItem = data.get(position);
        CardViewHolder cardViewHolder =  (CardViewHolder) holder;

        cardViewHolder.profitDate.setText(profitItem.getProfitDate());
        cardViewHolder.profitCount.setText("판매수량: "+profitItem.getProfitCount()+"개");
        cardViewHolder.profitTotalMoney.setText("순이익: "+profitItem.getProfitTotalMoney()+"원");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView profitDate;
        private TextView profitCount;
        private TextView profitTotalMoney;

        public CardViewHolder(View itemView) {
            super(itemView);
            profitDate = (TextView) itemView.findViewById(R.id.profit_date);
            profitCount = (TextView) itemView.findViewById(R.id.profit_count);
            profitTotalMoney = (TextView) itemView.findViewById(R.id.profit_total_money);
        }
    }
}
