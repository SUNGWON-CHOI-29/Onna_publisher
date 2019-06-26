package com.example.watsonz.onna_publisher.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.watsonz.onna_publisher.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Created by watsonz on 2016-05-24.
 */
public class CuponCustomAdapter extends BaseExpandableListAdapter{

    private ArrayList<String> groupList = null;
    private ArrayList<HashMap<String, Integer>> childList = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;

    public CuponCustomAdapter(Context c, ArrayList groupList,
                                    ArrayList childList){
        super();
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
    }

    public ArrayList<HashMap<String, Integer>> getChildList(){
        return childList;
    }
    // 그룹 포지션을 반환한다.
    @Override
    public String getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    // 그룹 사이즈를 반환한다.
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    // 그룹 ID를 반환한다.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // 그룹뷰 각각의 ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_cupon_row, parent, false);
            //viewHolder.tv_groupName = (TextView) v.findViewById(R.id.tv_group);
            viewHolder.iv_image = (ImageView) v.findViewById(R.id.iv_image);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
        if(isExpanded){
            // viewHolder.iv_image.setBackgroundColor(Color.GREEN);
        }else{
            //viewHolder.iv_image.setBackgroundColor(Color.WHITE);
        }
        String index = getGroup(groupPosition);
        if(index == "메인")
            viewHolder.iv_image.setImageResource(R.drawable.main);
        if(index == "음료")
            viewHolder.iv_image.setImageResource(R.drawable.drink);
        if(index == "사이드")
            viewHolder.iv_image.setImageResource(R.drawable.side);
        //viewHolder.tv_groupName.setText(getGroup(groupPosition));

        return v;
    }

    // 차일드뷰를 반환한다.
    @Override
    public String getChild(int groupPosition, int childPosition) {
        Set set =  childList.get(groupPosition).keySet();
        Object []keys = set.toArray();
        Arrays.sort(keys, nameSorter);
        return (String)keys[childPosition];
    }

    public int getChildValue(int groupPosition, int childPosition) {
        String key = getChild(groupPosition, childPosition);
        return childList.get(groupPosition).get(key);
    }

    public int setChildVaue(int groupPosition, int childPosition, int Value){
        String key = getChild(groupPosition, childPosition);
        childList.get(groupPosition).put(key,Value);
        return 0;
    }
    // 차일드뷰 사이즈를 반환한다.
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    // 차일드뷰 ID를 반환한다.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // 차일드뷰 각각의 ROW
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_cupon_child_row, null);
            viewHolder.tv_childName = (TextView) v.findViewById(R.id.tv_child);
            viewHolder.tv_childNum = (TextView) v.findViewById(R.id.num);
            viewHolder.bt_minus = (Button) v.findViewById(R.id.minus);
            viewHolder.bt_plus = (Button) v.findViewById(R.id.plus);
            v.setTag(viewHolder);
        }else{
        viewHolder = (ViewHolder)v.getTag();
    }
        viewHolder.bt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout)v.getParent();
                TextView tv_num = (TextView)rl.findViewById(R.id.num);
                int num = getChildValue(groupPosition,childPosition);
                if(num > 0)num--;
                setChildVaue(groupPosition, childPosition, num);
                tv_num.setText(String.valueOf(getChildValue(groupPosition, childPosition)));
            }
        });
        viewHolder.bt_plus.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  RelativeLayout rl = (RelativeLayout)v.getParent();
                  TextView tv_num = (TextView)rl.findViewById(R.id.num);
                  int num = getChildValue(groupPosition,childPosition);
                  num++;
                  setChildVaue(groupPosition, childPosition, num);
                  tv_num.setText(String.valueOf(getChildValue(groupPosition, childPosition)));
              }
       });
        viewHolder.tv_childName.setText(getChild(groupPosition, childPosition));
        viewHolder.tv_childNum.setText(String.valueOf(getChildValue(groupPosition, childPosition)));
        return v;
    }

    @Override
    public boolean hasStableIds() {	return true; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return false; }

    class ViewHolder {
        public ImageView iv_image;
        public TextView tv_childName;
        public TextView tv_childNum;
        public Button bt_plus;
        public Button bt_minus;
    }
    public static Comparator nameSorter = new Comparator() {
        @Override
        public int compare(Object lhs, Object rhs) {
            String str_lhs = (String)lhs;
            String str_rhs = (String)rhs;
            return (-1) * str_rhs.compareTo(str_lhs);
        }
    };
}