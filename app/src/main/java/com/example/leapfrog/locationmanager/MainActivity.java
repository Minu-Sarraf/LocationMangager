package com.example.leapfrog.locationmanager;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LocInterface, View.OnClickListener {

    @BindView(R.id.button)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btn.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.LocationCode : {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetLocation l = new GetLocation(MainActivity.this);
                    l.callForLocation();
                } else {
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void location(double lat, double lon) {
        Toast.makeText(this,"Lat: "+lat+" Lon: " +lon,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        GetLocation l = new GetLocation(MainActivity.this);
        l.callForLocation();
    }

}

