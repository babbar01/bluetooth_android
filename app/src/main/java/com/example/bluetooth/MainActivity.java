package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    static final UUID muuid=UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    EditText etData;
    Button btnSend;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etData=findViewById(R.id.etData);
        btnSend=findViewById(R.id.btnSend);
        tvResult=findViewById(R.id.tvResult);

        final BluetoothAdapter btAdapter=BluetoothAdapter.getDefaultAdapter();
        System.out.println(btAdapter.getBondedDevices());
        final BluetoothDevice hc05=btAdapter.getRemoteDevice("98:D3:41:F5:C4:40");
        BluetoothSocket socket=null;
        int counter=0;
        do {
            try {
                socket = hc05.createRfcommSocketToServiceRecord(muuid);
                System.out.println(socket);
                socket.connect();
                System.out.println(socket.isConnected());
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }counter++;
        }while(!socket.isConnected()&& counter<3);

        try {
            OutputStream output=socket.getOutputStream();
            output.write(22);
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        final BluetoothSocket finalSocket = socket;
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data=etData.getText().toString().trim();
                int dta=Integer.parseInt(data);
                etData.setText(null);
                try {
                    OutputStream output= finalSocket.getOutputStream();
                    output.write(dta);
                    System.out.println("data sent to device connected");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int ab = 0;
                try {
                    InputStream input=finalSocket.getInputStream();
                    input.skip(input.available());
                    ab=input.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tvResult.setText(Integer.toString(ab));

            }
        });





    }
}
