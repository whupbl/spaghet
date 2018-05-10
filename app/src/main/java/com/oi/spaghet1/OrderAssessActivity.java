package com.oi.spaghet1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAssessActivity extends AppCompatActivity {

    private SpaghetAPI spaghetAPI = MainActivity.spaghetAPI;
    private String postID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = new Intent();
        postID = getIntent().getStringExtra("postID");
        Log.i("НАЖАЛИ КНОПКУ", "Оценить");
        //showRatingDialog();
        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(this);

        ratingdialog.setIcon(android.R.drawable.btn_star_big_on);
        ratingdialog.setTitle("Ваша оценка");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View linearlayout = inflater.inflate(R.layout.ratingdialog, null);
        ratingdialog.setView(linearlayout);

        final RatingBar rating = (RatingBar) linearlayout.findViewById(R.id.ratingbar);


        ratingdialog.setPositiveButton("Готово",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final Call<RequestStatus> requestStatusCall = spaghetAPI.assessOrder(postID,
                                String.valueOf(rating.getRating()));
                        final RequestStatus rs = new RequestStatus();

                        requestStatusCall.enqueue(new Callback<RequestStatus>() {
                            @Override
                            public void onResponse(Call<RequestStatus> call, Response<RequestStatus> response) {
                                if (response.isSuccessful()) {
                                    rs.setStatus(response.body().getStatus());
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderAssessActivity.this);
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
                            public void onFailure(Call<RequestStatus> call, Throwable t) {

                            }

                        });
                        intent.putExtra("requestCode", 1);
                        setResult(RESULT_OK, intent);
                        dialog.dismiss();

                        finish();
                    }
                })

                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                intent.putExtra("requestCode", 2);
                                setResult(RESULT_OK, intent);
                                dialog.cancel();

                                finish();
                            }
                        });

        ratingdialog.create();
        ratingdialog.show();


    }
}
