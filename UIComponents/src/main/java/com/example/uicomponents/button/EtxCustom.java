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

    public enum TypeEtx {
        DEFAULT, HOVER, ERROR
    }

    // Конструкторы
    public EtxCustom(@NonNull Context context) {
        super(context);
        initLayout();  // ✅ Загружаем layout
    }

    public EtxCustom(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout();  // ✅ Загружаем layout
    }

    public EtxCustom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();  // ✅ Загружаем layout
    }

    // Метод для загрузки layout и инициализации EditText
    private void initLayout() {
        // Инфлейтим layout, который содержит EditText с id = "etx"
        LayoutInflater.from(getContext()).inflate(R.layout.etx, this, true);
        etx = findViewById(R.id.etx);

        // Важно: проверяем, что etx найден!
        if (etx == null) {
            throw new IllegalStateException("EditText with id 'etx' not found in layout");
        }
    }

    // Метод для загрузки кастомного layout (если нужно)
    public void init(Integer idLayout) {
        if (idLayout == null) return;
        LayoutInflater.from(getContext()).inflate(idLayout, this, true);
        etx = findViewById(R.id.etx);
    }

    // Метод для установки текста и стиля
    public void init(String value, TypeEtx type) {
        if (etx == null) {
            throw new IllegalStateException("etx is null! Make sure initLayout() was called.");
        }

        etx.setHint(value);

        switch (type) {
            case DEFAULT:
                etx.setBackgroundResource(R.drawable.etx_default);
                etx.setTextColor(Color.parseColor("#000000"));
                break;
            case ERROR:
                etx.setBackgroundResource(R.drawable.etx_error);
                etx.setTextColor(Color.parseColor("#000000"));
                break;
            case HOVER:
                etx.setBackgroundResource(R.drawable.etx_hover);
                etx.setTextColor(Color.parseColor("#000000"));
                break;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (etx != null) {
            etx.setEnabled(enabled);
        }
    }
}
