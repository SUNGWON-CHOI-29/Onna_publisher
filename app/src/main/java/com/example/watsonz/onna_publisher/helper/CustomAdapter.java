package com.example.watsonz.onna_publisher.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.watsonz.onna_publisher.R;

import java.util.ArrayList;

/**
 * Created by watsonz on 2016-03-10.
 */
public class CustomAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Infoauction> infoList;
    private ViewHolder viewHolder;

    public CustomAdapter(Context c , ArrayList<Infoauction> array){
        inflater = LayoutInflater.from(c);
        infoList = array;
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        View v = convertview;
        BitmapDrawable bitmap;
        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_row, null);
            viewHolder.name = (TextView)v.findViewById(R.id.tv_name);
            viewHolder.place = (TextView)v.findViewById(R.id.tv_place);
            viewHolder.imguser = (ImageView)v.findViewById(R.id.img_user);
            viewHolder.time = (TextView)v.findViewById(R.id.tv_time);
            v.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)v.getTag();
        }
        Resources res = v.getResources();

        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.user);
        viewHolder.imguser.setImageDrawable(bitmap);
        viewHolder.name.setText(infoList.get(position).name);
        viewHolder.place.setText(infoList.get(position).place);
        viewHolder.time.setText(infoList.get(position).time);

        return v;
    }

    public void setArrayList(ArrayList<Infoauction> arrays){
        this.infoList = arrays;
    }

    public ArrayList<Infoauction> getArrayList(){
        return infoList;
    }


    /*
     * ViewHolder
     */
    class ViewHolder{
        TextView name;
        TextView place;
        TextView time;
        ImageView imguser;
    }

}
