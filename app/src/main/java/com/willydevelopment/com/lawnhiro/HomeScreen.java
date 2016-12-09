package com.willydevelopment.com.lawnhiro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity {

    final Context context = this;
    private Button button;
    private EditText finalAddress;
    /*private EditText address1;
    private EditText address2;
    private EditText city;
    private EditText state;
    private EditText zip;*/

    private String tempAddress1;
    private String tempAddress2;
    private String tempCity;
    private String tempState;
    private String tempZip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        finalAddress = (EditText) findViewById(R.id.textFinalAddress);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        /*address1 = (EditText) findViewById(R.id.textDialogAddress1);
        address2 = (EditText) findViewById(R.id.textDialogAddress2);
        city = (EditText) findViewById(R.id.textDialogCity);
        state = (EditText) findViewById(R.id.textDialogState);
        zip = (EditText) findViewById(R.id.textDialogZip);*/
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            //}
        //});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void getAddressTextViewClick(View view) {
// get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.address_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText address1 = (EditText) promptsView
                .findViewById(R.id.textDialogAddress1);
        final EditText address2 = (EditText) promptsView
                .findViewById(R.id.textDialogAddress2);
        final EditText city = (EditText) promptsView
                .findViewById(R.id.textDialogCity);
        final EditText state = (EditText) promptsView
                .findViewById(R.id.textDialogState);
        final EditText zip = (EditText) promptsView
                .findViewById(R.id.textDialogZip);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                tempAddress1 = address1.getText().toString();
                                tempAddress2 = address2.getText().toString();
                                tempCity = city.getText().toString();
                                tempState = state.getText().toString();
                                tempZip = zip.getText().toString();

                                finalAddress.setText(tempAddress1 + ", " + tempAddress2 + ", "
                                + tempCity + ", " + tempState + ", " + tempZip);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void getPriceButtonClick (View view) {
        String addressBodyText;

            addressBodyText = finalAddress.getText().toString();

            Email m = new Email("spencermorris22@gmail.com", "sp00k1obli182$");
            String[] toArray = {"jj_morris10@hotmail.com"};
            m.setTo(toArray);
            m.setFrom("test@lawnhiro.com");
            m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
            m.setBody(addressBodyText);

            try {

                if(m.send()) {
                    Toast.makeText(HomeScreen.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HomeScreen.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e) {
                //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                Log.e("HomeScreen", "Could not send email", e);
            }




    }
}
