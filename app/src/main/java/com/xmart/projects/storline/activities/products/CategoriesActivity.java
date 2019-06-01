package com.xmart.projects.storline.activities.products;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xmart.projects.storline.R;

public class CategoriesActivity extends AppCompatActivity {

    private ListView lstCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setTitle("Categorías");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lstCategories = (ListView) findViewById(R.id.lstCategories);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.productsCategories, android.R.layout.simple_list_item_1);
        lstCategories.setAdapter(adapter);

        lstCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("category", lstCategories.getItemAtPosition(position).toString());
                setResult(RESULT_OK, returnIntent);

                finish();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }
}
