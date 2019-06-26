package com.example.watsonz.onna_publisher.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by watsonz on 2016-03-10.
 */
public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_OWNER = "owner";
    private static final String TABLE_AUCTION = "auction";
    private static final String TABLE_PURCHASE_CUPON = "purchase_cupon";

    // Login Table Columns names
    private static final String KEY_AUCTION_ID = "auction_id";
    private static final String KEY_USER_UID = "userUid";
    private static final String KEY_OWNER_UID = "ownerUid";
    private static final String KEY_ID = "id";
    private static final String KEY_NUM = "num";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String KEY_place = "place";
    private static final String KEY_people = "people";
    private static final String KEY_menu = "menu";
    private static final String KEY_price = "price";
    private static final String KEY_time = "time";
    private static final String KEY_SEND = "send";

    private static final String KEY_CUPON_ID = "cupon_id";
    private static final String KEY_STORE = "store";
    private static final String KEY_STORE_UID = "store_uid";
    private static final String KEY_MAIN_A = "mainA";
    private static final String KEY_MAIN_B = "mainB";
    private static final String KEY_MAIN_C = "mainC";
    private static final String KEY_SIDE_A = "sideA";
    private static final String KEY_SIDE_B = "sideB";
    private static final String KEY_SIDE_C = "sideC";
    private static final String KEY_DRINK_A = "drinkA";
    private static final String KEY_DRINK_B = "drinkB";
    private static final String KEY_DRINK_C = "drinkC";

    public ArrayList<String> id_set = new ArrayList<String>();
    public ArrayList<String> purchase_cupon_id = new ArrayList<String>();

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_OWNER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUM + " TEXT UNIQUE,"
                + KEY_NAME + " TEXT,"
                + KEY_place + " TEXT," + KEY_OWNER_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_AUCTION_TABLE = "CREATE TABLE " + TABLE_AUCTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_AUCTION_ID + " TEXT UNIQUE,"
                + KEY_USER_UID + " TEXT,"
                + KEY_NAME + " TEXT," + KEY_place + " TEXT,"
                + KEY_people + " TEXT," + KEY_menu + " TEXT,"
                + KEY_price + " TEXT," + KEY_time + " TEXT," + KEY_SEND + " TEXT"+ ")";
        db.execSQL(CREATE_AUCTION_TABLE);

        String CREATE_PURHCASE_CUPON_TABLE = "CREATE TABLE " + TABLE_PURCHASE_CUPON + "("
                + KEY_CUPON_ID + " TEXT UNIQUE," + KEY_STORE + " TEXT,"
                + KEY_price + " TEXT," + KEY_time + " TEXT,"
                + KEY_MAIN_A + " TEXT," + KEY_MAIN_B + " TEXT," + KEY_MAIN_C + " TEXT,"
                + KEY_SIDE_A + " TEXT," + KEY_SIDE_B + " TEXT," + KEY_SIDE_C + " TEXT,"
                + KEY_DRINK_A + " TEXT," + KEY_DRINK_B + " TEXT," + KEY_DRINK_C + " TEXT" + ")";
        db.execSQL(CREATE_PURHCASE_CUPON_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OWNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUCTION);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addOwner(String num, String name, String place, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NUM, num);
        values.put(KEY_NAME, name); // Name
        values.put(KEY_place, place); // Email
        values.put(KEY_OWNER_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_OWNER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New owner inserted into sqlite: " + id);
    }

    public void addAuction(String id, String uid, String name, String place, String people, String menu,String price, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AUCTION_ID,id);
        values.put(KEY_USER_UID,uid);
        values.put(KEY_NAME, name); // Name
        values.put(KEY_place, place);
        values.put(KEY_people,people);
        values.put(KEY_menu, menu);
        values.put(KEY_price, price);
        values.put(KEY_time, time);
        values.put(KEY_SEND, "false");

        // Inserting Row
        long db_id = db.insert(TABLE_AUCTION, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Auction inserted into sqlite: " + db_id);
    }

    public void addPurchaseCupon(String cupon_id, String store, String price, String time, String mainA, String mainB, String mainC,
                                 String sideA, String sideB, String sideC, String drinkA, String drinkB, String drinkC) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUPON_ID, cupon_id);
        values.put(KEY_STORE, store); // Name
        values.put(KEY_price, price);
        values.put(KEY_time,time);
        values.put(KEY_MAIN_A, mainA);
        values.put(KEY_MAIN_B, mainB);
        values.put(KEY_MAIN_C, mainC);
        values.put(KEY_SIDE_A, sideA);
        values.put(KEY_SIDE_B, sideB);
        values.put(KEY_SIDE_C, sideC);
        values.put(KEY_DRINK_A, drinkA);
        values.put(KEY_DRINK_B, drinkB);
        values.put(KEY_DRINK_C, drinkC);

        // Inserting Row
        long id = db.insert(TABLE_PURCHASE_CUPON, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New Purchased Cupon inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> owner = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_OWNER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            owner.put("num", cursor.getString(1));
            owner.put("name", cursor.getString(2));
            owner.put("place", cursor.getString(3));
            owner.put("uid", cursor.getString(4));
            owner.put("created_at", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + owner.toString());

        return owner;
    }
    public HashMap<String, String> getAuction(String id) {
        HashMap<String, String> auction_list = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_AUCTION + " WHERE auction_id = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            auction_list.put("auction_id", cursor.getString(1));
            auction_list.put("userUid", cursor.getString(2));
            auction_list.put("name", cursor.getString(3));
            auction_list.put("place", cursor.getString(4));
            auction_list.put("people", cursor.getString(5));
            auction_list.put("menu", cursor.getString(6));
            auction_list.put("price", cursor.getString(7));
            auction_list.put("time", cursor.getString(8));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + auction_list.toString());

        return auction_list;
    }
    public HashMap<String, String> getPurchaseCupon(String id) {
        HashMap<String, String> cupon_list = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_PURCHASE_CUPON + " WHERE cupon_id = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cupon_list.put("cupon_id", cursor.getString(0));
            cupon_list.put("store", cursor.getString(1));
            cupon_list.put("price", cursor.getString(2));
            cupon_list.put("time", cursor.getString(3));
            cupon_list.put("mainA", cursor.getString(4));
            cupon_list.put("mainB", cursor.getString(5));
            cupon_list.put("mainC", cursor.getString(6));
            cupon_list.put("sideA", cursor.getString(7));
            cupon_list.put("sideB", cursor.getString(8));
            cupon_list.put("sideC", cursor.getString(9));
            cupon_list.put("drinkA", cursor.getString(10));
            cupon_list.put("drinkB", cursor.getString(11));
            cupon_list.put("drinkC", cursor.getString(12));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching cupon from Sqlite: " + cupon_list.toString());

        return cupon_list;
    }

    public void setAuctionSend(String id) {
        String updateQuery = "UPDATE " + TABLE_AUCTION + " set send = 'true'" + " WHERE auction_id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL(updateQuery);
        // Move to first row
        db.close();
        // return user
    }
    /**
     * DB에서 받아온 값을 ArrayList에 Add
     */
    public ArrayList<Infoauction> doWhileCursorToArray(){

        Cursor mCursor;
        Infoauction mInfoClass;
        ArrayList<Infoauction> mInfoArray;
        String selectQuery = "SELECT * FROM " + TABLE_AUCTION;

        SQLiteDatabase db = this.getReadableDatabase();
        mCursor = db.rawQuery(selectQuery, null);
        mInfoArray = new ArrayList<Infoauction>();

        while (mCursor.moveToNext()) {
            String send = mCursor.getString(mCursor.getColumnIndex("send"));
            Log.d(TAG, "send response :" + send);
            if(send.equals("false")) {
                Log.d(TAG, "send response : not sended");
                mInfoClass = new Infoauction(
                        mCursor.getString(mCursor.getColumnIndex("auction_id")),
                        mCursor.getString(mCursor.getColumnIndex("userUid")),
                        mCursor.getString(mCursor.getColumnIndex("name")),
                        mCursor.getString(mCursor.getColumnIndex("place")),
                        mCursor.getString(mCursor.getColumnIndex("people")),
                        mCursor.getString(mCursor.getColumnIndex("menu")),
                        mCursor.getString(mCursor.getColumnIndex("price")),
                        mCursor.getString(mCursor.getColumnIndex("time"))
                );
                id_set.add(mCursor.getString(mCursor.getColumnIndex("auction_id")));
                mInfoArray.add(mInfoClass);
            }
        }
        mCursor.close();
        return mInfoArray;
    }
    public ArrayList<InfoCupon> doWhileCursorToArray_Purchasecupon(){
        Cursor mCursor;
        InfoCupon mInfoClass;
        ArrayList<InfoCupon> mInfoArray;
        String selectQuery = "SELECT * FROM " + TABLE_PURCHASE_CUPON;

        SQLiteDatabase db = this.getReadableDatabase();
        mCursor = db.rawQuery(selectQuery, null);
        mInfoArray = new ArrayList<InfoCupon>();
        while (mCursor.moveToNext()) {

            mInfoClass = new InfoCupon(
                    mCursor.getString(mCursor.getColumnIndex("cupon_id")),
                    "","",
                    mCursor.getString(mCursor.getColumnIndex("store")),
                    mCursor.getString(mCursor.getColumnIndex("price")),
                    mCursor.getString(mCursor.getColumnIndex("time")),
                    mCursor.getString(mCursor.getColumnIndex("mainA")),
                    mCursor.getString(mCursor.getColumnIndex("mainB")),
                    mCursor.getString(mCursor.getColumnIndex("mainC")),
                    mCursor.getString(mCursor.getColumnIndex("sideA")),
                    mCursor.getString(mCursor.getColumnIndex("sideB")),
                    mCursor.getString(mCursor.getColumnIndex("sideC")),
                    mCursor.getString(mCursor.getColumnIndex("drinkA")),
                    mCursor.getString(mCursor.getColumnIndex("drinkB")),
                    mCursor.getString(mCursor.getColumnIndex("drinkC")));
            purchase_cupon_id.add(mCursor.getString(mCursor.getColumnIndex("cupon_id")));
            mInfoArray.add(mInfoClass);
        }

        mCursor.close();
        return mInfoArray;
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteAuction(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AUCTION, KEY_AUCTION_ID+"=?", new String[]{id});
        db.close();
        Log.d(TAG, "Deleted response :"+id);
    }
}
