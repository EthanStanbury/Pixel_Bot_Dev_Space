package com.example.mischa.pixelbotui.Intergration.Bluetooth;

import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mischa.pixelbotui.Swarm.Solution;
import com.example.mischa.pixelbotui.UI.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ethan on 20/10/18.
 */

public class BluetoothUtil extends IntentService {

    // 98:D3:32:31:7A:19 address
    // 98:D3:32:31:7A:6D address
    private final String DEVICE_NAME="HC-05";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    public LinkedHashMap<String, BluetoothDevice> devices = new LinkedHashMap<>();
    static  final public HashMap<String, BluetoothSocket> sockets = new LinkedHashMap<>();
    public BluetoothSocket socket;
    public OutputStream outputStream;
    public InputStream inputStream;
    public BluetoothAdapter  bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    public boolean deviceConnected = false;

    public IBinder mBinder;


    private Activity activity;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BluetoothUtil(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        onClickStart();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        String activityIntent = intent.getAction();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        return super.onStartCommand(intent, flags, startId);
    }

    public void giveActivity(Activity activity){
        this.activity = activity;
    }

    public void onClickSend(HashMap<String, Solution> Solution) {

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

    public void onClickStart() {
        System.out.println("Tried to send test to the HC");
        if(BTinit())
        {
            if(BTconnect())
            {

                Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
                deviceConnected=true;

                Context context = activity.getApplicationContext();
                CharSequence text = "Connected to: " + devices.size()+ " Device(s)";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


            }

        }
    }

    public boolean BTinit()
    {

        boolean found=false;

        if (bluetoothAdapter == null) {
            Toast.makeText(activity.getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(activity.getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
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
                System.out.println("Making Socket");
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
            Toast.makeText(activity.getApplicationContext(),"Please pair a device",Toast.LENGTH_SHORT).show();
        }

        if (sockets.isEmpty()){
            connected = false;
        }
        return connected;
    }

    private String intColourLetter(int intColour) {
        String colour = "N";
        switch (intColour){
            case -65536:
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
    }

    public void onClickStop(View view) throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }


}
