package com.list.shopping.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.list.shopping.R;
import com.list.shopping.adapters.CustomAdapter;
import com.list.shopping.database.DatabaseHelper;
import com.list.shopping.database.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroceryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroceryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroceryFragment extends Fragment {

    private static final String TAG = GroceryFragment.class.getName();

    ListView listGrocery;
    DatabaseHelper db;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GroceryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GroceryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroceryFragment newInstance() {
        GroceryFragment fragment = new GroceryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = DatabaseHelper.getInstance(container.getContext());
        return inflater.inflate(R.layout.fragment_grocery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        listGrocery = (ListView) view.findViewById(R.id.listGrocery);

        List<User> T_users = db.getAllUsers();

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        for (User user : T_users){
            HashMap<String, String> hashMap = new HashMap<>();//create a hashmap to store the data in key value pair
            hashMap.put("login", user.login);
            hashMap.put("lastName", user.lastName);
            arrayList.add(hashMap);//add the hashmap into arrayList
        }


        String[] from = {"login", "lastName"};//string array
        int[] to = {R.id.textViewListLogin, R.id.textViewListName};//int array of views id's
        CustomAdapter simpleAdapter = new CustomAdapter(this.getContext(), arrayList, android.R.layout.list_content, from, to);//Create object and set the parameters for simpleAdapter
        listGrocery.setAdapter(simpleAdapter);//sets the adapter for listView

        //perform listView item click event
        listGrocery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "Item selected : " + adapterView.getItemAtPosition(i));
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @OnItemClick(R.id.listGrocery)
    public void onGridGroceryListener(int position){
        Log.d(TAG, "Click on grid !!!! = " + position);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            DatabaseHelper db = DatabaseHelper.getInstance(context);

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
