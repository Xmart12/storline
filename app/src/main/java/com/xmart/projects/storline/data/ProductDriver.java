package com.xmart.projects.storline.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.xmart.projects.storline.R;
import com.xmart.projects.storline.activities.fragments.CartFragment;
import com.xmart.projects.storline.activities.products.AddProductActivity;
import com.xmart.projects.storline.activities.products.ViewProductActivity;
import com.xmart.projects.storline.adapters.ProductAdapter;
import com.xmart.projects.storline.adapters.SalesAdapter;
import com.xmart.projects.storline.helpers.Utils;
import com.xmart.projects.storline.models.Product;
import com.xmart.projects.storline.models.Sales;
import com.xmart.projects.storline.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by smart on 26/08/2018.
 */

public class ProductDriver {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private DatabaseReference productReference, categoriesReference, cartReference, salesReference;
    private StorageReference storageReference;
    private Context context;
    private Query products;
    private List<Product> productList;
    private List<Sales> salesList;
    private ProductAdapter productAdapter;
    private SalesAdapter salesAdapter;
    private Product productItem = null;
    public static User usersys;
    public String location;

    private EditText edtDesc, edtLongDesc, edtPrice, edtStock;
    private Spinner spnCategory;
    private CheckBox chkShow;
    private ImageButton imageButton;

    private View[] viewProductsControls;
    private View[] cartProductsControls;


    //Constuctor
    public ProductDriver(Context con, String location) {

        productReference = database.getReference("products");
        categoriesReference = database.getReference("categories");
        cartReference = database.getReference("carts");
        salesReference = database.getReference("sales");
        storageReference = storage.getReference("products-pictures");
        this.context = con;
        this.productList = new ArrayList<>();
        this.salesList = new ArrayList<>();
        this.location = location;
        this.productAdapter = new ProductAdapter(this.productList, context, this);
        this.salesAdapter = new SalesAdapter(salesList, context, this);
    }


    public void setControls(EditText edtDesc, EditText edtLongDesc, EditText edtPrice, EditText edtStock,
                            Spinner spnCat, CheckBox chkShow, ImageButton img) {
        this.edtDesc = edtDesc;
        this.edtLongDesc = edtLongDesc;
        this.edtPrice = edtPrice;
        this.edtStock = edtStock;
        this.spnCategory = spnCat;
        this.chkShow = chkShow;
        this.imageButton = img;
    }


    //Return Prodcut Adapter with data of Node Prodcuts
    public ProductAdapter getProducts() {
        products = productReference;
        products.addValueEventListener(productsListener);

        return productAdapter;
    }

    //Return Prodcut Adapter with data of Node Prodcuts order by value
    public ProductAdapter getProducts(String orden) {
        products = productReference.orderByChild(orden);
        products.addValueEventListener(productsListener);

        return productAdapter;
    }

    //Return Product adapter with data of Nodo Categories
    public ProductAdapter getProductsbyCategory(String category) {
        products = categoriesReference.child(category);
        products.addValueEventListener(productsListener);

        return productAdapter;
    }

    //Return Prodcut adapter with data in Nodo Carts
    public ProductAdapter getProductsbyCart(View[] views) {

        this.cartProductsControls = views;

        products = cartReference.child(user.getUid());
        products.addValueEventListener(productsListener);

        return productAdapter;
    }

    //Return a especific product node
    public Product getOneProduct(String key) {

        DatabaseReference prod = productReference.child(key);
        prod.addValueEventListener(oneProductListener);

        return this.productItem;
    }

    //Return a especific prodcuct node for View
    public Product getOneViewProduct(String key, View[] views) {

        this.viewProductsControls = views;

        productReference.child(key).addValueEventListener(oneViewProductListener);

        return this.productItem;
    }

    //Return a Sales Adapter with data in Node Sales
    public SalesAdapter getUserSales() {

        try {

            salesReference.child(user.getUid()).addValueEventListener(salesListener);
        }
        catch (Exception e) {

            Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }



        return salesAdapter;
    }

    //Push a Product Node
    public void addProduct(Product product, Uri imageUri) {
        try {

            //Get ProductNode
            String key = productReference.push().getKey();
            productReference.child(key).setValue(product);
            productItem = product;

            uploadPicture(key, imageUri);

            if (productItem.isShow()) {
                //Get Node All Categories
                categoriesReference.child("All/" + key).setValue(productItem);
                categoriesReference.child(productItem.getCategory() + "/" + key).setValue(productItem);
            }

        } catch (Exception e) {

        }
    }

    //Push a Product Cart Node
    public void addProductCart(Product prod) {
        try {

            cartReference.child(user.getUid() + "/" + prod.getKey()).setValue(prod);

        } catch (Exception e) {

        }
    }

    //Edit a Product Node
    public void editProduct(String key, Product product, Uri imageUri) {
        try {

            //Update product node
            productReference.child(key).setValue(product);
            productItem = product;

            //Change picture
            uploadPicture(key, imageUri);

            //Remove last category
            categoriesReference.child("All/" + key).removeValue();
            categoriesReference.child(AddProductActivity.product.getCategory() + "/" + key).removeValue();

            if (product.isShow()) {
                //Update product node all categorie
                categoriesReference.child("All/" + key).setValue(product);

                //Add new Category
                categoriesReference.child(productItem.getCategory() + "/" + key).setValue(productItem);
            }

        } catch (Exception e) {

        }
    }

    //Remove a product node
    public boolean removeProduct(Product product) {

        try {

            OnFailureListener deletefailure = new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context.getApplicationContext(), "Ocurrió un error en la eliminación", Toast.LENGTH_SHORT).show();
                }
            };

            // deleting database product node
            productReference.child(product.getKey()).removeValue().addOnFailureListener(deletefailure);

            //deleting category all node
            categoriesReference.child("All/" + product.getKey()).removeValue().addOnFailureListener(deletefailure);

            //deleting category node
            categoriesReference.child(product.getCategory() + "/" + product.getKey()).removeValue().addOnFailureListener(deletefailure);

            // get storage file ref
            StorageReference prodcutImg = storageReference.child(product.getImgName());

            //deleting storage file
            prodcutImg.delete().addOnFailureListener(deletefailure);

            return true;

        } catch (Exception ex) {

            Toast.makeText(context.getApplicationContext(), "Ocurrió un error en la eliminación", Toast.LENGTH_SHORT).show();;

            return false;
        }

    }

    //Remove a product cart node
    public boolean removeCartProduct(Product product) {

        try {

            cartReference.child(user.getUid() + "/" + product.getKey()).removeValue();

            Toast.makeText(context.getApplicationContext(), "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();

            return true;

        } catch (Exception e) {

            Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            return false;

        }

    }

    //Confirm Shop Cart
    public boolean confirmShopCart(List<Product> prods, int items, double total) {

        try {

            UserDriver userDriver = new UserDriver(context, location);
            userDriver.getOneUser(user.getUid());

            Sales sale = new Sales(usersys, prods, new Date(), items, total);

            String key = salesReference.child(user.getUid()).push().getKey();

            salesReference.child(user.getUid() + "/" + key).setValue(sale);

            cartReference.child(user.getUid()).removeValue();

            Toast.makeText(context.getApplicationContext(), "Compra Finalizada", Toast.LENGTH_SHORT).show();

            return true;

        }
        catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            return false;
        }

    }


    //Upload Picture
    public void uploadPicture(final String key, Uri imageUri) {

        if (imageUri != null) {

            try {

                final String filename = key + "." + Utils.getFileExtension(imageUri, context);
                StorageReference fileref =  storageReference.child(filename);
                fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        productItem.setImgUrl(taskSnapshot.getDownloadUrl().toString());
                        productItem.setImgName(filename);

                        productReference.child(key).setValue(productItem);

                        if (productItem.isShow()) {
                            categoriesReference.child("All/" + key).setValue(productItem);
                            categoriesReference.child(productItem.getCategory() + "/" + key).setValue(productItem);
                        }

                        Toast.makeText(context.getApplicationContext(), "Información guardada correctamente", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            catch (Exception e) {

            }

        }

    }


    //Listeners


    //Listener for DataSnatshots Products
    private ValueEventListener productsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            productList.removeAll(productList);
            int items = 0;
            double total = 0;

            for (DataSnapshot data : dataSnapshot.getChildren()) {

                Product prod = data.getValue(Product.class);
                prod.setKey(data.getKey());

                items = items + 1;
                total = total + prod.getPrice();

                productList.add(prod);
            }

            if (location.equals("Cart")) {

                TextView tvItems = (TextView) cartProductsControls[0];
                TextView tvTotal = (TextView) cartProductsControls[1];

                tvItems.setText(String.valueOf(items));
                tvTotal.setText(Utils.currencyFormat(total));

                CartFragment.products = productList;
                CartFragment.Total = total;

            }

            productAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    //Listener for DataSnatShots Sales
    private ValueEventListener salesListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            salesList.removeAll(salesList);

            for (DataSnapshot data : dataSnapshot.getChildren()) {

                Sales sa = data.getValue(Sales.class);
                sa.setKey(data.getKey());

                salesList.add(sa);

            }

            salesAdapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    //Listener for DataSnatshot Product
    private ValueEventListener oneProductListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spnCategory.getAdapter();

            productItem = dataSnapshot.getValue(Product.class);
            edtDesc.setText(productItem.getDescription());
            edtLongDesc.setText(productItem.getLongDescription());
            edtPrice.setText(Utils.decimalFormat(productItem.getPrice()));
            edtStock.setText(String.valueOf(productItem.getStock()));
            spnCategory.setSelection(adapter.getPosition(productItem.getCategory()));
            chkShow.setChecked(productItem.isShow());
            Picasso.get().load(productItem.getImgUrl()).fit().centerCrop().into(imageButton);

            AddProductActivity.product =  productItem;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    //Listener for DataSnatshots View Product
    private ValueEventListener oneViewProductListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            CollapsingToolbarLayout collapser = (CollapsingToolbarLayout) viewProductsControls[0];
            ImageView imgProduct = (ImageView) viewProductsControls[1];
            TextView tvLongDescrip = (TextView) viewProductsControls[2];
            TextView tvPrice = (TextView) viewProductsControls[3];
            TextView tvCategory = (TextView) viewProductsControls[4];

            productItem = dataSnapshot.getValue(Product.class);

            collapser.setTitle(productItem.getDescription());
            tvLongDescrip.setText(productItem.getLongDescription());
            tvPrice.setText(Utils.currencyFormat(productItem.getPrice()));
            tvCategory.setText(productItem.getCategory());

            Picasso.get().load(productItem.getImgUrl()).fit().centerCrop().into(imgProduct);

            ViewProductActivity.product = productItem;

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



}
