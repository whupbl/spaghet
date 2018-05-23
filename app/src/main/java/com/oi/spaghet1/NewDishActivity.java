package com.oi.spaghet1;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

//Создание нового блюда

public class NewDishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);

        Button button = (Button) findViewById(R.id.createDish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewDishActivity.this, SelectLocationActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Spinner s = (Spinner) findViewById(R.id.spinner7);

        String[] arraySpinner = Categories.getMap().keySet().toArray(new String[Categories.getMap().keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);


        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner s = (Spinner) findViewById(R.id.spinner7);
                String cat = s.getSelectedItem().toString();
                String[] arraySpinner = Categories.getSubcat(cat).toArray(new String[Categories.getSubcat(cat).size()]);
                s = (Spinner) findViewById(R.id.spinner8);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewDishActivity.this,
                        android.R.layout.simple_spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case 1:

                String latitude = data.getStringExtra("latitude");
                String longitude = data.getStringExtra("longitude");

                AlertDialog f = new AlertDialog.Builder(NewDishActivity.this).create();
                f.setMessage(latitude + " " + longitude);
                f.show();
                break;
        }
    }
}
