package com.xmart.projects.storline.activities.products;

import android.content.DialogInterface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmart.projects.storline.R;
import com.xmart.projects.storline.data.ProductDriver;
import com.xmart.projects.storline.models.Product;

public class ViewProductActivity extends AppCompatActivity {

    private String Action;
    private String ProduckKey;
    private ProductDriver productDriver;
    public static Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        productDriver = new ProductDriver(this, "ViewProduct");

        CollapsingToolbarLayout collapser = (CollapsingToolbarLayout) findViewById(R.id.collapser);
        ImageView imgProduct = (ImageView) findViewById(R.id.imgProductPhoto);
        TextView tvLongDescrip = (TextView) findViewById(R.id.tvLongDescription);
        TextView tvPrice = (TextView) findViewById(R.id.tvPrice);
        TextView tvCategory = (TextView) findViewById(R.id.tvCategory);

        //Get Intent Data
        Bundle intenData = getIntent().getExtras();
        this.Action = intenData.getString("Action");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_shop);
        fab.setOnClickListener(addCart());

        //Edit Product
        if (this.Action.equals("v")) {
            this.ProduckKey = intenData.getString("ProductKey");
            View[] views = { collapser, imgProduct, tvLongDescrip, tvPrice, tvCategory };
            this.product = productDriver.getOneViewProduct(this.ProduckKey, views);
        }
        else if (this.Action.equals("cv")) {

            fab.setVisibility(View.INVISIBLE);

            this.ProduckKey = intenData.getString("ProductKey");
            View[] views = { collapser, imgProduct, tvLongDescrip, tvPrice, tvCategory };
            this.product = productDriver.getOneViewProduct(this.ProduckKey, views);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }


    private View.OnClickListener addCart() {

        View.OnClickListener cartClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewProductActivity.this);
                builder.setMessage("¿Desea Comprar este producto?").setTitle("Confirmación");

                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        product.setKey(ProduckKey);

                        productDriver.addProductCart(product);
                        finish();

                    }
                });

                builder.setNegativeButton("No", null);

                AlertDialog dialog = builder.create();

                dialog.show();

            }
        };

        return cartClickListener;

    }


}
