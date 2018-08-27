package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.R;
import com.example.mischa.pixelbotui.Swarm.Bot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.zip.Inflater;

public class MainActivity extends Activity {

    private final String DEVICE_ADDRESS="98:D3:32:31:7A:19";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button clear, connectBT, submit;
    TextView textView;
    EditText editText;
    boolean deviceConnected=false;
    boolean stopThread;
    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    PBCanvas canvas;
    int[] saveState;
    int[] restoreState;
    public static HashMap<Integer, Integer> BotAmounts = new HashMap<>();
    ConstraintLayout main_layout;

    public static HashMap<String, Solution> Solution;
    public int botsTotal = 7;
    // Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();

        main_layout = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null);
        canvas = new PBCanvas(this);

        main_layout.addView(canvas);

        submit = main_layout.findViewById(R.id.bSubmit);
        submit.bringToFront();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIAdapter.createGridWpixel(canvas.uiGrid);

                Swarm.SwarmCreate(botsTotal,  devices);
                Solution = PathFinder.getSolutions(UIAdapter.destinationGrid);
                for (String key: Solution.keySet()) {
                    System.out.println("id is " + key + "With Path: " + Solution.get(key).Moves + "With Final colour of: " + Solution.get(key).Colour);

                }
                if (deviceConnected && BTinit()) {
                    onClickSend(view, Solution, sockets);
                }

                // start the new activity
                Intent intent = new Intent(canvas.context, SimActivity.class);
                canvas.context.startActivity(intent);
            }
        });

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

        canvas.setBackgroundColor(Color.WHITE);
        setContentView(main_layout);

        // The amount of bots we have to work with
        BotAmounts.put(-1162650,    70); //Red
        BotAmounts.put(-11713,      70); //Yellow
        BotAmounts.put(-15815319,   70); //Green
        BotAmounts.put(-12857684,   70); //Blue
        BotAmounts.put(-11268754,   70); //Purple
        BotAmounts.put(Color.BLACK, 0); //Black


    }

    // Save the state of the grid in the Bundle (similar to HashMap)
    @Override
    protected void onSaveInstanceState (Bundle state) {
        saveState = canvas.getSavedState();
        for (int i = 0; i < saveState.length; i++) {
            state.putInt("" + i, saveState[i]);
        }
        state.putInt("colour", canvas.selectedColour);
        System.out.println("Save: " + canvas.whiteBox);
        state.putInt("whiteBox", canvas.whiteBox);
    }

    // Get the saved state of the grid from the Bundle
    protected void onRestoreInstanceState (Bundle state) {
        restoreState = new int[state.size()];
        for (int i = 0; i < state.size(); i++) {
            restoreState[i] = state.getInt("" + i);
        }
        canvas.selectedColour = state.getInt("colour");
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

    public void onClickSend(View view, HashMap<String, Solution> Solution, HashMap<String, BluetoothSocket> sockets) {

        for (String address : Solution.keySet()) {
            String path = Solution.get(address).moves.toString();
            int intColour = Solution.get(address).colour;
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
        textView.append("\nSent Data:"+string+"\n");

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
        }
        return  colour;
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




}
