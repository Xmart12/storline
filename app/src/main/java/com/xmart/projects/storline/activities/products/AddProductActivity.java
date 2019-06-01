package com.xmart.projects.storline.activities.products;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xmart.projects.storline.R;
import com.xmart.projects.storline.data.ProductDriver;
import com.xmart.projects.storline.models.Product;

public class AddProductActivity extends AppCompatActivity {

    private ProductDriver productDriver = new ProductDriver(this, "AddProduct");

    private EditText edtDescription, edtLongDescription, edtPrice, edtStock;
    Spinner spnCategory;
    CheckBox chkShow;
    private ImageButton btnProductPhoto;

    private String ProduckKey = null, Action = null;
    private final int SELECT_PICTURE = 1;
    public static Product product;

    Uri selectedImageUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        setTitle("Detalle Producto");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtDescription = (EditText) findViewById(R.id.edtDescription);
        edtLongDescription = (EditText) findViewById(R.id.edtLongDesctiption);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
        edtStock = (EditText) findViewById(R.id.edtStock);
        spnCategory = (Spinner) findViewById(R.id.spnCategorie);
        chkShow = (CheckBox) findViewById(R.id.chkShow);
        btnProductPhoto = (ImageButton) findViewById(R.id.imgProductPhoto);

        productDriver.setControls(edtDescription, edtLongDescription, edtPrice, edtStock, spnCategory, chkShow, btnProductPhoto);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.productsCategories, android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);

        //Get Intent Data
        Bundle intenData = getIntent().getExtras();
        this.Action = intenData.getString("Action");

        //Edit Product
        if (this.Action.equals("e")) {
            this.ProduckKey = intenData.getString("ProductKey");
            this.product = productDriver.getOneProduct(this.ProduckKey);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void saveProduct(View view) {

        String desc = edtDescription.getText().toString();
        String longdesc = edtLongDescription.getText().toString();
        String cat = spnCategory.getSelectedItem().toString();
        boolean show = chkShow.isChecked();
        float precio = Float.parseFloat(edtPrice.getText().toString());
        int stock = Integer.parseInt(edtStock.getText().toString());

        Product prod = new Product(desc, longdesc, cat, precio, null, null, stock, show);

        if (this.Action.equals("a")) {
            productDriver.addProduct(prod, selectedImageUri);
        } else {
            prod.setImgUrl(product.getImgUrl());
            prod.setImgName(product.getImgName());
            productDriver.editProduct(this.ProduckKey, prod, selectedImageUri);
        }

        finish();
    }


    public void cancel(View view) {
        finish();
    }


    public void selectPhoto(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione una image"), SELECT_PICTURE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == this.RESULT_OK) {
                    selectedImageUri = imageReturnedIntent.getData();
                    if (requestCode == SELECT_PICTURE) {
                        Picasso.get().load(selectedImageUri).fit().centerCrop().into(btnProductPhoto);
                    }
                }
                break;
        }
    }

}
