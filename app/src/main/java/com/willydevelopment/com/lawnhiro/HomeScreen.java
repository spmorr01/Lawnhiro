package com.willydevelopment.com.lawnhiro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
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

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
    private String tempFinalAddress;

    private String addressParams;
    private String cityStateZipParams;

    private int lotSize;
    private int finishedSize;
    private int mowableSize;
    private int finalPrice;
    private int extraSqFt;
    private double priceModifier;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;


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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :"
                        //+ nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    //System.out.println("lotSizeSqFt "
                    //+ eElement.getAttribute("lotSizeSqFt"));

                    /*const lotSize = results.lotSizeSqFt;
                    const houseSize = results.finishedSqFt;
                    const mowableSizeSqFt = lotSize - houseSize;
                    -      let price = 25;
                    +      let price = "contact us for price";
                    +
                            +      if ( mowableSizeSqFt> 500 && mowableSizeSqFt< 2000 )
                        +        {price = 25}
                                +      else if ( mowableSizeSqFt >= 2000 && mowableSizeSqFt < 3000 )
                        +        {price = 27}
                                +      else if ( mowableSizeSqFt >= 3000 && mowableSizeSqFt < 4000)
                        +        {price = 30}
                                +      else if ( mowableSizeSqFt >= 4000 && mowableSizeSqFt < 5000)
                        +        {price = 33}
                                +      else if ( mowableSizeSqFt >= 5000 && mowableSizeSqFt < 6000)
                        +        {price = 36}
                                +      else if ( mowableSizeSqFt >= 6000 && mowableSizeSqFt < 7000)
                        +        {price = 39}
                                +      else if ( mowableSizeSqFt >= 7000 && mowableSizeSqFt < 8000)
                        +        {price = 41}
                                +      else if ( mowableSizeSqFt >= 8000 && mowableSizeSqFt < 9000)
                        +        {price = 43}
                                +      else if ( mowableSizeSqFt >= 9000 && mowableSizeSqFt < 10000)
                        +        {price = 45}
                                +      else if ( mowableSizeSqFt >= 10000 && mowableSizeSqFt < 11000)
                        +        {price = 47}
                                +      else if ( mowableSizeSqFt >= 11000 && mowableSizeSqFt < 13000)
                        +        {price = 49}
                                +      else if ( mowableSizeSqFt >= 13000 && mowableSizeSqFt < 15000)
                        +        {price = 54}
                                +      else if ( mowableSizeSqFt >= 15000 && mowableSizeSqFt < 17000)
                        +        {price = 58}
                                +      else if ( mowableSizeSqFt >= 17000 && mowableSizeSqFt < 19000)
                        +        {price = 62}
                                +      else if ( mowableSizeSqFt >= 19000 && mowableSizeSqFt < 21000)
                        +        {price = 67}
                                +

                                -      const extraSqFt = mowableSizeSqFt - 3000;
                    -      const priceModifier = Math.floor(extraSqFt / 2000);

                    -      if (priceModifier > 0) {
                        -        price += priceModifier * 5; // add $5
                        -      }

                    res.json({price, id: priceMap[price]});
                }).*/


                    lotSize = Integer.parseInt(eElement.getElementsByTagName("lotSizeSqFt").item(0).getTextContent());
                    finishedSize = Integer.parseInt(eElement.getElementsByTagName("finishedSqFt").item(0).getTextContent());
                    mowableSize = lotSize - finishedSize;
                    finalPrice = 25;

                    if ( mowableSize> 500 && mowableSize< 2000 )
                                {finalPrice = 25;}
                    else if ( mowableSize >= 2000 && mowableSize < 3000 )
                                {finalPrice = 27;}
                                      else if ( mowableSize >= 3000 && mowableSize < 4000)
                                {finalPrice = 30;}
                                      else if ( mowableSize >= 4000 && mowableSize < 5000)
                                {finalPrice = 33;}
                                      else if ( mowableSize >= 5000 && mowableSize < 6000)
                                {finalPrice = 36;}
                                      else if ( mowableSize >= 6000 && mowableSize < 7000)
                                {finalPrice = 39;}
                                      else if ( mowableSize >= 7000 && mowableSize < 8000)
                                {finalPrice = 41;}
                                      else if ( mowableSize >= 8000 && mowableSize < 9000)
                                {finalPrice = 43;}
                                      else if ( mowableSize >= 9000 && mowableSize < 10000)
                                {finalPrice = 45;}
                                     else if ( mowableSize >= 10000 && mowableSize < 11000)
                                {finalPrice = 47;}
                                      else if ( mowableSize >= 11000 && mowableSize < 13000)
                                {finalPrice = 49;}
                                      else if ( mowableSize >= 13000 && mowableSize < 15000)
                                {finalPrice = 54;}
                                      else if ( mowableSize >= 15000 && mowableSize < 17000)
                                {finalPrice = 58;}
                                     else if ( mowableSize >= 17000 && mowableSize < 19000)
                                {finalPrice = 62;}
                                      else if ( mowableSize >= 19000 && mowableSize < 21000)
                                {finalPrice = 67;}

                    extraSqFt = mowableSize - 3000;
                    priceModifier = Math.floor(extraSqFt / 2000);

                    if (priceModifier > 0) {
                        finalPrice += priceModifier * 5; // add $5
                        }

                    price.setText("$" + finalPrice);
                }
            }
            /*nodes = doc.getElementsByTagName("result");
            //Log.println(1, "Nodes", nodes.toString());
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                NodeList title = element.getElementsByTagName("lotSizeSqFt");
                Element line = (Element) title.item(0);
                lotSize.add(line.getTextContent());
                price.setText(lotSize.get(0).toString());
            }*/
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        /*try {
            URL url = new URL(
                    "http://www.zillow.com/webservice/GetDeepSearchResults.htm?zws-id=X1-ZWz1fafu2i84y3_1hdll&address=2735%20S%2037th%20Street&citystatezip=Lincoln+NE+68506");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("result");
*//** Assign textview array lenght by arraylist size *//*
            //name = new TextView[nodeList.getLength()];
            //website = new TextView[nodeList.getLength()];
            //category = new TextView[nodeList.getLength()];
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                //name[i] = new TextView(this);
                //website[i] = new TextView(this);
                //category[i] = new TextView(this);
                Element lotSizeElement = (Element) node;
                NodeList nameList = lotSizeElement.getElementsByTagName("lotSizeSqFt");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                name[i].setText("Name = "
                        + ((Node) nameList.item(0)).getNodeValue());
                NodeList websiteList = fstElmnt.getElementsByTagName("website");
                Element websiteElement = (Element) websiteList.item(0);
                websiteList = websiteElement.getChildNodes();
                website[i].setText("Website = "
                        + ((Node) websiteList.item(0)).getNodeValue());
                category[i].setText("Website Category = "
                        + websiteElement.getAttribute("category"));
                layout.addView(name[i]);
                layout.addView(website[i]);
                layout.addView(category[i]);
            }
        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
        }
*/


        //price.setText(lotSize.get(0).toString());
        //price.setText("$50");
        //price.setText(nodes.getLength());

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Accept",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String addressBodyText;
                                String priceText;
                                String orderNotesText;

                                addressBodyText = finalAddress.getText().toString();
                                priceText = price.getText().toString();
                                orderNotesText = orderNotes.getText().toString();

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
                                        Toast.makeText(HomeScreen.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(HomeScreen.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                                    Log.e("HomeScreen", "Could not send email", e);
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

    public void acceptPriceButtonClick(View view) {


    }


}
