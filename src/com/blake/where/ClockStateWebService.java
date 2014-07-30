package com.blake.where;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by blake on 7/12/14.
 */
public class ClockStateWebService {

    //web services methods
    public static int  POST_INSERT_ROW = 0;
    public static int  POST_RETRIEVE_ROWS = 1;
    public static int  GET_RETRIEVE_ROW = 2;
    public static int  POST_RETRIEVE_ROW = 3;
    public static int  POST_RETRIEVE_TYPE_ROW = 4;


    public static String obtainLastEntryType(String workerId) {
        HttpClient httpclient = new DefaultHttpClient();
        String link="http://ineeduneed.com/clockwork/clockwork_status.php";
        HttpPost httppost = new HttpPost(link);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("worker_id", workerId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line="";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        } catch (ClientProtocolException e) {
            return new String("Exception: " + e.getMessage());
        } catch (IOException e) {
            return new String("Exception: " + e.getMessage());
        }
    }


}