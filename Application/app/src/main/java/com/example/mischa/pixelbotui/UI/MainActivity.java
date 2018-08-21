package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Swarm.Bot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {

    private final String DEVICE_ADDRESS="98:D3:32:31:7A:19";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button startButton, sendButton,clearButton,stopButton;
    TextView textView;
    EditText editText;
    boolean deviceConnected=false;
    boolean stopThread;
    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    PBCanvas canvas;
    int[] saveState;
    int[] restoreState;
    public static HashMap<Integer, Integer> BotAmounts = new HashMap<>();


    // Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frame = new FrameLayout(this);
        canvas = new PBCanvas(this);
        LinearLayout appWidgets = new LinearLayout(this);

        Button submit = new Button(this);

        appWidgets.addView(submit);

        frame.addView(canvas);
        frame.addView(appWidgets);

        canvas.setBackgroundColor(Color.WHITE);
        setContentView(frame);

        // The amount of bots we have to work with
        BotAmounts.put(-1162650,    73); //Red
        BotAmounts.put(-11713,      73); //Yellow
        BotAmounts.put(-15815319,   73); //Green
        BotAmounts.put(-12857684,   73); //Blue
        BotAmounts.put(-11268754,   73); //Purple
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



    public void onClickSend(View view) {
        String string = editText.getText().toString();
        string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.append("\nSent Data:"+string+"\n");

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
