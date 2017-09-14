package com.onespringday.earnmoney.Inquiry;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.onespringday.earnmoney.PHPListItem;
import com.onespringday.earnmoney.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by yoseong on 2017-08-13.
 */

public class ProductDetailActivity extends AppCompatActivity {

    // 리스트에서 클릭한 아이템 정보 선언
    private String Get_Member_Id;
    private String Get_Korea_Name;
    private String Get_Color;
    private String Get_Size;

    // 레이아웃의 뷰 선언
    private TextView koreanName;
    private ImageView photo;
    private TextView name;
    private TextView chinaUnitCost;
    private TextView chinaUnitCostKoreaConversion;
    private TextView koreaUnitCost;
    private TextView price;
    private TextView luckyToday;
    private TextView friend;
    private TextView bomb;
    private TextView surtax;
    private TextView profit;
    private TextView color;
    private TextView size;
    private TextView url;

    // 상품 정보 불러오기
    private ArrayList<PHPListItem> listItem = new ArrayList<PHPListItem>();
    private PHPDownload task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // 리스트에서 클릭한 아이템 정보 받아옴
        Intent getInfo = getIntent();
        Get_Member_Id = getInfo.getStringExtra("Member_Id");
        Get_Korea_Name = getInfo.getStringExtra("Korea_Name");
        Get_Color = getInfo.getStringExtra("Color");
        Get_Size = getInfo.getStringExtra("Size");

        // 레이아웃의 뷰 선언 셋팅
        koreanName = (TextView) findViewById(R.id.detail_product_korean_name);
        photo = (ImageView) findViewById(R.id.detail_product_photo);
        name = (TextView) findViewById(R.id.detail_product_name);
        chinaUnitCost = (TextView) findViewById(R.id.detail_product_china_unit_cost);
        chinaUnitCostKoreaConversion = (TextView) findViewById(R.id.detail_china_unit_cost_korea_conversion);
        koreaUnitCost = (TextView) findViewById(R.id.detail_korean_unit_cost);
        price = (TextView) findViewById(R.id.detail_product_price);
        luckyToday = (TextView) findViewById(R.id.detail_lucky_today_price);
        friend = (TextView) findViewById(R.id.detail_friend_price);
        bomb = (TextView) findViewById(R.id.detail_bomb_sale_price);
        surtax = (TextView) findViewById(R.id.detail_surtax);
        profit = (TextView) findViewById(R.id.detail_profit);
        color = (TextView) findViewById(R.id.detail_color);
        size = (TextView) findViewById(R.id.detail_size);
        url = (TextView) findViewById(R.id.detail_purchase_url);

        // PHP로 MySQL에서 데이터 불러와 연결
        task = new PHPDownload();
        task.execute("http://yoseong92.cafe24.com/earnmoney/get_product_info.php");
    }

    // 상품정보 불러오기
    private class PHPDownload extends AsyncTask<String, String, String> {

        private String Photo;
        private String Name;
        private String Korea_Name;
        private String China_Unit_Cost;
        private String China_Unit_Cost_Korea_Conversion;
        private String Korea_Unit_Cost;
        private String Price;
        private String LuckyToday;
        private String Friend;
        private String Bomb;
        private String Surtax;
        private String Profit;
        private String Color;
        private String Size;
        private String Url;
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
            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Name = jo.getString("Name");
                    Korea_Name = jo.getString("Korea_Name");
                    China_Unit_Cost = jo.getString("China_Unit_Cost");
                    Korea_Unit_Cost = jo.getString("Korea_Unit_Cost");
                    Price = jo.getString("Price");
                    LuckyToday = jo.getString("LuckyToday");
                    Friend = jo.getString("Friend");
                    Bomb = jo.getString("Bomb");
                    Color = jo.getString("Color");
                    Size = jo.getString("Size");
                    Url = jo.getString("Url");
                    Photo = jo.getString("Photo");
                    Member_Id = jo.getString("Member_Id");
                    listItem.add(new PHPListItem(Name, Korea_Name, China_Unit_Cost, Korea_Unit_Cost, Price, LuckyToday, Friend, Bomb, Color, Size, Url, Photo, Member_Id));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < listItem.size(); i++) {
                    if (Get_Member_Id.equals(listItem.get(i).getData(12)) && Get_Korea_Name.equals(listItem.get(i).getData(1)) && Get_Color.equals(listItem.get(i).getData(8)) && Get_Size.equals(listItem.get(i).getData(9))) {

                        China_Unit_Cost_Korea_Conversion = String.valueOf((Integer.parseInt(listItem.get(i).getData(2)) * 170));
                        Surtax = String.valueOf(Integer.parseInt(listItem.get(i).getData(4)) / 10);
                        Profit = String.valueOf(Integer.parseInt(listItem.get(i).getData(4)) - Integer.parseInt(Surtax) - Integer.parseInt(listItem.get(i).getData(3)));

                        name.setText("상품명: " + listItem.get(i).getData(0));
                        koreanName.setText(listItem.get(i).getData(1));
                        chinaUnitCost.setText("중국 단가: " + listItem.get(i).getData(2));
                        chinaUnitCostKoreaConversion.setText("환율적용 단가: " + China_Unit_Cost_Korea_Conversion);
                        koreaUnitCost.setText("한국 원가: " + listItem.get(i).getData(3));
                        price.setText("정상 판매가: " + listItem.get(i).getData(4));
                        luckyToday.setText("럭투 판매가: " + listItem.get(i).getData(5));
                        friend.setText("지인 판매가: " + listItem.get(i).getData(6));
                        bomb.setText("땡처리 판매가: " + listItem.get(i).getData(7));
                        surtax.setText("부가세: " + Surtax);
                        profit.setText("이익금: " + Profit);
                        color.setText("색상: " + listItem.get(i).getData(8));
                        size.setText("사이즈: " + listItem.get(i).getData(9));
                        url.setText("구입링크: " + listItem.get(i).getData(10));
                        Glide.with(getApplicationContext()).load(listItem.get(i).getData(11)).into(photo);
                    }
            }
        }
    }
}
