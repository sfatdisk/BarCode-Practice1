package com.example.jeffreychou.appcontacts.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeffreychou.appcontacts.R;
import com.example.jeffreychou.appcontacts.database.DatabaseHelper;
import com.example.jeffreychou.appcontacts.listener.OnSelectionListener;

/**
 * Created by jeffreychou on 8/3/15.
 *
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Cursor mCursor;
    private int idColumnIndex, firstnameColumnIndex, surnameColumnIndex, phoneColumnIndex, emailColumnIndex;


    // --- updating the latest cursor
    public void swapCursor(Cursor cursor){
        mCursor=cursor;

        if(mCursor!=null){

            mCursor.moveToFirst();
            idColumnIndex=mCursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ID);
            firstnameColumnIndex= mCursor.getColumnIndex(DatabaseHelper.COLUMN_USER_FIRSTNAME);
            surnameColumnIndex= mCursor.getColumnIndex(DatabaseHelper.COLUMN_USER_SURNAME);
            phoneColumnIndex=mCursor.getColumnIndex(DatabaseHelper.COLUMN_USER_PHONE);
            emailColumnIndex=mCursor.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL);
        }
        notifyDataSetChanged(); // notify the recyclerView to refresh data
    }

    // --- RecyclerView.Adapter ---
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    // the ViewGroup is RecyclerView because it setup this adapter
    // next, inflate cardView to the RecyclerView

        View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_user, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) { // similar to getView() method in the ListView
        // ViewHolder is CardView

        Log.d("checkIndex"," current index is: "+position);

        final Context context= viewHolder.mTextView.getContext();

        final long id = getItemId(position); // for onClickListener, rarId for retrieving information
        final int size= getItemCount();

        // setup onClickListener
        viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO- use id pass to DisplayContactFragment
                ((OnSelectionListener)context).onItemSelected(id, size);
            }
        });

        // set the text
        mCursor.moveToPosition(position);
        viewHolder.mTextView.setText( mCursor.getString(firstnameColumnIndex)+" "+mCursor.getString(surnameColumnIndex) );
    }

    @Override
    public long getItemId(int position){
        mCursor.moveToPosition(position);
        return mCursor.getLong(idColumnIndex); // get the choose row itemId
    }

    @Override
    public int getItemCount() {
        return mCursor!=null? mCursor.getCount():0 ;
    }

    // --- ViewHolder --------------------------

    public final static class ViewHolder extends RecyclerView.ViewHolder {  // hold all views within the CardView

        private CardView mCardView;
        private TextView mTextView;


        public ViewHolder( View itemView) { //constructor
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cardView);
            mTextView = (TextView)itemView.findViewById(R.id.tv_full_name);
        }
    }

}
