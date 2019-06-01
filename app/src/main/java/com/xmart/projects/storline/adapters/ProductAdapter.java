package com.xmart.projects.storline.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xmart.projects.storline.R;
import com.xmart.projects.storline.activities.products.AddProductActivity;
import com.xmart.projects.storline.activities.products.ViewProductActivity;
import com.xmart.projects.storline.data.ProductDriver;
import com.xmart.projects.storline.helpers.Utils;
import com.xmart.projects.storline.models.Product;


import java.util.List;

public class ProductAdapter extends Adapter<ProductAdapter.ViewHolder> {

    private List<Product> items;
    private Context context;
    private ProductDriver productDriver;
    private String location;

    public ProductAdapter(List<Product> items, Context context, ProductDriver productDriver) {
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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //Put Data in ViewHolder
        holder.description.setText(items.get(position).getDescription());
        holder.longDescription.setText(items.get(position).getLongDescription());
        holder.price.setText(Utils.currencyFormat(items.get(position).getPrice()));
        Picasso.get().load(items.get(position).getImgUrl()).fit().centerCrop().into(holder.image);

        if (this.location.equals("Products")) {

            // Edit Listener (Onclick)
            holder.linearLayout.setOnClickListener(getEditClickListener(position));

            // Delete Listener (OnLongClick)
            holder.linearLayout.setOnLongClickListener(getDeleteLongClickListener(position));

        }
        else if (this.location.equals("Categories")) {

            holder.linearLayout.setOnClickListener(getViewClickListener(position, "v"));

        }
        else if (this.location.equals("Cart")) {

            holder.linearLayout.setOnClickListener(getViewClickListener(position, "cv"));
            holder.linearLayout.setOnLongClickListener(getDeleteCartLongClickListener(position));

        }


    }

    //Listeners

    //View Listener
    private View.OnClickListener getViewClickListener(final int position, final String action) {

        View.OnClickListener viewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, ViewProductActivity.class);
                i.putExtra("Action", action);
                i.putExtra("ProductKey", items.get(position).getKey());
                context.startActivity(i);

            }
        };

        return  viewListener;

    }

    //Edit Listener
    private View.OnClickListener getEditClickListener(final int position) {

        View.OnClickListener editlistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, AddProductActivity.class);
                i.putExtra("Action", "e");
                i.putExtra("ProductKey", items.get(position).getKey());
                context.startActivity(i);
            }
        };

        return editlistener;

    }

    //Delete Listener
    private View.OnLongClickListener getDeleteLongClickListener(final int position) {

        View.OnLongClickListener deletelistener = new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Está seguro de eliminar registro?").setTitle("Confirmación");

                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        //Deleting product
                        productDriver.removeProduct(items.get(position));
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();

                return true;
            }
        };

        return deletelistener;
    }

    //Delete Product Cart Listener
    private View.OnLongClickListener getDeleteCartLongClickListener(final int position) {

        View.OnLongClickListener deleteListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("¿Está seguro de eliminar registro del carrito?").setTitle("Confirmación");

                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        productDriver.removeCartProduct(items.get(position));

                    }
                });

                builder.setNegativeButton("No", null);

                AlertDialog dialog = builder.create();

                dialog.show();

                return true;

            }
        };

        return deleteListener;

    }


    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Campos respectivos de un item
        public TextView description;
        public TextView longDescription;
        public TextView price;
        public ImageButton image;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            description = (TextView) itemView.findViewById(R.id.tvDescription);
            longDescription = (TextView) itemView.findViewById(R.id.tvLongDescription);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            image = (ImageButton) itemView.findViewById(R.id.imgFotoProd);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout);
        }

    }

}
