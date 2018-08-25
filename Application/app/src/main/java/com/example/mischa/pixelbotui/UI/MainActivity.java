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
import com.example.mischa.pixelbotui.Swarm.Direction;
import com.example.mischa.pixelbotui.Swarm.PathFinder;
import com.example.mischa.pixelbotui.Swarm.Solution;
import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.Inflater;

public class MainActivity extends Activity {
    // 98:D3:32:31:7A:19 address
    // 98:D3:32:31:7A:6D address
    private final String DEVICE_NAME="HC-05";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private LinkedHashMap<String, BluetoothDevice> devices = new LinkedHashMap<>();
    private HashMap<String, BluetoothSocket> sockets = new LinkedHashMap<>();
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button clear, connectBT, submit;
    private ArrayList deviceAddresses = new ArrayList();
    TextView textView;
    EditText editText;
    boolean deviceConnected=false;
    boolean stopThread;
    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    PBCanvas canvas;
    int[] saveState;
    int[] restoreState;
    ConstraintLayout main_layout;
    public static HashMap<String, Solution> Solution;
    public int botsTotal = 2;
    // Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();

        main_layout = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null);
        canvas = new PBCanvas(this, botsTotal);

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
                    System.out.println("id is " + key + "With Path: " + Solution.get(key));

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
                onClickStart(view);
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
            String string = Solution.get(address).toString();
            string.concat("\n");

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
                outputStream.write(string.getBytes());
                System.out.println("Sending message to Device: " + address + " Message is: " + string);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

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
