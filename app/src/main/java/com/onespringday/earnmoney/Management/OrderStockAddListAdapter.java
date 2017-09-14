package com.onespringday.earnmoney.Management;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by yoseong on 2017-08-25.
 */

public class OrderStockAddListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<OrderStockItem> data;

    private SharedPreferences pref_member;
    private String memberId;

    private SharedPreferences pref_order_stock;
    private SharedPreferences.Editor editor_order_stock;

    private String todayDate;

    public OrderStockAddListAdapter (Context context, List<OrderStockItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler view 에 반복될 아이템 레이아웃을 연결
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.stock_update_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // data 설정과 레이아웃 설정
        final OrderStockItem orderStockItem= data.get(position);
        final CardViewHolder cardViewHolder =  (CardViewHolder) holder;

        pref_member = context.getSharedPreferences("pref_member", 0);
        memberId = pref_member.getString("id", "");

        // 오늘 날짜 구하기
        SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance(); // 오늘날짜
        todayDate = today.format(calendar.getTime());

        // 상세 정보 설정
        cardViewHolder.orderName.setText(orderStockItem.getOrderStockName());
        cardViewHolder.orderColor.setText(orderStockItem.getOrderStockColor()+",");
        cardViewHolder.orderSize.setText(orderStockItem.getOrderStockSize());
        cardViewHolder.orderCount.setText(orderStockItem.getOrderStockCount());

        cardViewHolder.orderAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cardViewHolder.itemView.getContext());
                // 제목 셋팅
                alertDialogBuilder.setTitle("주문 재고 입력");

                // alertdialog 셋팅
                alertDialogBuilder
                        .setMessage("주문 재고 수량을 입력하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            PHPUpload upload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/save_order_stock_info.php");
                                            String result = upload.PHPRequest(orderStockItem.getOrderStockName(), orderStockItem.getOrderStockColor(),orderStockItem.getOrderStockSize(),
                                                    Integer.parseInt(cardViewHolder.orderCount.getText().toString()), todayDate, memberId, 0);
                                            if (!result.equals("")) {
                                                Toast.makeText(context, "주문 재고 수량이 입력되었습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                        .setNegativeButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                // alertdialog 생성
                AlertDialog alertDialog = alertDialogBuilder.create();
                // alertdialog 보여주기
                alertDialog.show();
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
        private EditText orderCount;
        private Button orderAddBtn;


        public CardViewHolder(View itemView) {
            super(itemView);
            orderName = (TextView) itemView.findViewById(R.id.now_stock_name);
            orderColor = (TextView) itemView.findViewById(R.id.now_stock_color);
            orderSize = (TextView) itemView.findViewById(R.id.now_stock_size);
            orderCount = (EditText) itemView.findViewById(R.id.now_stock_count);
            orderAddBtn = (Button) itemView.findViewById(R.id.now_stock_add_btn);
        }
    }
}