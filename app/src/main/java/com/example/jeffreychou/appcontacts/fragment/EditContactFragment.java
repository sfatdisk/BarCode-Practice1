package com.example.jeffreychou.appcontacts.fragment;


import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeffreychou.appcontacts.R;
import com.example.jeffreychou.appcontacts.database.DatabaseHelper;
import com.example.jeffreychou.appcontacts.listener.FragmentTransitListener;
import com.example.jeffreychou.appcontacts.provider.ContactProvider;
import com.example.jeffreychou.appcontacts.support.AppConstant;

/**
 * Created by jeffreychou on 7/29/15.
 */
public class EditContactFragment extends Fragment implements View.OnClickListener {

    private static final String TAG= "EditContactFragment";


    private EditText edtFirstName, edtSurname, edtPhone,edtEmail;
    private Button btnSave;

    private long userId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Preserve across reconfigurations
        setRetainInstance(true); // if command this line, still keep scroll position, WHY??
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View mRootView= inflater.inflate(R.layout.fragment_edit_contact, container, false);

        edtFirstName=(EditText)mRootView.findViewById(R.id.edtFirstName);
        edtSurname  =(EditText)mRootView.findViewById(R.id.edtSurname);
        edtPhone    =(EditText)mRootView.findViewById(R.id.edtPhone);
        edtEmail    =(EditText)mRootView.findViewById(R.id.edtEmail);
        btnSave     =(Button)mRootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO- savedInstanceState
    }

    // ---  Button Save Click Listener ---
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:{
                save();
                // TODO- go back to attached activity and remove EditContactFragment
                ((FragmentTransitListener)getActivity()).onTransition(AppConstant.TAG_EDIT_CONTACT_FRAGMENT);
            }
        }
    }
    // ----------

    private void save(){
        ContentValues values= new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_FIRSTNAME, edtFirstName.getText().toString());
        values.put(DatabaseHelper.COLUMN_USER_SURNAME, edtSurname.getText().toString());
        values.put(DatabaseHelper.COLUMN_USER_PHONE, edtPhone.getText().toString() );
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, edtEmail.getText().toString());

        if(userId==0){ // insert
            Uri newUri = getActivity().getContentResolver().insert( ContactProvider.CONTENT_URI, values);
            userId= ContentUris.parseId(newUri); // need ??

        }else{ // update
            Uri updateUri= ContentUris.withAppendedId( ContactProvider.CONTENT_URI, userId);

            int count= getActivity().getContentResolver().update(updateUri, values, null, null);

            if( count!=1){
                throw new IllegalStateException("Unable to update "+userId );
            }
        }
        // remind user the information saved
        Toast.makeText(getActivity(), getString(R.string.user_info_saved),Toast.LENGTH_SHORT);

        //TODO -- make keyboard hidden
    }

}
