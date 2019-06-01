package com.xmart.projects.storline.activities.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmart.projects.storline.R;
import com.xmart.projects.storline.activities.products.CategoriesActivity;
import com.xmart.projects.storline.data.ProductDriver;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryProductFragment extends Fragment {

    private RecyclerView rcvLista;
    private LinearLayoutManager lManager;
    private ProductDriver productDriver;
    private String Category;


    public CategoryProductFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.Category = getArguments().getString("category");
        } else {
            this.Category = "All";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_product, container, false);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.Category.equals("All")) {
            getActivity().setTitle("Productos");
        } else {
            getActivity().setTitle("Productos de " + this.Category);
        }


        productDriver = new ProductDriver(this.getActivity(), "Categories");

        rcvLista = (RecyclerView) view.findViewById(R.id.rcvLista);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        rcvLista.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        rcvLista.setAdapter(productDriver.getProductsbyCategory(this.Category));
    }



}
