package com.onespringday.earnmoney;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by yoseong on 2017-08-07.
 */

public class RegisterActivity extends AppCompatActivity {

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

    // 회원 정보 관련 입력 선언
    private ImageView logoPhoto;
    private EditText memberId;
    private EditText memberPw;
    private EditText memberPwCheck;
    private EditText memberShopName;
    private EditText memberShopUrl;

    // 버튼 선언
    private Button memberIdCheck;
    private Button logoPhotoSelectBtn;
    private Button memberRegisterBtn;

    // 아이디 중복 체크 관련 선언
    private ArrayList<PHPListItem> listItem = new ArrayList<PHPListItem>();
    private int idFlag = 1;
    private PHPDownload task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // layout의 뷰와 set
        logoPhoto = (ImageView) findViewById(R.id.logo_photo);
        memberId = (EditText) findViewById(R.id.member_id);
        memberPw = (EditText) findViewById(R.id.member_password);
        memberPwCheck = (EditText) findViewById(R.id.member_password_check);
        memberShopName = (EditText) findViewById(R.id.member_shop_name);
        memberShopUrl = (EditText) findViewById(R.id.member_shop_url);

        logoPhotoSelectBtn = (Button) findViewById(R.id.logo_photo_select_btn);
        memberRegisterBtn = (Button) findViewById(R.id.member_register_btn);

        memberIdCheck = (Button) findViewById(R.id.member_id_check);

//        final ImageView pwCheckImage = (ImageView) findViewById(R.id.member_password_check_photo);
        final ImageView pwCheckImage2 = (ImageView) findViewById(R.id.member_password_check_photo_2);

//       pwCheckImage.setVisibility(View.INVISIBLE);
        pwCheckImage2.setVisibility(View.INVISIBLE);

        // 아이디 비밀번호 영문과 숫자만 입력
        memberId.setFilters(new InputFilter[]{filter});
        memberPw.setFilters(new InputFilter[]{filter});

        // logo 사진 선택
        logoPhotoSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlbum();
            }
        });

        // 중복 아이디 검사
        task = new PHPDownload();
        task.execute("http://yoseong92.cafe24.com/earnmoney/get_member_info.php");

        // 비밀번호 일치 검사
        memberPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = memberPw.getText().toString();
                String check = memberPwCheck.getText().toString();

                Drawable xMark= getResources().getDrawable(R.drawable.xmark_20px);
                Drawable checkMark= getResources().getDrawable(R.drawable.checkmark_20px);
                pwCheckImage2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                // 비밀번호와 비밀번호 확인 값이 같을 경우에만 체크박스가 보여진다.
                if (pw.equals(check)) {
                    pwCheckImage2.setVisibility(View.VISIBLE);
                    pwCheckImage2.setImageDrawable(checkMark);
                } else {
                    pwCheckImage2.setVisibility(View.VISIBLE);
                    pwCheckImage2.setImageDrawable(xMark);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        memberPwCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = memberPw.getText().toString();
                String check = memberPwCheck.getText().toString();

                Drawable xMark= getResources().getDrawable(R.drawable.xmark_20px);
                Drawable checkMark= getResources().getDrawable(R.drawable.checkmark_20px);
                pwCheckImage2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                // 비밀번호와 비밀번호 확인 값이 같을 경우에만 체크박스가 보여진다.
                if (pw.equals(check)) {
                    pwCheckImage2.setVisibility(View.VISIBLE);
                    pwCheckImage2.setImageDrawable(checkMark);
                } else {
                    pwCheckImage2.setVisibility(View.VISIBLE);
                    pwCheckImage2.setImageDrawable(xMark);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 가입하기 버튼 클릭 시 정보가 다 입력 되었는지 check 후 회원가입
        memberRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력이 다 됐는지 확인하는 변수
                int flag = 0;
                // 아이디 4~16자 사이
                if (memberId.getText().length() < 4 || memberId.getText().length() > 16) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "아이디는 4~16자 사이입니다", Toast.LENGTH_SHORT).show();
                    memberId.requestFocus();
                }
                // 비밀번호 6~16자 사이
                if (memberPw.getText().length() < 6 || memberPw.getText().length() > 16) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "비밀번호는 6~16자 사이입니다", Toast.LENGTH_SHORT).show();
                    memberId.requestFocus();
                }

                // 아이디 입력 확인
                if (memberId.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                    memberId.requestFocus();
                }
                // 비밀번호 입력 확인
                if (memberPw.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    memberPw.requestFocus();
                }
                // 비밀번호와 비밀번호 체크 동일한지 확인
                if (!memberPw.getText().toString().equals(memberPwCheck.getText().toString())) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    memberPw.requestFocus();
                }
                // 상호명 입력 확인
                if (memberShopName.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "상호명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    memberShopName.requestFocus();
                }
                // url 입력 확인
                if (memberShopUrl.getText().toString().equals("")) {
                    flag = 1;
                    Toast.makeText(getApplicationContext(), "쇼핑몰 URL을 입력하세요.", Toast.LENGTH_SHORT).show();
                    memberShopUrl.requestFocus();
                }
                // 아이디 중복체크 검사 여부
                if (idFlag == 1) {
                    Toast.makeText(getApplicationContext(), "아이디 중복 체크 검사를 해주세요.", Toast.LENGTH_SHORT).show();
                }

                // 다 입력했을 경우
                if (flag == 0 && idFlag == 0) {

                    try {
                        PHPUpload upload = new PHPUpload("http://yoseong92.cafe24.com/earnmoney/save_member_info.php");
                        String result = upload.PHPRequest(String.valueOf(memberId.getText()), String.valueOf(memberPw.getText()), String.valueOf(memberShopName.getText()), String.valueOf(memberShopUrl.getText()),
                                String.valueOf("http://yoseong92.cafe24.com/earnmoney/LogoPhoto/"+imageFileName));

                        if (!result.equals("")) {
                            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Intent register_to_login = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(register_to_login);
                    finish();

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        // 수정 취소버튼 클릭시 alertdialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // 제목 셋팅
        alertDialogBuilder.setTitle("회원가입 취소");
        // alertdialog 셋팅
        alertDialogBuilder
                .setMessage("\'뒤로\' 버튼 클릭 시 회원가입이 되지 않습니다. 취소하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent register_to_login = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(register_to_login);
                                finish();
                            }
                        })
                .setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        // alertdialog 생성
        AlertDialog alertDialog = alertDialogBuilder.create();
        // alertdialog 보여주기
        alertDialog.show();
    }

    // 회원 아이디 중복 확인 데이터 불러오기
    private class PHPDownload extends AsyncTask<String, String, String> {

        private String Memeber_Id;

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
                        for (; ;) {
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
                    Memeber_Id = jo.getString("Member_Id");
                    listItem.add(new PHPListItem(Memeber_Id));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 아이디 중복 체크
            memberIdCheck.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int flag = 0;
                    for(int i = 0; i < listItem.size(); i++) {
                        Log.i("중복확인", listItem.get(i).getData(0));
                        if(memberId.getText().toString().equals(listItem.get(i).getData(0))) {
                            flag++;
                        }
                    }
                    if(flag == 0) {
                        idFlag = 0;
                        Toast.makeText(getApplicationContext(), "사용하실 수 있는 ID입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "동일한 ID가 존재합니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        memberId.setText("");
                        idFlag = 1;
                    }
                }
            });

        }
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
        File storageDir = getExternalFilesDir("Logo");
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
                isAlbum = false;
                cropImage();
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
                logoPhoto.setImageURI(Uri.parse(mCurrentPhotoPath));
                galleryAddPic();
                // 업로드
                uploadFile(mCurrentPhotoPath);
                break;

        }
    }

    // 로고 사진 업로드 함수
    public void uploadFile (String filePath) {

        String url = "http://yoseong92.cafe24.com/earnmoney/save_photo.php";

        try {
            // logo photo 저장 하기 위해 flag 0 전달
            PHPUpload phpUpload = new PHPUpload(getApplicationContext(), 0);
            phpUpload.setPath(filePath);
            phpUpload.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 영문과 숫자만 입력하도록
    protected InputFilter filter= new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };
}
