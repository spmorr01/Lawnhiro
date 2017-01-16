package com.willydevelopment.com.lawnhiro;

import android.content.Context;
import android.text.TextUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by JJ on 1/16/17.
 */
public class ZillowCaller {
    private NodeList nList;
    private String addressParams;
    private String cityStateZipParams;

    public NodeList GetZillowAPIData(Context context, String tempAddress1, String tempAddress2, String tempCity,
                                     String tempState, String tempZip) {
        try {
            if (TextUtils.isEmpty(tempAddress2)) {
                addressParams = tempAddress1;
            } else {
                addressParams = tempAddress1 + ", " + tempAddress2 + ", ";
            }
            cityStateZipParams = tempCity + ", " + tempState + " " + tempZip;

            URL url = new URL("http://www.zillow.com/webservice/GetDeepSearchResults.htm?zws-id=" + context.getString(R.string.zillow_api_key) + "&address=" + URLEncoder.encode(addressParams, "UTF-8") + "&citystatezip=" + URLEncoder.encode(cityStateZipParams, "UTF-8"));

            URLConnection conn = url.openConnection(); //add citystatezip parameter!!!!

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());

            doc.getDocumentElement().normalize();
            //System.out.println("Root element :"
            //+ doc.getDocumentElement().getNodeName());


            nList = doc.getElementsByTagName("result");
        }  catch (Exception e) {
        e.printStackTrace();
    }
        return nList;
    }
}
