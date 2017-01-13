package com.willydevelopment.com.lawnhiro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class Settings extends AppCompatActivity {
    final Context context = this;

    EditText Address1;
    EditText Address2;
    EditText City;
    EditText State;
    EditText Zip;

    private String tempAddress1;
    private String tempAddress2;
    private String tempCity;
    private String tempState;
    private String tempZip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Address1 = (EditText) findViewById(R.id.textSettingsDialogAddress1);
        Address2 = (EditText) findViewById(R.id.textSettingsDialogAddress2);
        City = (EditText) findViewById(R.id.textSettingsDialogCity);
        State = (EditText) findViewById(R.id.textSettingsDialogState);
        Zip = (EditText) findViewById(R.id.textSettingsDialogZip);

        final AddressPreferences addressPreference = new AddressPreferences();
        final SharedPreferences tempSettings = getSharedPreferences("LAWNHIRO_ADDRESS_PREFERENCES", MODE_PRIVATE);
        String addressArray[];// = new String[5];
        addressArray = addressPreference.getAddressPreferences(tempSettings);

        tempAddress1 = addressArray[0]; //settings.getString("Address1", "");
        tempAddress2 = addressArray[1]; //settings.getString("Address2", "");
        tempCity = addressArray[2]; //settings.getString("City", "");
        tempState = addressArray[3]; //settings.getString("State", "");
        tempZip = addressArray[4]; //settings.getString("Zip", "");

        Address1.setText(tempAddress1);
        if (!TextUtils.isEmpty(tempAddress2)) {
            Address2.setText(tempAddress2);
        }
        City.setText(tempCity);
        State.setText(tempState);
        Zip.setText(tempZip);
    }

    public void saveDefaultAddressInformationClick(View view) {
        tempAddress1 = Address1.getText().toString();
        tempAddress2 = Address2.getText().toString();
        tempCity = City.getText().toString();
        tempState = State.getText().toString();
        tempZip = Zip.getText().toString();
        setDefaultAddressInformation();

        Intent intent=new Intent();
        setResult(1,intent);
        finish();//finishing activity
        }

    public void setDefaultAddressInformation() {
        AddressPreferences addressPreference = new AddressPreferences();
        SharedPreferences tempSettings = getSharedPreferences("LAWNHIRO_ADDRESS_PREFERENCES", MODE_PRIVATE);
        addressPreference.setAddressPreferences(tempSettings, tempAddress1, tempAddress2, tempCity, tempState, tempZip);
    }

}
