package com.willydevelopment.com.lawnhiro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;

public class HomeScreen extends AppCompatActivity {

    final Context context = this;
    private EditText finalAddress;

    private String tempName;
    private String tempEmail;
    private String tempAddress1;
    private String tempAddress2;
    private String tempCity;
    private String tempState;
    private String tempZip;

    private int lotSize;
    private int finishedSize;
    private int mowableSize;
    private int finalPrice;

    private String priceText;
    private String addressBodyText;
    private String orderNotesText;
    private String businessSourceSelection;

    private String payPalOrderID;

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

            .clientId("AeL5Z6IirMijkry6LzbZ8aS9E47B0AH2tHizjdJxrvMprG6X93w7w5I1zjJYQsOkYKzF0ZWLt5CcpkJ-");

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



        getUserSettings();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
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
            //Toast.makeText(HomeScreen.this, "Email was sent successfully.", Toast.LENGTH_LONG).show(); //Add settings layout item here
            Intent i = new Intent(HomeScreen.this, Settings.class);
            startActivityForResult(i, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            Toast.makeText(HomeScreen.this, "Settings set successfully!", Toast.LENGTH_LONG).show();
            getUserSettings();
        }

        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    payPalOrderID = confirm.toJSONObject().getJSONObject("response").getString("id");
                    String responseState = confirm.toJSONObject().getJSONObject("response").getString("state");
                    if (responseState.equals("approved")) {
                        Toast.makeText(HomeScreen.this, "Payment successful", Toast.LENGTH_LONG).show();
                        // TODO: send 'confirm' to your server for verification.
                        // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                        // for more details.
                        AddOrder addOrder = new AddOrder();
                        boolean orderAdded = addOrder.addOrderToLawnhiro(context.getString(R.string.mobile_api_url), businessSourceSelection, payPalOrderID, tempName, tempEmail, tempAddress1, tempAddress2, tempCity,
                                tempState, tempZip, mowableSize, finalPrice, "Lawnhiro", orderNotesText);

                        if (orderAdded) {
                            Toast.makeText(HomeScreen.this, "Order added successfully!", Toast.LENGTH_LONG).show();
                        }

                        Email m = new Email("order.lawnhiro@gmail.com", "0rd3rL@WN");
                        String[] toArray = {"jj_morris10@hotmail.com"};
                        m.setTo(toArray);
                        m.setFrom("test@lawnhiro.com");
                        m.setSubject("New Lawnhiro Order!");
                        m.setBody("Address: " + addressBodyText + "\n"
                                + "Accepted Price: " + priceText + "\n"
                                + "Order Notes: " + orderNotesText);

                        try {
                            if (m.send()) {
                                //Toast.makeText(HomeScreen.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(HomeScreen.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("email", "email didn't send: ", e);
                            Toast.makeText(HomeScreen.this, "There was a problem sending the email. Please contact Lawnhiro.", Toast.LENGTH_LONG).show();
                        }

                        NotificationCompat.Builder nBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Lawnhiro")
                                .setContentText("Your order has been sent successfully");

                        NotificationManager nNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        nNotificationManager.notify(1, nBuilder.build());
                        //NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    } else {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                        dlgAlert.setMessage("Something went wrong when trying to confirm your PayPal payment. Please try again or contact " +
                                "Lawnhiro for support.");
                        dlgAlert.setTitle("Error Confirming Payment");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.create().show();

                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                    }
                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (requestCode == 0 && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(HomeScreen.this, "Payment cancelled.", Toast.LENGTH_LONG).show();
            Log.i("paymentExample", "The user canceled.");
        } else if (requestCode == 0 && resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(HomeScreen.this, "Payment was invalid. Please try again.", Toast.LENGTH_LONG).show();
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }

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
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                tempAddress1 = address1.getText().toString();
                                tempAddress2 = address2.getText().toString();
                                tempCity = city.getText().toString();
                                tempState = state.getText().toString();
                                tempZip = zip.getText().toString();

                                if (TextUtils.isEmpty(tempAddress2)) {
                                    finalAddress.setText(tempAddress1 + ", "
                                            + tempCity + ", " + tempState + " " + tempZip);
                                } else {
                                    finalAddress.setText(tempAddress1 + ", " + tempAddress2 + ", "
                                            + tempCity + ", " + tempState + " " + tempZip);
                                }


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    public void getPriceButtonClick(View view) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.price_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText orderNotes = (EditText) promptsView
                .findViewById(R.id.textNotes);

        final TextView price = (TextView) promptsView
                .findViewById(R.id.textPrice);

        final Spinner businessSource = (Spinner) promptsView
                .findViewById(R.id.businessSource);

        PriceCalculator priceCalculator = new PriceCalculator();
        ZillowCaller zillowCaller = new ZillowCaller();

        NodeList nodeList = zillowCaller.GetZillowAPIData(context, tempAddress1, tempAddress2, tempCity, tempState, tempZip);

        if (nodeList.getLength() == 0) {
            Toast.makeText(HomeScreen.this, "Unable to resolve address information. Please try again!", Toast.LENGTH_LONG).show();
        } else {
            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node nNode = nodeList.item(temp);
                //System.out.println("\nCurrent Element :"
                //+ nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    lotSize = Integer.parseInt(eElement.getElementsByTagName("lotSizeSqFt").item(0).getTextContent());
                    finishedSize = Integer.parseInt(eElement.getElementsByTagName("finishedSqFt").item(0).getTextContent());

                }
            }


            mowableSize = lotSize - finishedSize;
            finalPrice = priceCalculator.CalculatePrice(mowableSize);
            price.setText("$" + finalPrice);

            alertDialogBuilder.setCancelable(true);

            // create alert dialog
            final AlertDialog alertDialog = alertDialogBuilder.create();

            final Button paypalButton = (Button) promptsView.findViewById(R.id.paypalButton);
            paypalButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent paypalIntent = new Intent(HomeScreen.this, PayPalService.class); //move this to where we actually  use paypal

                    paypalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                    startService(paypalIntent);

                    addressBodyText = finalAddress.getText().toString();
                    priceText = price.getText().toString();
                    orderNotesText = orderNotes.getText().toString();
                    businessSourceSelection = businessSource.getSelectedItem().toString();

                    PayPalPayment payment = new PayPalPayment(new BigDecimal(finalPrice), "USD", "Lawnhiro Order",
                            PayPalPayment.PAYMENT_INTENT_SALE);

                    Intent intent = new Intent(v.getContext(), PaymentActivity.class);

                    // send the same configuration for restart resiliency
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                    startActivityForResult(intent, 0);

                    alertDialog.dismiss();

                }
            });

            // show it
            alertDialog.show();
        }
}

    private void getUserSettings() {
        final AddressPreferences addressPreference = new AddressPreferences();
        final SharedPreferences tempSettings = getSharedPreferences("LAWNHIRO_ADDRESS_PREFERENCES", MODE_PRIVATE);
        String addressArray[];// = new String[5];
        addressArray = addressPreference.getAddressPreferences(tempSettings);

        tempName = addressArray[0];
        tempEmail = addressArray[1];
        tempAddress1 = addressArray[2]; //settings.getString("Address1", "");
        tempAddress2 = addressArray[3]; //settings.getString("Address2", "");
        tempCity = addressArray[4]; //settings.getString("City", "");
        tempState = addressArray[5]; //settings.getString("State", "");
        tempZip = addressArray[6]; //settings.getString("Zip", "");

        if (TextUtils.isEmpty(tempName) && TextUtils.isEmpty(tempEmail) && TextUtils.isEmpty(tempAddress1) && TextUtils.isEmpty(tempAddress2) && TextUtils.isEmpty(tempCity)
                && TextUtils.isEmpty(tempState) && TextUtils.isEmpty(tempZip)) {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.set_default_address_prompt, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText name = (EditText) promptsView
                    .findViewById(R.id.textDialogName);
            final EditText email = (EditText) promptsView
                    .findViewById(R.id.textDialogEmail);
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
                                public void onClick(DialogInterface dialog, int id) {
                                    // get user input and set it to result
                                    // edit text
                                    tempName = name.getText().toString();
                                    tempEmail = email.getText().toString();
                                    tempAddress1 = address1.getText().toString();
                                    tempAddress2 = address2.getText().toString();
                                    tempCity = city.getText().toString();
                                    tempState = state.getText().toString();
                                    tempZip = zip.getText().toString();

                                    addressPreference.setAddressPreferences(tempSettings, tempName, tempEmail, tempAddress1, tempAddress2, tempCity, tempState, tempZip);

                                    Toast.makeText(HomeScreen.this, "Default address saved!", Toast.LENGTH_LONG).show();
                                    getUserSettings();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }

        else if (TextUtils.isEmpty(tempAddress2)) {
            finalAddress.setText(tempAddress1 + ", "
                    + tempCity + ", " + tempState + " " + tempZip);
        } else {
            finalAddress.setText(tempAddress1 + ", " + tempAddress2 + ", "
                    + tempCity + ", " + tempState + " " + tempZip);
        }
    }
}
