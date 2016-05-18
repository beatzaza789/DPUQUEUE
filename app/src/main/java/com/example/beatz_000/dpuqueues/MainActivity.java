package com.example.beatz_000.dpuqueues;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    @SuppressLint("NewApi")
    private Handler handler = new Handler();
    String di = "0";
    String ser = "0";
    String Q_NO = "0";
    String imei = "imei";
    String Q_TimeWait = "";
    String Q_Work = "0";
    String C_ID = "0";
    String Status = "";
    String service = "";
    String queue = "";
    String totime = "";
    //String ip = "192.168.1.37"; //beat
    //String ip = "192.168.81.1"; //nick
    //String ip = "192.168.157.1"; //aom
    //String ip = "seehamart.tk"; //web
    //String ip = "www.beatapisit.tk"; //web1freehost
    String ip = "dpuq.azurewebsites.net"; //azure
    int page = 0;
    int pagebooking = 0;
    int Q_Next = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        //setContentView(R.layout.userlogin);
        //login();
        //signup();
        //ShowMain();
        //PushStatus();
        //int di =0;
        if(status()==true){
            ShowMain();
            PushStatus();
        }
        else{
            login();
        }
    }

    public boolean status(){
        String url = "http://"+ip+"/dpuq/userchecklogin.php";
        String respond="";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("imei", imei));
        //params.add(new BasicNameValuePair("Emp_ID", Emp_NO));
        //params.add(new BasicNameValuePair("StartTime", StartTime));
        try {
            String resultServer  = getHttpPost(url,params);
            JSONObject c;
            c = new JSONObject(resultServer);
            respond = c.getString("status");


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(respond.equals("0")){
            return false;
        }
        else{
            return true;
        }
    }

    public void login(){
        setContentView(R.layout.userlogin);
        page = 0;
        final Button login = (Button) findViewById(R.id.button);
        final Button signup = (Button) findViewById(R.id.button3);

        login.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (CheckLogin() == 1) {
                    hidkeyborde();
                    ShowMain();
                } else
                    login();
            }
        });
        signup.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                signup();
            }
        });

    }
    public int CheckLogin(){
        final EditText Username = (EditText) findViewById(R.id.editUsername);
        final EditText Password = (EditText) findViewById(R.id.editPassword);
        String result = "";
        String url = "http://"+ip+"/dpuq/userlogin.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Username", Username.getText().toString()));
        params.add(new BasicNameValuePair("Password", Password.getText().toString()));
        params.add(new BasicNameValuePair("imei", imei));
        try {
            String resultServer  = getHttpPost(url,params);
            JSONObject c;
            c = new JSONObject(resultServer);
            result = c.getString("result");


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(result.equals("false")){
            Toast.makeText(MainActivity.this, "Login again", Toast.LENGTH_SHORT).show();
            return 0;}
        else{
            return 1;}
    }

    public void signup(){
        setContentView(R.layout.signup);
        page = 9;
        final EditText name = (EditText) findViewById(R.id.editText);
        final EditText sername = (EditText) findViewById(R.id.editText2);
        final EditText uid = (EditText) findViewById(R.id.editText3);
        final EditText pass = (EditText) findViewById(R.id.editText4);
        final Button singup = (Button) findViewById(R.id.buttonsignup);

        singup.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String post = "";
                if(name.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "ข้อมูลไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
                else if(sername.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "ข้อมูลไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
                else if(uid.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "ข้อมูลไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
                else if(pass.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "ข้อมูลไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                }
                else {

                    String url = "http://" + ip + "/dpuq/signup.php";

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("name", name.getText().toString()));
                    params.add(new BasicNameValuePair("sername", sername.getText().toString()));
                    params.add(new BasicNameValuePair("imei", imei));
                    params.add(new BasicNameValuePair("u_id", uid.getText().toString()));
                    params.add(new BasicNameValuePair("password", pass.getText().toString()));

                    String strStatusID = "0";
                    String strError = "Unknow Status!";
                    try {
                        String resultServer = getHttpPost(url, params);
                        JSONObject c;

                        c = new JSONObject(resultServer);
                        strStatusID = c.getString("StatusID");
                        strError = c.getString("Error");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // Prepare Save Data
                    if (strStatusID.equals("0")) {
            /*ad.setMessage(strError);
            ad.show();*/
                        Toast.makeText(MainActivity.this, strError, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Save Data Successfully", Toast.LENGTH_SHORT).show();
                        login();
                    }
                }
            }
        });
    }

    public void ShowMain()    {
        setContentView(R.layout.main);
        page =0;
        final LinearLayout online = (LinearLayout) findViewById(R.id.linearLayout3);
        final LinearLayout confirm = (LinearLayout) findViewById(R.id.linearLayout4);
        final LinearLayout myq = (LinearLayout) findViewById(R.id.myqueues);

        online.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ShowDivision();
            }
        });
        confirm.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ShowCode();
            }
        });
        myq.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ShowmyQ();
            }
        });
    }

    /////////////////////////////////////////////

    public void ShowDivision()    {
        setContentView(R.layout.division);
        page = 1;
        final LinearLayout btn2 = (LinearLayout) findViewById(R.id.tomoney);
        final LinearLayout btn1 = (LinearLayout) findViewById(R.id.toregister);
        btn2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ShowMoney();
                //setContentView(R.layout.money);
                di = "1" ;
            }
        });
        btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ShowRegister();
                //setContentView(R.layout.money);
                di = "2";
            }
        });
    }

    public void ShowRegister()    {
        setContentView(R.layout.register);
        page = 12;
        pagebooking = 2;

        Q_TimeWait = "notCon";
        C_ID = "0";
        Q_NO = "0";
        Q_Next = 0;
        final LinearLayout btn11 = (LinearLayout) findViewById(R.id.register01);
        final LinearLayout btn12 = (LinearLayout) findViewById(R.id.register02);
        final LinearLayout btn13 = (LinearLayout) findViewById(R.id.register03);
        final LinearLayout btn14 = (LinearLayout) findViewById(R.id.register04);
        final LinearLayout btn15 = (LinearLayout) findViewById(R.id.register05);
        final LinearLayout btn16 = (LinearLayout) findViewById(R.id.register06);
        final LinearLayout btn17 = (LinearLayout) findViewById(R.id.register07);

        btn11.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "7";
                showBooking();
            }
        });
        btn12.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "8";
                showBooking();
            }
        });
        btn13.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "9";
                showBooking();
            }
        });
        btn14.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "10";
                showBooking();
            }
        });
        btn15.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "11";
                showBooking();
            }
        });
        btn16.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "12";
                showBooking();
            }
        });
        btn17.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "13";
                showBooking();
            }
        });
    }

    public void ShowMoney()    {
        setContentView(R.layout.money);
        page = 11;
        pagebooking = 1;

        Q_TimeWait = "notCon";
        C_ID = "0";
        Q_NO = "0";
        Q_Next = 0;
        final LinearLayout btn11 = (LinearLayout) findViewById(R.id.money01);
        final LinearLayout btn12 = (LinearLayout) findViewById(R.id.money02);
        final LinearLayout btn13 = (LinearLayout) findViewById(R.id.money03);
        final LinearLayout btn14 = (LinearLayout) findViewById(R.id.money04);
        final LinearLayout btn15 = (LinearLayout) findViewById(R.id.money05);
        final LinearLayout btn16 = (LinearLayout) findViewById(R.id.money06);

        btn11.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "1";
                showBooking();
            }
        });
        btn12.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "2";
                showBooking();
            }
        });
        btn13.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "3";
                showBooking();
            }
        });
        btn14.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "4";
                showBooking();
            }
        });
        btn15.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "5";
                showBooking();
            }
        });
        btn16.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ser = "6";
                showBooking();
            }
        });
    }

    public void showBooking()    {
        setContentView(R.layout.booking);

        final TextView textViewQ1 = (TextView) findViewById(R.id.textView16);
        final TextView textViewQ2 = (TextView) findViewById(R.id.textView17);
        final TextView textViewQ3 = (TextView) findViewById(R.id.textView18);
        final TextView textViewQ4 = (TextView) findViewById(R.id.textView28);
        final Button btn1 = (Button) findViewById(R.id.button);

        //GetData();
        textViewQ4.setVisibility(View.INVISIBLE);
        int check = 0;
        check = GetData();
        if(check==0){
            btn1.setVisibility(View.INVISIBLE);
            textViewQ4.setVisibility(View.VISIBLE);
        }
        else if(check==2){
            btn1.setVisibility(View.INVISIBLE);
            textViewQ4.setVisibility(View.VISIBLE);
            textViewQ4.setText("ไม่สามารถจองคิวได้");
        }

        textViewQ1.setText(Q_NO);
        //String strQ_Next = Q_Next + "";
        textViewQ2.setText(Q_Work);

        textViewQ3.setText(Q_TimeWait);

        page = 101;

        btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(SaveData())
                {
                    // When Save Complete
                    ShowMain();
                }
            }
        });
    }

    /////////////////////////////////////////////

    public void ShowCode()   {
       setContentView(R.layout.code);
       //openkeyborde();
       page = 2;
       final Button btncode = (Button) findViewById(R.id.buttoncode);

       btncode.setOnClickListener(new OnClickListener() {
           public void onClick(View v) {
               CheckCode();
               //setContentView(R.layout.codedetail);
           }
       });

   }

    public void CheckCode(){
        final EditText Incode = (EditText) findViewById(R.id.editCode);

        String url = "http://"+ip+"/dpuq/checkcode.php";
        String CodeQ = "";
        String CodetimeW = "";
        String codestatus = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("code", Incode.getText().toString()));
        params.add(new BasicNameValuePair("Q_Tel", imei));
        try {
        String resultServer  = getHttpPost(url,params);

        JSONObject c;

            c = new JSONObject(resultServer);
            CodeQ = c.getString("Q_No");
            CodetimeW = c.getString("Q_TimeWait");
            codestatus = c.getString("Status");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(codestatus .equals("1")){
            openkeyborde();
            setContentView(R.layout.codedetail);
            final Button codeok = (Button) findViewById(R.id.button2);
            final TextView textViewCodeQ1 = (TextView) findViewById(R.id.textView25);
            final TextView textViewCodeQ2 = (TextView) findViewById(R.id.textView27);

            textViewCodeQ1.setText(CodeQ);
            textViewCodeQ2.setText(CodetimeW);

            codeok.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ShowMain();
                }
            });
        }
        else{
            openkeyborde();
            Toast.makeText(MainActivity.this, "รหัสยืนยันผิด", Toast.LENGTH_SHORT).show();
            ShowMain();
        }
    }

    /////////////////////////////////////////////

    public void ShowmyQ(){
        page = 3;
        setContentView(R.layout.listqueues);
        final ListView lisView1 = (ListView)findViewById(R.id.listView1);


        String url = "http://"+ip+"/dpuq/myQ.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("imei", "2147483647"));
        params.add(new BasicNameValuePair("imei", imei));

        try {
            JSONArray data = new JSONArray(getHttpPost(url, params));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
                String service = "";
                switch(c.getString("S_ID")){
                    case "1": service = "ชำระเงินค่าเทอม"; break;
                    case "2": service = "ทำสัญญาผ่อนชำระเงิน"; break;
                    case "3": service = "ติดต่อโอนทุน มธบ,กยศ"; break;
                    case "4": service = "จ่ายเงินสดและเช็ค"; break;
                    case "5": service = "ตรวจสอบเอกสารเบิกเงิน"; break;
                    case "6": service = "จองงบประมาณ,ยืมเงินทดรองจ่าย"; break;
                    case "7": service = "ขึ้นทะเบียนบัณฑิต"; break;
                    case "8": service = "ทำหนังสือรับรอง"; break;
                    case "9": service = "รับเอกสาร"; break;
                    case "10": service = "ยื่นคำร้อง"; break;
                    case "11": service = "ทำ/รับ บัตรนักศึกษา"; break;
                    case "12": service = "เปลี่ยนชื่อนามสกุล"; break;
                    case "13": service = "รับมอบตัวนักศึกษา"; break;


                }

                map = new HashMap<String, String>();
                map.put("Q_No", c.getString("Q_No"));
                map.put("Q_ToTime", c.getString("Timewait"));
                map.put("Service_ID", service);
                MyArrList.add(map);

            }


            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(MainActivity.this, MyArrList, R.layout.listqueuescolom,
                    new String[] {"Q_No", "Q_ToTime", "Service_ID"}, new int[] {R.id.Q_no, R.id.Q_time, R.id.Q_Service});
            lisView1.setAdapter(sAdap);



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////

    public int GetData() {
        String url = "http://"+ip+"/dpuq/bookingget.php";
        String respond ="";
        String checkbook ="";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("division", di));
        params.add(new BasicNameValuePair("service", ser));
        params.add(new BasicNameValuePair("imei", imei));
        try {
        String resultServer  = getHttpPost(url,params);



        JSONObject c;

            c = new JSONObject(resultServer);//tString("Q_TimeWait");
            respond = c.getString("check");
            Q_NO = c.getString("Q_No");
            Q_TimeWait = c.getString("SumTime");
            Q_Work = c.getString("workQ");
            checkbook = c.getString("checkbook");//Toast.makeText(MainActivity.this, checkbook, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(checkbook.equals("0")){
            if(respond.equals("0")){
                Q_NO = "0";
                Q_Work = "0";
                Q_TimeWait = "00:00:00";
                return 0;
            }
            else{
                //Q_Next = Integer.parseInt(Q_NO);
                //Q_Next ++;
                return 1;
            }
        }
        else {
            return 2;
        }




    }

    public boolean SaveData()    {


        String url = "http://"+ip+"/dpuq/booking.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("no", Q_NO));
        params.add(new BasicNameValuePair("Q_Tel", imei));
        params.add(new BasicNameValuePair("Q_TimeWait", Q_TimeWait));
        params.add(new BasicNameValuePair("Q_Totime", Q_Work));
        params.add(new BasicNameValuePair("S_ID", ser));
        params.add(new BasicNameValuePair("C_ID", C_ID));
        params.add(new BasicNameValuePair("D_ID", di));


        String strStatusID = "0";
        String strError = "Unknow Status!";
        try {
            String resultServer  = getHttpPost(url,params);
            JSONObject c;

            c = new JSONObject(resultServer);
            strStatusID = c.getString("StatusID");
            strError = c.getString("Error");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Prepare Save Data
        if(strStatusID.equals("0"))
        {
            /*ad.setMessage(strError);
            ad.show();*/
            Toast.makeText(MainActivity.this, strError, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Save Data Successfully", Toast.LENGTH_SHORT).show();
        }


        return true;
    }

    /////////////////////////////////////////////

    public void PushStatus() {

        Runnable runnable = new Runnable() {

            public void run() {

                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            // Handler thread
                            Status = "";
                            Status = GetStatus();
                            if(!Status.equals("")){
                                createNotification();
                                //Toast.makeText(MainActivity.this, Status+"  "+queue+"\n"+ service+"\n"+ totime, Toast.LENGTH_SHORT).show();
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    public String GetStatus() {
        service = "";
        queue = "";
        totime = "";

        String url = "http://"+ip+"/dpuq/pushstatus.php";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("imei", imei));
        try {

            JSONArray data = new JSONArray(getHttpPost(url, params));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                switch(c.getString("S_ID")){
                    case "1": service = "ชำระเงินค่าเทอม"; break;
                    case "2": service = "ทำสัญญาผ่อนชำระเงิน"; break;
                    case "3": service = "ติดต่อโอนทุน มธบ,กยศ"; break;
                    case "4": service = "จ่ายเงินสดและเช็ค"; break;
                    case "5": service = "ตรวจสอบเอกสารเบิกเงิน"; break;
                    case "6": service = "จองงบประมาณ,ยืมเงินทดรองจ่าย"; break;
                    case "7": service = "ขึ้นทะเบียนบัณฑิต"; break;
                    case "8": service = "ทำหนังสือรับรอง"; break;
                    case "9": service = "รับเอกสาร"; break;
                    case "10": service = "ยื่นคำร้อง"; break;
                    case "11": service = "ทำ/รับ บัตรนักศึกษา"; break;
                    case "12": service = "เปลี่ยนชื่อนามสกุล"; break;
                    case "13": service = "รับมอบตัวนักศึกษา"; break;
                }
                queue = c.getString("Q_No");
                totime = c.getString("Q_ToTime");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return queue;

    }

    public void createNotification() {

        Intent intent = new Intent(this, notification.class);
        //intent.putExtra("message", MESSAGE);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(notification.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification =
                new NotificationCompat.Builder(this) // this is context
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("DPU Q")
                        .setContentText("คิวที่ "+queue+" "+ service/*+" เวลา "+ totime*/)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.sound = alarmSound;
        //notification.defaults = Notification.DEFAULT_SOUND; // Sound
        notification.defaults = Notification.DEFAULT_VIBRATE; // Vibrate

        notificationManager.notify(1000, notification);

    }

    /////////////////////////////////////////////

    public void hidkeyborde(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void openkeyborde(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        
    }

    public String getHttpPost(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    @Override
    public void onBackPressed() {
        switch (page) {
            case 0      : finish(); break;
            case 1      : ShowMain(); break;
            case 11     : ShowDivision(); break;
            case 12     : ShowDivision(); break;
            case 101    :   if(pagebooking==1)
                                ShowMoney();
                            if(pagebooking==2)
                                ShowRegister();
                            break;
            case 2      : ShowMain(); break;
            case 3      : ShowMain(); break;

            case 9: login(); break;
            //case 6 : ShowConventView2(); break;
            //case 7 : ShowConventView1(); break;
            //default:             ShowMain(); break;


        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            String url = "http://"+ip+"/dpuq/userlogout.php";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("imei", imei));

            String resultServer  = getHttpPost(url, params);

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
