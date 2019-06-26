package com.example.watsonz.onna_publisher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.watsonz.onna_publisher.helper.AppConfig;
import com.example.watsonz.onna_publisher.helper.AppController;
import com.example.watsonz.onna_publisher.helper.CustomAdapter_Purchasecupon;
import com.example.watsonz.onna_publisher.helper.InfoCupon;
import com.example.watsonz.onna_publisher.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by watsonz on 2015-07-29.
 */
public class profile_main extends Fragment implements View.OnClickListener {
    private SQLiteHandler db;
    private ArrayList<InfoCupon> mInfoArray;
    private CustomAdapter_Purchasecupon mAdapter;
    final String TAG = "my_selling_list";
    String store_name;
    String store_uid;
    EditText tv_userName;

    TextView noitem;

        public profile_main() {

        }

        static profile_main newInstance(int SectionNumber){
            profile_main fragment = new profile_main();
            Bundle args = new Bundle();
            args.putInt("section_number", SectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_profile,
                    container, false);

            noitem = (TextView)rootView.findViewById(R.id.tv_noitem);
            Button verfiy = (Button)rootView.findViewById(R.id.verify);
            verfiy.setOnClickListener(this);
            tv_userName = (EditText)rootView.findViewById(R.id.user_id);
            db = new SQLiteHandler(getActivity().getApplicationContext());
            HashMap<String, String> store_info = db.getUserDetails();
            store_name = store_info.get("name");
            store_uid = store_info.get("uid");
            getCuponList(store_uid);
            mInfoArray = new ArrayList<InfoCupon>();

            mAdapter = new CustomAdapter_Purchasecupon(getActivity(), mInfoArray);
            ListView mListview = (ListView)rootView.findViewById(R.id.listView);
            mListview.setAdapter(mAdapter);
            mListview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            return rootView;
        }

    public void onClick(View v){
        switch (v.getId()) {
           case R.id.verify:
               String name = tv_userName.getText().toString().trim();
               if (!name.isEmpty())
               {
                   verify_visit(name,store_uid,store_name);
               }
            break;
        }
    }
    private void getCuponList(final String send_user_name) {
        // Tag used to cancel the request
        String tag_string_req = "req_cupon_list";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PURCHASE_CUPON_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "cupon Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);

                    JSONArray cupon = jObj.getJSONArray("cupon");
                    int size = cupon.length();

                    for(int i =0; i < size; i++){
                        String id = cupon.getJSONObject(i).getString("id");
                        String store = cupon.getJSONObject(i).getString("name");
                        String price = cupon.getJSONObject(i).getString("price");
                        String time = cupon.getJSONObject(i).getString("time");
                        String mainA = cupon.getJSONObject(i).getString("mainA");
                        String mainB = cupon.getJSONObject(i).getString("mainB");
                        String mainC = cupon.getJSONObject(i).getString("mainC");
                        String sideA = cupon.getJSONObject(i).getString("sideA");
                        String sideB = cupon.getJSONObject(i).getString("sideB");
                        String sideC = cupon.getJSONObject(i).getString("sideC");
                        String drinkA = cupon.getJSONObject(i).getString("drinkA");
                        String drinkB = cupon.getJSONObject(i).getString("drinkB");
                        String drinkC = cupon.getJSONObject(i).getString("drinkC");
                        Log.d(TAG, "cupon Response: " + store + price);
                        db.addPurchaseCupon(id, store, price, time,mainA, mainB, mainC,
                                sideA, sideB, sideC, drinkA, drinkB, drinkC);
                    }
                    mInfoArray.clear();
                    mInfoArray = db.doWhileCursorToArray_Purchasecupon();
                    mAdapter.setArrayList(mInfoArray);
                    mAdapter.notifyDataSetChanged();
                    // Inserting row in users table

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(mInfoArray.size() == 0)noitem.setText("현재 등록된 항목이 없습니다.");
                else noitem.setText("");
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
                params.put("name", send_user_name);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void verify_visit(final String send_user_name, final String store_uid, final String store_name) {
        // Tag used to cancel the request
        String tag_string_req = "verify_visit";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VERIFY_VISIT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "cupon Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        // Now store the user in SQLite
                        JSONObject review = jObj.getJSONObject("review");
                        String id = review.getString("id");
                        Toast.makeText(getContext(),
                                "방문 인증이 되셨습니다 : "+id, Toast.LENGTH_LONG).show();
                        //Log.e(TAG, "Login result: " + error.getMessage());
                        // Inserting row in users table
                        //db.addOwner(num, name, place, uid, created_at);

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
                params.put("name", send_user_name);
                params.put("ownerUid", store_uid);
                params.put("store", store_name);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
