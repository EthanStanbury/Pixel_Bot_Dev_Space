package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.mischa.pixelbotui.Intergration.Bluetooth.BluetoothUtil;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.R;
import com.example.mischa.pixelbotui.Swarm.PathFinder;
import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.io.Serializable;

public class AdminActivity extends Activity {

    Button connectBT;
    BluetoothUtil mBTU;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final LayoutInflater inflater = getLayoutInflater();


        mBTU = (BluetoothUtil)getApplicationContext();


        ConstraintLayout admin_layout = (ConstraintLayout) inflater.inflate(R.layout.activity_admin, null);


        connectBT = admin_layout.findViewById(R.id.connectBT);
        connectBT.bringToFront();

    }

    public void bluetoothStart(View view) {
        mBTU.onClickStart();

    }
}
