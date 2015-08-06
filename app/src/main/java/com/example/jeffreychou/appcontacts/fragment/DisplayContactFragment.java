package com.example.jeffreychou.appcontacts.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jeffreychou.appcontacts.R;
import com.example.jeffreychou.appcontacts.database.DatabaseHelper;
import com.example.jeffreychou.appcontacts.listener.FragmentTransitListener;
import com.example.jeffreychou.appcontacts.provider.ContactProvider;
import com.example.jeffreychou.appcontacts.support.AppConstant;

/**
 * Created by jeffreychou on 7/29/15.
 */
public class DisplayContactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Button.OnClickListener{


    private FragmentTransitListener mCallback;

    private Button mBtnClose, mBtnRandom;
    private TextView mTextView;

    private long userId, randomId;
    private int tableSize;
    private boolean randomAddress=true;
    private Bundle args;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            mCallback=(FragmentTransitListener)activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FragmentTransitListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
        // use loader to query data from database in background thread
        getLoaderManager().initLoader(0, args, this);  // 2nd param:Bundle
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_display_contacts, container, false);

        mBtnRandom=(Button)v.findViewById(R.id.btnRandom);
        mBtnClose=(Button)v.findViewById(R.id.btnClose);
        mBtnRandom.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);

        mTextView=(TextView)v.findViewById(R.id.tvDisplay);
        return v;
    }


    // --- Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // TODO -- load specific data with userId
        if (args != null) {
            userId = args.getLong(DatabaseHelper.COLUMN_USER_ID);
            tableSize=args.getInt(DatabaseHelper.TABLE_USER);
        }


        Uri data;
        if(!randomAddress){
            randomId= getRandomId(tableSize);
            Log.d("randomAddress",  "randomId= " + randomId + "\n" + "tableSize=" + tableSize);
            data= ContentUris.withAppendedId(ContactProvider.CONTENT_URI,randomId);
        }else{
            data= ContentUris.withAppendedId(ContactProvider.CONTENT_URI,userId);
        }

        return new CursorLoader(getActivity(),data,null, null, null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // TODO -- use the Cursor to populate TextView
        String result=
                data.getString( data.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FIRSTNAME))+" "
                +data.getString(data.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_SURNAME))+"\n"
                +data.getString(data.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE))+"\n"
                +data.getString(data.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_EMAIL))
                ;
        mTextView.setText(result);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //
    }

    // --- Button Click Listener
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnClose:{
                // TODO -- go to ListContactFragment
                mCallback.onTransition(AppConstant.TAG_DISPLAY_CONTACT_FRAGMENT);
                break;
            }
            case R.id.btnRandom:{
                // TODO -- refresh dada by random number
                randomAddress=false;
                getLoaderManager().restartLoader(0,args,this);
                break;
            }
        }
    }

    // --- produce randomId
    public long getRandomId(int number){
        int randomNum= (int)(Math.random()*100);
        long rst=(long) randomNum %(number+1);
        return rst==0L? rst+1:rst;
    }
}
