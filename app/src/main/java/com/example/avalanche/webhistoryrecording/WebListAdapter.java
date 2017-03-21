package com.example.avalanche.webhistoryrecording;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avalanche on 2/2/17.
 */
public class WebListAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private List<SpyApp> webList;

    public WebListAdapter(Context context, ArrayList<SpyApp> webList) {
        this.context = context;
        this.webList = webList;
    }

    @Override
    public int getCount() {
        return webList.size();
    }

    @Override
    public Object getItem(int location) {
        if(webList.size() == 0) {
            return null;
        }
        return webList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.web_listrow, null);


        TextView txt_title = (TextView) convertView.findViewById(R.id.textview_title);

        //  TextView itemPrice = (TextView) convertView.findViewById(R.id.textView_ProductPrice);

        //final ImageView toggleButton_favourate= (ImageView) convertView.findViewById(R.id.imageview_favorate);

        final SpyApp obj = webList.get(position);

        txt_title.setText(obj.getTitle());
        //itemPrice.setText(obj.getItemPrice());

        return convertView;

    }


}
