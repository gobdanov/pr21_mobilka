package com.example.pr19_20_mobilka;

import static com.example.pr19_20_mobilka.MainActivity.TOKEN;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.network.datas.products.ProductGetUser;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.models.Product;
import com.example.uicomponents.button.BtnBig;
import com.example.uicomponents.button.BtnCustom;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import domains.callbacks.OnTabClickListener;
import domains.managers.PermissionManager;

public class ProductsFragment extends Fragment {

    public ProductsFragment(Context context, OnTabClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    View btnOpenAddProduct;
    LinearLayout llContent;
    private List<Product> Products = new ArrayList<>();
    Context context;
    OnTabClickListener listener;

    public ProductsFragment() {
        // Пустой конструктор
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        PermissionManager.GetPermission(context, MainActivity.init);

        btnOpenAddProduct = view.findViewById(R.id.btnOpenAddProduct);
        llContent = view.findViewById(R.id.llContent);

        btnOpenAddProduct.setOnClickListener(v->{
            listener = new OnTabClickListener() {
                @Override
                public void OnTabClick(Integer position) {

                }
            };
            listener.OnTabClick(-1);
        });

        ProductGetUser();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        ProductGetUser();
    }

    public void ProductGetUser(){
        ProductGetUser RequestProductGetUser= new ProductGetUser(TOKEN, new MyResponseCallback() {
            @Override
            public void onCompile(String result) {
                Log.d("Product Get User", result);
                Products = new GsonBuilder().create().fromJson(
                        result,
                        new TypeToken<ArrayList<Product>>(){}.getType()
                );
                CreateElement();
                for (Product pr : Products){
                    Log.d("Products", pr.name);
                }
            }

            @Override
            public void onError(String error) {
                Log.e("Product Get User", error);
            }
        });
        RequestProductGetUser.execute();
    }

    public void CreateElement(){
        llContent.removeAllViews();
        for (Product product : Products){
            View itemProduct = LayoutInflater.from(context).inflate(R.layout.item,llContent,false);
            BtnBig btnOpen = itemProduct.findViewById(R.id.btnOpenProduct);
            TextView tvName = itemProduct.findViewById(R.id.tvName);
            TextView tvPrice = itemProduct.findViewById(R.id.tvPrice);

            btnOpen.init("открыть", BtnCustom.TypeButton.PRIMARY);
            btnOpen.Btn.setTextSize(16);

            btnOpen.Btn.setText(product.name);
            tvPrice.setText(product.price+"р");

            registerForContextMenu(itemProduct);

            llContent.addView(itemProduct);
        }
    }
}