package com.booklist.booktest.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booklist.booktest.R;
import com.booklist.booktest.library.models.ImageLinks;
import com.booklist.booktest.library.models.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookViewHolders> {

    private List<Item> itemList;
    private Context context;

    public BookRecyclerViewAdapter(Context context, List<Item> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public BookViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_item, null);
        BookViewHolders bookViewHolders = new BookViewHolders(layoutView);
        return bookViewHolders;
    }

    @Override
    public void onBindViewHolder(BookViewHolders holder, int position) {
        Item item = itemList.get(position);

        ImageLinks imageLinks = item.getVolumeInfo().getImageLinks();
        if (imageLinks != null) {
            Picasso.with(context).load(imageLinks.getThumbnail()).error(R.drawable.ic_book_cover).into(holder.bookPhoto);
        }
        holder.bookName.setText(item.getVolumeInfo().getTitle());
        final String link = item.getVolumeInfo().getInfoLink();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                context.startActivity(browserIntent);
                Log.d("OpenLink", link);
            }
        });
    }



    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public void resetState() {
        itemList.clear();
        notifyDataSetChanged();
    }

}
