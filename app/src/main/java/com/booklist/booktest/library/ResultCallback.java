package com.booklist.booktest.library;


import com.booklist.booktest.library.models.Result;

public interface ResultCallback {
    void onSuccess(Result result);
    void onFailure(Throwable t);
}
