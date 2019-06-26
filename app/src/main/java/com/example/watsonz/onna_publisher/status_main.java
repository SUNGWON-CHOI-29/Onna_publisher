package com.example.watsonz.onna_publisher;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.watsonz.onna_publisher.helper.AppConfig;
import com.example.watsonz.onna_publisher.helper.AppController;
import com.example.watsonz.onna_publisher.helper.CustomAdapter;
import com.example.watsonz.onna_publisher.helper.Infoauction;
import com.example.watsonz.onna_publisher.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
/**
 * Created by watsonz on 2015-07-29.
 */
public class status_main extends Fragment{
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private SQLiteHandler db;
    private Cursor mCursor;
    private Infoauction mInfoClass;
    private ArrayList<Infoauction> mInfoArray;
    private CustomAdapter mAdapter;
    final String TAG = "MainActivity";
    HashMap<String, String> getauction = new HashMap<String, String>();
    TextView tv_noitem;
    Bundle auction_extras;

    public status_main() {

        }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
        static status_main newInstance(int SectionNumber){
            status_main fragment = new status_main();
            Bundle args = new Bundle();
            args.putInt("section_number", SectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
    @Override
    public void onResume() {
        super.onResume();
        //sm.registerListener(accL, accSensor, SensorManager.SENSOR_DELAY_NORMAL);    // 가속도 센서 리스너 오브젝트를 등록
        mInfoArray.clear();
        mInfoArray = db.doWhileCursorToArray();
        mAdapter.setArrayList(mInfoArray);
        mAdapter.notifyDataSetChanged();
        if(mInfoArray.size() == 0)tv_noitem.setText("현재 등록된 항목이 없습니다.");
        else tv_noitem.setText("");
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_status,
                    container, false);
            db = new SQLiteHandler(getActivity().getApplicationContext());
            HashMap<String, String> store = db.getUserDetails();
            String place = store.get("place");
            Log.d(TAG, "place Response: " + place);
            getAuctionList(place);
            mInfoArray = new ArrayList<Infoauction>();
            tv_noitem = (TextView)rootView.findViewById(R.id.tv_noitem);
            //mInfoArray = db.doWhileCursorToArray();
            mAdapter = new CustomAdapter(getActivity(), mInfoArray);
            ListView mListview = (ListView)rootView.findViewById(R.id.listView);
            mListview.setAdapter(mAdapter);
            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    check_auction(db.id_set.get(i));
                    getauction = db.getAuction(db.id_set.get(i));
                    Log.d(TAG, "id Response: " + db.id_set.get(i));
                    // 다음 액티비티로 넘길 Bundle 데이터를 만든다.
                    auction_extras = new Bundle();
                   auction_extras.putString("id", db.id_set.get(i));
                   auction_extras.putString("userUid", getauction.get("userUid"));
                   auction_extras.putString("name", getauction.get("name"));
                   auction_extras.putString("place", getauction.get("place"));
                   auction_extras.putString("people", getauction.get("people"));
                   auction_extras.putString("menu", getauction.get("menu"));
                   auction_extras.putString("price", getauction.get("price"));
                   auction_extras.putString("time", getauction.get("time"));
                    // 인텐트를 생성한다.
                    // 컨텍스트로 현재 액티비티를, 생성할 액티비티로 ItemClickExampleNextActivity 를 지정한다.
                }
            });
            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
                getActivity().startService(intent);    //서비스 실행
            }
            return rootView;
        }
    private boolean checkPlayServices() {
       GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }
    private void getAuctionList(final String place) {
        // Tag used to cancel the request
        String tag_string_req = "req_Auction_list";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_AUC_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "list Response: " + response.toString());

                    try {
                            JSONObject jObj = new JSONObject(response);

                            JSONArray auction = jObj.getJSONArray("auction");
                            int size = auction.length();

                            for(int i =0; i < size; i++){
                                String id = auction.getJSONObject(i).getString("id");
                                String uid = auction.getJSONObject(i).getString("userUid");
                                String name = auction.getJSONObject(i).getString("name");
                                String place = auction.getJSONObject(i).getString("place");
                                String people = auction.getJSONObject(i).getString("people");
                                String menu = auction.getJSONObject(i).getString("menu");
                                String price = auction.getJSONObject(i).getString("price");
                                String time = auction.getJSONObject(i).getString("time");
                                Log.d(TAG, "add Response: " + name + place + menu + price + time);
                                db.addAuction(id,uid,name, place, people, menu, price, time);
                            }
                    // Inserting row in users table
                    mInfoArray.clear();
                    mInfoArray = db.doWhileCursorToArray();
                    mAdapter.setArrayList(mInfoArray);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(mInfoArray.size() == 0)tv_noitem.setText("현재 등록된 항목이 없습니다.");
                else tv_noitem.setText("");
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
                params.put("place", place);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void check_auction(final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_Auction_list";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_AUC_CHECK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "list Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        Intent intent = new Intent(getActivity(), check_auction_Activity.class);
                        // 위에서 만든 Bundle을 인텐트에 넣는다.
                        intent.putExtras(auction_extras);
                        // 액티비티를 생성한다.
                        startActivity(intent);
                    } else {
                        // Error in login. Get the error message
                        Toast.makeText(getContext(),
                                "이미 삭제된 Auction입니다", Toast.LENGTH_LONG).show();
                        db.deleteAuction(id);
                        db.id_set.remove(id);
                        mInfoArray.clear();
                        mInfoArray = db.doWhileCursorToArray();
                        mAdapter.setArrayList(mInfoArray);
                        mAdapter.notifyDataSetChanged();
                    }
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
                params.put("auction_id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
