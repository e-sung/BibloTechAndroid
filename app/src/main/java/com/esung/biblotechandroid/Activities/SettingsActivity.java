package com.esung.biblotechandroid.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esung.biblotechandroid.Network.NodeJsApi;
import com.esung.biblotechandroid.R;

import static com.esung.biblotechandroid.Utility.SharedPrefUtil.BASE_URL;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.NETWORK;
import static com.esung.biblotechandroid.Utility.SharedPrefUtil.NOT_SET;

public class SettingsActivity extends AppCompatActivity {


    String protocol;
    String FullIpAddress;
    String ipAddress;
    String portNumber;
    String ip0;
    String ip1;
    String ip2;
    String ip3;
    private EditText mProtocol;
    private EditText mIp0;
    private EditText mIp1;
    private EditText mIp2;
    private EditText mIp3;
    private EditText mPort;
    private Button mButton;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mProtocol = (EditText) findViewById(R.id.etv_protocol);
        mIp0 = (EditText) findViewById(R.id.etv_ip_0);
        mIp1 = (EditText) findViewById(R.id.etv_ip_1);
        mIp2 = (EditText) findViewById(R.id.etv_ip_2);
        mIp3 = (EditText) findViewById(R.id.etv_ip_3);
        mPort = (EditText) findViewById(R.id.etv_port);
        mButton = (Button) findViewById(R.id.bt_setServerAddress);

        mSharedPref = getSharedPreferences(NETWORK, MODE_PRIVATE);
        mEditor = mSharedPref.edit();

        String baseURL = mSharedPref.getString(BASE_URL, NOT_SET);
        if (baseURL == NOT_SET) {
            Toast.makeText(this, getString(R.string.set_server_address), Toast.LENGTH_LONG).show();
            baseURL = "http://192.168.0.12:3030/";
            parseBaseUrl(baseURL);
            setUpUrl();
        } else {
            String baseUrl = mSharedPref.getString(BASE_URL, null);
            parseBaseUrl(baseUrl);
            setUpUrl();
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baseURL = "";
                baseURL = baseURL + mProtocol.getText().toString();
                baseURL = baseURL + "://";
                baseURL = baseURL + mIp0.getText().toString();
                baseURL = baseURL + ".";
                baseURL = baseURL + mIp1.getText().toString();
                baseURL = baseURL + ".";
                baseURL = baseURL + mIp2.getText().toString();
                baseURL = baseURL + ".";
                baseURL = baseURL + mIp3.getText().toString();
                baseURL = baseURL + ":";
                baseURL = baseURL + mPort.getText().toString();
                baseURL = baseURL + "/";

                Toast.makeText(SettingsActivity.this, "Server set " + baseURL, Toast.LENGTH_SHORT).show();
                mEditor.putString(BASE_URL, baseURL);
                mEditor.commit();
                NodeJsApi.refreshInstance(getApplicationContext());
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        });
    }

    private void parseBaseUrl(String baseURL) {
        protocol = baseURL.split("://")[0];
        FullIpAddress = baseURL.split("://")[1];
        ipAddress = FullIpAddress.split(":")[0];
        portNumber = FullIpAddress.split(":")[1].split("/")[0];
        ip0 = ipAddress.split("\\.")[0];
        ip1 = ipAddress.split("\\.")[1];
        ip2 = ipAddress.split("\\.")[2];
        ip3 = ipAddress.split("\\.")[3];
    }

    private void setUpUrl() {
        mProtocol.setText(protocol);
        mIp0.setText(ip0);
        mIp1.setText(ip1);
        mIp2.setText(ip2);
        mIp3.setText(ip3);
        mPort.setText(portNumber);
    }
}
