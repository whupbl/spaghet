package com.oi.spaghet1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

// Главная активность, которая с картой
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    // Объявили карту
    private GoogleMap mMap;
    int t;
    List<Point> points;
    // Метод, срабатывающий при создании активности вот этой вот главной
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // Кнопочка розовая круглая
        fab.setOnClickListener(fabListener);


        // Менюшка слева
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //бИРЮЗОВАЯ ШАПКА СВЕРХУ
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Фрагмент с картой
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        networktask = new NetworkTask();
    }

    private View.OnClickListener fabListener = new View.OnClickListener() {
        public void onClick(View v){
            networktask = new NetworkTask(); //New instance of NetworkTask
            networktask.execute();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //networktask.SendDataToNetwork("command:10".getBytes());
            networktask.SendDataToNetwork("GET / HTTP/1.1\r\n\r\n".getBytes());
            Log.i("NOS CMD", "Я тут !!!!!! 2");
            t = 10;
        }
    };

    // Метод, срабатывающий при уничтожении активности вот этой вот главной
    @Override
    protected void onDestroy() {
        super.onDestroy();
        networktask.cancel(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng krsk = new LatLng(56.009390, 92.853491);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(krsk, 10));
        if (points != null) {
            for (Point p : points) {
                LatLng pos = new LatLng(p.getLat(), p.getLng());
                googleMap.addMarker(new MarkerOptions().position(pos)
                        .title(p.getName()).snippet(p.getDescription()));
            }
            mMap.addMarker(new MarkerOptions()
                    .title("Красноярск")
                    .snippet("Ня-ня-ня")
                    .position(krsk));
        }
    }

    // Вернулись из активности с фильтрами, где requestCode это код нашей активности, resultCode - с ошибками или нет, data - данные оттуда
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String cat = data.getStringExtra("cat");
        String subcat = data.getStringExtra("subcat");
        String search = data.getStringExtra("search");
        int start = data.getIntExtra("start", 100);
        int end = data.getIntExtra("end", 400);
        networktask = new NetworkTask(); //New instance of NetworkTask
        networktask.execute();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //networktask.SendDataToNetwork("command:10".getBytes());
        networktask.SendDataToNetwork("GET / HTTP/1.1\r\n\r\n".getBytes());
        Log.i("NOS CMD", "Я тут !!!!!! 2");
        t = 11;
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    byte[] getCatCommand() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("command", "10");
        return obj.toString().getBytes();
    }





    NetworkTask networktask;
    public class NetworkTask extends AsyncTask<Void, byte[], Boolean> {
        Socket nsocket; //Network Socket
        InputStream nis; //Network Input Stream
        OutputStream nos; //Network Output Stream

        @Override
        protected void onPreExecute() {
            Log.i("AsyncTask", "onPreExecute");
        }

        @Override
        protected Boolean doInBackground(Void... params) { //This runs on a different thread
            boolean result = false;
            try {
                Log.i("AsyncTask", "doInBackground: Creating socket");
                SocketAddress sockaddr = new InetSocketAddress("172.20.10.2", 15);
                nsocket = new Socket();
                nsocket.connect(sockaddr, 5000); //5 second connection timeout
                if (nsocket.isConnected()) {
                    nis = nsocket.getInputStream();
                    nos = nsocket.getOutputStream();
                    Log.i("AsyncTask", "doInBackground: Socket created, streams assigned");
                    Log.i("AsyncTask", "doInBackground: Waiting for inital data...");

                    byte[] buffer = new byte[4096];

                    Log.i("AsyncTask", "doInBackground: Я начал 1.");
                    int read = nis.read(buffer, 0, 4096); //This is blocking
                    Log.i("AsyncTask", "doInBackground: Я закончил 1.");
                    while(read != -1){
                        byte[] tempdata = new byte[read];
                        System.arraycopy(buffer, 0, tempdata, 0, read);
                        publishProgress(tempdata);
                        Log.i("AsyncTask", "doInBackground: Got some data");
                        read = nis.read(buffer, 0, 4096); //This is blocking
                        Log.i("AsyncTask", "doInBackground: Я закончил 2.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("AsyncTask", "doInBackground: IOException");
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("AsyncTask", "doInBackground: Exception");
                result = true;
            } finally {
                try {
                    nis.close();
                    nos.close();
                    nsocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("AsyncTask", "doInBackground: Finished");
            }
            return result;
        }

        public boolean SendDataToNetwork(final byte[] cmd) {
            if (nsocket.isConnected())
            {
                Log.i("hjgjhgvkjh", "SendDataToNetwork: Writing received message to socket");
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            nos.write(cmd);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            Log.i("hjgjhgvkjh", "SendDataToNetwork: Message send failed. Caught an exception");
                        }
                    }
                }).start();
                Log.i("NOS CMD", "Я тут !!!!!!");

                return true;
            }

            Log.i("hjgjhgvkjh", "SendDataToNetwork: Cannot send message. Socket is closed");
            return false;
        }

        @Override
        protected void onProgressUpdate(byte[]... values) {
            if (values.length > 0) {
                Log.i("AsyncTask", "onProgressUpdate: " + values[0].length + " bytes received.");
                switch (t){
                    case 10:
                        // TODO Создаем категории, запускаем интент
                        categories.addSubcatToCat("первое", "щи");
                        categories.addSubcatToCat("первое", "борщ");
                        categories.addSubcatToCat("второе", "котлетка с макарошками");
                        categories.addSubcatToCat("второе", "котлетка с пюрешкой");
                        System.out.print(categories.getSubcat("второе"));
                        Intent intent = new Intent(MainActivity.this, settings.class);
                        startActivityForResult(intent, 1);
                        break;
                    case 11:
                        points = new ArrayList<>();
                        Point p1 = new Point(56.009390, 92.853491, "p1", "d1", 12, "c1",
                                "8923224434", 200, 4.3);
                        points.add(p1);
                        Point p2 = new Point(56.019390, 92.853491, "p2", "d2", 22, "c2",
                                "8923224434", 220, 4.1);
                        points.add(p2);
                        Point p3 = new Point(56.029390, 92.853491, "p3", "d3", 13, "c3",
                                "8923224434", 30, 4.5);
                        points.add(p3);
                        plotPointsOnMap();
                }
            }
        }

        @Override
        protected void onCancelled() {
            Log.i("AsyncTask", "Cancelled.");
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Log.i("AsyncTask", "onPostExecute: Completed with an Error.");
            } else {
                Log.i("AsyncTask", "onPostExecute: Completed.");
            }
        }
    }

    public void plotPointsOnMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

}
