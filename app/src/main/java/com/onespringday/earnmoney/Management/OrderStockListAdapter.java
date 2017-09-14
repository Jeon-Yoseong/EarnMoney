package com.onespringday.earnmoney.Management;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.onespringday.earnmoney.PHPUpload;
import com.onespringday.earnmoney.R;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by yoseong on 2017-08-25.
 */

public class OrderStockListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<OrderStockItem> data;

    private SharedPreferences pref_member;
    private String memberId;

    public OrderStockListAdapter(Context context, List<OrderStockItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler view 에 반복될 아이템 레이아웃을 연결
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.order_stock_list_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // data 설정과 레이아웃 설정
        final OrderStockItem orderStockItem = data.get(position);
        final CardViewHolder cardViewHolder =  (CardViewHolder) holder;

        pref_member = context.getSharedPreferences("pref_member", 0);
        memberId = pref_member.getString("id", "");

        // 상세 정보 설정
        cardViewHolder.orderName.setText(orderStockItem.getOrderStockName());
        cardViewHolder.orderColor.setText(orderStockItem.getOrderStockColor()+",");
        cardViewHolder.orderSize.setText(orderStockItem.getOrderStockSize());
        cardViewHolder.orderCount.setText(orderStockItem.getOrderStockCount());
        cardViewHolder.orderDate.setText(orderStockItem.getOrderStockDate());

        // 수령 완료 시 재고 테이블에 수량 추가시켜줌
        cardViewHolder.orderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PHPUpload upload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/update_stock_info_by_order_stock.php");
                    String result = upload.PHPRequest(orderStockItem.getOrderStockName(), orderStockItem.getOrderStockColor(), orderStockItem.getOrderStockSize(), Integer.parseInt(orderStockItem.getOrderStockCount()), orderStockItem.getOrderStockDate()
                            , memberId, 1);
                    if (!result.equals("")) {
                        Toast.makeText(context, "재고 입력이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    PHPUpload flagUpload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/update_order_stock_info.php");
                    flagUpload.PHPRequest(orderStockItem.getOrderStockName(), orderStockItem.getOrderStockColor(), orderStockItem.getOrderStockSize(), Integer.parseInt(orderStockItem.getOrderStockCount()), orderStockItem.getOrderStockDate()
                            , memberId, 1);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

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
        private TextView orderDate;
        private Button orderConfirm;

        public CardViewHolder(View itemView) {
            super(itemView);
            orderName = (TextView) itemView.findViewById(R.id.order_stock_list_name);
            orderColor = (TextView) itemView.findViewById(R.id.order_stock_list_color);
            orderSize = (TextView) itemView.findViewById(R.id.order_stock_list_size);
            orderCount = (TextView) itemView.findViewById(R.id.order_stock_list_count);
            orderDate = (TextView) itemView.findViewById(R.id.order_stock_list_date);
            orderConfirm = (Button) itemView.findViewById(R.id.order_stock_list_confirm);
        }
    }
}
