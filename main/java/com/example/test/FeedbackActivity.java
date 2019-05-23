package com.example.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

public class FeedbackActivity extends FragmentActivity implements AsyncResponseFeedback {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    public void sendFeedback(View v) {
        NetworkTask networkTask = new NetworkTask();
        networkTask.delegate = this;
        String text = ((EditText)findViewById(R.id.editText)).getText().toString();
        networkTask.execute(text);
    }

    @Override
    public void showInfo(int errorcode) {
        Toast t;

        if(errorcode == 0) {
            ((EditText)findViewById(R.id.editText)).setText("");
            t = Toast.makeText(FeedbackActivity.this,"Gönderildi", Toast.LENGTH_LONG);
        }
        else {
            t = Toast.makeText(FeedbackActivity.this,"Gönderilemedi, bağlantı hatası"
                    , Toast.LENGTH_LONG);
        }
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
    }

    private class NetworkTask extends AsyncTask<String, Void, Integer> {
        public AsyncResponseFeedback delegate = null;

        @Override
        protected Integer doInBackground(String... strings) {
            Client c = new Client();
            int errorcode = 1;
            try {
                errorcode = c.sendText(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return errorcode;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            delegate.showInfo(integer);
        }
    }
}