package com.example.watsonz.onna_publisher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by watsonz on 2015-07-29.
 */
public class shake_main extends Fragment implements View.OnClickListener{              //쉐이크 프레그먼트 implement


    public shake_main(){
        //퍼블릭 생성
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    static shake_main newInstance(int SectionNumber){
        shake_main fragment = new shake_main();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_shake, container, false);
        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }
}
