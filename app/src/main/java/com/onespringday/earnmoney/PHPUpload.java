package com.onespringday.earnmoney;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yoseong on 2017-08-10.
 */

public class PHPUpload extends AsyncTask <String, String, String> {

    private URL url;

    // 이미지 서버에 업로드
    private Context context; // 생성자 호출 시
    private ProgressDialog mProgressDialog; // 진행 상태 다이얼로그
    private String fileName; // 파일 위치

    private HttpURLConnection photoConn = null; // 네트워크 연결 객체
    private DataOutputStream dos = null; // 서버 전송 시 데이터 작성한 뒤 전송

    private String lineEnd = "\r\n"; // 구분자
    private String twoHyphens = "--";
    private String boundary = "*****";

    private int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1024;
    private File sourceFile;
    int serverResponseCode;
    private String TAG = "FileUpload";
    private String line = null;

    public PHPUpload (String url) throws MalformedURLException{
        this.url = new URL(url);
    }
/*
    public PHPUpload (Context context) {
        this.context = context;
    }
*/
    // 로고 사진인지 상품 사진인지 구분
    int photoFlag;
    public PHPUpload (Context context, int photoFlag) {
        this.context = context;
        this.photoFlag = photoFlag;
    }

    private String readStream (InputStream in) throws IOException {
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = null;

        while ((line = reader.readLine()) != null)
            jsonHtml.append(line);

        reader.close();
        return jsonHtml.toString();
    }

    public void setPath (String uploadFilePath) {
        this.fileName = uploadFilePath;
        this.sourceFile = new File(uploadFilePath);
    }

    public String PHPRequest (String Member_Id, String Member_Password, String Shop_Name, String Shop_Url, String Logo_Photo) {

        try {
            NetworkUtil.setNetworkPolicy();
            String postData = "Member_Id=" + Member_Id + "&" + "Member_Password=" + Member_Password + "&" + "Shop_Name=" + Shop_Name + "&" + "Shop_Url=" + Shop_Url + "&" + "Logo_Photo=" + Logo_Photo;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;

        } catch (IOException e) {
            Log.i("PHPUpload", "upload was failed");
            return null;
        }
    }

    public String PHPRequest (String Name, String Korea_Name, int China_Unit_Cost, int Korea_Unit_Cost, int Price, int LuckyToday, int Friend, int Bomb, String Color, String Size, String Url, String Photo, String Member_Id) {

        try {
            NetworkUtil.setNetworkPolicy();
            String postData = "Name=" + Name + "&" + "Korea_Name=" + Korea_Name + "&" + "China_Unit_Cost=" + China_Unit_Cost + "&" + "Korea_Unit_Cost=" + Korea_Unit_Cost + "&" + "Price=" + Price
                    + "&" + "LuckyToday=" + LuckyToday + "&" + "Friend=" + Friend + "&" + "Bomb=" + Bomb + "&" + "Color=" + Color + "&" + "Size=" + Size
                    + "&" + "Url=" + Url + "&" + "Photo=" + Photo + "&" + "Member_Id=" + Member_Id;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;

        } catch (IOException e) {
            Log.i("PHPUpload", "upload was failed");
            return null;
        }
    }

    public String PHPRequest (String Korea_Name, String Color, String Size, int Count, String Date, String Member_Id) {

        try {
            NetworkUtil.setNetworkPolicy();
            String postData = "Korea_Name=" + Korea_Name + "&" + "Color=" + Color + "&" + "Size=" + Size + "&" + "Count=" + Count + "&" + "Date=" + Date + "&" + "Member_Id=" + Member_Id;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;

        } catch (IOException e) {
            Log.i("PHPUpload", "upload was failed");
            return null;
        }
    }

    public String PHPRequest (String Korea_Name, String Color, String Size, int Count, String Date, String Member_Id, int Flag) {

        try {
            NetworkUtil.setNetworkPolicy();
            String postData = "Korea_Name=" + Korea_Name + "&" + "Color=" + Color + "&" + "Size=" + Size + "&" + "Count=" + Count + "&" + "Date=" + Date + "&" + "Member_Id=" + Member_Id + "&" + "Flag=" + Flag;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;

        } catch (IOException e) {
            Log.i("PHPUpload", "upload was failed");
            return null;
        }
    }

    public String PHPRequest (String Korea_Name, String Color, String Size, int Stock_Count, String Member_Id) {

        try {
            NetworkUtil.setNetworkPolicy();
            String postData = "Korea_Name=" + Korea_Name + "&" + "Color=" + Color + "&" + "Size=" + Size + "&" + "Stock_Count=" + Stock_Count + "&" + "Member_Id=" + Member_Id;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;

        } catch (IOException e) {
            Log.i("PHPUpload", "upload was failed");
            return null;
        }
    }

    public String PHPRequest (String Korea_Name, String Color, String Size, int Count, String Order_Date, String Return_Date, String Member_Id, int Flag) {
        try {
            NetworkUtil.setNetworkPolicy();
            String postData = "Korea_Name=" + Korea_Name + "&" + "Color=" + Color + "&" + "Size=" + Size + "&" + "Count=" + Count + "&" + "Order_Date=" + Order_Date + "&" + "Return_Date=" + Return_Date
                    + "&" + "Member_Id=" + Member_Id + "&" + "Flag=" + Flag;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;

        } catch (IOException e) {
            Log.i("PHPUpload", "upload was failed");
            return null;
        }
    }

    @Override
    protected String doInBackground(String... params) {
        // 해당 위치의 파일이 있는지 검사
        if (!sourceFile.isFile()) {
            Log.e(TAG, "sourFile(" + fileName +")is Not A Find");
            return null;
        } else {
            String success = "Success";
            Log.i(TAG, "sourceFile(" + fileName + ") is A File");
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(params[0]);
                Log.i("params[0]", params[0]);

                // Open a HTTP connection to the URL
                photoConn = (HttpURLConnection) url.openConnection();
                photoConn.setDoInput(true);
                photoConn.setDoOutput(true);
                photoConn.setUseCaches(false);
                photoConn.setRequestMethod("POST");
                photoConn.setRequestProperty("Connection", "Keep-Alive");
                photoConn.setRequestProperty("ENCTYPE", "multipart/form-data");
                photoConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                photoConn.setRequestProperty("upload_file", fileName);
                Log.i(TAG, "fileName: " + fileName);

                // dataoutput은 outputstream이란 클래스를 가져오며, outputStream은 FileOutputStream의 하위 클래스이다.
                // output은 쓰기, input은 읽기, 데이터를 전송할 때 전송할 내용을 적는 것
                dos = new DataOutputStream(photoConn.getOutputStream());

                // 사용자 이름으로 폴더를 생성하기 위해 사용자 이름을 서버로 전송한다. 하나의 인자 전달 data1 = logo photo
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"data1\"" + lineEnd); // name의 \\ 안 인자가 PHP의 key
                dos.writeBytes(lineEnd);
                if(photoFlag == 0) { // photoFlag가 0인 경우 Logo사진이 저장, 아닐 경우 상품 사진이 저장
                    dos.writeBytes("LogoPhoto"); // logophoto 라는 값을 전달
                } else {
                    dos.writeBytes("ProductPhoto"); // productphoto 라는 값을 전달
                }

                dos.writeBytes(lineEnd);

                // 이미지 전송, 데이터 전달 uploaded_file 이라는 PHP key 값에 저장되는 내용은 fileName
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necessary after file data... 마지막에 two~~ lineEnd로 마무리 (인자 나열이 끝났음을 알림)
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = photoConn.getResponseCode();
                String serverResponseMessage = photoConn.getResponseMessage();

                Log.i(TAG, "[UploadImageToServer] HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200) {

                }

                // 결과 확인
                BufferedReader rd = null;

                rd = new BufferedReader(new InputStreamReader(photoConn.getInputStream(), "UTF-8"));

                while ((line = rd.readLine()) != null) {
                    Log.i("Upload State", line);
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (Exception e) {
                Log.e(TAG + "Error", e.toString());
            }
            return success;
        }
    }

    public String getLine() {
        return line;
    }
}
