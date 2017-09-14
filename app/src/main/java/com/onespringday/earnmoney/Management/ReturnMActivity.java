package com.onespringday.earnmoney.Management;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.onespringday.earnmoney.Inquiry.OrderActivity;
import com.onespringday.earnmoney.Inquiry.OrderDateDialog;
import com.onespringday.earnmoney.Inquiry.ProductActivity;
import com.onespringday.earnmoney.Inquiry.ProfitActivity;
import com.onespringday.earnmoney.Inquiry.ReturnActivity;
import com.onespringday.earnmoney.Inquiry.SalesActivity;
import com.onespringday.earnmoney.PHPListItem;
import com.onespringday.earnmoney.R;
import com.onespringday.earnmoney.Registration.ProductRActivity;
import com.onespringday.earnmoney.Search.ProductSearchActivity;
import com.onespringday.earnmoney.Search.ProductSearchDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by yoseong on 2017-08-27.
 */

public class ReturnMActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Recycler View 관련 선언
    private RecyclerView addRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ReturnMAddListAdapter addAdapter;
    private List<ReturnMItem> addData;

    // 상품 정보 불러오기
    private ArrayList<PHPListItem> returnAddListItem = new ArrayList<PHPListItem>();
    private PHPReturnAddDownload returnAddTask;

    // 반품 정보 Recycler View 관련 선언
    private RecyclerView listRecyclerView;
    private LinearLayoutManager listLinearLayoutManager;
    private ReturnMListAdapter listAdapter;
    private List<ReturnMItem> listData;

    // 반품 정보 불러오기
    private ArrayList<PHPListItem> returnListItem = new ArrayList<PHPListItem>();
    private PHPReturnListDownload returnTask;

    // Member ID 불러오기 위한 Shared Preference 선언
    private SharedPreferences pref_member;
    private String memberId;
    private String Logo_Photo;
    private String Shop_Name;
    private String Shop_Url;

    private String todayDate;

    // 레이아웃 뷰 선언
    private Button selectDateBtn;
    private TextView returnDate;
    private TextView returnDateDay;;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_management);

        // 툴바 셋팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_return_management);
        toolbar.setTitle("반품관리");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);

        // DrawerLayout 셋팅
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.return_management_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // NavigationView 셋팅
        NavigationView navigationView = (NavigationView) findViewById(R.id.return_management_nav_view);
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

        // 오늘 날짜 불러오기
        SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance(); // 오늘날짜
        todayDate = today.format(calendar.getTime());

        // 탭 호스트 셋팅
        tabHost = (TabHost) findViewById(R.id.return_tab);
        tabHost.setup();

        TabHost.TabSpec listStock = tabHost.newTabSpec("list").setContent(R.id.list_return).setIndicator("반품 조회");
        tabHost.addTab(listStock);

        TabHost.TabSpec insertStock = tabHost.newTabSpec("update").setContent(R.id.add_return).setIndicator("반품 입력");
        tabHost.addTab(insertStock);

        // PHP로 MySQL에서 데이터 불러와 연결
        returnAddTask = new PHPReturnAddDownload();
        returnAddTask.execute("http://yoseong92.cafe24.com/earnmoney/get_return_m_info.php");

        returnTask = new PHPReturnListDownload();
        returnTask.execute("http://yoseong92.cafe24.com/earnmoney/get_return_info.php");

        selectDateBtn = (Button) findViewById(R.id.select_return_management_date_btn);
        returnDate = (TextView) findViewById(R.id.return_management_date);
        returnDateDay = (TextView) findViewById(R.id.return_management_date_day);

        if (returnDate.getText().toString().equals("")) {
            returnDate.setText(todayDate);
        }

        try {
            returnDateDay.setText("(" + getDateDay(todayDate, "yyyy/MM/dd") + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 새로운 업데이트 된 정보를 불러오기
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("list")) {
                    PHPReturnListDownload task2 = new PHPReturnListDownload();
                    task2.execute("http://yoseong92.cafe24.com/earnmoney/get_return_info.php");
                }
            }
        });

    }

    /**
     * Navigation View를 위한 코드들 (DrawerLayout)
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.return_management_drawer_layout);
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
            Intent go_to_order_return = new Intent(getApplicationContext(), ReturnActivity.class);
            startActivity(go_to_order_return);
            finish();
        } else if (id == R.id.order_management) {
            Intent go_to_order_management = new Intent(getApplicationContext(), OrderMActivity.class);
            startActivity(go_to_order_management);
            finish();
        } else if (id == R.id.now_stock_management) {
            Intent go_to_now_stock = new Intent(getApplicationContext(), NowStockActivity.class);
            startActivity(go_to_now_stock);
            finish();
        } else if (id == R.id.order_stock_management) {
            Intent go_to_order_stock = new Intent(getApplicationContext(), OrderStockActivity.class);
            startActivity(go_to_order_stock);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.return_management_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 날짜에 해당하는 요일 구하기
    public static String getDateDay(String date, String dateType) throws Exception {

        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }
        return day;
    }

    // 상품정보 불러오기
    private class PHPReturnAddDownload extends AsyncTask<String, String, String> {
        // 진행중 원형 spinner
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
            returnAddListItem.clear();
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
                    returnAddListItem.add(new PHPListItem(Korea_Name, Color, Size, Count, Order_Date, Return_Date, Member_Id));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            addData = new ArrayList<>();
            ArrayList<ReturnMItem> recentListItem = new ArrayList<>();
            String product_name = "";
            String product_color = "";
            String product_size = "";
            if (memberId.equals(returnAddListItem.get(returnAddListItem.size()-1).getData(6))) {
                product_name = returnAddListItem.get(returnAddListItem.size()-1).getData(0);
                product_color = returnAddListItem.get(returnAddListItem.size()-1).getData(1);
                product_size = returnAddListItem.get(returnAddListItem.size()-1).getData(2);
                recentListItem.add(new ReturnMItem(product_name, product_color, product_size, returnAddListItem.get(returnAddListItem.size()-1).getData(3), returnAddListItem.get(returnAddListItem.size()-1).getData(4),
                        returnAddListItem.get(returnAddListItem.size()-1).getData(5)));
            }


            for (int i = returnAddListItem.size() - 2; i >= 0; i--) {
                if (memberId.equals(returnAddListItem.get(i).getData(6))) {
                    if (!(product_name.equals(returnAddListItem.get(i).getData(0)) && product_color.equals(returnAddListItem.get(i).getData(1)) && product_size.equals(returnAddListItem.get(i).getData(2)))) {
                        recentListItem.add(new ReturnMItem(returnAddListItem.get(i).getData(0), returnAddListItem.get(i).getData(1), returnAddListItem.get(i).getData(2),
                                returnAddListItem.get(returnAddListItem.size()-1).getData(3), returnAddListItem.get(returnAddListItem.size()-1).getData(4), returnAddListItem.get(returnAddListItem.size()-1).getData(5)));
                        product_name = returnAddListItem.get(i).getData(0);
                        product_color = returnAddListItem.get(i).getData(1);
                        product_size = returnAddListItem.get(i).getData(2);
                    }
                }
            }

            // 역순으로 data에 데이터를 추가시켜줌
            for (int i = recentListItem.size() - 1; i >= 0 ; i--) {
                if (todayDate.equals(recentListItem.get(i).getOrderDate())) {
                    addData.add(new ReturnMItem(recentListItem.get(i).getReturnName(), recentListItem.get(i).getReturnColor(), recentListItem.get(i).getReturnSize(), recentListItem.get(i).getReturnCount(),
                            recentListItem.get(i).getOrderDate(), recentListItem.get(i).getReturnDate()));
                } else {
                    addData.add(new ReturnMItem(recentListItem.get(i).getReturnName(), recentListItem.get(i).getReturnColor(), recentListItem.get(i).getReturnSize(), "0",
                            recentListItem.get(i).getOrderDate(), recentListItem.get(i).getReturnDate()));
                }
                System.out.println("addData: "+recentListItem.get(i).getReturnName());
            }


            // 어댑터에 data 넘겨줌
            addAdapter = new ReturnMAddListAdapter(getApplicationContext(), addData);

            // LinearLayoutManager 설정
            mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // Recycler View 설정
            addRecyclerView = (RecyclerView) findViewById(R.id.return_add_recyclerView);
            addRecyclerView.setHasFixedSize(true);
            addRecyclerView.setLayoutManager(mLinearLayoutManager);

            // 어댑터 연결
            addRecyclerView.setAdapter(addAdapter);
            addAdapter.notifyDataSetChanged();

            /*
            for (int i = 0; i < returnAddListItem.size(); i++) {
                if (memberId.equals(returnAddListItem.get(i).getData(6))) {
                    addData.add(new ReturnMItem(returnAddListItem.get(i).getData(0), returnAddListItem.get(i).getData(1), returnAddListItem.get(i).getData(2), returnAddListItem.get(i).getData(3),
                            returnAddListItem.get(i).getData(4), returnAddListItem.get(i).getData(5)));
                }
            }

            */

        }
    }

    // 반품 정보 불러오기
    private class PHPReturnListDownload extends AsyncTask<String, String, String> {

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
            returnListItem.clear();
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
                    returnListItem.add(new PHPListItem(Korea_Name, Color, Size, Count, Order_Date, Return_Date, Member_Id));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 데이터 리스트에 추가
            listData= new ArrayList<>();
            listData.clear();
            // 날짜 선택 다이얼로그 불러오기
            final OrderDateDialog orderDateDialog = new OrderDateDialog(ReturnMActivity.this);

            selectDateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            int dateCheckFlag = 0;
                            if (TextUtils.isEmpty(orderDateDialog.getSelectDate())) {
                                orderDateDialog.setSelectDate(todayDate);
                            }
                            returnDate.setText(orderDateDialog.getSelectDate());
                            try {
                                returnDateDay.setText("(" + getDateDay(orderDateDialog.getSelectDate(), "yyyy/MM/dd") + ")");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            listData.clear();
                            for (int i = 0; i < returnListItem.size(); i++) {
                                if (memberId.equals(returnListItem.get(i).getData(6))) {
                                    if (orderDateDialog.getSelectDate().equals(returnListItem.get(i).getData(5))) {
                                        listData.add(new ReturnMItem(returnListItem.get(i).getData(0), returnListItem.get(i).getData(1), returnListItem.get(i).getData(2), returnListItem.get(i).getData(3),
                                                returnListItem.get(i).getData(4), returnListItem.get(i).getData(5)));
                                    } else {
                                        dateCheckFlag++;
                                    }
                                }
                            }
                            if (returnListItem.size() == dateCheckFlag) {
                                Toast.makeText(getApplicationContext(), "선택하신 날짜에 반품리스트가 없습니다. 날짜를 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                                //listData.add(new ReturnMItem("", "", "", "", "", ""));
                            }
                            // 리스트의 데이터를 변경시켜줌
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                    orderDateDialog.show();
                }
            });
            if (returnDate.getText().toString().equals(todayDate)) {
                for (int i = 0; i < returnListItem.size(); i++) {
                    if (memberId.equals(returnListItem.get(i).getData(6)) && returnDate.getText().toString().equals(returnListItem.get(i).getData(5))) {
                        listData.add(new ReturnMItem(returnListItem.get(i).getData(0), returnListItem.get(i).getData(1), returnListItem.get(i).getData(2), returnListItem.get(i).getData(3), returnListItem.get(i).getData(4),
                                returnListItem.get(i).getData(5)));
                    }
                }
            }
            // 어댑터에 data 넘겨줌
            listAdapter = new ReturnMListAdapter(getApplicationContext(), listData);

            // LinearLayoutManager 설정
            listLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
            listLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            // Recycler View 설정
            listRecyclerView = (RecyclerView) findViewById(R.id.return_list_recyclerView);
            listRecyclerView.setHasFixedSize(true);
            listRecyclerView.setLayoutManager(listLinearLayoutManager);

            // 어댑터 연결
            listRecyclerView.setAdapter(listAdapter);
        }
    }
}
