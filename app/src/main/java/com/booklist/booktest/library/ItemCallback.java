package com.booklist.booktest.library;


import com.booklist.booktest.library.models.Item;

public interface ItemCallback {
    void onSuccess(Item item);
    void onFailure(Throwable t);
}
