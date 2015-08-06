package com.example.jeffreychou.appcontacts.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jeffreychou.appcontacts.R;
import com.example.jeffreychou.appcontacts.adapter.UserListAdapter;
import com.example.jeffreychou.appcontacts.listener.FragmentTransitListener;
import com.example.jeffreychou.appcontacts.provider.ContactProvider;
import com.example.jeffreychou.appcontacts.support.AppConstant;

/**
 * Created by jeffreychou on 7/29/15.
 */
public class ListContactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Button.OnClickListener {

    private UserListAdapter adapter;
    private RecyclerView mRecyclerView;
    private Button mBtnAdd;

    private FragmentTransitListener mCallback;
    private EditContactFragment mEditContactFragment;

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

        adapter = new UserListAdapter();
        // use loader to query data from database in background thread
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_list_contacts, container, false);

        mBtnAdd =(Button)v.findViewById(R.id.btnAdd);
        mBtnAdd.setOnClickListener(this);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        return v;
    }

    // --- CursorLoader ---
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContactProvider.CONTENT_URI, null, null, null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    // --- Button Listener ------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAdd:{
                // TODO-- go to EditContactFragment
                mCallback.onTransition(AppConstant.TAG_LIST_CONTACT_FRAGMENT);
                //((FragmentTransitListener)getActivity()).onTransition(AppConstant.TAG_LIST_CONTACT_FRAGMENT);
            }
        }
    }
    // --------------


}
