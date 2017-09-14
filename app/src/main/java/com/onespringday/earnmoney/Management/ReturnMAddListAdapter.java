package com.onespringday.earnmoney.Management;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onespringday.earnmoney.Inquiry.OrderDateDialog;
import com.onespringday.earnmoney.PHPListItem;
import com.onespringday.earnmoney.PHPUpload;
import com.onespringday.earnmoney.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by yoseong on 2017-08-27.
 */

public class ReturnMAddListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    // 상품 정보 불러오기
    private ArrayList<PHPListItem> listItem = new ArrayList<PHPListItem>();
    private PHPDownload task;
    ArrayList<ReturnMItem> changedItem = new ArrayList<>();

    private Context context;
    private List<ReturnMItem> data;

    private SharedPreferences pref_member;
    private String memberId;

    private String todayDate;

    public ReturnMAddListAdapter (Context context, List<ReturnMItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler view 에 반복될 아이템 레이아웃을 연결
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.return_management_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        task = new PHPDownload();
        task.execute("http://yoseong92.cafe24.com/earnmoney/get_return_info.php");
        // data 설정과 레이아웃 설정
        final ReturnMItem returnMItem= data.get(position);
        final CardViewHolder cardViewHolder =  (CardViewHolder) holder;

        pref_member = context.getSharedPreferences("pref_member", 0);
        memberId = pref_member.getString("id", "");

        // 반품 수량 변수
        final int[] count = {Integer.parseInt(returnMItem.getReturnCount())};

        // 오늘 날짜 구하기
        SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance(); // 오늘날짜
        todayDate = today.format(calendar.getTime());

        // 상세 정보 설정
        cardViewHolder.returnName.setText(returnMItem.getReturnName());
        cardViewHolder.returnColor.setText(returnMItem.getReturnColor());
        cardViewHolder.returnSize.setText(returnMItem.getReturnSize());
        cardViewHolder.returnCount.setText(returnMItem.getReturnCount());
        cardViewHolder.orderDate.setText(returnMItem.getOrderDate());
        cardViewHolder.returnDate.setText(todayDate);

        // 주문 날짜 선택
        cardViewHolder.orderDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new PHPDownload();
                task.execute("http://yoseong92.cafe24.com/earnmoney/get_return_info.php");
                final OrderDateDialog orderDateDialog = new OrderDateDialog(cardViewHolder.itemView.getContext());
                orderDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (TextUtils.isEmpty(orderDateDialog.getSelectDate())) {
                            orderDateDialog.setSelectDate(todayDate);
                        }
                        cardViewHolder.orderDate.setText(orderDateDialog.getSelectDate());
                    }
                });
                orderDateDialog.show();

            }
        });
/*
        // 반품 날짜 선택
        cardViewHolder.returnDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OrderDateDialog orderDateDialog = new OrderDateDialog(cardViewHolder.itemView.getContext());
                orderDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (TextUtils.isEmpty(orderDateDialog.getSelectDate())) {
                            orderDateDialog.setSelectDate(todayDate);
                        }
                        cardViewHolder.returnDate.setText(orderDateDialog.getSelectDate());
                    }
                });
                orderDateDialog.show();
            }
        });
*/
        // 반품 수량 추가 버튼 클릭 시
        cardViewHolder.returnPlus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                count[0]++;
                cardViewHolder.returnCount.setText(String.valueOf(count[0]));
                try {
                    // 주문수량 + 클릭 시 플래그 1 보내줌
                    PHPUpload upload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/save_return_info.php");
                    String result = upload.PHPRequest(returnMItem.getReturnName(), returnMItem.getReturnColor(), returnMItem.getReturnSize(), count[0], String.valueOf(cardViewHolder.orderDate.getText()),
                            String.valueOf(cardViewHolder.returnDate.getText()), memberId, 1);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        // 반품 수량 제거 버튼 클릭 시
        cardViewHolder.returnMinus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                count[0]--;
                cardViewHolder.returnCount.setText(String.valueOf(count[0]));
                try {
                    // 주문수량 + 클릭 시 플래그 1 보내줌
                    PHPUpload upload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/save_return_info.php");
                    String result = upload.PHPRequest(returnMItem.getReturnName(), returnMItem.getReturnColor(), returnMItem.getReturnSize(), count[0], String.valueOf(cardViewHolder.orderDate.getText()),
                            String.valueOf(cardViewHolder.returnDate.getText()), memberId, 0);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });


        // 주문 날짜가 바꼈을 경우 그 날짜에 맞는 반품 수량 보여주기
        cardViewHolder.orderDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count1, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count1) {

                for (int i = 0; i < changedItem.size(); i++) {
                    if (returnMItem.getReturnName().equals(changedItem.get(i).getReturnName()) && returnMItem.getReturnColor().equals(changedItem.get(i).getReturnColor())
                            && returnMItem.getReturnSize().equals(changedItem.get(i).getReturnSize())) {
                        if (cardViewHolder.orderDate.getText().toString().equals(changedItem.get(i).getOrderDate()) && cardViewHolder.returnDate.getText().toString().equals(changedItem.get(i).getReturnDate())) {
                            count[0] = Integer.parseInt(changedItem.get(i).getReturnCount());
                            cardViewHolder.returnCount.setText(String.valueOf(count[0]));
                            break;
                        } else {
                            cardViewHolder.returnCount.setText("0");
                        }
                    }
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
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

        private TextView returnName;
        private TextView returnColor;
        private TextView returnSize;
        private TextView returnCount;
        private ImageView returnPlus;
        private ImageView returnMinus;
        private TextView orderDate;
        private TextView returnDate;
        private Button orderDateBtn;
//        private Button returnDateBtn;


        public CardViewHolder(View itemView) {
            super(itemView);
            returnName = (TextView) itemView.findViewById(R.id.return_management_name);
            returnColor = (TextView) itemView.findViewById(R.id.return_management_color);
            returnSize = (TextView) itemView.findViewById(R.id.return_management_size);
            returnCount = (TextView) itemView.findViewById(R.id.return_management_count);
            returnPlus = (ImageView) itemView.findViewById(R.id.return_management_plus);
            returnMinus = (ImageView) itemView.findViewById(R.id.return_management_minus);
            orderDate = (TextView) itemView.findViewById(R.id.return_management_order_date);
            returnDate = (TextView) itemView.findViewById(R.id.return_management_return_date);
            orderDateBtn = (Button) itemView.findViewById(R.id.return_management_order_date_btn);
//            returnDateBtn = (Button) itemView.findViewById(R.id.return_management_return_date_btn);
        }
    }

    private class PHPDownload extends AsyncTask<String, String, String> {

        private String Korea_Name;
        private String Color;
        private String Size;
        private String Count;
        private String Order_Date;
        private String Return_Date;
        private String Member_Id;

        @Override
        protected String doInBackground(String... params) {

            StringBuilder jsonHtml = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            String line = br.readLine();
                            if (line == null) break;
                            jsonHtml.append(line + " \n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonHtml.toString();
        }

        @Override
        protected void onPostExecute(String str) {
            listItem.clear();
            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Korea_Name = jo.getString("Korea_Name");
                    Color = jo.getString("Color");
                    Size = jo.getString("Size");
                    Count = jo.getString("Count");
                    Order_Date = jo.getString("Order_Date");
                    Return_Date = jo.getString("Return_Date");
                    Member_Id = jo.getString("Member_Id");
                    listItem.add(new PHPListItem(Korea_Name, Color, Size, Count, Order_Date, Return_Date, Member_Id));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 데이터 리스트에 추가
            changedItem.clear();
            for (int i = 0; i < listItem.size(); i++) {
                if (memberId.equals(listItem.get(i).getData(6))) {
                        changedItem.add(new ReturnMItem(listItem.get(i).getData(0), listItem.get(i).getData(1), listItem.get(i).getData(2), listItem.get(i).getData(3), listItem.get(i).getData(4), listItem.get(i).getData(5)));
                }
            }
        }
    }
}