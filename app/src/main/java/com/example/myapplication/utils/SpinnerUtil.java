package com.example.myapplication.utils;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.constant.LibConfig;

import java.util.List;

public class SpinnerUtil {

    public void changeSpinner(Context context, Spinner spinner, List<String> spinnerItems, CallBack callBack) {
        spinner.setDropDownWidth(600); //下拉宽度
        //spinner.setDropDownHorizontalOffset(100); //下拉的横向偏移
        //mSpinner.setDropDownVerticalOffset(100); //下拉的纵向偏移

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                R.layout.item_select, spinnerItems);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.item_drop);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                callBack.onItemSelected((TextView) view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public interface CallBack {
        void onItemSelected(TextView view);
    }
}
