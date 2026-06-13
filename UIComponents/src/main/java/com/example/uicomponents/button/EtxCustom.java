package com.example.uicomponents.button;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.uicomponents.R;

public class EtxCustom extends ConstraintLayout {
    public EditText etx;
    public enum TypeEtx{
        DEFAULT, HOVER,ERROR
    }
    public EtxCustom(@NonNull Context context){
        super(context);
        init(null);
    }
    public EtxCustom(@NonNull Context context, @Nullable AttributeSet attrs){
        super(context,attrs);
        init(null);
    }
    public EtxCustom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(null);
    }

    public void init(Integer idLayout){
        if (idLayout == null) return;
        LayoutInflater.from(this.getContext()).inflate(idLayout,this,true);
        etx = findViewById(R.id.etx);
    }

    public void init(String value, TypeEtx type) {
        etx.setText(value);
        if(type == EtxCustom.TypeEtx.DEFAULT){
            etx.setBackgroundResource(R.drawable.etx_default);
            etx.setTextColor(Color.parseColor("#ffffff"));
        }
        else if(type == TypeEtx.ERROR){
            etx.setBackgroundResource(R.drawable.etx_error);
            etx.setTextColor(Color.parseColor("#ffffff"));
        }
        else if(type == TypeEtx.HOVER){
            etx.setBackgroundResource(R.drawable.etx_hover);
            etx.setTextColor(Color.parseColor("#ffffff"));
        }
    }
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        etx.setEnabled(enabled);
    }
}
