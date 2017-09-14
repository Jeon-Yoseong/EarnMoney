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
import java.util.List;

/**
 * Created by yoseong on 2017-08-23.
 */

public class NowStockAddListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<NowStockItem> data;

    private SharedPreferences pref_member;
    private String memberId;

    public NowStockAddListAdapter(Context context, List<NowStockItem> data) {
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
        final NowStockItem nowStockItem = data.get(position);
        final CardViewHolder cardViewHolder =  (CardViewHolder) holder;

        pref_member = context.getSharedPreferences("pref_member", 0);
        memberId = pref_member.getString("id", "");

        // 상세 정보 설정
        cardViewHolder.orderName.setText(nowStockItem.getStockName());
        cardViewHolder.orderColor.setText(nowStockItem.getStockColor()+",");
        cardViewHolder.orderSize.setText(nowStockItem.getStockSize());
        cardViewHolder.orderCount.setText(nowStockItem.getStockCount());

        cardViewHolder.orderAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cardViewHolder.itemView.getContext());
                // 제목 셋팅
                alertDialogBuilder.setTitle("재고 수량 수정");

                // alertdialog 셋팅
                alertDialogBuilder
                        .setMessage("재고 수량을 수정하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            PHPUpload upload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/update_stock_info.php");
                                            String result = upload.PHPRequest(nowStockItem.getStockName(), nowStockItem.getStockColor(),nowStockItem.getStockSize(), Integer.parseInt(cardViewHolder.orderCount.getText().toString()), memberId);
                                            if (!result.equals("")) {
                                                Toast.makeText(context, "재고 수량이 수정되었습니다.", Toast.LENGTH_SHORT).show();
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
