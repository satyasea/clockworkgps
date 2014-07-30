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
public class RestWebServices {

    //web services methods
    public static int  POST_INSERT_ROW = 0;
    public static int  POST_RETRIEVE_ROWS = 1;
    public static int  GET_RETRIEVE_ROW = 2;
    public static int  POST_RETRIEVE_ROW = 3;
    public static int  POST_RETRIEVE_TYPE_ROW = 4;




    public static String postSelectRowsAsJSONString() {
        String link ="http://ineeduneed.com/clockwork/clockwork_login_list_post2.php";
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);
        try {
            // Execute HTTP Post Request
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
            // TODO Auto-generated catch block
            return new String("Exception: " + e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return new String("Exception: " + e.getMessage());
        }
    }

    //helper method
    //todo: must be modified for punches data query
    public static List<String> getResultsJSONAsList(String result) {
        List<String> dbRows = new ArrayList<String>();
        int id;
        String name;
        String pass;
        String role;
        JSONArray array = null;
        if(result.length()==0){
            return dbRows;
        }
        try {
            array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                id = row.getInt("id");
                name = row.getString("Username");
                pass = row.getString("Password");
                role = row.getString("Role");
                dbRows.add(id + " " + name + " " + pass + " " + role);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }finally{
            return dbRows;
        }
    }



    public static String getData(String user, String pass) {
        String link = "http://ineeduneed.com/clockwork/clockwork_login_get.php?user="
                +user+"&pass="+pass;
        try{
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
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
        }catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }

}