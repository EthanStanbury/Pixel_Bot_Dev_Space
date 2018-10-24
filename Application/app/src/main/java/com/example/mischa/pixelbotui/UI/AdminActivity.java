package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.R;
import com.example.mischa.pixelbotui.Swarm.PathFinder;
import com.example.mischa.pixelbotui.Swarm.Swarm;

public class AdminActivity extends Activity {

    Button connectBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final LayoutInflater inflater = getLayoutInflater();

        ConstraintLayout admin_layout = (ConstraintLayout) inflater.inflate(R.layout.activity_admin, null);


        connectBT = admin_layout.findViewById(R.id.connectBT);
        connectBT.bringToFront();
//        connectBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                test();
//
//            }
//        });
        //                MainActivity activity = new MainActivity();
//                activity.onClickStart();
    }

    public void bluetoothStart(View view) {
        //This is the method that gets called when the bluetooth button in AdminActivity is called
    }
}
