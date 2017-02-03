package com.willydevelopment.com.lawnhiro;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by JJ on 2/2/17.
 */
public class AddOrder {
    private boolean addSuccess;

    public boolean addOrderToLawnhiro(String webApiHttp, String tempName, String tempEmail, String tempAddress1, String tempAddress2,
                                        String tempCity, String tempState, String tempZip, int mowableSize, int finalPrice, String orderNotes) {



        StringBuilder sb = new StringBuilder();

        //String webApiHttp = String.valueOf(R.string.mobile_api_url);


        HttpURLConnection urlConnection=null;
        try {
            addSuccess = false;
            URL url = new URL(webApiHttp);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            //urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
            urlConnection.connect();

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("BusinessSource", "Facebook");
            jsonParam.put("PayPalOrderID", "PAY-123123");
            jsonParam.put("Name", tempName);
            jsonParam.put("Email", tempEmail);
            jsonParam.put("Address1", tempAddress1);
            jsonParam.put("Address2", tempAddress2);
            jsonParam.put("City", tempCity);
            jsonParam.put("State", tempState);
            jsonParam.put("Zip", tempZip);
            jsonParam.put("ServiceType", "Lawnhiro");
            jsonParam.put("CalculationResultArea", mowableSize); //Area here
            jsonParam.put("CalculationResultPrice", finalPrice); //Price here
            jsonParam.put("CustomerNotes", orderNotes);

            OutputStreamWriter out = new   OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonParam.toString());
            out.close();

            int HttpResult =urlConnection.getResponseCode();

            if(HttpResult == HttpURLConnection.HTTP_OK){
                addSuccess = true;

            }else{
                addSuccess = false;
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(urlConnection!=null)
                urlConnection.disconnect();
        }


        return addSuccess;
    }
}
