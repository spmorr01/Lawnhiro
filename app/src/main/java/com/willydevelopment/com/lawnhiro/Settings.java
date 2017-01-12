package com.willydevelopment.com.lawnhiro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void saveDefaultAddressInformationClick(View view) {
        /*SharedPreferences settings = getSharedPreferences("LAWNHIRO_ADDRESS_PREFERENCES", MODE_PRIVATE);   //getPreferences(Context.MODE_PRIVATE);
        Editor editSettings = settings.edit();
        //editSettings.clear();
        editSettings.putString("Address1", tempAddress1); //Address1.getText().toString();
        editSettings.putString("Address2", tempAddress2);
        editSettings.putString("City", tempCity);
        editSettings.putString("State", tempState);
        editSettings.putString("Zip", tempZip);

        editSettings.apply();*/

        tempAddress1 = Address1.getText().toString();
        tempAddress2 = Address2.getText().toString();
        tempCity = Zip.getText().toString();
        tempState = State.getText().toString();
        tempZip = Zip.getText().toString();
        setDefaultAddressInformation();

        //Toast.makeText(Settings.this, "Settings set successfully!", Toast.LENGTH_LONG).show();
        //JsonFileWriter JsonFileWriter = new JsonFileWriter(pathToInternalStorage, tempAddress1, tempAddress2, tempCity, tempState, tempZip);
    }

    public void setDefaultAddressInformation() {
        AddressPreferences addressPreference = new AddressPreferences();
        SharedPreferences tempSettings = getSharedPreferences("LAWNHIRO_ADDRESS_PREFERENCES", MODE_PRIVATE);
        addressPreference.setAddressPreferences(tempSettings, tempAddress1, tempAddress2, tempCity, tempState, tempZip);
    }

}
