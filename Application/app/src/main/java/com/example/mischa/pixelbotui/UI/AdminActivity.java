package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.mischa.pixelbotui.R;

public class AdminActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final LayoutInflater inflater = getLayoutInflater();

        ConstraintLayout admin_layout = (ConstraintLayout) inflater.inflate(R.layout.activity_admin, null);

        Button connectBT = admin_layout.findViewById(R.id.connectBT);
        connectBT.bringToFront();
        connectBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CONNECT");
                //onClickStart(view);
            }
        });
    }
}
