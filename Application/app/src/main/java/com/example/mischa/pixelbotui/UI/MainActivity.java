package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mischa.pixelbotui.Intergration.Bluetooth.BluetoothUtil;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.R;
import com.example.mischa.pixelbotui.Swarm.PathFinder;
import com.example.mischa.pixelbotui.Swarm.Solution;
import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {


    Button  submit;
    ImageButton menu, paintBrush, red, yellow, green, cyan, blue, magenta, white, eraser, clear;
    TextView textView;
    boolean stopThread;
    IBinder mBTU;
    Activity activity = this;

    public  final static String SER_KEY = "com.example.mischa.pixelbotui.UI.ser";

    public static final String ON_CLICK =  "ON_CLICK";

    PBCanvas canvas;
    int[] saveState;
    int[] restoreState;
    ConstraintLayout main_layout;
    public ConstraintLayout top, bottom;
    public static HashMap<String, Solution> Solution;
    Context context = this;
    private final String password = ""; //1337
    FrameLayout holder;

    public void startService(View view) {
        Intent intent = new Intent(getBaseContext(), BluetoothUtil.class);
        startService(intent);
    }

    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), BluetoothUtil.class));
    }



    // Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(main_layout.findViewById(R.id.holder));
        BluetoothUtil.onBind;
        mBTU.giveActivity(this);

        final LayoutInflater inflater = getLayoutInflater();

        main_layout = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null);
        canvas = new PBCanvas(this);
        holder = main_layout.findViewById(R.id.holder);
        holder.addView(canvas);

        top = main_layout.findViewById(R.id.top);
        top.bringToFront();

        bottom = main_layout.findViewById(R.id.bottom);
        bottom.bringToFront();

        submit = main_layout.findViewById(R.id.bSubmit);
        submit.bringToFront();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIAdapter.createGridWpixel(canvas.uiGrid);
                Swarm.SwarmCreate(canvas.TotalBots,  mBTU.devices);
                Solution = PathFinder.getSolutions(UIAdapter.destinationGrid);
                for (String key: Solution.keySet()) {
                    System.out.println("id is " + key + "With Path: " + Solution.get(key).Moves + "With Final colour of: " + Solution.get(key).Colour);

                }
                if (mBTU.deviceConnected && mBTU.BTinit()) {
                    mBTU.onClickSend(Solution);
                }

                // start the new activity
                Intent intent = new Intent(canvas.context, SimActivity.class);
                canvas.context.startActivity(intent);
            }
        });

        clear = main_layout.findViewById(R.id.bClear);
        clear.bringToFront();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvas.clear();
            }
        });

        menu = main_layout.findViewById(R.id.menu);
        menu.bringToFront();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onClickStart();
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, menu);
                popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch ((String) menuItem.getTitle()) {
                            case "Admin":
                                View passwordPopup = inflater.inflate(R.layout.password_popup, null);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setView(passwordPopup);
                                final EditText inputPassword = passwordPopup.findViewById(R.id.userInput);

                                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (inputPassword.getText().toString().equals(password)) {
                                            Intent intent = new Intent(context, AdminActivity.class);
                                           startActivity(intent);
                                        } else {
                                            dialogInterface.cancel();
                                        }
                                    }
                                });
                                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();

                                alertDialog.show();

                                break;

                            case "Reset":
                                //RESET ALL BOTS (REMOVE PICTURE FROM WALL)
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        red = main_layout.findViewById(R.id.bRed);
        red.bringToFront();
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Mischy Moo");

                paintBrush.setImageResource(R.drawable.paintbrushred);
                canvas.selectedColour = Color.RED;
            }
        });

        yellow = main_layout.findViewById(R.id.bYellow);
        yellow.bringToFront();
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintBrush.setImageResource(R.drawable.paintbrushyellow);
                canvas.selectedColour = Color.YELLOW;
            }
        });

        green = main_layout.findViewById(R.id.bGreen);
        green.bringToFront();
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintBrush.setImageResource(R.drawable.paintbrushgreen);
                canvas.selectedColour = Color.GREEN;
            }
        });

        cyan = main_layout.findViewById(R.id.bCyan);
        cyan.bringToFront();
        cyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintBrush.setImageResource(R.drawable.paintbrushcyan);
                canvas.selectedColour = Color.CYAN;
            }
        });

        blue = main_layout.findViewById(R.id.bBlue);
        blue.bringToFront();
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintBrush.setImageResource(R.drawable.paintbrushblue);
                canvas.selectedColour = Color.BLUE;
            }
        });

        magenta = main_layout.findViewById(R.id.bMagenta);
        magenta.bringToFront();
        magenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintBrush.setImageResource(R.drawable.paintbrushmagenta);
                canvas.selectedColour = Color.MAGENTA;
            }
        });

        white = main_layout.findViewById(R.id.bWhite);
        white.bringToFront();
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintBrush.setImageResource(R.drawable.paintbrushwhite);
                canvas.selectedColour = Color.WHITE;
            }
        });

        paintBrush = main_layout.findViewById(R.id.bPaintBrush);
        paintBrush.bringToFront();

        eraser = main_layout.findViewById(R.id.bEraser);
        eraser.bringToFront();
        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintBrush.setImageResource(R.drawable.paintbrushwhiteoutline);
                canvas.selectedColour = Color.TRANSPARENT;
            }
        });

        canvas.setBackgroundColor(Color.WHITE);
        setContentView(main_layout);

        // The amount of bots we have to work with



    }

    // Save the state of the grid in the Bundle (similar to HashMap)
    @Override
    protected void onSaveInstanceState (Bundle state) {
        saveState = canvas.getSavedState();
        for (int i = 0; i < saveState.length; i++) {
            state.putInt("" + i, saveState[i]);
        }
        state.putInt("colour", canvas.selectedColour);
        //System.out.println("Save: " + canvas.whiteBox);
        //state.putInt("whiteBox", canvas.whiteBox);
    }

    // Get the saved state of the grid from the Bundle
    protected void onRestoreInstanceState (Bundle state) {
        restoreState = new int[state.size()];
        for (int i = 0; i < state.size(); i++) {
            restoreState[i] = state.getInt("" + i);
        }
        canvas.selectedColour = state.getInt("colour");
        //canvas.whiteBox = state.getInt("whiteBox");
        canvas.giveRestoreState(restoreState);


    }






    static public MainActivity getInstance() {
        MainActivity instance = null;

        if (instance == null) {
            instance = new MainActivity();
            return instance;
        } else {
            return instance;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1){
//            onClickStart();
//
//        }
//        finish();
//    }
}
