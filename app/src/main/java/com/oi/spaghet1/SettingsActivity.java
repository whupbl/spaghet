package com.oi.spaghet1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Spinner s = (Spinner) findViewById(R.id.spinner);

        String[] arraySpinner = Categories.getMap().keySet().toArray(new String[Categories.getMap().keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);


        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner s = (Spinner) findViewById(R.id.spinner);
                String cat = s.getSelectedItem().toString();
                String[] arraySpinner = Categories.getSubcat(cat).toArray(new String[Categories.getSubcat(cat).size()]);
                s = (Spinner) findViewById(R.id.spinner2);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingsActivity.this,
                        android.R.layout.simple_spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        final Button button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                Spinner s = (Spinner) findViewById(R.id.spinner);
                intent.putExtra("cat", s.getSelectedItem().toString());
                s = (Spinner) findViewById(R.id.spinner2);
                intent.putExtra("subcat", s.getSelectedItem().toString());
                EditText et = (EditText) findViewById(R.id.start);
                intent.putExtra("start", et.getText().toString());
                et = (EditText) findViewById(R.id.end);
                intent.putExtra("end", et.getText().toString());
                et = (EditText) findViewById(R.id.search_et);
                intent.putExtra("search", et.getText().toString());
                et = (EditText) findViewById(R.id.cooking_time);
                intent.putExtra("cookingTime", et.getText().toString());
                intent.putExtra("requestCode", 1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
