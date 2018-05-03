package com.oi.spaghet1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutionException;

public class settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Spinner s = (Spinner) findViewById(R.id.spinner);

        String[] arraySpinner = categories.getMap().keySet().toArray(new String[categories.getMap().keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);


        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner s = (Spinner) findViewById(R.id.spinner);
                String cat = s.getSelectedItem().toString();
                String[] arraySpinner = categories.getSubcat(cat).toArray(new String[categories.getSubcat(cat).size()]);
                s = (Spinner) findViewById(R.id.spinner2);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(settings.this,
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
                intent.putExtra("start", et.getText());
                et = (EditText) findViewById(R.id.end);
                intent.putExtra("end", et.getText());
                et = (EditText) findViewById(R.id.search_et);
                intent.putExtra("search", et.getText());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
