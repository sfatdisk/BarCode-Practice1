package com.example.jeffreychou.appcontacts.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jeffreychou.appcontacts.R;
import com.example.jeffreychou.appcontacts.database.DatabaseHelper;
import com.example.jeffreychou.appcontacts.fragment.DisplayContactFragment;
import com.example.jeffreychou.appcontacts.fragment.EditContactFragment;
import com.example.jeffreychou.appcontacts.fragment.ListContactFragment;
import com.example.jeffreychou.appcontacts.listener.FragmentTransitListener;
import com.example.jeffreychou.appcontacts.listener.OnSelectionListener;
import com.example.jeffreychou.appcontacts.provider.ContactProvider;
import com.example.jeffreychou.appcontacts.support.AppConstant;


public class MainActivity extends Activity implements FragmentTransitListener, OnSelectionListener {

    // Default data
    private String[] firstName, surname,phone, email;
    public static final String PREFS_FILE ="PrefsFile";
    // Fragments
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private EditContactFragment    mEditContactFragment;
    private DisplayContactFragment mDisplayContactFragment;
    private ListContactFragment    mListContactFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO- fill out default user information from database
        firstName= getResources().getStringArray(R.array.firstName);
        surname=getResources().getStringArray(R.array.surname);
        phone=getResources().getStringArray(R.array.phone);
        email=getResources().getStringArray(R.array.email);

        SharedPreferences preference = getSharedPreferences(PREFS_FILE,0);
        boolean firstSetUpDefaultData= preference.getBoolean(AppConstant.TAG_DEFAULT_DATA ,true);

        if(firstSetUpDefaultData){ // default == true
            setUpDefaultData();
        }

        // TODO - Handle Fragments
        mFragmentManager= getFragmentManager();

        if( null != savedInstanceState ){
            restoreState(savedInstanceState);
        }else{
            getListContactFragment();
        }
    }


    private void setUpDefaultData(){
        // TODO- setup  DB, create user table, create default data

        String TAG= "insert";
        ContentValues values= new ContentValues();
        for(int i=0;i< firstName.length;++i){
            values.put(DatabaseHelper.COLUMN_USER_FIRSTNAME,firstName[i] );

            values.put(DatabaseHelper.COLUMN_USER_SURNAME, surname[i]);
            values.put(DatabaseHelper.COLUMN_USER_PHONE, phone[i]);
            values.put(DatabaseHelper.COLUMN_USER_EMAIL,email[i] );
            Uri defaultData= getContentResolver().insert(ContactProvider.CONTENT_URI, values);
        }
    }

    private void getListContactFragment(){
        // TODO- Add/Replace initially ListContactFragment to Activity

        mFragmentTransaction= mFragmentManager.beginTransaction();

        mListContactFragment = new ListContactFragment();
        mFragmentTransaction.replace(R.id.fragment_container, mListContactFragment, AppConstant.TAG_LIST_CONTACT_FRAGMENT);

        mFragmentTransaction.commit();
    }

    private void getEditContactFragment(){
        // TODO- Add/Replace EditContactFragment Fragment to Activity

        mFragmentTransaction= mFragmentManager.beginTransaction();

        mEditContactFragment = new EditContactFragment();
        mFragmentTransaction.replace(R.id.fragment_container, mEditContactFragment, AppConstant.TAG_EDIT_CONTACT_FRAGMENT);

        mFragmentTransaction.commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState ){
        // TODO -- Restore saved currently attached Fragment state
        if( null != mListContactFragment ){
            savedInstanceState.putString(AppConstant.TAG_LIST_CONTACT_FRAGMENT, mListContactFragment.getTag() );
        }
        if( null != mEditContactFragment ){
            savedInstanceState.putString(AppConstant.TAG_EDIT_CONTACT_FRAGMENT, mEditContactFragment.getTag() );
        }
        if( null != mDisplayContactFragment){
            savedInstanceState.putString(AppConstant.TAG_DISPLAY_CONTACT_FRAGMENT, mDisplayContactFragment.getTag() );
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    private void restoreState(Bundle savedInstanceState){
        //TODO - Restore saved instance state
        mListContactFragment=
                (ListContactFragment)mFragmentManager.findFragmentByTag( savedInstanceState.getString(AppConstant.TAG_LIST_CONTACT_FRAGMENT) );

        mEditContactFragment=
                (EditContactFragment)mFragmentManager.findFragmentByTag( savedInstanceState.getString(AppConstant.TAG_EDIT_CONTACT_FRAGMENT) );

        mDisplayContactFragment=
                (DisplayContactFragment)mFragmentManager.findFragmentByTag( savedInstanceState.getString(AppConstant.TAG_DISPLAY_CONTACT_FRAGMENT) );
    }

    // --- overflow Menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // --- FragmentListener : Use to transit fragments
    @Override
    public void onTransition(String tag) {

        if( tag.equals(AppConstant.TAG_LIST_CONTACT_FRAGMENT) ){
            // TODO-- go to EditContactFragment
            getEditContactFragment();

        }else if(tag.equals(AppConstant.TAG_EDIT_CONTACT_FRAGMENT)){
            // TODO-- go to ListContactFragment from EditContactFragment
            getListContactFragment();
        }else{ // TAG_DISPLAY_CONTACT_FRAGMENT
            // TODO-- go to ListContactFragment from DisplayContactFragment
            getListContactFragment();
        }
    }

    // --- OnSelectionListener
    @Override
    public void onItemSelected(long position, int size) {
        // TODO- Add/Replace DisplayFragment Fragment to Activity
        mFragmentTransaction= mFragmentManager.beginTransaction();

        mDisplayContactFragment= new DisplayContactFragment();
        Bundle args= new Bundle();
        args.putLong(DatabaseHelper.COLUMN_USER_ID, position);
        args.putInt(DatabaseHelper.TABLE_USER,size);
        mDisplayContactFragment.setArguments(args);

        mFragmentTransaction.replace(R.id.fragment_container, mDisplayContactFragment, AppConstant.TAG_DISPLAY_CONTACT_FRAGMENT);
        mFragmentTransaction.commit();

    }

    // ---- Prevent Default data load again
    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences preference= getSharedPreferences(PREFS_FILE,0);
        preference.edit().putBoolean(AppConstant.TAG_DEFAULT_DATA,false).commit();
    }
}
