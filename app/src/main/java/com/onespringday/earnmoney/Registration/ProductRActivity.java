package com.onespringday.earnmoney.Registration;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.onespringday.earnmoney.Inquiry.OrderActivity;
import com.onespringday.earnmoney.Inquiry.ProductActivity;
import com.onespringday.earnmoney.Inquiry.ProfitActivity;
import com.onespringday.earnmoney.Inquiry.ReturnActivity;
import com.onespringday.earnmoney.Inquiry.SalesActivity;
import com.onespringday.earnmoney.Management.NowStockActivity;
import com.onespringday.earnmoney.Management.OrderMActivity;
import com.onespringday.earnmoney.Management.OrderStockActivity;
import com.onespringday.earnmoney.Management.ReturnMActivity;
import com.onespringday.earnmoney.PHPUpload;
import com.onespringday.earnmoney.Search.ProductSearchActivity;
import com.onespringday.earnmoney.Search.ProductSearchDialog;
import com.onespringday.earnmoney.R;
import com.onespringday.earnmoney.ExchangeRateSettingsActivity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by yoseong on 2017-08-09.
 */

public class ProductRActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // 입력 부분 선언
    private ImageView productPhoto;
    private Button productPhotoSelectBtn;
    private EditText productName;
    private EditText productKoreanName;
    private EditText chinaUnitCost;
    private TextView chinaUnitCostKoreaConversion;
    private Button chinaUnitCostKoreaConversionBtn;
    private EditText koreaUnitCost;
    private EditText productPrice;
    private EditText productLucky;
    private EditText productFriend;
    private EditText productBomb;
    private TextView productSurtax;
    private Button calculationSurtaxBtn;
    private TextView productProfit;
    private Button calculationProfitBtn;
    private EditText productColor;
    private EditText productSize;
    private EditText productUrl;
    private Button productRegistrationBtn;

    // 현재 회원 아이디 Shared Preference 로 불러오기
    private SharedPreferences pref_member;
    private String Member_Id;
    private String Logo_Photo;
    private String Shop_Name;
    private String Shop_Url;

   // 이미지 크롭
    static final int MY_PERMISSON_CAMERA = 1;
    static final int REQUEST_TAKE_PHOTO = 2001;
    static final int REQUEST_TAKE_ALBUM = 2002;
    static final int REQUEST_IMAGE_CROP = 2003;
    String mCurrentPhotoPath;
    Uri photoURI, albumURI;
    boolean isAlbum = false; // crop시 사진을 찍은 것인지, 앨범에서 가져온 것인지 확인하는 플래그
    String imageFileName; // 서버 사진 path

    // 사진 선택 다이얼로그 위해 선언
    private Context mContext = this;

    // 환율 선언
    private int exchangeRate;
    private SharedPreferences pref_setting;
    private String todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_registration);

        // 툴바 셋팅
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_product_registration);
        toolbar.setTitle("상품등록");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);

        // DrawerLayout 셋팅
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.product_registration_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // NavigationView 셋팅
        NavigationView navigationView = (NavigationView) findViewById(R.id.product_registration_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 입력 부분 선언
        productPhoto = (ImageView) findViewById(R.id.product_photo_registration);
        productPhotoSelectBtn = (Button) findViewById(R.id.product_photo_select_btn);
        productName = (EditText) findViewById(R.id.product_name_registration);
        productKoreanName = (EditText) findViewById(R.id.product_name_kor_registration);
        chinaUnitCost = (EditText) findViewById(R.id.product_china_unit_cost_registration);
        chinaUnitCostKoreaConversion = (TextView) findViewById(R.id.product_china_unit_cost_korea_conversion);
        chinaUnitCostKoreaConversionBtn = (Button) findViewById(R.id.product_china_unit_cost_korea_conversion_btn);
        koreaUnitCost = (EditText) findViewById(R.id.product_korea_unit_cost_registration);
        productPrice = (EditText) findViewById(R.id.product_price_registration);
        productLucky = (EditText) findViewById(R.id.product_lucky_today_price_registration);
        productFriend = (EditText) findViewById(R.id.product_friend_price_registration);
        productBomb = (EditText) findViewById(R.id.product_bomb_sale_price_registration);
        productSurtax = (TextView) findViewById(R.id.product_surtax_registration);
        calculationSurtaxBtn = (Button) findViewById(R.id.product_surtax_calculation_btn);
        productProfit = (TextView) findViewById(R.id.product_profit_registration);
        calculationProfitBtn = (Button) findViewById(R.id.product_profit_calculation_btn);
        productColor = (EditText) findViewById(R.id.product_color_registration);
        productSize = (EditText) findViewById(R.id.product_size_registration);
        productUrl = (EditText) findViewById(R.id.product_purchase_url);
        productRegistrationBtn = (Button) findViewById(R.id.product_registration_btn);

        // 상품 사진 등록 관련
        productPhotoSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlbum();
            }
        });


        // 환율 정보는 shared preference 로
        pref_setting = PreferenceManager.getDefaultSharedPreferences(this);
        exchangeRate = Integer.parseInt(pref_setting.getString("exchange_rate", "0"));
        Toast.makeText(getApplicationContext(), "현재 환율: "+exchangeRate, Toast.LENGTH_SHORT).show();


        // 중국 단가에 환율 곱해서 한국돈으로 단가 변환
        chinaUnitCostKoreaConversionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // null이 아닐 경우
                if(chinaUnitCost.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "중국 단가를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    chinaUnitCostKoreaConversion.setText(String.valueOf(Integer.parseInt(chinaUnitCost.getText().toString()) * exchangeRate));
                }
            }
        });

        // 부가세와 이익금 계산
        calculationSurtaxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productPrice.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "판매가를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    productSurtax.setText(String.valueOf((int)(Integer.parseInt(productPrice.getText().toString())*0.1)));
                }
            }
        });

        calculationProfitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (productPrice.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "판매가를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                if (koreaUnitCost.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "한국 원가를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                if (productSurtax.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "부가세를 계산하세요.", Toast.LENGTH_SHORT).show();
                }
                if (!(productPrice.getText().toString().equals("") || koreaUnitCost.getText().toString().equals("") || productSurtax.getText().toString().equals(""))) {
                    productProfit.setText(String.valueOf(Integer.parseInt(productPrice.getText().toString())
                            - Integer.parseInt(koreaUnitCost.getText().toString())
                            - Integer.parseInt(productSurtax.getText().toString())));
                }
            }
        });

        // 회원 아이디 불러오기
        pref_member = getSharedPreferences("pref_member", 0);
        Member_Id = pref_member.getString("id", "");
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

        // 상품 등록 버튼 클릭 시
        productRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                // 빈칸 없이 등록할 수 있게 설정
                if (productName.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "상품명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    productName.requestFocus();
                }
                if (productKoreanName.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "한글상품명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    productKoreanName.requestFocus();
                }
                if (chinaUnitCost.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "중국단가를 입력하세요.", Toast.LENGTH_SHORT).show();
                    chinaUnitCost.requestFocus();
                }
                if (koreaUnitCost.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "한국원가를 입력하세요.", Toast.LENGTH_SHORT).show();
                    koreaUnitCost.requestFocus();
                }
                if (productPrice.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "정상판매가를 입력하세요.", Toast.LENGTH_SHORT).show();
                    productPrice.requestFocus();
                }
                if (productColor.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "색상을 입력하세요.", Toast.LENGTH_SHORT).show();
                    productColor.requestFocus();
                }
                if (productSize.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "사이즈를 입력하세요.", Toast.LENGTH_SHORT).show();
                    productSize.requestFocus();
                }
                if (productUrl.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "구입링크를 입력하세요.", Toast.LENGTH_SHORT).show();
                    productUrl.requestFocus();
                }

                if (flag == 0) {
                    try {
                        PHPUpload upload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/save_product_info.php");
                        PHPUpload stockUpload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/save_stock_info.php");

                        // 선택 입력 사항이 NULL일 경우 저장되지 않아 0으로 초기화
                        if (chinaUnitCost.getText().toString().equals("")) {
                            chinaUnitCost.setText("0");
                        }
                        if (productLucky.getText().toString().equals("")) {
                            productLucky.setText("0");
                        }
                        if (productFriend.getText().toString().equals("")) {
                            productFriend.setText("0");
                        }
                        if (productBomb.getText().toString().equals("")) {
                            productBomb.setText("0");
                        }

                        // 색상별, 사이즈별 구분자 ','로 잘라서 저장
                        ArrayList<String> colorArr = new ArrayList<String>();
                        ArrayList<String> sizeArr = new ArrayList<String>();
                        if (productColor.getText().toString().contains(",")) {
                            StringTokenizer st = new StringTokenizer(productColor.getText().toString(), ",");
                            while (st.hasMoreTokens()) {
                                colorArr.add(st.nextToken());
                            }
                        } if (productSize.getText().toString().contains(",")) {
                            StringTokenizer st2 = new StringTokenizer(productSize.getText().toString(), ",");
                            while (st2.hasMoreTokens()) {
                                sizeArr.add(st2.nextToken());
                            }
                        }

                        String result = null;

                        if (colorArr.size() == 0 && sizeArr.size() == 0) {
                            result = upload.PHPRequest(String.valueOf(productName.getText()), String.valueOf(productKoreanName.getText()), Integer.parseInt(chinaUnitCost.getText().toString()), Integer.parseInt(koreaUnitCost.getText().toString()),
                                    Integer.parseInt(productPrice.getText().toString()), Integer.parseInt(productLucky.getText().toString()), Integer.parseInt(productFriend.getText().toString()), Integer.parseInt(productBomb.getText().toString()),
                                    String.valueOf(productColor.getText()), String.valueOf(productSize.getText()), String.valueOf(productUrl.getText()), String.valueOf("http://yoseong92.cafe24.com/earnmoney/ProductPhoto/"+imageFileName), Member_Id);

                            stockUpload.PHPRequest(String.valueOf(productKoreanName.getText()), String.valueOf(productColor.getText()), String.valueOf(productSize.getText()), 0, Member_Id);
                        } else if (colorArr.size() == 0) {
                            for (int j = 0; j < sizeArr.size(); j++) {
                                result = upload.PHPRequest(String.valueOf(productName.getText()), String.valueOf(productKoreanName.getText()), Integer.parseInt(chinaUnitCost.getText().toString()), Integer.parseInt(koreaUnitCost.getText().toString()),
                                        Integer.parseInt(productPrice.getText().toString()), Integer.parseInt(productLucky.getText().toString()), Integer.parseInt(productFriend.getText().toString()), Integer.parseInt(productBomb.getText().toString()),
                                        String.valueOf(productColor.getText()), String.valueOf(sizeArr.get(j)), String.valueOf(productUrl.getText()), String.valueOf("http://yoseong92.cafe24.com/earnmoney/ProductPhoto/"+imageFileName), Member_Id);
                                stockUpload.PHPRequest(String.valueOf(productKoreanName.getText()), String.valueOf(productColor.getText()), String.valueOf(sizeArr.get(j)), 0, Member_Id);
                            }
                        } else if (sizeArr.size() == 0) {
                            for (int i = 0; i < colorArr.size(); i++) {
                                result = upload.PHPRequest(String.valueOf(productName.getText()), String.valueOf(productKoreanName.getText()), Integer.parseInt(chinaUnitCost.getText().toString()), Integer.parseInt(koreaUnitCost.getText().toString()),
                                        Integer.parseInt(productPrice.getText().toString()), Integer.parseInt(productLucky.getText().toString()), Integer.parseInt(productFriend.getText().toString()), Integer.parseInt(productBomb.getText().toString()),
                                        String.valueOf(colorArr.get(i)), String.valueOf(productSize.getText()), String.valueOf(productUrl.getText()), String.valueOf("http://yoseong92.cafe24.com/earnmoney/ProductPhoto/"+imageFileName), Member_Id);
                                stockUpload.PHPRequest(String.valueOf(productKoreanName.getText()), String.valueOf(colorArr.get(i)), String.valueOf(productSize.getText()), 0, Member_Id);
                            }
                        } else {
                            for (int i = 0; i < colorArr.size(); i++) {
                                for (int j = 0; j < sizeArr.size(); j++) {
                                    result = upload.PHPRequest(String.valueOf(productName.getText()), String.valueOf(productKoreanName.getText()), Integer.parseInt(chinaUnitCost.getText().toString()), Integer.parseInt(koreaUnitCost.getText().toString()),
                                            Integer.parseInt(productPrice.getText().toString()), Integer.parseInt(productLucky.getText().toString()), Integer.parseInt(productFriend.getText().toString()), Integer.parseInt(productBomb.getText().toString()),
                                            String.valueOf(colorArr.get(i)), String.valueOf(sizeArr.get(j)), String.valueOf(productUrl.getText()), String.valueOf("http://yoseong92.cafe24.com/earnmoney/ProductPhoto/"+imageFileName), Member_Id);
                                    stockUpload.PHPRequest(String.valueOf(productKoreanName.getText()), String.valueOf(colorArr.get(i)), String.valueOf(sizeArr.get(j)), 0, Member_Id);
                                }
                            }
                        }



                        if (!result.equals("")) {
                            Toast.makeText(getApplicationContext(), "상품등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                            // 등록이 완료되면 등록 칸 모두 초기화 시킴
                            productPhoto.setImageBitmap(null);
                            productName.setText("");
                            productKoreanName.setText("");
                            chinaUnitCost.setText("");
                            chinaUnitCostKoreaConversion.setText("");
                            koreaUnitCost.setText("");
                            productPrice.setText("");
                            productLucky.setText("");
                            productFriend.setText("");
                            productBomb.setText("");
                            productSurtax.setText("");
                            productProfit.setText("");
                            productColor.setText("");
                            productSize.setText("");
                            productUrl.setText("");
                        } else {
                            Toast.makeText(getApplicationContext(), "상품등록에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onResume을 통해 sharedpreference 종료 시 새로 바꾼 환율이 적용되도록 해줌
        exchangeRate = Integer.parseInt(pref_setting.getString("exchange_rate", "0"));
        Toast.makeText(getApplicationContext(), "현재 환율: "+exchangeRate, Toast.LENGTH_SHORT).show();
    }

    /**
     * Navigation View를 위한 코드들 (DrawerLayout)
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.product_registration_drawer_layout);
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
            Intent go_to_now_stock = new Intent(getApplicationContext(), NowStockActivity.class);
            startActivity(go_to_now_stock );
            finish();
        } else if (id == R.id.order_stock_management) {
            Intent go_to_order_stock= new Intent(getApplicationContext(), OrderStockActivity.class);
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
                    go_to_product_search.putExtra("Member_Id", Member_Id);
                    if (customDialog.getProductKind().equals("한글 상품명")) {
                        go_to_product_search.putExtra("Korea_Name", customDialog.getProductName());
                        go_to_product_search.putExtra("Name", "");
                    } else if (customDialog.getProductKind().equals("상품명")) {
                        go_to_product_search.putExtra("Korea_Name", "");
                        go_to_product_search.putExtra("Name", customDialog.getProductName());
                    }
                    startActivity(go_to_product_search);
                }
            });
            customDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.product_registration_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 로고 사진 등록 함수 이미지 가져오기 소스
     */

    /**
     * 권한 check 함수
     */
    public boolean isCheck(String permission) {
        switch (permission) {
            case "Camera":
                Log.i("Camera Permission", "CALL");
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("알림")
                                .setMessage("저장소 권한은 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 헝ㅇ하셔야 합니다.")
                                .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:com.ssu.sangjunianjuni.smartbabycare"));
                                        mContext.startActivity(intent);
                                    }
                                })
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((Activity) mContext).finish();
                                    }
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSON_CAMERA);
                    }
                } else {
                    return true;
                }
                break;
        }
        return true;
    }

    /**
     * 파일 생성
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        imageFileName = "JPEG_"+timeStamp+"_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = getExternalFilesDir("Product");
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFileName = image.getName();
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    /**
     * 앨범 열기
     */
    public void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    /**
     * 이미지 crop하기
     */
    public void cropImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setDataAndType(photoURI, "image/*");
        //       cropIntent.putExtra("outputX", 640); // CROP한이미지의 X축 크기
        //       cropIntent.putExtra("outputY", 640); // CROP한이미지의 Y축 크기
        cropIntent.putExtra("aspectX", 1); // CROP 박스의 X축 비율
        cropIntent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율
        cropIntent.putExtra("scale", true);
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        if(isAlbum == false) {
            cropIntent.putExtra("output", photoURI); // 크롭된 이미지를 해당 경로에 저장
        } else if (isAlbum==true) {
            cropIntent.putExtra("output", albumURI); // 크롭된 이미지를 해당 경로에 저장
        }

        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }
    /**
     * 동기화 갤러리 새로고침
     * 앨범이나 카메라로 사진 찍고 crop한 이후에 앨벙가면 해당 이미지가 안들어와 있는 경우가 많다.
     * 이 기능은 저장을 하고 Commint, 확정을 짓는 것과 같다. Uri 주소를 통해 현재 패스, 경로에 저장된 파일의 위치를 가져와 해당 Uri 위치를 브로드캐스트하여 보내주면 된다.
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult", "CALL");
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
          /*      isAlbum = false;
                cropImage();*/
                break;
            case REQUEST_TAKE_ALBUM:
                isAlbum = true;
                File albumFile = null;

                try {
                    albumFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (albumFile != null) {
                    albumURI = Uri.fromFile(albumFile);
                }
                // 앨범 선택에서 백 버튼 클릭 시 오류 발생 안하게
                if (data == null) {
                    break;
                }
                photoURI = data.getData();
                cropImage();
                break;
            case REQUEST_IMAGE_CROP:
                productPhoto.setImageURI(Uri.parse(mCurrentPhotoPath));
                galleryAddPic();
                // 업로드
                uploadFile(mCurrentPhotoPath);
                break;
        }
    }

    public void uploadFile (String filePath) {

        String url = "http://yoseong92.cafe24.com/earnmoney/save_photo.php";

        try {
            // Product Photo 전달하기 위해 flag 1 전달
            PHPUpload phpUpload = new PHPUpload(getApplicationContext(), 1);
            phpUpload.setPath(filePath);
            phpUpload.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
