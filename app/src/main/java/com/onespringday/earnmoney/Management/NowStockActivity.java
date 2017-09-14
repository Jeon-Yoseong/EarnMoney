package com.onespringday.earnmoney.Management;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.onespringday.earnmoney.Inquiry.OrderActivity;
import com.onespringday.earnmoney.Inquiry.ProductActivity;
import com.onespringday.earnmoney.Inquiry.ProfitActivity;
import com.onespringday.earnmoney.Inquiry.ReturnActivity;
import com.onespringday.earnmoney.Inquiry.SalesActivity;
import com.onespringday.earnmoney.PHPListItem;
import com.onespringday.earnmoney.Search.ProductSearchActivity;
import com.onespringday.earnmoney.Search.ProductSearchDialog;
import com.onespringday.earnmoney.R;
import com.onespringday.earnmoney.Registration.ProductRActivity;

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
 * Created by yoseong on 2017-08-06.
 */

public class NowStockActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // 재고 수량 입력 Recycler View 관련 선언
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private NowStockAddListAdapter adapter;
    private List<NowStockItem> data;

    // 재고 수량 리스트 Recycler View 관련 선언
    private RecyclerView listRecyclerView;
    private LinearLayoutManager listLinearLayoutManager;
    private NowStockListAdapter listAdapter;

    // Member ID 불러오기 위한 Shared Preference 선언
    private SharedPreferences pref_member;
    private String memberId;
    private String Logo_Photo;
    private String Shop_Name;
    private String Shop_Url;

    // 상품 정보 불러오기
    private ArrayList<PHPListItem> listItem = new ArrayList<PHPListItem>();
    private PHPDownload task;

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_stock);

        // 툴바 셋팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_now_stock);
        toolbar.setTitle("현재재고관리");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);

        // DrawerLayout 셋팅
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.now_stock_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // NavigationView 셋팅
        NavigationView navigationView = (NavigationView) findViewById(R.id.now_stock_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //  Member ID 불러오기 위한 Shared Preference
        pref_member = getSharedPreferences("pref_member", 0);
        memberId = pref_member.getString("id", "");
        Logo_Photo = pref_member.getString("logo", "");
        Shop_Name = pref_member.getString("name", "");
        Shop_Url = pref_member.getString("url", "");

        // 이미지와 텍스트를 사용자에 맞게 변환
        View nav_header_view = navigationView.getHeaderView(0);

        ImageView nav_header_image = (ImageView) nav_header_view.findViewById(R.id.nav_image);
        TextView nav_header_name = (TextView) nav_header_view.findViewById(R.id.nav_name);
        TextView nav_header_url = (TextView) nav_header_view.findViewById(R.id.nav_url);

        nav_header_image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(getApplicationContext()).load(Logo_Photo).into(nav_header_image);

        nav_header_name.setText(Shop_Name);
        nav_header_url.setText(Shop_Url);

        // 탭 호스트 셋팅
        tabHost = (TabHost) findViewById(R.id.now_stock_tab);
        tabHost.setup();

        TabHost.TabSpec listStock = tabHost.newTabSpec("list").setContent(R.id.list_stock).setIndicator("재고 조회");
        tabHost.addTab(listStock);

        TabHost.TabSpec insertStock = tabHost.newTabSpec("update").setContent(R.id.update_stock).setIndicator("재고 수정");
        tabHost.addTab(insertStock);

        // PHP로 MySQL에서 데이터 불러와 연결
        task = new PHPDownload();
        task.execute("http://yoseong92.cafe24.com/earnmoney/get_stock_info.php");

        // 새로운 업데이트 된 정보를 불러오기
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("list")) {
                    PHPDownload task2 = new PHPDownload();
                    task2.execute("http://yoseong92.cafe24.com/earnmoney/get_stock_info.php");
                }
            }
        });

    }

    /**
     * Navigation View를 위한 코드들 (DrawerLayout)
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.now_stock_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.product_registration) {
            Intent go_to_product_registration= new Intent(getApplicationContext(), ProductRActivity.class);
            startActivity(go_to_product_registration);
            finish();
        } else if (id == R.id.order_inquiry) {
            Intent go_to_order= new Intent(getApplicationContext(), OrderActivity.class);
            startActivity(go_to_order);
            finish();
        } else if (id == R.id.product_inquiry) {
            Intent go_to_product = new Intent(getApplicationContext(), ProductActivity.class);
            startActivity(go_to_product);
            finish();
        } else if (id == R.id.sales_inquiry) {
            Intent go_to_sales = new Intent(getApplicationContext(), SalesActivity.class);
            startActivity(go_to_sales);
            finish();
        } else if (id == R.id.profit_inquiry) {
            Intent go_to_profit = new Intent(getApplicationContext(), ProfitActivity.class);
            startActivity(go_to_profit);
            finish();
        } else if (id == R.id.return_inquiry) {
            Intent go_to_return = new Intent(getApplicationContext(), ReturnActivity.class);
            startActivity(go_to_return );
            finish();
        } else if (id == R.id.order_management) {
            Intent go_to_order_management = new Intent(getApplicationContext(), OrderMActivity.class);
            startActivity(go_to_order_management);
            finish();
        } else if (id == R.id.now_stock_management) {

        } else if (id == R.id.order_stock_management) {
            Intent go_to_order_stock = new Intent(getApplicationContext(), OrderStockActivity.class);
            startActivity(go_to_order_stock);
            finish();
        } else if (id == R.id.return_management){
            Intent go_to_return_management = new Intent(getApplicationContext(), ReturnMActivity.class);
            startActivity(go_to_return_management);
            finish();
        } else if (id == R.id.product_search) {
            // spinner에 넣을 값을 생성자로 넣어줌
            String[] str = getResources().getStringArray(R.array.spinnerArray);
            int spinneritem = android.R.layout.simple_spinner_item;
            //상품 검색 눌렀을 때 다이얼로그 뜨게 함
            final ProductSearchDialog customDialog = new ProductSearchDialog(this, str, spinneritem);

            customDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {

                }
            });
            customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent go_to_product_search = new Intent(getApplicationContext(), ProductSearchActivity.class);
                    go_to_product_search.putExtra("Member_Id", memberId);
                    if (customDialog.getProductKind().equals("한글 상품명")) {
                        go_to_product_search.putExtra("Korea_Name", customDialog.getProductName());
                        go_to_product_search.putExtra("Name", "");
                    } else if (customDialog.getProductKind().equals("상품명")) {
                        go_to_product_search.putExtra("Korea_Name", "");
                        go_to_product_search.putExtra("Name", customDialog.getProductName());
                    }
                    // 상품명을 입력하지 않고 다이얼로그 종료 시 오류 발생해 넣어준 코드, 널이 아닐경우만 intent한다.
                    if (!TextUtils.isEmpty(customDialog.getProductName())) {
                        startActivity(go_to_product_search);
                    }
                }
            });
            customDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.now_stock_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 주문정보 불러오기
    private class PHPDownload extends AsyncTask<String, String, String> {

        private String Korea_Name;
        private String Color;
        private String Size;
        private String Stock_Count;
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
        protected void onPostExecute(final String str) {
            // 재 실행시 리스트 비워주기 위해
            listItem.clear();
            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Korea_Name = jo.getString("Korea_Name");
                    Color = jo.getString("Color");
                    Size = jo.getString("Size");
                    Stock_Count = jo.getString("Stock_Count");
                    Member_Id = jo.getString("Member_Id");
                    listItem.add(new PHPListItem(Korea_Name, Color, Size, Stock_Count, Member_Id));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /**
             * Recycler View
             */
            // 데이터 리스트에 추가
            data= new ArrayList<>();
            data.clear();
            for (int i = 0; i < listItem.size(); i++) {
                if(memberId.equals(listItem.get(i).getData(4))) {
                    data.add(new NowStockItem(listItem.get(i).getData(0), listItem.get(i).getData(1), listItem.get(i).getData(2), listItem.get(i).getData(3)));
                }
            }
/*
            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    if (tabId.equals("list")) {
                        data.clear();
                        for (int i = 0; i < listItem.size(); i++) {
                            if(memberId.equals(listItem.get(i).getData(4))) {
                                data.add(new NowStockItem(listItem.get(i).getData(0), listItem.get(i).getData(1), listItem.get(i).getData(2), listItem.get(i).getData(3)));
                            }
                        }
                        listAdapter.notifyDataSetChanged();
                    }
                }
            });*/

            // 어댑터에 data 넘겨줌
            adapter = new NowStockAddListAdapter(getApplicationContext(), data);

            // LinearLayoutManager 설정
            mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // Recycler View 설정
            recyclerView = (RecyclerView) findViewById(R.id.now_stock_add_recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(mLinearLayoutManager);

            // 어댑터 연결
            recyclerView.setAdapter(adapter);


            // 어댑터에 data 넘겨줌
            listAdapter = new NowStockListAdapter(getApplicationContext(), data);

            // LinearLayoutManager 설정
            listLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
            listLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            listRecyclerView = (RecyclerView) findViewById(R.id.now_stock_recyclerView);
            listRecyclerView.setHasFixedSize(true);
            listRecyclerView.setLayoutManager(listLinearLayoutManager);

            listRecyclerView.setAdapter(listAdapter);

        }
    }
}
