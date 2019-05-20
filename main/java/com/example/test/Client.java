package com.example.test;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    String serverIP;
    int serverPort;
    DataInputStream in;
    DataOutputStream out;
    String received;

    public Client() {
        this.serverIP = Constants.SERVER_IP;
        this.serverPort = Constants.SERVER_PORT;
    }

    public void handleRequest(int op, int id, LatLng latLng) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("op",op);
        jsonObject.put("point_type",id);
        jsonObject.put("lat",latLng.latitude);
        jsonObject.put("lng",latLng.longitude);
        try {
            Socket s = new Socket(serverIP,serverPort);
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
            out.writeUTF(jsonObject.toString());
            received = in.readUTF();
            out.close();
            in.close();
            s.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
