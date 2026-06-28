package com.example.pr19_20_mobilka;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.network.datas.products.ProductGetUser;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.models.Product;
import com.example.uicomponents.button.BtnBig;
import com.example.uicomponents.button.BtnCustom;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import domains.managers.PermissionManager;

public class MainActivity extends AppCompatActivity {

    //ПОМЕНЯТЬ!!!!!!
    public static String TOKEN = "642a1edd-ea95-49c6-bbec-c2252b7f0c55";
    //ПОМЕНЯТЬ!!!!!!

    View btnOpenAddProduct;
    LinearLayout AllContent;
    List<Product> Products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionManager.GetPermission(this, this);

        btnOpenAddProduct = findViewById(R.id.btnOpenAddProduct);
        AllContent = findViewById(R.id.AllContent);

        btnOpenAddProduct.setOnClickListener(v->{
            Intent intent = new Intent(this, ProductActivity.class);
            startActivity(intent);
        });

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
            }

            @Override
            public void onError(String error) {
                Log.e("Product Get User", error);
            }
        });
        RequestProductGetUser.execute();
    }

    public void CreateElement(){
        for (Product product : Products){
            View itemProduct = LayoutInflater.from(this).inflate(R.layout.item,AllContent,false);
            BtnBig btnBig = itemProduct.findViewById(R.id.btnOpenProduct);
            TextView tbName = itemProduct.findViewById(R.id.tbName);
            TextView tbPrice = itemProduct.findViewById(R.id.tvPrice);

            btnBig.init("открыть", BtnCustom.TypeButton.PRIMARY);

            tbName.setText(product.name);

            tbPrice.setText(product.price+"р");
            AllContent.addView(itemProduct);
        }
    }
}