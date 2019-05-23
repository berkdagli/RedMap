package com.example.test;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
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

    public int sendText(String text) throws JSONException {
        int op = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("op",op);
        jsonObject.put("message",(String)text);
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(serverIP,serverPort),1000);
            out = new DataOutputStream(s.getOutputStream());
            out.writeUTF(jsonObject.toString());
            out.close();
            s.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }
}