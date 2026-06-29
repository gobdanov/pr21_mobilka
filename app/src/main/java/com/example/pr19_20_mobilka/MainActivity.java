package com.example.pr19_20_mobilka;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class MainActivity extends AppCompatActivity {

    //ПОМЕНЯТЬ!!!!!!!
    public static String TOKEN = "642a1edd-ea95-49c6-bbec-c2252b7f0c55";
    //ПОМЕНЯТЬ!!!!!!!

    public static MainActivity init;
    public Fragment openFragment;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init = this;
        context = this;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        NavigationMenu menu = new NavigationMenu(this, MenuItemSelect);

        ft.add(R.id.menu_navigation, menu);
        ft.commit();
    }

    OnTabClickListener MenuItemSelect = new OnTabClickListener() {
        @Override
        public void OnTabClick(Integer position) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(openFragment != null){
                ft.remove(openFragment);
            }
            if(position == -1){
                openFragment = new ProductsFragment(context, null);
                ft.add(R.id.content, openFragment);
            } else if (position == 2){
                openFragment = new ProductsFragment(context, MenuItemSelect);
                ft.add(R.id.content, openFragment);
            }

            ft.commit();
        }
    };

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        ProductsFragment productFragment = (ProductsFragment) openFragment;

        productFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        menu.add(1,101, Menu.NONE, "изменить");
        menu.add(2,102, Menu.NONE, "удалить");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getGroupId() == 1){
            Toast.makeText(this,"изменение элемента", Toast.LENGTH_SHORT).show();
        }
        else if (item.getGroupId() == 2){
            Toast.makeText(this,"удаление элемента", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

}