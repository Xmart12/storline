package com.xmart.projects.storline.activities.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xmart.projects.storline.R;
import com.xmart.projects.storline.data.ProductDriver;
import com.xmart.projects.storline.data.UserDriver;
import com.xmart.projects.storline.models.Product;
import com.xmart.projects.storline.models.User;

import java.security.PublicKey;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private RecyclerView rcvLista;
    private LinearLayoutManager lManager;
    private ProductDriver productDriver;


    private TextView tvItems, tvTotal;

    public static List<Product> products;
    public static double Total = 0;



    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Carrito");

        productDriver = new ProductDriver(this.getActivity(), "Cart");

        rcvLista = (RecyclerView) view.findViewById(R.id.lstCart);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        rcvLista.setLayoutManager(lManager);

        tvItems = (TextView) view.findViewById(R.id.tvItems);
        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        Button btnConfirmShop = (Button) view.findViewById(R.id.btnConfirmShop);

        btnConfirmShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmShop(v);
            }
        });

        View[] views = { tvItems, tvTotal };

        // Crear un nuevo adaptador
        rcvLista.setAdapter(productDriver.getProductsbyCart(views));
    }


    public void confirmShop(View view) {

        String compra = tvTotal.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Desea confirmar la compra por " + compra + " ?").setTitle("Confirmación");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                productDriver.confirmShopCart(products, products.size(), Total);

            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();

    }
}
