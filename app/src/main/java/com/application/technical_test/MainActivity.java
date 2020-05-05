package com.application.technical_test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import android.view.View;

import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.security.*;
import java.security.Signature;




public class MainActivity extends AppCompatActivity {

    TextView varText;
    String info;




    static final int PERMISSION_READ_STATE = 123;


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ServiceCast", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }



    public void Start(View view) throws Exception {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            MyTelephonyManager();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_READ_STATE);
        }
    }





    private void MyTelephonyManager() throws Exception {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }




        @SuppressLint("HardwareIds") String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        @SuppressLint("HardwareIds") String IMEINumber = manager.getDeviceId();


        info = "\n This is the IMEI of the mobile : " +IMEINumber;
        info += "\n This is the ID of the mobile phone: " +id;


        String alg = "DSA";
        KeyPairGenerator kg = KeyPairGenerator.getInstance(alg);
        KeyPair keyPair = kg.genKeyPair();


        Signature sign = Signature.getInstance(alg);
        sign.initSign (keyPair.getPrivate());

        SignedObject so = new SignedObject(info, keyPair.getPrivate(), sign);


        if (so.verify(keyPair.getPublic(), sign))
            try {
                Object myobj = so.getObject();
                
                varText = (TextView) findViewById(R.id.ID);
                varText.setText(myobj.toString());

                
            } catch (java.lang.ClassNotFoundException e) {};



    }






};










