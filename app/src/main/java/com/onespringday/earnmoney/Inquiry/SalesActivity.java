package com.onespringday.earnmoney.Inquiry;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.onespringday.earnmoney.Management.NowStockActivity;
import com.onespringday.earnmoney.Management.OrderMActivity;
import com.onespringday.earnmoney.Management.OrderStockActivity;
import com.onespringday.earnmoney.Management.ReturnMActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by yoseong on 2017-08-06.
 */

public class SalesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // 그래프 선언
    private LineChart lineChart;

    // Recycler View 관련 선언
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SalesListAdapter adapter;
    private List<SalesItem> listdata;

    // Member ID 불러오기 위한 Shared Preference 선언
    private SharedPreferences pref_member;
    private String memberId;
    private String Logo_Photo;
    private String Shop_Name;
    private String Shop_Url;

    // 상품 정보 불러오기
    private ArrayList<PHPListItem> listItem = new ArrayList<PHPListItem>();
    private PHPDownload task;

    private String todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        // 툴바 셋팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sales);
        toolbar.setTitle("매출조회");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);

        // DrawerLayout 셋팅
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.sales_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // NavigationView 셋팅
        NavigationView navigationView = (NavigationView) findViewById(R.id.sales_nav_view);
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

        // 오늘 날짜 구하기
        SimpleDateFormat today = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance(); // 오늘날짜
        todayDate = today.format(calendar.getTime());

        // 매출 정보 불러오기
        task = new PHPDownload();
        task.execute("http://yoseong92.cafe24.com/earnmoney/get_sales_info.php");
    }

    /**
     * Navigation View를 위한 코드들 (DrawerLayout)
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.sales_drawer_layout);
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
            Intent go_to_now_stock = new Intent(getApplicationContext(), NowStockActivity.class);
            startActivity(go_to_now_stock);
            finish();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.sales_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 요일 알아 오기
    private String doDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        String strWeek = null;
        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (nWeek == 1) {
            strWeek = "일요일";
        } else if (nWeek == 2) {
            strWeek = "월요일";
        } else if (nWeek == 3) {
            strWeek = "화요일";
        } else if (nWeek == 4) {
            strWeek = "수요일";
        } else if (nWeek == 5) {
            strWeek = "목요일";
        } else if (nWeek == 6) {
            strWeek = "금요일";
        }  else if (nWeek == 7) {
            strWeek = "토요일";
        }

        return strWeek;
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

    // 상품정보 불러오기
    private class PHPDownload extends AsyncTask<String, String, String> {

        private String Korea_Name;
        private String Product_Color; // Color 클래스 사용하기 위해 Product_Color라 선언
        private String Size;
        private String Count;
        private String Date;
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
                    Member_Id = jo.getString("Member_Id");
                    listItem.add(new PHPListItem(Korea_Name, Product_Color, Size, Count, Date, Price, Member_Id));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            /**
             * Recycler View
             */
            listdata = new ArrayList<>();
            // ArrayList에 판매날짜와 수량, 금액 저장 
            ArrayList<SalesListDateItem> dateList = new ArrayList<SalesListDateItem>();
            //ArrayList에 해당 판매날짜의 총수량, 총금액 저장
            ArrayList<SalesListDateItem> totalDateList = new ArrayList<>();
            for (int i = listItem.size() - 1; i >= 0 ; i--) {
                if (listItem.get(i).getData(6).equals(memberId)) {
                    int count = Integer.parseInt(listItem.get(i).getData(3));
                    int price = Integer.parseInt(listItem.get(i).getData(3)) * Integer.parseInt(listItem.get(i).getData(5));
                    try {
                        dateList.add(new SalesListDateItem(listItem.get(i).getData(4), String.valueOf(count), String.valueOf(price)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // ArrayList 날짜 내림차순으로 정렬
            Collections.sort(dateList, new Comparator<SalesListDateItem>() {
                @Override
                public int compare(SalesListDateItem o1, SalesListDateItem o2) {
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
                int sumPrice = 0; // 날짜별 매출금액 합계

                for(int i = 0; i < dateList.size(); i++) {
                    // 날짜가 같으면 수량과 금액을 더해준다. 날짜가 바뀌면 리스트에 합계 데이터를 추가하고 합계를 초기화 시켜주고 날짜도 초기화 해준다.
                    if(preDate.equals(dateList.get(i).getDate())) {
                        sumCount += Integer.parseInt(dateList.get(i).getCount());
                        sumPrice += Integer.parseInt(dateList.get(i).getPrice());
                    } else {
                        try {
                            listdata.add(new SalesItem(preDate+"("+getDateDay(preDate, "yyyy/MM/dd")+")", String.valueOf(sumCount), String.valueOf(sumPrice)));
                            totalDateList.add(new SalesListDateItem(preDate, String.valueOf(sumCount), String.valueOf(sumPrice)));
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
                            listdata.add(new SalesItem(preDate+"("+getDateDay(preDate, "yyyy/MM/dd")+")", String.valueOf(sumCount), String.valueOf(sumPrice)));
                            totalDateList.add(new SalesListDateItem(preDate, String.valueOf(sumCount), String.valueOf(sumPrice)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                // 해당 날짜가 비어있으면 0으로 데이터를 넣어준다.
                String tempTodayDate = todayDate;
                System.out.println("size:"+totalDateList.size());
                int j = 0;
                for (int i = 0; i < 14; i++) {
                    // totalDateList의 크기 이상으로 커지면 break를 해준다.
                    if (j == totalDateList.size()) {
                        break;
                    }
                    if (!totalDateList.get(j).getDate().equals(tempTodayDate)) {
                        totalDateList.add(new SalesListDateItem(tempTodayDate, "0", "0"));
                        tempTodayDate = getYesterdayDate(tempTodayDate);
                    } else {
                        tempTodayDate = getYesterdayDate(tempTodayDate);
                        j++;
                    }
                }
                // 날짜 내림차순으로 정렬해준다.
                Collections.sort(totalDateList, new Comparator<SalesListDateItem>() {
                    @Override
                    public int compare(SalesListDateItem o1, SalesListDateItem o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                });

                // 어댑터에 data 넘겨줌
                adapter = new SalesListAdapter(getApplicationContext(), listdata );

                // LinearLayoutManager 설정
                mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
                mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                // Recycler View 설정
                recyclerView = (RecyclerView) findViewById(R.id.sales_recyclerview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(mLinearLayoutManager);

                // 어댑터 연결
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                /**
                 * 매출 꺽은선 그래프 설정
                 */
                // 해당 요일별 그래프에 추가되는 데이터의 숫자 설정
                int graphSize = 0;
                String todayStr = null;
                try {
                    todayStr = getDateDay(todayDate, "yyyy/MM/dd");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (todayStr) {
                    case "일":
                        graphSize = 8;
                        break;
                    case "월":
                        graphSize = 9;
                        break;
                    case "화":
                        graphSize = 10;
                        break;
                    case "수":
                        graphSize = 11;
                        break;
                    case "목":
                        graphSize = 12;
                        break;
                    case "금":
                        graphSize = 13;
                        break;
                    case "토":
                        graphSize = 14;
                        break;
                }

                // 요일 설정
                int[] dayArr = {0, 1, 2, 3, 4, 5, 6};

                final HashMap<Integer, String> dayMap = new HashMap<>();
                dayMap.put(0, "일요일");
                dayMap.put(1, "월요일");
                dayMap.put(2, "화요일");
                dayMap.put(3, "수요일");
                dayMap.put(4, "목요일");
                dayMap.put(5, "금요일");
                dayMap.put(6, "토요일");

                // 지난주 데이터
                ArrayList<Entry> values = new ArrayList<Entry>();
                // 이번주 데이터
                ArrayList<Entry> values2 = new ArrayList<Entry>();
                // totalDateList의 크기
                int totalDateListSize = totalDateList.size();
                int countSize = 0;

                for(int i = graphSize; i < 14; i++ ) {
                    values2.add(new Entry(i - 7, 0));
                }

                for (int i = graphSize-1; i >= 0; i--) {

                    // 이번주 데이터
                    if (i > 6) {
                        // 이전 날짜의 데이터가 없는 경우 0을 넣어준다.
                        if (countSize == totalDateListSize) {
                            values2.add(new Entry(i - 7, 0));
                        } else {
                            values2.add(new Entry(i - 7, Integer.parseInt(totalDateList.get(countSize).getCount())));
                            countSize++;
                        }
                    } else {
                        // 이전 날짜의 데이터가 없는 경우 0을 넣어준다.
                        if (countSize == totalDateListSize) {
                            values.add(new Entry(i, 0));
                        } else {
                            values.add(new Entry(i, Integer.parseInt(totalDateList.get(countSize).getCount())));
                            countSize++;
                        }
                    }
                }
                // x축 정렬
                Collections.sort(values, new Comparator<Entry>() {
                    @Override
                    public int compare(Entry o1, Entry o2) {
                        return String.valueOf(o1.getX()).compareTo(String.valueOf(o2.getX()));
                    }
                });
                Collections.sort(values2, new Comparator<Entry>() {
                    @Override
                    public int compare(Entry o1, Entry o2) {
                        return String.valueOf(o1.getX()).compareTo(String.valueOf(o2.getX()));
                    }
                });

                // 지난주 데이터 추가
                LineDataSet lineDataSet = new LineDataSet(values, "지난주 매출 수");
                // 그래프 선 색 (빨강)
                lineDataSet.setColor(Color.parseColor("#FF0000"));
                // 그래프 선 위 원의 색
                lineDataSet.setCircleColor(Color.parseColor("#FF0000"));
                // 그래프 원 위에 표시되는 수의 글자 크기 색
                lineDataSet.setValueTextSize(10);
                lineDataSet.setValueTextColor(Color.parseColor("#000000"));
                lineDataSet.setDrawIcons(false);

                // 이번주 데이터 추가
                LineDataSet lineDataSet2 = new LineDataSet(values2, "이번주 매출 수");
                // 파랑
                lineDataSet2.setColor((Color.parseColor("#0000FF")));
                lineDataSet2.setCircleColor(Color.parseColor("#0000FF"));
                lineDataSet2.setValueTextSize(10);
                lineDataSet2.setValueTextColor(Color.parseColor("#000000"));
                lineDataSet2.setDrawIcons(false);

                lineChart = (LineChart) findViewById(R.id.order_chart);
                lineChart.setDrawGridBackground(false);
                // no description text
                lineChart.getDescription().setEnabled(false);

                // disable touch gesture
                lineChart.setDragEnabled(false);
                lineChart.setScaleEnabled(false);
                lineChart.setPinchZoom(true);

                // disable right label
                lineChart.getAxisRight().setDrawLabels(false);

                // background color
                lineChart.setBackgroundColor(Color.parseColor("#FFFFFF"));

                // legend 위치 설정
                lineChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);


                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(lineDataSet); // add the datasets
                dataSets.add(lineDataSet2);
                // create a data object with the datasets
                LineData data = new LineData(dataSets);

                // x value 값을 String으로 나오게 하기 위한 설정
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return dayMap.get((int)value);
                    }
                });

                // x 밸류 밑부분에 나오게
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                // set data
                lineChart.setData(data);
                lineChart.invalidate();

                // hight line 제거
                lineChart.getData().setHighlightEnabled(false);
            }

        }
    }
}
