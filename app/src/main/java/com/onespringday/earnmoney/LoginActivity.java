package com.onespringday.earnmoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by yoseong on 2017-08-05.
 */

public class LoginActivity extends AppCompatActivity {

    private int loginFlag = 0;

    private PHPDownload task;

    // 자동 로그인 & 사용자 아이디 Shared Preference 에 저장해 사용하기 위해 선언
    private SharedPreferences pref_auto_login;
    private SharedPreferences.Editor editor_auto_login;
    private SharedPreferences pref_member;
    private SharedPreferences.Editor editor_member;

    // login 위한 EditText 선언
    private EditText loginId;
    private EditText loginPassword;

    // 자동 로그인 체크박스
    private CheckBox autoLogin;

    // 버튼 선언
    private Button loginBtn;
    private Button registerBtn;

    // 로그인에 필요한 회원정보 불러오기
    private ArrayList<PHPListItem> listItem = new ArrayList<PHPListItem>();

    // 백 버튼 2번 클릭시 종료 변수
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 버튼 설정
        loginBtn = (Button) findViewById(R.id.login_btn);
        registerBtn = (Button) findViewById(R.id.register_btn);

        // EditText 설정
        loginId = (EditText) findViewById(R.id.login_id);
        loginPassword = (EditText) findViewById(R.id.login_password);

        // 자동 로그인 체크박스 설정
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);

        // 회원 가입

        // 회원 가입으로 넘어가기
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_to_register = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(login_to_register);
                finish();
            }
        });

        // 로그인 위해 php로 데이터 불러오기 위한 선언
        task = new PHPDownload();
        task.execute("http://yoseong92.cafe24.com/earnmoney/get_member_info.php");
    }

    // 회원 아이디 중복 확인 데이터 불러오기
    private class PHPDownload extends AsyncTask<String, String, String> {

        private String Member_Id;
        private String Member_Password;
        private String Shop_Name;
        private String Shop_Url;
        private String Logo_Photo;

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
                    Member_Id = jo.getString("Member_Id");
                    Member_Password = jo.getString("Member_Password");
                    Shop_Name = jo.getString("Shop_Name");
                    Shop_Url = jo.getString("Shop_Url");
                    Logo_Photo = jo.getString("Logo_Photo");
                    listItem.add(new PHPListItem(Member_Id, Member_Password, Shop_Name, Shop_Url, Logo_Photo));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            // 자동 로그인 Shared Preference
            pref_auto_login = getSharedPreferences("pref", 0);
            editor_auto_login = pref_auto_login.edit();
            if(pref_auto_login.getBoolean("autoLogin", false)) {
                loginId.setText(pref_auto_login.getString("id",""));
                loginPassword.setText(pref_auto_login.getString("password",""));
                autoLogin.setChecked(true);
            }

            // 로그인
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //자동 로그인
                    if(autoLogin.isChecked()) {
                        String id = loginId.getText().toString();
                        String password = loginPassword.getText().toString();

                        editor_auto_login.putString("id", id);
                        editor_auto_login.putString("password", password);
                        editor_auto_login.putBoolean("autoLogin", true);
                        editor_auto_login.commit();
                    } else {
                        editor_auto_login.clear();
                        editor_auto_login.commit();
                    }

                    for (int i = 0; i < listItem.size(); i++) {
                        String tmpId = listItem.get(i).getData(0);
                        String tmpPw = listItem.get(i).getData(1);
                        System.out.println(listItem.get(i).getData(0));
                        if (tmpId.equals(loginId.getText().toString())) {
                            if (tmpPw.equals(loginPassword.getText().toString())) {
                                loginFlag = 1;
                                // 사용자 아이디 shared preference 에 저장
                                pref_member= getSharedPreferences("pref_member", 0);
                                editor_member= pref_member.edit();
                                editor_member.putString("id", loginId.getText().toString());
                                editor_member.putString("name", listItem.get(i).getData(2));
                                editor_member.putString("url", listItem.get(i).getData(3));
                                editor_member.putString("logo", listItem.get(i).getData(4));
                                editor_member.commit();
                                break;
                            }
                        }
                    }

                    if (loginFlag == 1) {
                        Intent login_to_main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(login_to_main);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "아이디나 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @Override
    public void onBackPressed () {
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
