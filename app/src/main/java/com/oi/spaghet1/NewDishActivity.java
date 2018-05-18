package com.oi.spaghet1;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Создание нового блюда

public class NewDishActivity extends AppCompatActivity {

    private Retrofit retrofit;
    public static SpaghetAPI spaghetAPI;
    private int UserID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://ae9845f5.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        spaghetAPI = retrofit.create(SpaghetAPI.class);

        UserID = getIntent().getIntExtra("UserID", 1);

        Spinner s = (Spinner) findViewById(R.id.spinner7);

        String[] arraySpinner = categories.getMap().keySet().toArray(new String[categories.getMap().keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.createDish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewDishActivity.this, SelectLocationActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner s = (Spinner) findViewById(R.id.spinner7);
                String cat = s.getSelectedItem().toString();
                String[] arraySpinner = categories.getSubcat(cat).toArray(new String[categories.getSubcat(cat).size()]);
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

                Spinner spin1 = (Spinner) findViewById(R.id.spinner8);

                String subcat = ((TextView)spin1.getSelectedView()).getText().toString();
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String name = ((EditText) findViewById(R.id.nameTextEdit)).getText().toString();
                String desc = ((EditText) findViewById(R.id.description)).getText().toString();
                Double cost = Double.parseDouble(((EditText) findViewById(R.id.cost)).getText().toString());
                Double cooking_time = Double.parseDouble(((EditText) findViewById(R.id.cooking_time)).getText().toString());

                final Call<RequestStatus> addDishCall = spaghetAPI.addDish(String.valueOf(UserID), name, subcat, String.valueOf(cost),
                        desc, String.valueOf(cooking_time));

                addDishCall.enqueue(new Callback<RequestStatus>() {
                    @Override
                    public void onResponse(Call<RequestStatus> call, Response<RequestStatus> response) {
                        if (response.isSuccessful()) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(NewDishActivity.this);
                            builder.setTitle("Успех!")
                                    .setMessage("Ваше блюдо было успешно добавлено!")
                                    .setCancelable(true)
                                    .setNeutralButton("Хорошо",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();

                            NewDishActivity.this.finish();

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<RequestStatus> call, Throwable t) {
                        Toast.makeText(NewDishActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
                    }

                });

                break;
        }
    }
}
