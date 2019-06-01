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
import com.xmart.projects.storline.activities.products.AddProductActivity;
import com.xmart.projects.storline.data.ProductDriver;

public class ProductsFragment extends Fragment {

    private RecyclerView rcvLista;
    private LinearLayoutManager lManager;

    private ProductDriver productDriver;

    public ProductsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Productos");

        productDriver = new ProductDriver(this.getActivity(), "Products");

        FloatingActionButton fab_agregar = (FloatingActionButton) view.findViewById(R.id.fab_agregar);
        rcvLista = (RecyclerView) view.findViewById(R.id.rcvLista);

        fab_agregar.setOnClickListener(newProductListener);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        rcvLista.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        rcvLista.setAdapter(productDriver.getProducts());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);


    }


    private View.OnClickListener newProductListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Intent i = new Intent(getActivity(), AddProductActivity.class);
            i.putExtra("Action", "a");
            startActivity(i);

        }

    };


}
