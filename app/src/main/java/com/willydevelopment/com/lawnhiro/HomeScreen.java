package com.willydevelopment.com.lawnhiro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
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
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class HomeScreen extends AppCompatActivity {

    final Context context = this;
    private Button button;
    private EditText finalAddress;
    //private EditText orderNotes;
    //private TextView price;       //FINISHING ADDING ALL OF THESE AND CLEANING UP OTHER CODE!
    /*private EditText address1;
    private EditText address2;
    private EditText city;
    private EditText state;
    private EditText zip;*/


    private File pathToInternalStorage;


    private String jsonOutput;
    private Boolean settingsSet;
    private MediaScannerConnection msConn;
    private File file;

    private String tempAddress1;
    private String tempAddress2;
    private String tempCity;
    private String tempState;
    private String tempZip;
    private String tempFinalAddress;

    private String addressParams;
    private String cityStateZipParams;

    private int lotSize;
    private int finishedSize;
    private int mowableSize;
    private int finalPrice;
    private int extraSqFt;
    private double priceModifier;
    private String priceText;
    private String addressBodyText;
    private String orderNotesText;

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
        pathToInternalStorage = context.getFilesDir();
        System.out.println("**!!**" + pathToInternalStorage);
        finalAddress = (EditText) findViewById(R.id.textFinalAddress);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent paypalIntent = new Intent(this, PayPalService.class); //move this to where we actually  use paypal

        paypalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(paypalIntent);

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
                    Toast.makeText(HomeScreen.this, "Payment successful", Toast.LENGTH_LONG).show();
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.


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
                        Toast.makeText(HomeScreen.this, "There was a problem sending the email Please contact Lawnhiro.", Toast.LENGTH_LONG).show();
                    }

                    NotificationCompat.Builder nBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Lawnhiro")
                            .setContentText("Your order has been sent successfully");

                    NotificationManager nNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nNotificationManager.notify(1, nBuilder.build());
                    //NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
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


        try {
            if (TextUtils.isEmpty(tempAddress2)) {
                addressParams = tempAddress1;
            } else {
                addressParams = tempAddress1 + ", " + tempAddress2 + ", ";
            }
            cityStateZipParams = tempCity + ", " + tempState + " " + tempZip;

            URL url = new URL("http://www.zillow.com/webservice/GetDeepSearchResults.htm?zws-id=" + getString(R.string.zillow_api_key) + "&address=" + URLEncoder.encode(addressParams, "UTF-8") + "&citystatezip=" + URLEncoder.encode(cityStateZipParams, "UTF-8"));

            URLConnection conn = url.openConnection(); //add citystatezip parameter!!!!

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());

            doc.getDocumentElement().normalize();
            //System.out.println("Root element :"
            //+ doc.getDocumentElement().getNodeName());


            NodeList nList = doc.getElementsByTagName("result");

        //System.out.println(nList.getLength());
        //System.out.println("----------------------------");
        if (nList.getLength() == 0) {
            Toast.makeText(HomeScreen.this, "Unable to resolve address information. Please try again!", Toast.LENGTH_LONG).show();
        } else {
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :"
                //+ nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    lotSize = Integer.parseInt(eElement.getElementsByTagName("lotSizeSqFt").item(0).getTextContent());
                    finishedSize = Integer.parseInt(eElement.getElementsByTagName("finishedSqFt").item(0).getTextContent());

                }
            }


            mowableSize = lotSize - finishedSize;
            finalPrice = 25;

            if (mowableSize > 500 && mowableSize < 2000) {
                finalPrice = 25;
            } else if (mowableSize >= 2000 && mowableSize < 3000) {
                finalPrice = 27;
            } else if (mowableSize >= 3000 && mowableSize < 4000) {
                finalPrice = 30;
            } else if (mowableSize >= 4000 && mowableSize < 5000) {
                finalPrice = 33;
            } else if (mowableSize >= 5000 && mowableSize < 6000) {
                finalPrice = 36;
            } else if (mowableSize >= 6000 && mowableSize < 7000) {
                finalPrice = 39;
            } else if (mowableSize >= 7000 && mowableSize < 8000) {
                finalPrice = 41;
            } else if (mowableSize >= 8000 && mowableSize < 9000) {
                finalPrice = 43;
            } else if (mowableSize >= 9000 && mowableSize < 10000) {
                finalPrice = 45;
            } else if (mowableSize >= 10000 && mowableSize < 11000) {
                finalPrice = 47;
            } else if (mowableSize >= 11000 && mowableSize < 13000) {
                finalPrice = 49;
            } else if (mowableSize >= 13000 && mowableSize < 15000) {
                finalPrice = 54;
            } else if (mowableSize >= 15000 && mowableSize < 17000) {
                finalPrice = 58;
            } else if (mowableSize >= 17000 && mowableSize < 19000) {
                finalPrice = 62;
            } else if (mowableSize >= 19000 && mowableSize < 21000) {
                finalPrice = 67;
            }

            extraSqFt = mowableSize - 3000;
            priceModifier = Math.floor(extraSqFt / 2000);

            if (priceModifier > 0) {
                finalPrice += priceModifier * 5; // add $5
            }
            price.setText("$" + finalPrice);





        alertDialogBuilder.setCancelable(true);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final Button paypalButton = (Button) promptsView.findViewById(R.id.paypalButton);
        paypalButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                addressBodyText = finalAddress.getText().toString();
                priceText = price.getText().toString();
                orderNotesText = orderNotes.getText().toString();

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
        } catch (Exception e) {
            e.printStackTrace();
        }

}

    private void getUserSettings() {
        final AddressPreferences addressPreference = new AddressPreferences();
        final SharedPreferences tempSettings = getSharedPreferences("LAWNHIRO_ADDRESS_PREFERENCES", MODE_PRIVATE);
        String addressArray[];// = new String[5];
        addressArray = addressPreference.getAddressPreferences(tempSettings);

        tempAddress1 = addressArray[0]; //settings.getString("Address1", "");
        tempAddress2 = addressArray[1]; //settings.getString("Address2", "");
        tempCity = addressArray[2]; //settings.getString("City", "");
        tempState = addressArray[3]; //settings.getString("State", "");
        tempZip = addressArray[4]; //settings.getString("Zip", "");

        if (TextUtils.isEmpty(tempAddress1) && TextUtils.isEmpty(tempAddress2) && TextUtils.isEmpty(tempCity)
                && TextUtils.isEmpty(tempState) && TextUtils.isEmpty(tempZip)) {
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.set_default_address_prompt, null);

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

                                    addressPreference.setAddressPreferences(tempSettings, tempAddress1, tempAddress2, tempCity, tempState, tempZip);

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
