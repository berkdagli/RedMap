package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    public void sendFeedback(View v) {
        ((EditText)findViewById(R.id.editText)).setText("");
        Toast t = Toast.makeText(FeedbackActivity.this,"Gönderildi", Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();
    }
}
