package com.xmart.projects.storline.activities.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmart.projects.storline.R;
import com.xmart.projects.storline.data.ProductDriver;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalesFragment extends Fragment {

    private RecyclerView rcvLista;
    private LinearLayoutManager lManager;
    private ProductDriver productDriver;


    public SalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Mis Compras");

         productDriver = new ProductDriver(this.getActivity(), "Sales");

        rcvLista = (RecyclerView) view.findViewById(R.id.rcvLista);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        rcvLista.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        rcvLista.setAdapter(productDriver.getUserSales());
    }
}
