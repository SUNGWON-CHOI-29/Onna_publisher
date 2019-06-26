package com.example.watsonz.onna_publisher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.watsonz.onna_publisher.helper.AppConfig;
import com.example.watsonz.onna_publisher.helper.AppController;
import com.example.watsonz.onna_publisher.helper.CuponCustomAdapter;
import com.example.watsonz.onna_publisher.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by watsonz on 2016-03-16.
 */
public class check_auction_Activity extends Activity implements View.OnClickListener{
    ImageView user_img;
    String user_name;
    String auction_id;
    String store_name;
    String user_uid;
    String uid;

    private SQLiteHandler db;
    private ArrayList<String> mGroupList = null;
    private ArrayList<HashMap<String, Integer>> mChildList = null;
    private ArrayList<HashMap<String, Integer>> resultList = null;
    private HashMap<String, Integer> mainProduct = null;
    private HashMap<String, Integer> sideProduct = null;
    private HashMap<String, Integer> drinkProduct = null;
    ExpandableListView mListView;
    private CuponCustomAdapter mBaseExpandableAdapter = null;
    AlertDialog.Builder m_alert;
    final String TAG = "check_auction_Activity";
    TextView tv_time;
    EditText money;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteHandler(this.getApplicationContext());

        setContentView(R.layout.activity_check_auction);
        mListView = (ExpandableListView)findViewById(R.id.listview);

        m_alert = new AlertDialog.Builder(this);
        mGroupList = new ArrayList<String>();
        mChildList = new ArrayList<HashMap<String, Integer>>();
        mainProduct = new HashMap<String, Integer>();
        sideProduct = new HashMap<String, Integer>();
        drinkProduct = new HashMap<String, Integer>();

        mGroupList.add("메인");
        mGroupList.add("사이드");
        mGroupList.add("음료");

        // 메인 해당하는 child 리스트 추가
        mainProduct.put("메인 A",0);
        mainProduct.put("메인 B",0);
        mainProduct.put("메인 C",0);

        // 자동모드에 해당하는 child 리스트 추가
        sideProduct.put("사이드 A",0);
        sideProduct.put("사이드 B",0);
        sideProduct.put("사이드 C",0);

        // 자동모드에 해당하는 child 리스트 추가
        drinkProduct.put("음료 A",0);
        drinkProduct.put("음료 B", 0);
        drinkProduct.put("음료 C", 0);

        // 그룹 리스트에 차일드 리스트 붙이기
        mChildList.add(mainProduct);
        mChildList.add(sideProduct);
        mChildList.add(drinkProduct);

        HashMap<String, String> user = db.getUserDetails();
        store_name = user.get("name");
        uid = user.get("uid");
        auction_id = getIntent().getStringExtra("id");
        user_name = getIntent().getStringExtra("name");
        user_uid = getIntent().getStringExtra("userUid");
        String menu = getIntent().getStringExtra("menu");
        String people = getIntent().getStringExtra("people");
        String price = getIntent().getStringExtra("price");
        String time = getIntent().getStringExtra("time");

        TextView tx1 = (TextView)findViewById(R.id.people);
        TextView tx2 = (TextView)findViewById(R.id.menu);
        TextView tx3 = (TextView)findViewById(R.id.price);
        tv_time  = (TextView)findViewById(R.id.time);
        TextView tx5 = (TextView)findViewById(R.id.user_name);
        user_img = (ImageView)findViewById(R.id.img_user);
        money = (EditText)findViewById(R.id.money);

        BitmapDrawable bitmap;
        Resources res = getResources();
        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.user);
        user_img.setImageDrawable(bitmap);
        
        tx1.setText(people);
        tx2.setText(menu);
        tx3.setText(price);
        tv_time.setText(time);
        tx5.setText(user_name);



        Button send = (Button)findViewById(R.id.send);
        send.setOnClickListener(this);

        mBaseExpandableAdapter = new CuponCustomAdapter(this, mGroupList, mChildList);
        mListView.setAdapter(mBaseExpandableAdapter);

        // 그룹 클릭 했을 경우 이벤트
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // 차일드 클릭 했을 경우 이벤트
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String childValue = (String)parent.getItemAtPosition(childPosition);
                // child 클릭 시 실제 item 값 가져오기
                childValue = (String)parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                Toast.makeText(getApplicationContext(), "c click = " + childValue,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

                // 그룹이 닫힐 경우 이벤트
                mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) {
                    }
        });

        // 그룹이 열릴 경우 이벤트
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int groupCount = mBaseExpandableAdapter.getGroupCount();

                // 한 그룹을 클릭하면 나머지 그룹들은 닫힌다.
                for (int i = 0; i < groupCount; i++) {
                    if (!(i == groupPosition))
                        mListView.collapseGroup(i);
                }
            }
        });
    }
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.send:
                resultList = mBaseExpandableAdapter.getChildList();
                String result = "";
                int mainA = resultList.get(0).get("메인 A"); final String str_mainA = String.valueOf(mainA);
                int mainB = resultList.get(0).get("메인 B"); final String str_mainB = String.valueOf(mainB);
                int mainC = resultList.get(0).get("메인 C"); final String str_mainC = String.valueOf(mainC);
                int sideA = resultList.get(1).get("사이드 A"); final String str_sideA = String.valueOf(sideA);
                int sideB = resultList.get(1).get("사이드 B"); final String str_sideB = String.valueOf(sideB);
                int sideC = resultList.get(1).get("사이드 C"); final String str_sideC = String.valueOf(sideC);
                int drinkA = resultList.get(2).get("음료 A"); final String str_drinkA = String.valueOf(drinkA);
                int drinkB = resultList.get(2).get("음료 B"); final String str_drinkB = String.valueOf(drinkB);
                int drinkC = resultList.get(2).get("음료 C"); final String str_drinkC = String.valueOf(drinkC);
                if(mainA != 0) result = result.concat("메인 A   : "+mainA+"\n");
                if(mainB != 0)result = result.concat("메인 B   : "+mainB+"\n");
                if(mainC != 0)result = result.concat("메인 C   : "+mainC+"\n");
                if(sideA != 0)result = result.concat("사이드 A : "+sideA+"\n");
                if(sideB != 0) result = result.concat("사이드 B : "+sideB+"\n");
                if(sideC != 0) result = result.concat("사이드 C : "+sideC+"\n");
                if(drinkA != 0)result = result.concat("음료 A   : "+drinkA+"\n");
                if(drinkB != 0)result = result.concat("음료 B   : "+drinkB+"\n");
                if(drinkC != 0)result = result.concat("음료 C   : "+drinkC+ "\n\n");
                result = result.concat("금액 : " + money.getText().toString().trim());


                m_alert.setTitle("쿠폰 내역") //팝업창 타이틀바
                        .setMessage(result) //팝업창 내용
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {

                            }
                        })
                        .setPositiveButton("발송", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int sumthin) {
                                        sendCupon(auction_id,user_name,uid, store_name, money.getText().toString().trim(), tv_time.getText().toString().trim(),
                                                str_mainA, str_mainB, str_mainC,
                                                str_sideA, str_sideB,str_sideC,
                                                str_drinkA, str_drinkB, str_drinkC);
                                        db.setAuctionSend(auction_id);
                        finish();
                    }
                })
                .setCancelable(false)
                .show(); // 팝업창 보여줌
        break;
    }
    }
    private void sendCupon(final String aid, final String name,final String uid, final String store, final String price, final String time, final String mainA, final String mainB, final String mainC,
                           final String SideA, final String SideB, final String SideC, final String DrinkA, final String DrinkB, final String DrinkC) {
        // Tag used to cancel the request
        String tag_string_req = "req_Send_Cupon";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CUPON_SEND, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "cupon Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    JSONObject cupon = jObj.getJSONObject("cupon");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String id = cupon.getString("id");
                        Toast.makeText(getApplicationContext(), "cupon successfully registered." + id, Toast.LENGTH_LONG).show();
                        pushCupon(store_name,user_uid);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                    // Inserting row in users table
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("auction_id",aid);
                params.put("name", name);
                params.put("uid", uid);
                params.put("store", store);
                params.put("price", price);
                params.put("time",time);
                params.put("mainA", mainA);
                params.put("mainB", mainB);
                params.put("mainC", mainC);
                params.put("sideA", SideA);
                params.put("sideB", SideB);
                params.put("sideC", SideC);
                params.put("drinkA", DrinkA);
                params.put("drinkB", DrinkB);
                params.put("drinkC", DrinkC);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void pushCupon(final String sender_name,final String receiver_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_Send_Cupon";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUSH_CUPON, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "cupon Response: " + response.toString());

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("sender", sender_name);
                params.put("receiver", receiver_id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
