package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
<<<<<<< HEAD
=======
import android.app.AlertDialog;
>>>>>>> Bugfixn'
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
<<<<<<< HEAD
=======
import android.content.DialogInterface;
>>>>>>> Bugfixn'
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
<<<<<<< HEAD
=======
import android.view.MenuItem;
>>>>>>> Bugfixn'
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
<<<<<<< HEAD
import android.widget.LinearLayout;
=======
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
>>>>>>> Bugfixn'
import android.widget.TextView;
import android.widget.Toast;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.R;
import com.example.mischa.pixelbotui.Swarm.Bot;
import com.example.mischa.pixelbotui.Swarm.Direction;
import com.example.mischa.pixelbotui.Swarm.PathFinder;
import com.example.mischa.pixelbotui.Swarm.Solution;
import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
<<<<<<< HEAD
import java.util.Collections;
import java.util.HashMap;
=======
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
>>>>>>> Bugfixn'
import java.util.Set;
import java.util.UUID;
import java.util.zip.Inflater;

public class MainActivity extends Activity {
<<<<<<< HEAD

    private final String DEVICE_ADDRESS="98:D3:32:31:7A:19";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
=======
    // 98:D3:32:31:7A:19 address
    // 98:D3:32:31:7A:6D address
    private final String DEVICE_NAME="HC-05";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private LinkedHashMap<String, BluetoothDevice> devices = new LinkedHashMap<>();
    private HashMap<String, BluetoothSocket> sockets = new LinkedHashMap<>();
>>>>>>> Bugfixn'
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
<<<<<<< HEAD
    Button clear, connectBT, submit;
    TextView textView;
    EditText editText;
=======
    Button sdfclear, submit;
    ImageButton menu, paintBrush, red, yellow, green, cyan, blue, magenta, white, eraser, clear;
    private ArrayList deviceAddresses = new ArrayList();
    TextView textView;
>>>>>>> Bugfixn'
    boolean deviceConnected=false;
    boolean stopThread;
    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    PBCanvas canvas;
    int[] saveState;
    int[] restoreState;
<<<<<<< HEAD
    public static HashMap<Integer, Integer> BotAmounts = new HashMap<>();
    ConstraintLayout main_layout;
=======
    ConstraintLayout main_layout;
    public ConstraintLayout top, bottom;
    public static HashMap<String, Solution> Solution;
    Context context = this;
    private final String password = "1337";
    FrameLayout holder;



>>>>>>> Bugfixn'
    // Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

<<<<<<< HEAD
        LayoutInflater inflater = getLayoutInflater();

        main_layout = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null);
        canvas = new PBCanvas(this);

        main_layout.addView(canvas);
=======
        final LayoutInflater inflater = getLayoutInflater();

        main_layout = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null);
        canvas = new PBCanvas(this);
        holder = main_layout.findViewById(R.id.holder);
        holder.addView(canvas);

        top = main_layout.findViewById(R.id.top);
        top.bringToFront();

        bottom = main_layout.findViewById(R.id.bottom);
        bottom.bringToFront();
>>>>>>> Bugfixn'

        submit = main_layout.findViewById(R.id.bSubmit);
        submit.bringToFront();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIAdapter.createGridWpixel(canvas.uiGrid);
<<<<<<< HEAD
                SwarmAdapter.SwarmCreate(MainActivity.BotAmounts);
=======
                Swarm.SwarmCreate(canvas.TotalBots,  devices);
                Solution = PathFinder.getSolutions(UIAdapter.destinationGrid);
                for (String key: Solution.keySet()) {
                    System.out.println("id is " + key + "With Path: " + Solution.get(key).Moves + "With Final colour of: " + Solution.get(key).Colour);

                }
                if (deviceConnected && BTinit()) {
                    onClickSend(view, Solution, sockets);
                }
>>>>>>> Bugfixn'

                // start the new activity
                Intent intent = new Intent(canvas.context, SimActivity.class);
                canvas.context.startActivity(intent);
            }
        });

<<<<<<< HEAD
        connectBT = main_layout.findViewById(R.id.bConnect);
        connectBT.bringToFront();
        connectBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CONNECT");
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

//        main_layout.addView(frame);
=======
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
                                            context.startActivity(intent);
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
>>>>>>> Bugfixn'

        canvas.setBackgroundColor(Color.WHITE);
        setContentView(main_layout);

        // The amount of bots we have to work with
<<<<<<< HEAD
        BotAmounts.put(-1162650,    70); //Red
        BotAmounts.put(-11713,      70); //Yellow
        BotAmounts.put(-15815319,   70); //Green
        BotAmounts.put(-12857684,   70); //Blue
        BotAmounts.put(-11268754,   70); //Purple
        BotAmounts.put(Color.BLACK, 0); //Black
=======

>>>>>>> Bugfixn'


    }

    // Save the state of the grid in the Bundle (similar to HashMap)
    @Override
    protected void onSaveInstanceState (Bundle state) {
        saveState = canvas.getSavedState();
        for (int i = 0; i < saveState.length; i++) {
            state.putInt("" + i, saveState[i]);
        }
        state.putInt("colour", canvas.selectedColour);
<<<<<<< HEAD
        System.out.println("Save: " + canvas.whiteBox);
        state.putInt("whiteBox", canvas.whiteBox);
=======
        //System.out.println("Save: " + canvas.whiteBox);
        //state.putInt("whiteBox", canvas.whiteBox);
>>>>>>> Bugfixn'
    }

    // Get the saved state of the grid from the Bundle
    protected void onRestoreInstanceState (Bundle state) {
        restoreState = new int[state.size()];
        for (int i = 0; i < state.size(); i++) {
            restoreState[i] = state.getInt("" + i);
        }
        canvas.selectedColour = state.getInt("colour");
<<<<<<< HEAD
        canvas.whiteBox = state.getInt("whiteBox");
        canvas.giveRestoreState(restoreState);


    }

    public void BluetoothConnect(){

    }


    public boolean BTinit()
    {
        boolean found=false;

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }
=======
        //canvas.whiteBox = state.getInt("whiteBox");
        canvas.giveRestoreState(restoreState);

>>>>>>> Bugfixn'

    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return connected;
    }

    public void onClickStart(View view) {
        System.out.println("Tried to send test to the HC");
        if(BTinit())
        {
            if(BTconnect())
            {
                Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
                // setUiEnabled(true);
                deviceConnected=true;
                textView.append("\nConnection Opened to these addresses: \n");
                for (BluetoothDevice iterator : bondedDevices)
                {
                    textView.append(iterator.getAddress() + "\n");
                    textView.append(iterator.getName()+ "\n");
                }

                try {
                    outputStream.write("test".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }

        }
    }



<<<<<<< HEAD
    public void onClickSend(View view) {
        String string = editText.getText().toString();
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.append("\nSent Data:"+string+"\n");

=======
    public boolean BTinit()
    {
        boolean found=false;

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {

                if(iterator.getName().equals(DEVICE_NAME))
                {

                    devices.put(iterator.getAddress(), iterator);
                    found=true;
                }
            }
        }
        return found;
    }

    public boolean BTconnect()
    {

        boolean connected=true;

        for (String deviceAddress: devices.keySet()) {
            try{
                BluetoothSocket tempSock = devices.get(deviceAddress).createRfcommSocketToServiceRecord(PORT_UUID);
                tempSock.connect();
                sockets.put(deviceAddress, tempSock);
            } catch (IOException e){
            e.printStackTrace();
            devices.remove(deviceAddress);
            }
        }
        if(!devices.isEmpty())
        {
            for (String deviceAddress: sockets.keySet()) {
                try {
                    outputStream = sockets.get(deviceAddress).getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    sockets.remove(deviceAddress);
                }
                try {
                    inputStream = sockets.get(deviceAddress).getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    sockets.remove(deviceAddress);
                }
            }

        }else{
            Toast.makeText(getApplicationContext(),"Please pair a device",Toast.LENGTH_SHORT).show();
        }

        if (sockets.isEmpty()){
            connected = false;
        }
        return connected;
    }

    public void onClickStart(View view) {
        System.out.println("Tried to send test to the HC");
        if(BTinit())
        {
            if(BTconnect())
            {
                Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
                deviceConnected=true;

                Context context = getApplicationContext();
                CharSequence text = "Connected to: " + devices.size()+ " Device(s)";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


            }

        }
    }



    public void onClickSend(View view, HashMap<String, Solution> Solution, HashMap<String, BluetoothSocket> sockets) {

        for (String address : Solution.keySet()) {
            String path = Solution.get(address).Moves.toString();
            int intColour = Solution.get(address).Colour;
            String desColour = intColourLetter(intColour);

            String message = path +"<"+desColour+ ">";
            System.out.println("FINAL STRING IS: " + message);
            try {
                outputStream = sockets.get(address).getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                sockets.remove(address);
            }
            try {
                inputStream = sockets.get(address).getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                sockets.remove(address);
            }

            try {
                outputStream.write(message.getBytes());
                System.out.println("Sending message to Device: " + address + " Message is: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private String intColourLetter(int intColour) {
        String colour = "N";
        switch (intColour){
            case -1162650:
                colour = "E";
                break;

            case -16711936: //Green
                colour =  "G";
                break;

            case -16776961: //Blue
                colour = "B";
                break;

            case -256: //Yellow
                colour = "Y";
                break;

            case -16711681: //Cyan
                colour = "C";
                break;

            case -65281: //Magenta
                colour = "M";
                break;

            case -1: //White
                colour = "W";
                break;
            case 0:
                colour = "M";
                break;
        }
        return  colour;
>>>>>>> Bugfixn'
    }

    public void onClickStop(View view) throws IOException {
        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();
//        setUiEnabled(false);
        deviceConnected=false;
        textView.append("\nConnection Closed!\n");
    }

    public void onClickClear(View view) {
        textView.setText("");
    }


<<<<<<< HEAD


=======
>>>>>>> Bugfixn'
}
