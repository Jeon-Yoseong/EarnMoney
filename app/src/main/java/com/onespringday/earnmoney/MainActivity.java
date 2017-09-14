package com.onespringday.earnmoney;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.onespringday.earnmoney.Inquiry.OrderActivity;
import com.onespringday.earnmoney.Inquiry.ProductActivity;
import com.onespringday.earnmoney.Inquiry.ProfitActivity;
import com.onespringday.earnmoney.Inquiry.ReturnActivity;
import com.onespringday.earnmoney.Inquiry.ReturnItem;
import com.onespringday.earnmoney.Inquiry.ReturnListDateItem;
import com.onespringday.earnmoney.Inquiry.SalesActivity;
import com.onespringday.earnmoney.Management.NowStockActivity;
import com.onespringday.earnmoney.Management.OrderMActivity;
import com.onespringday.earnmoney.Management.OrderStockActivity;
import com.onespringday.earnmoney.Management.ReturnMActivity;
import com.onespringday.earnmoney.Registration.ProductRActivity;
import com.onespringday.earnmoney.Search.ProductSearchActivity;
import com.onespringday.earnmoney.Search.ProductSearchDialog;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // 제목 TextView 선언
    private TextView order;
    private TextView sales;
    private TextView returnQuantity;
    private TextView profit;
    private TextView todayTop3;
    private TextView weeklyTop3;

    // 주문 관련 TextView 선언
    private TextView yesterdayOrderCount;
    private TextView todayOrderCount;
    private TextView weeklyOrderCount;

    // 매출 관련 TextView 선언
    private TextView yesterdaySalesPrice;
    private TextView todaySalesPrice;
    private TextView weeklySalesPrice;

    // 반품 관련 TextView 선언
    private TextView yesterdayReturnQuantity;
    private TextView todayReturnQuantity;
    private TextView weeklyReturnQuantity;

    // 순이익 관련 TextView 선언
    private TextView yesterdayProfitPrice;
    private TextView todayProfitPrice;
    private TextView weeklyProfitPrice;

    // 투데이 인기상품 관련 ImageView 선언
    private ImageView todayFirstProductPhoto;
    private ImageView todaySecondProductPhoto;
    private ImageView todayThirdProductPhoto;

    // 투데이 인기상품 관련 TextView 선언
    private TextView todayFirstProductName;
    private TextView todayFirstProductPrice;
    private TextView todaySecondProductName;
    private TextView todaySecondProductPrice;
    private TextView todayThirdProductName;
    private TextView todayThirdProductPrice;

    // 한 주간 인기상품 관련 ImageView 선언
    private ImageView weeklyFirstProductPhoto;
    private ImageView weeklySecondProductPhoto;
    private ImageView weeklyThirdProductPhoto;

    // 한 주간 인기상품 관련 TextView 선언
    private TextView weeklyFirstProductName;
    private TextView weeklyFirstProductPrice;
    private TextView weeklySecondProductName;
    private TextView weeklySecondProductPrice;
    private TextView weeklyThirdProductName;
    private TextView weeklyThirdProductPrice;

    // 백 버튼 2번 클릭시 종료 변수
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    // Member ID 불러오기 위한 Shared Preference 선언
    private SharedPreferences pref_member;
    private String memberId;
    private String Logo_Photo;
    private String Shop_Name;
    private String Shop_Url;

    // 상품 정보 불러오기
    private ArrayList<PHPListItem> listItem = new ArrayList<PHPListItem>();
    private PHPDownload task;
    private List<MainListItem> listdata;

    // 반품 정보 불러오기
    private ArrayList<PHPListItem> returnListItem = new ArrayList<PHPListItem>();
    private PHPReturnDownload returnTask;

    // 오늘 날짜
    private String todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Member ID 불러오기 위한 Shared Preference
        pref_member = getSharedPreferences("pref_member", 0);
        memberId = pref_member.getString("id", "");
        Logo_Photo = pref_member.getString("logo", "");
        Shop_Name = pref_member.getString("name", "");
        Shop_Url = pref_member.getString("url", "");

        // 툴바 셋팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("돈벌자");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);

        // DrawerLayout 셋팅
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // NavigationView 셋팅
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 이미지와 텍스트를 사용자에 맞게 변환
        View nav_header_view = navigationView.getHeaderView(0);

        ImageView nav_header_image = (ImageView) nav_header_view.findViewById(R.id.nav_image);
        TextView nav_header_name = (TextView) nav_header_view.findViewById(R.id.nav_name);
        TextView nav_header_url = (TextView) nav_header_view.findViewById(R.id.nav_url);

        nav_header_image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(getApplicationContext()).load(Logo_Photo).into(nav_header_image);

        nav_header_name.setText(Shop_Name);
        nav_header_url.setText(Shop_Url);

        // custom font 적용
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumGothic.ttf"));

        // 레이아웃의 뷰들 선언해주는 함수
        setLayoutView();

        // 어제, 오늘, 내일 날짜 구하기
        SimpleDateFormat yesterday = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); // 어제날짜
        String yesterdayDate = yesterday.format(calendar.getTime());

        SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        calendar = Calendar.getInstance(); // 오늘날짜
        todayDate = today.format(calendar.getTime());

        // 판매수량, 매출 순이익. top3 불러오기
        task = new PHPDownload();
        task.execute("http://yoseong92.cafe24.com/earnmoney/get_profit_info.php");

        // 반품 수량 불러오기
        returnTask = new PHPReturnDownload();
        returnTask.execute("http://yoseong92.cafe24.com/earnmoney/get_return_total_info.php");
    }

    @Override
    protected void onResume() {
        super.onResume();
        task = new PHPDownload();
        task.execute("http://yoseong92.cafe24.com/earnmoney/get_profit_info.php");
        returnTask = new PHPReturnDownload();
        returnTask.execute("http://yoseong92.cafe24.com/earnmoney/get_return_total_info.php");
    }

    // custom font 적용
    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

    /**
     * Navigation View를 위한 코드들 (DrawerLayout)
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
                finish();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "\'뒤로\'버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
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
            Intent go_to_setting = new Intent(getApplicationContext(), ExchangeRateSettingsActivity.class);
            startActivity(go_to_setting);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.product_registration) {
            Intent go_to_product_registration = new Intent(getApplication(), ProductRActivity.class);
            startActivity(go_to_product_registration);
        } else if (id == R.id.order_inquiry) {
            Intent go_to_order = new Intent(getApplication(), OrderActivity.class);
            startActivity(go_to_order);
        } else if (id == R.id.product_inquiry) {
            Intent go_to_product = new Intent(getApplicationContext(), ProductActivity.class);
            startActivity(go_to_product);
        } else if (id == R.id.sales_inquiry) {
            Intent go_to_sales = new Intent(getApplicationContext(), SalesActivity.class);
            startActivity(go_to_sales);
        } else if (id == R.id.profit_inquiry) {
            Intent go_to_profit = new Intent(getApplicationContext(), ProfitActivity.class);
            startActivity(go_to_profit);
        } else if (id == R.id.return_inquiry) {
            Intent go_to_return = new Intent(getApplicationContext(), ReturnActivity.class);
            startActivity(go_to_return);
        } else if (id == R.id.order_management) {
            Intent go_to_order_management = new Intent(getApplicationContext(), OrderMActivity.class);
            startActivity(go_to_order_management);
        } else if (id == R.id.now_stock_management) {
            Intent go_to_now_stock = new Intent(getApplicationContext(), NowStockActivity.class);
            startActivity(go_to_now_stock);
        } else if (id == R.id.order_stock_management) {
            Intent go_to_order_stock = new Intent(getApplicationContext(), OrderStockActivity.class);
            startActivity(go_to_order_stock);
        } else if (id == R.id.return_management){
            Intent go_to_return_management = new Intent(getApplicationContext(), ReturnMActivity.class);
            startActivity(go_to_return_management);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    // 해당 날짜보다 하루 전 날짜 구하기
    private String getYesterdayDate (String today) {
        int year, month, day;
        StringTokenizer st = new StringTokenizer(today, "/");
        year = Integer.parseInt(st.nextToken());
        month = Integer.parseInt(st.nextToken());
        day = Integer.parseInt(st.nextToken());

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        calendar.add(Calendar.DATE, -1); // 어제날짜
        SimpleDateFormat yesterday = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String yesterdayDate = yesterday.format(calendar.getTime());

        return yesterdayDate;
    }

    // 해당 날짜보다 일주일 전 날짜 구하기
    private String getWeekendAgoDate (String today) {
        int year, month, day;
        StringTokenizer st = new StringTokenizer(today, "/");
        year = Integer.parseInt(st.nextToken());
        month = Integer.parseInt(st.nextToken());
        day = Integer.parseInt(st.nextToken());

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day);
        calendar.add(Calendar.DATE, -7); // 7일 전 날짜
        SimpleDateFormat weekendAgo = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String weekendAgoDate = weekendAgo.format(calendar.getTime());

        return weekendAgoDate;
    }

    // 날짜별 상품 판매, 금액, 순이익 정보 불러오기
    private class PHPDownload extends AsyncTask<String, String, String> {

        private String Korea_Name;
        private String Product_Color; // Color 클래스 사용하기 위해 Product_Color라 선언
        private String Size;
        private String Count;
        private String Date;
        private String Price;
        private String Korea_Unit_Cost;
        private String Member_Id;
        private String Product_Photo;

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
                    Product_Color = jo.getString("Color");
                    Size = jo.getString("Size");
                    Count = jo.getString("Count");
                    Date = jo.getString("Date");
                    Price = jo.getString("Price");
                    Korea_Unit_Cost = jo.getString("Korea_Unit_Cost");
                    Member_Id = jo.getString("Member_Id");
                    Product_Photo = jo.getString("Product_Photo");
                    listItem.add(new PHPListItem(Korea_Name, Product_Color, Size, Count, Date, Price, Korea_Unit_Cost, Member_Id, Product_Photo));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listdata = new ArrayList<>();
            // ArrayList에 판매날짜와 수량, 금액 저장
            ArrayList<MainListItem> dateList = new ArrayList<>();
            //ArrayList에 해당 판매날짜의 총수량, 총금액 저장
            ArrayList<MainListItem> totalDateList = new ArrayList<>();
            // onResume을 위해 초기화 새로고침을 위해성
            listdata.clear();
            dateList.clear();
            totalDateList.clear();

            for (int i = listItem.size() - 1; i >= 0; i--) {
                if (listItem.get(i).getData(7).equals(memberId)) {
                    int count = Integer.parseInt(listItem.get(i).getData(3));
                    int price = Integer.parseInt(listItem.get(i).getData(5));
                    int profit = 0;
                    double surfix = price / 10;
                    if (count != 0) {
                        // 판매가*수량 - 0.1판매가*수량 - 한국원가*수량
                        profit = (price - (int) surfix - Integer.parseInt(listItem.get(i).getData(6))) * Integer.parseInt(listItem.get(i).getData(3));
                        // 판매가 계산
                        price = Integer.parseInt(listItem.get(i).getData(5)) * Integer.parseInt(listItem.get(i).getData(3));
                    }
                    dateList.add(new MainListItem(listItem.get(i).getData(0), listItem.get(i).getData(1), listItem.get(i).getData(4), String.valueOf(count), String.valueOf(price), String.valueOf(profit), listItem.get(i).getData(8)));
                }
            }
            // ArrayList 날짜 내림차순으로 정렬
            Collections.sort(dateList, new Comparator<MainListItem>() {
                @Override
                public int compare(MainListItem o1, MainListItem o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }

                @Override
                public boolean equals(Object obj) {
                    return false;
                }
            });

            // 데이터가 없을 경우에만 실행되도록 함
            if (dateList.size() != 0) {
                String preDate = dateList.get(0).getDate(); // 이전 데이터의 날짜, 첫 번째 날짜는 가장 최근의 날짜로 설정
                int sumCount = 0; // 날짜별 판매수량 합계
                int sumPrice = 0; // 날짜별 매출 합계
                int sumProfit = 0; // 날짜별 순이익 합계

                for (int i = 0; i < dateList.size(); i++) {
                    // 날짜가 같으면 수량과 금액을 더해준다. 날짜가 바뀌면 리스트에 합계 데이터를 추가하고 합계를 초기화 시켜주고 날짜도 초기화 해준다.
                    if (preDate.equals(dateList.get(i).getDate())) {
                        sumCount += Integer.parseInt(dateList.get(i).getCount());
                        sumPrice += Integer.parseInt(dateList.get(i).getPrice());
                        sumProfit += Integer.parseInt(dateList.get(i).getProfit());
                    } else {

                        totalDateList.add(new MainListItem(preDate, String.valueOf(sumCount), String.valueOf(sumPrice), String.valueOf(sumProfit)));

                        preDate = dateList.get(i).getDate();
                        sumCount = Integer.parseInt(dateList.get(i).getCount());
                        sumPrice = Integer.parseInt(dateList.get(i).getPrice());
                        sumProfit = Integer.parseInt(dateList.get(i).getProfit());
                    }
                    // 맨 마지막에 있는 날짜는 데이터 추가가 안돼서 추가로 코드 작성해줘 마지막 날짜도 추가해준다.
                    if (i == dateList.size() - 1) {
                        totalDateList.add(new MainListItem(preDate, String.valueOf(sumCount), String.valueOf(sumPrice), String.valueOf(sumProfit)));
                    }
                }

                // 해당 날짜가 비어있으면 0으로 데이터를 넣어준다.
                String tempTodayDate = todayDate;
                int j = 0;
                for (int i = 0; i < 7; i++) {
                    // totalDateList의 크기 이상으로 커지면 break를 해준다.
                    if (j == totalDateList.size()) {
                        break;
                    }
                    if (!totalDateList.get(j).getDate().equals(tempTodayDate)) {
                        totalDateList.add(new MainListItem(tempTodayDate, "0", "0", "0"));
                        tempTodayDate = getYesterdayDate(tempTodayDate);
                    } else {
                        tempTodayDate = getYesterdayDate(tempTodayDate);
                        j++;
                    }
                }
                // 날짜 내림차순으로 정렬해준다.
                Collections.sort(totalDateList, new Comparator<MainListItem>() {
                    @Override
                    public int compare(MainListItem o1, MainListItem o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                });


                // 주문수량
                // 오늘 주문 수량이 0일경우
                if (totalDateList.get(0).getCount().equals("0")) {
                    todayOrderCount.setText("0건"); // 오늘 주문수량
                    todaySalesPrice.setText("0원"); // 오늘 매출
                    todayProfitPrice.setText("0원"); // 오늘 순이익
                } else {
                    todayOrderCount.setText(totalDateList.get(0).getCount() + "건"); // 오늘 주문수량
                    todaySalesPrice.setText(totalDateList.get(0).getPrice() + "원"); // 오늘 매출
                    todayProfitPrice.setText(totalDateList.get(0).getProfit() + "원"); // 오늘 순이익
                }
                // 어제 주문수량이 0일경우
                if (totalDateList.size() == 1) {
                    yesterdayOrderCount.setText("0건"); // 어제 주문수량
                    yesterdaySalesPrice.setText("0원"); // 어제 매출
                    yesterdayProfitPrice.setText("0원"); // 어제 순이익
                } else {
                    yesterdayOrderCount.setText(totalDateList.get(1).getCount() + "건"); // 어제 주문수량
                    yesterdaySalesPrice.setText(totalDateList.get(1).getPrice() + "원"); // 어제 매출
                    yesterdayProfitPrice.setText(totalDateList.get(1).getProfit() + "원"); // 어제 순이익
                }
                /*
                if (totalDateList.get(1).getCount().equals("0")) {
                    yesterdayOrderCount.setText("0건"); // 어제 주문수량
                    yesterdaySalesPrice.setText("0원"); // 어제 매출
                    yesterdayProfitPrice.setText("0원"); // 어제 순이익
                } else {
                    yesterdayOrderCount.setText(totalDateList.get(1).getCount() + "건"); // 어제 주문수량
                    yesterdaySalesPrice.setText(totalDateList.get(1).getPrice() + "원"); // 어제 매출
                    yesterdayProfitPrice.setText(totalDateList.get(1).getProfit() + "원"); // 어제 순이익
                }
*/
                int weeklyCount = 0; // 일주일 주문수량
                for (int i = 0; i < totalDateList.size(); i++) {
                    if (i == 7) {
                        break;
                    }
                    weeklyCount += Integer.parseInt(totalDateList.get(i).getCount());
                }
                weeklyOrderCount.setText(String.valueOf(weeklyCount) + "건");

                int weeklyPrice = 0; // 일주일 매출
                for (int i = 0; i < totalDateList.size(); i++) {
                    if (i == 7) {
                        break;
                    }
                    weeklyPrice += Integer.parseInt(totalDateList.get(i).getPrice());
                }
                weeklySalesPrice.setText(String.valueOf(weeklyPrice) + "원");

                int weeklyProfit = 0; // 일주일 순이익
                for (int i = 0; i < totalDateList.size(); i++) {
                    if (i == 7) {
                        break;
                    }
                    weeklyProfit += Integer.parseInt(totalDateList.get(i).getProfit());
                }
                weeklyProfitPrice.setText(String.valueOf(weeklyProfit) + "원");

                // 투데이 인기상품 top3
                ArrayList<MainListItem> todayTop3 = new ArrayList<>();
                todayTop3.clear();// onResume을 위해 초기화


                int tmpCount = 0;
                int tmpPrice = 0;
                String tmpName = dateList.get(0).getName();
                String tmpColor = dateList.get(0).getColor();
                String tmpPhoto = dateList.get(0).getPhoto();
                // 사이즈 다른 경우 합치는 코드
                for (int i = 0; i < dateList.size(); i++) {
                    if (dateList.get(i).getDate().equals(todayDate)) {
                        if (tmpName.equals(dateList.get(i).getName()) && tmpColor.equals(dateList.get(i).getColor())) {
                            tmpCount += Integer.parseInt(dateList.get(i).getCount());
                            tmpPrice += Integer.parseInt(dateList.get(i).getPrice());
                        } else {
                            todayTop3.add(new MainListItem(tmpName, tmpColor, String.valueOf(tmpCount), String.valueOf(tmpPrice), tmpPhoto));
                            tmpName = dateList.get(i).getName();
                            tmpColor = dateList.get(i).getColor();
                            tmpCount = Integer.parseInt(dateList.get(i).getCount());
                            tmpPrice = Integer.parseInt(dateList.get(i).getPrice());
                            tmpPhoto = dateList.get(i).getPhoto();
                        }
                        if (dateList.size() -1 == i) {
                            todayTop3.add(new MainListItem(tmpName, tmpColor, String.valueOf(tmpCount), String.valueOf(tmpPrice), tmpPhoto));
                        }
                    }
                }


/*
                for (int i = 0; i < dateList.size(); i++) {
                    if (dateList.get(i).getDate().equals(todayDate)) {
                        todayTop3.add(new MainListItem(dateList.get(i).getName(), dateList.get(i).getColor(), dateList.get(i).getDate(), dateList.get(i).getCount(),
                                dateList.get(i).getPrice(), dateList.get(i).getProfit(), dateList.get(i).getPhoto()));
                    }
                }*/


                // 오늘 팔린 물품 종류가 3개 미만일 경우 나머지 0으로 처리
                int tempSize = todayTop3.size();
                if (tempSize < 3) {
                    for (int i = 0; i < 3 - tempSize; i++) {
                        Log.i("todayTop3SizeSSibal", ""+todayTop3.size());
                        todayTop3.add(new MainListItem("0", "0", "0", "0", "0", "0", "0"));
                    }
                }
                Log.i("todayTop3Size", ""+todayTop3.size());
                for (int i = 0; i < todayTop3.size(); i++) {
                    Log.i("todayTop3", todayTop3.get(i).getName()+","+todayTop3.get(i).getColor()+","+todayTop3.get(i).getCount()+","+todayTop3.get(i).getPrice());
                }
                Collections.sort(todayTop3, new Comparator<MainListItem>() {
                    @Override
                    public int compare(MainListItem o1, MainListItem o2) {
                        return o2.getCount().compareTo(o1.getCount());
                    }

                    @Override
                    public boolean equals(Object obj) {
                        return false;
                    }
                });

                // today top3 셋팅, 널일경우 아무것도 안나오도록 한다.
                int temp = 0;
                if (todayTop3.get(0).getCount().equals("0")) {
                    todayFirstProductName.setText("");
                    todayFirstProductPrice.setText("");
                } else {
                    temp = (int) Integer.parseInt(todayTop3.get(0).getPrice()) / Integer.parseInt(todayTop3.get(0).getCount());
                    todayFirstProductName.setText(todayTop3.get(0).getName() + "(" + todayTop3.get(0).getColor() + ")");
                    todayFirstProductPrice.setText(String.valueOf(temp) + "원");
                    Glide.with(getApplicationContext()).load(todayTop3.get(0).getPhoto()).into(todayFirstProductPhoto);
                }
                if (todayTop3.get(1).getCount().equals("0")) {
                    todaySecondProductName.setText("");
                    todaySecondProductPrice.setText("");
                } else {
                    temp = (int) Integer.parseInt(todayTop3.get(1).getPrice()) / Integer.parseInt(todayTop3.get(1).getCount());
                    todaySecondProductName.setText(todayTop3.get(1).getName() + "(" + todayTop3.get(1).getColor() + ")");
                    todaySecondProductPrice.setText(String.valueOf(temp) + "원");
                    Glide.with(getApplicationContext()).load(todayTop3.get(1).getPhoto()).into(todaySecondProductPhoto);
                }
                if (todayTop3.get(2).getCount().equals("0")) {
                    todayThirdProductName.setText("");
                    todayThirdProductPrice.setText("");;
                } else {
                    temp = (int) Integer.parseInt(todayTop3.get(2).getPrice()) / Integer.parseInt(todayTop3.get(2).getCount());
                    todayThirdProductName.setText(todayTop3.get(2).getName() + "(" + todayTop3.get(2).getColor() + ")");
                    todayThirdProductPrice.setText(String.valueOf(temp) + "원");
                    Glide.with(getApplicationContext()).load(todayTop3.get(2).getPhoto()).into(todayThirdProductPhoto);
                }


                // 한 주간 top3
                ArrayList<MainListItem> weekendTop3 = new ArrayList<>(); // 각 상품마다 각 날짜별 팔린 내용 arrayList
                ArrayList<MainListItem> totalWeekendTop3 = new ArrayList<>(); // 각 상품마다 7일간 팔린 수량을 다 더한 arrayList
                for (int i = 0; i < dateList.size(); i++) {
                    if (!getWeekendAgoDate(todayDate).equals(dateList.get(i).getDate())) {
                        weekendTop3.add(new MainListItem(dateList.get(i).getName(), dateList.get(i).getColor(), dateList.get(i).getDate(), dateList.get(i).getCount(), dateList.get(i).getPrice(),
                                dateList.get(i).getProfit(), dateList.get(i).getPhoto()));
                    } else {
                        break;
                    }
                }

                Collections.sort(weekendTop3, new Comparator<MainListItem>() {
                    @Override
                    public int compare(MainListItem o1, MainListItem o2) {
                        int flag;
                        flag = o2.getName().compareTo(o1.getName());
                        if (flag == 0) {
                            flag = o2.getColor().compareTo(o1.getColor());
                        }
                        return flag;
                    }
                });

                for (int i = 0; i < weekendTop3.size(); i++) {
                    Log.i("weekend", weekendTop3.get(i).getName()+","+weekendTop3.get(i).getColor()+","+weekendTop3.get(i).getCount());
                }

                String preName = weekendTop3.get(0).getName();
                String preColor = weekendTop3.get(0).getColor();
                String prePhoto = weekendTop3.get(0).getPhoto();
                sumCount = 0; // 각 상품별 7일간 총 판매량을 더해줄 변수
                sumPrice = 0;
                for (int i = 0; i < weekendTop3.size(); i++) {
                    if (preName.equals(weekendTop3.get(i).getName()) && preColor.equals(weekendTop3.get(i).getColor())) {
                        sumCount += Integer.parseInt(weekendTop3.get(i).getCount());
                        sumPrice += Integer.parseInt(weekendTop3.get(i).getPrice());
                    } else {
                        totalWeekendTop3.add(new MainListItem(preName, preColor, String.valueOf(sumCount), String.valueOf(sumPrice), prePhoto));
                        preName = weekendTop3.get(i).getName();
                        preColor = weekendTop3.get(i).getColor();
                        prePhoto = weekendTop3.get(i).getPhoto();
                        sumCount = Integer.parseInt(weekendTop3.get(i).getCount());
                        sumPrice = Integer.parseInt(weekendTop3.get(i).getPrice());
                    }

                    if (weekendTop3.size()-1 == i) {
                        totalWeekendTop3.add(new MainListItem(preName, preColor, String.valueOf(sumCount), String.valueOf(sumPrice), prePhoto));
                    }
                }

                // 판매 물품이 3개 미만일 경우 나머지 0으로 처리
                tempSize = totalWeekendTop3.size();
                if (tempSize < 3) {
                    for (int i = 0; i < 3 - tempSize; i++) {
                        totalWeekendTop3.add(new MainListItem("0", "0", "0", "0", "0"));
                    }
                }

                // 판매수량 내림차순으로 정렬
                Collections.sort(totalWeekendTop3, new Comparator<MainListItem>() {
                    @Override
                    public int compare(MainListItem o1, MainListItem o2) {
                        return Integer.parseInt(o1.getCount()) > Integer.parseInt(o2.getCount()) ? -1 : Integer.parseInt(o1.getCount()) < Integer.parseInt(o2.getCount()) ? 1:0;
                    }
                });

                // weekly top3 셋팅, 널일경우 아무것도 안나오도록 한다.
                temp = 0;
                if (totalWeekendTop3.get(0).getCount().equals("0")) {
                    weeklyFirstProductName.setText("");
                    weeklyFirstProductPrice.setText("");
                } else {
                    temp = (int) Integer.parseInt(totalWeekendTop3.get(0).getPrice()) / Integer.parseInt(totalWeekendTop3.get(0).getCount());
                    weeklyFirstProductName.setText(totalWeekendTop3.get(0).getName() + "(" + totalWeekendTop3.get(0).getColor() + ")");
                    weeklyFirstProductPrice.setText(String.valueOf(temp) + "원");
                    Glide.with(getApplicationContext()).load(totalWeekendTop3.get(0).getPhoto()).into(weeklyFirstProductPhoto);
                }
                if (totalWeekendTop3.get(1).getCount().equals("0")) {
                    weeklySecondProductName.setText("");
                    weeklySecondProductPrice.setText("");
                } else {
                    temp = (int) Integer.parseInt(totalWeekendTop3.get(1).getPrice()) / Integer.parseInt(totalWeekendTop3.get(1).getCount());
                    weeklySecondProductName.setText(totalWeekendTop3.get(1).getName() + "(" + totalWeekendTop3.get(1).getColor() + ")");
                    weeklySecondProductPrice.setText(String.valueOf(temp) + "원");
                    Glide.with(getApplicationContext()).load(totalWeekendTop3.get(1).getPhoto()).into(weeklySecondProductPhoto);
                }
                if (todayTop3.get(2).getCount().equals("0")) {
                    weeklyThirdProductName.setText("");
                    weeklyThirdProductPrice.setText("");
                } else {
                    temp = (int) Integer.parseInt(totalWeekendTop3.get(2).getPrice()) / Integer.parseInt(totalWeekendTop3.get(2).getCount());
                    weeklyThirdProductName.setText(totalWeekendTop3.get(2).getName() + "(" + totalWeekendTop3.get(2).getColor() + ")");
                    weeklyThirdProductPrice.setText(String.valueOf(temp) + "원");
                    Glide.with(getApplicationContext()).load(totalWeekendTop3.get(2).getPhoto()).into(weeklyThirdProductPhoto);
                }

                for (int i = 0; i < totalWeekendTop3.size(); i++) {
                    Log.i("totalWeekend", totalWeekendTop3.get(i).getName()+","+totalWeekendTop3.get(i).getColor()+","+totalWeekendTop3.get(i).getCount()+","+totalWeekendTop3.get(i).getPrice()+","+totalWeekendTop3.get(i).getPhoto());
                }

            }

        }
    }

    // 반품 정보 불러오기
    private class PHPReturnDownload extends AsyncTask<String, String, String> {

        private String Korea_Name;
        private String Product_Color;
        private String Size;
        private String Count;
        private String Order_Date;
        private String Return_Date;
        private String Price;
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
                    Product_Color = jo.getString("Color");
                    Size = jo.getString("Size");
                    Count = jo.getString("Count");
                    Order_Date = jo.getString("Order_Date");
                    Return_Date = jo.getString("Return_Date");
                    Price = jo.getString("Price");
                    Member_Id = jo.getString("Member_Id");
                    returnListItem.add(new PHPListItem(Korea_Name, Product_Color, Size, Count, Order_Date, Return_Date, Price, Member_Id));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // ArrayList에 반품날짜와 수량, 금액 저장
            ArrayList<ReturnListDateItem> dateList = new ArrayList<>();
            //ArrayList에 해당 반품날짜의 총수량, 총금액 저장
            ArrayList<ReturnListDateItem> totalDateList = new ArrayList<>();

            dateList.clear();
            totalDateList.clear();

            for (int i = returnListItem.size() - 1; i >= 0 ; i--) {
                if (memberId.equals(returnListItem.get(i).getData(7))) {
                    int count = Integer.parseInt(returnListItem.get(i).getData(3));
                    int price = Integer.parseInt(returnListItem.get(i).getData(3)) * Integer.parseInt(returnListItem.get(i).getData(6));
                    dateList.add(new ReturnListDateItem(returnListItem.get(i).getData(5), String.valueOf(count), String.valueOf(price)));
                }
            }
            // ArrayList 날짜 내림차순으로 정렬
            Collections.sort(dateList, new Comparator<ReturnListDateItem>() {
                @Override
                public int compare(ReturnListDateItem o1, ReturnListDateItem o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }

                @Override
                public boolean equals(Object obj) {
                    return false;
                }
            });

            // 데이터가 없을 경우에만 실행되도록 함
            if (dateList.size() != 0) {
                String preDate = dateList.get(0).getDate(); // 이전 데이터의 날짜, 첫 번째 날짜는 가장 최근의 날짜로 설정
                int sumCount = 0; // 날짜별 반품수량 합계
                int sumPrice = 0; // 날짜별 반품금액 합계

                for(int i = 0; i < dateList.size(); i++) {
                    // 날짜가 같으면 수량과 금액을 더해준다. 날짜가 바뀌면 리스트에 합계 데이터를 추가하고 합계를 초기화 시켜주고 날짜도 초기화 해준다.
                    if(preDate.equals(dateList.get(i).getDate())) {
                        sumCount += Integer.parseInt(dateList.get(i).getCount());
                        sumPrice += Integer.parseInt(dateList.get(i).getPrice());
                    } else {
                        try {
                            totalDateList.add(new ReturnListDateItem(preDate, String.valueOf(sumCount), String.valueOf(sumPrice)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        preDate = dateList.get(i).getDate();
                        sumCount = Integer.parseInt(dateList.get(i).getCount());
                        sumPrice = Integer.parseInt(dateList.get(i).getPrice());
                    }
                    // 맨 마지막에 있는 날짜는 데이터 추가가 안돼서 추가로 코드 작성해줘 마지막 날짜도 추가해준다.
                    if (i == dateList.size()-1) {
                        try {
                            totalDateList.add(new ReturnListDateItem(preDate, String.valueOf(sumCount), String.valueOf(sumPrice)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                // 해당 날짜가 비어있으면 0으로 데이터를 넣어준다.
                String tempTodayDate = todayDate;
                int j = 0;
                for (int i = 0; i < 14; i++) {
                    // totalDateList의 크기 이상으로 커지면 break를 해준다.
                    if (j == totalDateList.size()) {
                        break;
                    }
                    if (!totalDateList.get(j).getDate().equals(tempTodayDate)) {
                        totalDateList.add(new ReturnListDateItem(tempTodayDate, "0", "0"));
                        tempTodayDate = getYesterdayDate(tempTodayDate);
                    } else {
                        tempTodayDate = getYesterdayDate(tempTodayDate);
                        j++;
                    }
                }
                // 날짜 내림차순으로 정렬해준다.
                Collections.sort(totalDateList, new Comparator<ReturnListDateItem>() {
                    @Override
                    public int compare(ReturnListDateItem o1, ReturnListDateItem o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                });

                // 반품 수량
                if (totalDateList.get(0).getCount().equals("0")) {
                    todayReturnQuantity.setText("0건"); // 오늘 반품 수량
                    yesterdayReturnQuantity.setText("0건"); // 어제 반품 수량
                } else {
                    todayReturnQuantity.setText(totalDateList.get(0).getCount() + "건"); // 오늘 반품 수량
                    yesterdayReturnQuantity.setText(totalDateList.get(1).getCount() + "건"); // 어제 반품 수량
                }
                int weeklyCount = 0; // 일주일 반품수량
                for (int i = 0; i < totalDateList.size(); i++) {
                    if (i == 7) {
                        break;
                    }
                    weeklyCount += Integer.parseInt(totalDateList.get(i).getCount());
                }
                weeklyReturnQuantity.setText(String.valueOf(weeklyCount) + "건");
            }
        }
    }

    public void setLayoutView () {
        // 제목 TextView Bold
        order = (TextView) findViewById(R.id.order);
        sales = (TextView) findViewById(R.id.sales);
        returnQuantity = (TextView) findViewById(R.id.return_quantity);
        profit = (TextView) findViewById(R.id.profit);
        todayTop3 = (TextView) findViewById(R.id.today_top_3);
        weeklyTop3 = (TextView) findViewById(R.id.weekly_top_3);

        order.setPaintFlags(order.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        sales.setPaintFlags(sales.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        returnQuantity.setPaintFlags(returnQuantity.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        profit.setPaintFlags(profit.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        todayTop3.setPaintFlags(todayTop3.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        weeklyTop3.setPaintFlags(weeklyTop3.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        // 주문 관련 TextView 선언
        yesterdayOrderCount = (TextView) findViewById(R.id.yesterday_order_count);
        todayOrderCount = (TextView) findViewById(R.id.today_order_count);
        weeklyOrderCount = (TextView) findViewById(R.id.weekly_order_count);

        // 매출 관련 TextView 선언
        yesterdaySalesPrice = (TextView) findViewById(R.id.yesterday_sales_price);
        todaySalesPrice = (TextView) findViewById(R.id.today_sales_price);
        weeklySalesPrice = (TextView) findViewById(R.id.weekly_sales_price);

        // 반품 관련 TextView 선언
        yesterdayReturnQuantity = (TextView) findViewById(R.id.yesterday_return_quantity);
        todayReturnQuantity = (TextView) findViewById(R.id.today_return_count);
        weeklyReturnQuantity = (TextView) findViewById(R.id.weekly_return_count);

        // 순이익 관련 TextView 선언
        yesterdayProfitPrice = (TextView) findViewById(R.id.yesterday_profit_price);
        todayProfitPrice = (TextView) findViewById(R.id.today_profit_price);
        weeklyProfitPrice = (TextView) findViewById(R.id.weekly_profit_price);

        // 투데이 인기상품 관련 ImageView 선언
        todayFirstProductPhoto = (ImageView) findViewById(R.id.today_top_3_first);
        todaySecondProductPhoto = (ImageView) findViewById(R.id.today_top_3_second);
        todayThirdProductPhoto = (ImageView) findViewById(R.id.today_top_3_third);

        // 투데이 인기상품 관련 TextView 선언
        todayFirstProductName = (TextView) findViewById(R.id.first_product_name);
        todayFirstProductPrice = (TextView) findViewById(R.id.first_product_price);
        todaySecondProductName = (TextView) findViewById(R.id.second_product_name);
        todaySecondProductPrice = (TextView) findViewById(R.id.second_product_price);
        todayThirdProductName = (TextView) findViewById(R.id.third_product_name);
        todayThirdProductPrice = (TextView) findViewById(R.id.third_product_price);

        // 한 주간 인기상품 관련 ImageView 선언
        weeklyFirstProductPhoto = (ImageView) findViewById(R.id.weekly_top_3_first);
        weeklySecondProductPhoto = (ImageView) findViewById(R.id.weekly_top_3_second);
        weeklyThirdProductPhoto = (ImageView) findViewById(R.id.weekly_top_3_third);

        // 한 주간 인기상품 관련 TextView 선언
        weeklyFirstProductName = (TextView) findViewById(R.id.weekly_first_product_name);
        weeklyFirstProductPrice = (TextView) findViewById(R.id.weekly_first_product_price);
        weeklySecondProductName = (TextView) findViewById(R.id.weekly_second_product_name);
        weeklySecondProductPrice = (TextView) findViewById(R.id.weekly_second_product_price);
        weeklyThirdProductName = (TextView) findViewById(R.id.weekly_third_product_name);
        weeklyThirdProductPrice = (TextView) findViewById(R.id.weekly_third_product_price);
    }
}
