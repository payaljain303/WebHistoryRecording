package com.example.avalanche.webhistoryrecording;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, FetchDataListener {
    CheckBox checkBox_WebFilter;
    SampleAlarmReceiver alarm = new SampleAlarmReceiver();
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String WEB_FILTER = "webkey";
    ListView listView_webSearch;
    public static ArrayList<SpyApp> webList = new ArrayList<SpyApp>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBox_WebFilter= (CheckBox) findViewById(R.id.checkbox_webFilter);
        listView_webSearch= (ListView) findViewById(R.id.listview_websearch);
        checkBox_WebFilter.setOnClickListener(this);

        SharedPreferences share = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String value = (share.getString(WEB_FILTER, ""));
        Log.e("value ",value);
        if (value.equals("checked")){
            System.out.println("value Checked");
            checkBox_WebFilter.setChecked(true);
        }else{
            checkBox_WebFilter.setChecked(false);
        }

        String IP=Utils.getIPAddress(true); // IPv4
         Log.e("IP ",IP);
        Utils.getIPAddress(false); // IPv6

        AsyncTaskClass task1 = new AsyncTaskClass(this);
        task1.execute("Fetch Web Info","9990000000");

        /*String[] proj = new String[] { Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL,Browser.BookmarkColumns.CREATED };
        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history, 1 = bookmark
        //Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI, proj, "1485837954647", null, null);
        Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI, null,Browser.BookmarkColumns.CREATED + ">1485837954647", null, null);
        if(mCur != null) {
            System.out.println("cursor value"+String.valueOf(mCur.getCount()));
        }

        mCur.moveToFirst();
        @SuppressWarnings("unused")
        String title = "";
        @SuppressWarnings("unused")
        String url = "";
        @SuppressWarnings("unused")
        String created = "";

        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            boolean cont = true;
            while (mCur.isAfterLast() == false && cont) {
                title = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.TITLE));
                url = mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.URL));
                created=mCur.getString(mCur.getColumnIndex(Browser.BookmarkColumns.CREATED));
                // Do something with title and url
                mCur.moveToNext();
                System.out.println("Bookmark "+mCur);
                Log.e("Bookmark ", title+" "+url+" "+created);
               // System.out.println(getDate(Long.parseLong(created), "dd/MM/yyyy hh:mm:ss.SSS"));
            }
        }*/

    }

    @Override
    public void onClick(View v) {
        if (checkBox_WebFilter.isChecked()){
            SharedPreferences share_loginAs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor edit_loginAs = share_loginAs.edit();
            edit_loginAs.putString(WEB_FILTER, "checked");
            edit_loginAs.commit();

              //if (hasRWPermission(MainActivity.this)) {

              alarm.setAlarm(this);

              //}else{
              //  requestRWPermission(MainActivity.this);
              //}

        }else{
            SharedPreferences share_loginAs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor edit_loginAs = share_loginAs.edit();
            edit_loginAs.putString(WEB_FILTER, "unchecked");
            edit_loginAs.commit();
            alarm.cancelAlarm(this);
        }
    }

    @Override
    public void onFetchComplete_WebList(ArrayList<SpyApp> data) {

        Log.e("Fetch Array value", String.valueOf(data.size()));
        webList=data;
        System.out.println("WebList "+webList.size());
        WebListAdapter mAdapter = new WebListAdapter(getApplicationContext(), webList);
        listView_webSearch.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

   /* public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }*/

}
