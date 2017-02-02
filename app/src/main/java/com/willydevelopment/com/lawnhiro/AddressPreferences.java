package com.willydevelopment.com.lawnhiro;

import android.content.SharedPreferences;

/**
 * Created by JJ on 1/8/17.
 */
public class AddressPreferences {
    public String[] getAddressPreferences(SharedPreferences tempSettings) {
        String resultArray[] = new String [7];

        resultArray[0] = tempSettings.getString("Name", "");
        resultArray[1] = tempSettings.getString("Email", "");
        resultArray[2] = tempSettings.getString("Address1", "");
        resultArray[3] = tempSettings.getString("Address2", "");
        resultArray[4] = tempSettings.getString("City", "");
        resultArray[5] = tempSettings.getString("State", "");
        resultArray[6] = tempSettings.getString("Zip", "");
        return resultArray;
    }

    public void setAddressPreferences(SharedPreferences tempSettings, String tempName, String tempEmail, String tempAddress1, String tempAddress2, String tempCity,
                                      String tempState, String tempZip) {
        //SharedPreferences tempSettings = Context.getSharedPreferences("LAWNHIRO_ADDRESS_PREFERENCES", MODE_PRIVATE);   //getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editSettings = tempSettings.edit();
        //editSettings.clear();
        editSettings.putString("Name", tempName);
        editSettings.putString("Email", tempEmail);
        editSettings.putString("Address1", tempAddress1); //Address1.getText().toString();
        editSettings.putString("Address2", tempAddress2);
        editSettings.putString("City", tempCity);
        editSettings.putString("State", tempState);
        editSettings.putString("Zip", tempZip);

        editSettings.apply();
    }
}


