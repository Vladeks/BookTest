package com.booklist.booktest;


import android.app.Fragment;
import android.os.Bundle;
import com.booklist.booktest.library.models.Item;


import java.util.ArrayList;


public class RetainedFragment  extends Fragment {

    private ArrayList<Item> bookList;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(ArrayList<Item> bookList) {
        this.bookList = bookList;
    }

    public ArrayList<Item> getData() {
        return bookList;
    }

}
