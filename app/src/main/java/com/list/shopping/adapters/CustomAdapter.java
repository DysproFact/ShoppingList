package com.list.shopping.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.list.shopping.R;
import com.list.shopping.database.Item;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * Created by Mathieu on 21/09/2017.
 */

public class CustomAdapter extends SimpleAdapter {
    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String, String>> arrayList;

    @BindView(R.id.textViewListLogin)
    TextView textLogin;
    @BindView(R.id.textViewListName)
    TextView textName;

    public CustomAdapter(Context context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.arrayList = data;
        inflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);


        return view;
    }

}
