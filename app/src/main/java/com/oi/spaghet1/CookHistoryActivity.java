package com.oi.spaghet1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CookHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<History> posts;
    private SpaghetAPI spaghetAPI;
    private Retrofit retrofit;
    HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_history);

        posts = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HistoryAdapter(posts, CookHistoryActivity.this);
        recyclerView.setAdapter(adapter);


        retrofit = adapter.retrofit;

        spaghetAPI = retrofit.create(SpaghetAPI.class);
        String id = getIntent().getStringExtra("UserID");
        final Call<ClientHistory> clientHistoryCall = spaghetAPI.getHistory(id);


        clientHistoryCall.enqueue(new Callback<ClientHistory>() {
            @Override
            public void onResponse(Call<ClientHistory> call, Response<ClientHistory> response) {
                if (response.isSuccessful()) {
                    Log.i("RESULT", "response " + response.body().toString());
                    posts.addAll(response.body().getChildren());
                    recyclerView.getAdapter().notifyDataSetChanged();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CookHistoryActivity.this);
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
            public void onFailure(Call<ClientHistory> call, Throwable t) {
                Toast.makeText(CookHistoryActivity.this,
                        "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case 1:
                recreate();
                break;
            case 2: break;
        }
    }
}
