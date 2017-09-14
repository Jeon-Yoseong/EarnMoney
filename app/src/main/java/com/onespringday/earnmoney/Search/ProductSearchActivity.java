package com.onespringday.earnmoney.Search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.onespringday.earnmoney.Inquiry.ProductItem;
import com.onespringday.earnmoney.Inquiry.ProductListAdapter;
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
import java.util.List;

/**
 * Created by yoseong on 2017-08-17.
 */

public class ProductSearchActivity extends AppCompatActivity {

    // Recycler View 관련 선언
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProductListAdapter adapter;
    private List<ProductItem> data;

    // Member ID 불러오기 위한 Shared Preference 선언
    private SharedPreferences pref_member;
    private String memberId;

    // 상품 정보 불러오기
    private ArrayList<PHPListItem> listItem = new ArrayList<PHPListItem>();
    private PHPDownload task;

    // Search Dialog로 넘어온 값
    private String Get_Member_Id = "";
    private String Get_Name = "";
    private String Get_Korea_Name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        // 리스트에서 클릭한 아이템 정보 받아옴
        Intent getInfo = getIntent();
        Get_Member_Id = getInfo.getStringExtra("Member_Id");
        Get_Korea_Name = getInfo.getStringExtra("Korea_Name");
        Get_Name = getInfo.getStringExtra("Name");



        //  Member ID 불러오기 위한 Shared Preference
        pref_member = getSharedPreferences("pref_member", 0);
        memberId = pref_member.getString("id", "");

        // PHP로 MySQL에서 데이터 불러와 연결
        task = new PHPDownload();
        task.execute("http://yoseong92.cafe24.com/earnmoney/get_product_info.php");

    }

    // 상품정보 불러오기
    private class PHPDownload extends AsyncTask<String, String, String> {

        private String Name;
        private String Korea_Name;
        private String Price;
        private String Korea_Unit_Cost;
        private String Profit;
        private String Color;
        private String Size;
        private String Photo;
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
                    Price = jo.getString("Price");
                    Korea_Unit_Cost = jo.getString("Korea_Unit_Cost");
                    Color = jo.getString("Color");
                    Size = jo.getString("Size");
                    Photo = jo.getString("Photo");
                    Member_Id = jo.getString("Member_Id");
                    listItem.add(new PHPListItem(Name, Korea_Name, "", Korea_Unit_Cost, Price, "", "", "", Color, Size, "", Photo, Member_Id));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Recycler View 관련 코드

            // DB에서 불러온 데이터를 리스트에 연결되는 ArrayList에 저장
            data = new ArrayList<>();
            for(int i = 0; i < listItem.size(); i++) {
                if (!Get_Name.equals("")) {
                    if (memberId.equals(listItem.get(i).getData(12)) && listItem.get(i).getData(0).contains(Get_Name)) {
                        Profit = String.valueOf(Integer.parseInt(listItem.get(i).getData(4)) - (Integer.parseInt(listItem.get(i).getData(4)) / 10) - Integer.parseInt(listItem.get(i).getData(3)));
                        data.add(new ProductItem(listItem.get(i).getData(11), listItem.get(i).getData(1), listItem.get(i).getData(4), listItem.get(i).getData(3), Profit, listItem.get(i).getData(8),
                                listItem.get(i).getData(9), listItem.get(i).getData(12)));
                    }
                }
                if (!Get_Korea_Name.equals("")) {
                    if (memberId.equals(listItem.get(i).getData(12)) && listItem.get(i).getData(1).contains(Get_Korea_Name)) {
                        Profit = String.valueOf(Integer.parseInt(listItem.get(i).getData(4)) - (Integer.parseInt(listItem.get(i).getData(4)) / 10) - Integer.parseInt(listItem.get(i).getData(3)));
                        data.add(new ProductItem(listItem.get(i).getData(11), listItem.get(i).getData(1), listItem.get(i).getData(4), listItem.get(i).getData(3), Profit, listItem.get(i).getData(8),
                                listItem.get(i).getData(9), listItem.get(i).getData(12)));
                    }
                }

            }

            // 어댑터에 data 넘겨줌
            adapter = new ProductListAdapter(getApplicationContext(), data);

            // LinearLayoutManager 설정
            mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // Recycler View 설정
            recyclerView = (RecyclerView) findViewById(R.id.product_search_recyclerview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLinearLayoutManager);

            // 어댑터 연결
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
