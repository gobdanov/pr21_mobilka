package com.example.uicomponents.button;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uicomponents.R;

public class BtnBig extends  BtnCustom{
    public BtnBig(@NonNull Context context){
        super(context);
    }
    public BtnBig(@NonNull Context context, @Nullable AttributeSet attrs){
        super(context,attrs);
    }
    public BtnBig(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }
    @Override
    public void init(Integer idLayout){
        super.init(R.layout.button);
    }
}
