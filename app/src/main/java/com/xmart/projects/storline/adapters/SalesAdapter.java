package com.xmart.projects.storline.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xmart.projects.storline.R;
import com.xmart.projects.storline.data.ProductDriver;
import com.xmart.projects.storline.helpers.Utils;
import com.xmart.projects.storline.models.Sales;

import java.util.List;

/**
 * Created by smart on 07/09/2018.
 */

public class SalesAdapter extends Adapter<SalesAdapter.ViewHolder> {

    private List<Sales> items;
    private Context context;
    private ProductDriver productDriver;
    private String location;


    public SalesAdapter(List<Sales> items, Context context, ProductDriver productDriver) {
        this.items = items;
        this.context = context;
        this.productDriver = productDriver;
        this.location = productDriver.location;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_cart, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Put Data in ViewHolder
        holder.datetx.setText(Utils.dateTimeFormat(items.get(position).getDate()));
        holder.itemtx.setText(String.valueOf(items.get(position).getItems()) + " Productos");
        holder.price.setText(Utils.currencyFormat(items.get(position).getTotal()));

    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Campos respectivos de un item
        public TextView datetx;
        public TextView itemtx;
        public TextView price;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            datetx = (TextView) itemView.findViewById(R.id.tvDate);
            itemtx = (TextView) itemView.findViewById(R.id.tvItems);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout);
        }

    }

}
