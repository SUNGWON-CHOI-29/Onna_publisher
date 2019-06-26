package com.example.watsonz.onna_publisher;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.watsonz.onna_publisher.helper.AppConfig;
import com.example.watsonz.onna_publisher.helper.AppController;
import com.example.watsonz.onna_publisher.helper.SQLiteHandler;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by watsonz on 2016-06-23.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "MyInstanceIDService";
    private SQLiteHandler db;

    public RegistrationIntentService()    {
        super(TAG);
        Log.d(TAG, "RegistrationIntentService()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            //TODO server로 token 전송
            Log.i(TAG, "token: " + token);
            db = new SQLiteHandler(this);
            HashMap<String, String> user = db.getUserDetails();
            String uid = user.get("uid");
            appIdRegi(token,uid);
        }
        catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }
    private void appIdRegi(final String appid, final String uid) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_APPID_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        // Now store the user in SQLite
                        //String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String AppID = user.getString("Appid");
                        Log.d(TAG, "appID Response: " + AppID);
                        //String email = user.getString("email");
                       // String created_at = user
                        //        .getString("created_at");

                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("appId", appid);
                params.put("uid", uid);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
