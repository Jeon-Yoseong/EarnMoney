package com.onespringday.earnmoney.Search;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.onespringday.earnmoney.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yoseong on 2017-08-10.
 * 상품 검색 다이얼로그
 */

public class ProductSearchDialog extends Dialog {

    Context context;
    String[] str;
    int spinneritem;

    // 다이얼로그 내부 뷰 선언
    private Spinner productSearchSpinner;
    private ImageButton productSearchBtn;
    private EditText productSearchInput;
    // 상품 이름 저장
    private String productName;
    // 스피너를 이용해 상품명인지 한글 상품명인지 구문
    private String productKind;

    public ProductSearchDialog (Context context, String[] str, int spinneritem) {
        super(context);
        this.context = context;
        this.str = str;
        this.spinneritem = spinneritem;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_product_search);

        productSearchBtn = (ImageButton) findViewById(R.id.product_search_btn);
        productSearchInput = (EditText) findViewById(R.id.product_search_input);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, str);
//        SpinnerAdapter adapter = new SpinnerAdapter(context, spinneritem, str);

        List<String> data = new ArrayList<>();
        data.add("[검색어 선택]");
        data.add("상품명");
        data.add("한글 상품명");

        AdapterSpinner1 adapter = new AdapterSpinner1(context, data);

        productSearchSpinner = (Spinner) findViewById(R.id.spinner2);
        productSearchSpinner.setAdapter(adapter);

        // 스피너를 이용해 상품명 종류 저장
        productSearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productKind = productSearchSpinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // 검색 버튼 터치 시 다이얼로그 종료 & 상품명 저장
        productSearchBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (productKind.equals("[검색어 선택]")) {
                    Toast.makeText(getContext(), "검색어 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    productName = productSearchInput.getText().toString();
                    dismiss();
                    return false;
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        dismiss();
        super.onBackPressed();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[] {};

        public SpinnerAdapter(final Context context,
                              final int textViewResourceId, final String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        /**
         * 스피너 클릭시 보여지는 View의 정의
         */
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_dropdown_item, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setTextSize(10);
            tv.setHeight(50);
            return convertView;
        }

        /**
         * 기본 스피너 View 정의
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);

            tv.setTextSize(10);
            return convertView;
        }
    }

    public class AdapterSpinner1 extends BaseAdapter {


        Context context;
        List<String> data;
        LayoutInflater inflater;


        public AdapterSpinner1(Context context, List<String> data){
            this.context = context;
            this.data = data;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            if(data!=null) return data.size();
            else return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null) {
                convertView = inflater.inflate(R.layout.spinner_header, parent, false);
            }

            if(data!=null){
                //데이터세팅
                String text = data.get(position);
                ((TextView)convertView.findViewById(R.id.spinner_text)).setText(text);
            }

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = inflater.inflate(R.layout.spinner_dropdown, parent, false);
            }

            //데이터세팅
            String text = data.get(position);
            ((TextView)convertView.findViewById(R.id.spinner_text)).setText(text);

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }


}
