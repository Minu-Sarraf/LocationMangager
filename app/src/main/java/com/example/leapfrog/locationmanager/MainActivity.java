package com.example.leapfrog.locationmanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, LocInterface, View.OnClickListener {

    private GLocation l;

    @BindView(R.id.button)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btn.setOnClickListener(this);
        l = new GLocation(this);
    }


    @Override
    public void getLocation(double lat, double lon) {
        String msg = String.valueOf(new StringBuilder(String.valueOf(lat)).append(",").append(lon));
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        l.callForLocation();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        l.callForLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(Constants.PERMISSIONS_REQ)))
            new AppSettingsDialog.Builder(this)
                    .setTitle("Open settings to allow PERMISSIONS?")
                    .setPositiveButton("Settings")
                    .setNegativeButton("Cancel")
                    .setRequestCode(Constants.LOCATION_CODE)
                    .build()
                    .show();

    }

}

