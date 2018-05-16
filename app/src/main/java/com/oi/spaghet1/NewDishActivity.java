package com.oi.spaghet1;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
