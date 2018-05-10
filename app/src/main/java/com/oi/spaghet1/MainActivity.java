package com.oi.spaghet1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Главная активность, которая с картой
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Retrofit retrofit;
    public static SpaghetAPI spaghetAPI;
    private List<Dish> points = null;
    private int UserID = 1;

    // Метод, срабатывающий при создании активности вот этой вот главной
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        // Кнопочка розовая круглая
        fab.setOnClickListener(fabListener);


        // Менюшка слева
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //бИРЮЗОВАЯ ШАПКА СВЕРХУ
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Фрагмент с картойc7
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        retrofit = new Retrofit.Builder()
                .baseUrl("http://41c7c494.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        spaghetAPI = retrofit.create(SpaghetAPI.class);

        Log.i("ГЛАВНАЯ АКТИВНОСТЬ", "ЗАГРУЗИЛАСЬ");
    }

    private View.OnClickListener fabListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.i("НАЖАЛИ КНОПКУ", "РОЗОВУЮ");

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
                            categories.addSubcatToCat(cl.getChildren().get(i).getCatName(), cl.getChildren().get(i).getSubName());
                        }
                        Intent intent = new Intent(MainActivity.this, settings.class);
                        startActivityForResult(intent, 1);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
    };

    // Метод, срабатывающий при уничтожении активности вот этой вот главной
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng krsk = new LatLng(56.009390, 92.853491);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(krsk, 10));
        if (points != null) {
            Log.e("ВСЕ ХОРОШО", "response code РИСУЕМ");
            for (Dish p : points) {
                LatLng pos = new LatLng(p.getLat(), p.getLng());
                JSONObject j = new JSONObject();
                try {
                    j.put("ID", p.getID());
                    j.put("Phone", p.getPhone());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(p.getTitle())
                        .snippet(p.getDescription() +
                                "\nПовар: " + p.getCookName() +
                                "\nКатегория: " + p.getCategory() + ". " + p.getSubcategory() +
                                "\nВремя приготовления: " + p.getCookingTime()+
                                "\nЦена: " + p.getPrice())

                ).setTag(j);
            }
        }
        JSONObject j = new JSONObject();
        try {
            j.put("ID", 1);
            j.put("Phone", "+79589");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions()
                .title("Красноярск")
                .snippet("Ня-ня-ня")
                .position(krsk)).setTag(j);


        mMap.setOnMarkerClickListener(this);
    }


    public boolean onMarkerClick(final Marker marker) {
        AlertDialog.Builder ad;
        Context context = MainActivity.this;
        String title = marker.getTitle();
        String message = marker.getSnippet();
        String button1String = "Отмена";
        String button2String = "Позвонить";

        ad = new AlertDialog.Builder(context);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setPositiveButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                JSONObject obj = (JSONObject)marker.getTag();
                String phone = null;
                String id = null;
                try {
                    phone = obj.get("Phone").toString();
                    id = obj.getString("ID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final Call<RequestStatus> rsCall = spaghetAPI.makeOrder(id, String.valueOf(UserID));
                rsCall.enqueue(new Callback<RequestStatus>() {
                    @Override
                    public void onResponse(Call<RequestStatus> call, Response<RequestStatus> response) {
                        if (response.isSuccessful()) {
                            Log.i("MAKE AN ORDER", "status: " + response.body().getStatus());
                        } else {
                           Log.e("MAKE AN ORDER", "response code " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestStatus> call, Throwable t) {}
                });

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
        ad.setNegativeButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        ad.setCancelable(true);
        ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });

        ad.show();
        return true;
    }
    // Вернулись из активности, где requestCode это код нашей активности, resultCode - с ошибками или нет, data - данные оттуда
    // requestCode = 1 - поиск по фильтрам
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case 1:
                Log.e("ВСЕ ХОРОШО", "response code МЫ ТУТ");
                String subcat = data.getStringExtra("subcat");
                String searchStr = data.getStringExtra("search");
                String start = data.getStringExtra("start");
                String end = data.getStringExtra("end");
                String cookingTime = data.getStringExtra("cookingTime");

                Log.e("ВСЕ ХОРОШО", "response code МЫ ТУТ Sub="+subcat+"&PriceFrom="+start+"&PriceTo="+end+"&CookingTime="+cookingTime+"&Str=" + searchStr);

                final Call<DishesList> dishesListCall = spaghetAPI.findDishes(subcat, start, end, cookingTime, searchStr);
                final DishesList dishes = new DishesList();

                dishesListCall.enqueue(new Callback<DishesList>() {
                    @Override
                    public void onResponse(Call<DishesList> call, Response<DishesList> response) {
                        if (response.isSuccessful()) {
                            Log.i("RESULT", "response " + response.body().toString());
                            dishes.setChildren(response.body().getChildren());
                            Log.i("PM", String.valueOf(dishes.getChildren().size()));
                            if (dishes.getChildren().size() != 0) {
                                points = dishes.getChildren();
                                plotPointsOnMap();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Увы...")
                                        .setMessage("По данному запросу ничего не найдено. Попробуйте изменить параметры")
                                        .setIcon(R.drawable.ic_menu_manage)
                                        .setCancelable(false)
                                        .setNegativeButton("ОК",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                    public void onFailure(Call<DishesList> call, Throwable t) {

                    }

                });
                break;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Совсем ничего", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favourites) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("UserID", String.valueOf(UserID));
            Log.i("\n\nИСТОРИЯ: ", "main -> history");
            startActivity(intent);


        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void plotPointsOnMap() {
        Log.e("MAPMAPMAPAMP GOOGLE", "ВСЕ ХОРОШО");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

}
