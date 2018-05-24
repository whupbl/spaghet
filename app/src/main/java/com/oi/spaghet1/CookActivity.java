package com.oi.spaghet1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Повар

public class CookActivity extends AppCompatActivity {

    private Retrofit retrofit;
    public static SpaghetAPI spaghetAPI;
    private int UserID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        applyFont((TextView)findViewById(R.id.cookLogoText), this);

        retrofit = new Retrofit.Builder()
                .baseUrl(SpaghetAPI.serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        spaghetAPI = retrofit.create(SpaghetAPI.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Call<CategoriesList> cats = spaghetAPI.getCategories();
                final CategoriesList cl = new CategoriesList();

                cats.enqueue(new Callback<CategoriesList>() {
                    @Override
                    public void onResponse(Call<CategoriesList> call, Response<CategoriesList> response) {
                        if (response.isSuccessful()) {
                            Log.i("RESULT", "response " + response.body().toString());
                            cl.setChildren(response.body().getChildren());
                            Log.i("PM", String.valueOf(cl.getChildren().size()));
                            Log.i("PM", String.valueOf(cl.getChildren().get(0).getCatName()));
                            for (int i = 0; i < cl.getChildren().size(); i++) {
                                Categories.addSubcatToCat(cl.getChildren().get(i).getCatName(), cl.getChildren().get(i).getSubName());
                            }
                            Intent intent = new Intent(CookActivity.this, NewDishActivity.class);
                            startActivityForResult(intent, 1);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CookActivity.this);
                            builder.setTitle("Ошибка!")
                                    .setMessage("Что-то пошло не так\n" + "Error: " + response.code())
                                    .setIcon(R.drawable.ic_menu_manage)
                                    .setCancelable(false)
                                    .setNegativeButton("Очень жаль",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                            Log.e("RESULT", "response code " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoriesList> call, Throwable t) {

                    }

                });
            }
        });

        ((Button) findViewById(R.id.cookHistoryButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CookActivity.this, CookHistoryActivity.class));
            }
        });


    }

    public static void applyFont(TextView tv, Activity context) {
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Playlist Script.otf"));
    }

}
