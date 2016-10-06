package com.example.hhoo7.udacitycoursep7;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookListAdapter extends ArrayAdapter<BookClass> {
    private Context mContext;

    public BookListAdapter(Context context, ArrayList<BookClass> bookList) {
        super(context, 0, bookList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        }

        BookClass bookClass = getItem(position);
        TextView titleView = (TextView) itemView.findViewById(R.id.title_view);
        TextView authorView = (TextView) itemView.findViewById(R.id.author_view);
        TextView publisherView = (TextView) itemView.findViewById(R.id.publisher_view);
        TextView publicshedDateView = (TextView) itemView.findViewById(R.id.publishedDate_view);

        titleView.setText(bookClass.getmBookTitle());
        authorView.setText(String.format("%s ：%s", mContext.getString(R.string.author),bookClass.getmAuthors()));
        publisherView.setText(String.format("%s ：%s",mContext.getString(R.string.publisher), bookClass.getmPublicsher()));
        publicshedDateView.setText(String.format("%s ：%s", mContext.getString(R.string.publisdate),bookClass.getmPublicshedDate()));

        return itemView;
    }

}
