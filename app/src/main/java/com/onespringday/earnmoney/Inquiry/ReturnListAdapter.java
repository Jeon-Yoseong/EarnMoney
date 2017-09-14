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
 * Created by yoseong on 2017-08-10.
 */

public class ReturnListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ReturnItem> data;

    public ReturnListAdapter (Context context, List<ReturnItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler view 에 반복될 아이템 레이아웃을 연결
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.return_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // data 설정과 레이아웃 설정
        ReturnItem returnItem= data.get(position);
        CardViewHolder cardViewHolder =  (CardViewHolder) holder;

        // 상세 정보 설정
        cardViewHolder.returnDate.setText(returnItem.getReturnDate());
        cardViewHolder.returnCount.setText("반품수량: "+returnItem.getReturnCount()+"개");
        cardViewHolder.returnTotalMoney.setText("반품액: "+returnItem.getReturnTotalMoney()+"원");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView returnDate;
        private TextView returnCount;
        private TextView returnTotalMoney;

        public CardViewHolder(View itemView) {
            super(itemView);
            returnDate = (TextView) itemView.findViewById(R.id.return_date);
            returnCount = (TextView) itemView.findViewById(R.id.return_count);
            returnTotalMoney = (TextView) itemView.findViewById(R.id.return_total_money);
        }
    }
}