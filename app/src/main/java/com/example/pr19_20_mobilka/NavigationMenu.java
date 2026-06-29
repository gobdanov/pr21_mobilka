package com.example.pr19_20_mobilka;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

import domains.adapters.MenuAdapter;
import domains.callbacks.OnTabClickListener;
import domains.models.MenuItem;

public class NavigationMenu extends Fragment {

    public RecyclerView RecyclerView;
    public MenuAdapter MenuAdapter;
    public Context context;

    public OnTabClickListener listener;

    public NavigationMenu() {
        // Required empty public constructor
    }

    public NavigationMenu(Context context, OnTabClickListener listener) {
        this.context = context;
        this.listener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_navigation_menu,container, false);

        List<MenuItem> Items = new ArrayList<>();
        Items.add(new MenuItem("главная", R.drawable.ic_home));
        Items.add(new MenuItem("каталог", R.drawable.ic_catalog));
        Items.add(new MenuItem("продукты", R.drawable.ic_order));
        Items.add(new MenuItem("профиль", R.drawable.ic_user));


        RecyclerView = view.findViewById(R.id.recycleView);


        RecyclerView.setLayoutManager(new GridLayoutManager(getContext(), Items.size()));

        MenuAdapter = new MenuAdapter(Items, listener);

        RecyclerView.setAdapter(MenuAdapter);

        return view;
    }
}