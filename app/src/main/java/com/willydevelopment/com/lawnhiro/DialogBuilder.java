package com.willydevelopment.com.lawnhiro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by JJ on 2/4/17.
 */
public class DialogBuilder {

    public void CreateNewStaticDialog(Context context, String tempDialogMessage, String tempDialogTitle, String tempPositiveButtonText, boolean tempCancelable) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(tempDialogMessage);
        dlgAlert.setTitle(tempDialogTitle);
        dlgAlert.setPositiveButton(tempPositiveButtonText, null);
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();

        dlgAlert.setPositiveButton(tempPositiveButtonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    public void Toaster(Context context, String tempToastMessage) {
        Toast.makeText(context, tempToastMessage, Toast.LENGTH_LONG).show();
    }
}
