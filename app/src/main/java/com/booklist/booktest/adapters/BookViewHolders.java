package com.booklist.booktest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.booklist.booktest.R;


public class BookViewHolders extends RecyclerView.ViewHolder {

    public TextView bookName;
    public ImageView bookPhoto;

    public BookViewHolders(View itemView) {
        super(itemView);
        bookName = (TextView) itemView.findViewById(R.id.book_name);
        bookPhoto = (ImageView) itemView.findViewById(R.id.book_photo);
    }


}
