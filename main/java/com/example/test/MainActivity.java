package com.example.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showInfo(View v) {
        int str;
        int title;
        switch(v.getId()) {
            case R.id.ilac_info:
                str = R.string.ilac_info;
                title = R.string.ilac_button;
            break;
            case R.id.bitkisel_yag_info:
                str = R.string.bitkisel_yag_info;
                title = R.string.bitkisel_yag_button;
            break;
            case R.id.pil_info:
                str = R.string.pil_info;
                title = R.string.pil_button;
            break;
            case R.id.cam_info:
                str = R.string.cam_info;
                title = R.string.cam_ambalaj_button;
            break;
            case R.id.diger_ambalaj_info:
                str = R.string.diger_ambalaj_info;
                title = R.string.diger_ambalaj_button;
            break;
            case R.id.elektronik_info:
                str = R.string.elektronik_info;
                title = R.string.elektronik_button;
            break;
            case R.id.tekstil_info:
                str = R.string.tekstil_info;
                title = R.string.tekstil_button;
            break;
            default:
                str = R.string.tekstil_info;
                title = R.string.tekstil_button;
            break;
        }
        Bundle args = new Bundle();
        args.putInt("str",str);
        args.putInt("title",title);
        InfoDialog info = new InfoDialog();
        info.setArguments(args);
        info.show(getSupportFragmentManager(), "infodialog");
    }

    public void showMap(View v) {
        int arg;
        Intent i = new Intent(MainActivity.this,MapActivity.class);
        switch (v.getId()) {
            case R.id.cam_ambalaj_button:
                arg = Constants.CAM;
                break;
            case R.id.ilac_button:
                arg = Constants.ILAC;
                break;
            case R.id.pil_button:
                arg = Constants.PIL;
                break;
            case R.id.bitkisel_yag_button:
                arg = Constants.YAG;
                break;
            case R.id.elektronik_button:
                arg = Constants.ELEKTRONIK;
                break;
            case R.id.tekstil_button:
                arg = Constants.TEKSTIL;
                break;
            default:
                arg = Constants.DIGER;
        }
        i.putExtra("pointID",arg);
        startActivity(i);
    }
}
