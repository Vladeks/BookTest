package com.booklist.booktest;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import com.booklist.booktest.adapters.BookRecyclerViewAdapter;
import com.booklist.booktest.adapters.EndlessRecyclerViewScrollListener;
import com.booklist.booktest.library.BooksApi;
import com.booklist.booktest.library.ResultCallback;
import com.booklist.booktest.library.models.Item;
import com.booklist.booktest.library.models.Result;


import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {

    private StaggeredGridLayoutManager sgLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView recyclerView;
    private BookRecyclerViewAdapter rcAdapter;
    private  String queryBook;
    private static final String GOOGLE_API_KEY ="AIzaSyAdkaiuJu7rFlW7uaoeERazfFhm5Pr5Ofc";
    private BooksApi booksApi;
    private RetainedFragment dataFragment;
    private ArrayList<Item> bookList;
    private int totalBooksItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        bookList = new ArrayList<Item>();

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        dataFragment = (RetainedFragment) fm.findFragmentByTag("data");
        // create the fragment and data the first time
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment, "data").commit();
            dataFragment.setData(bookList);

        } else {
            bookList = dataFragment.getData();
        }

        booksApi = BooksApi.create(GOOGLE_API_KEY);


        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        sgLayoutManager = new StaggeredGridLayoutManager(getDisplayColumns(this),StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sgLayoutManager);


        rcAdapter = new BookRecyclerViewAdapter(BookListActivity.this, bookList);
        recyclerView.setAdapter(rcAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(sgLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("LoadedItems", String.valueOf(bookList.size()));
                if (rcAdapter.getItemCount() < totalBooksItemCount) {
                    searchBooks(page);
                }else {
                    Toast.makeText(getApplicationContext(), "All books are loaded", Toast.LENGTH_LONG).show();
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // store the data in the fragment
        dataFragment.setData(bookList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Enter Author or Book Name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                queryBook = query;
                if (hasConnection()){
                    bookList.clear();
                    rcAdapter.resetState();
                    scrollListener.resetState();
                    searchBooks(0);
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                }
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    private void searchBooks(int page) {
        booksApi.searchBooks(queryBook,
                page,
                new ResultCallback() {
                    @Override
                    public void onSuccess(Result result) {
                       try{
                           bookList.addAll(result.getItems());
                           rcAdapter.notifyItemRangeInserted(rcAdapter.getItemCount(), bookList.size() - 1);
                           totalBooksItemCount = result.getTotalItems();
                           Log.d("TotalItemCount",result.getTotalItems().toString());
                           for (Item it : result.getItems()){
                               Log.d("Item",it.getVolumeInfo().getTitle() +"  "+ it.getVolumeInfo().getImageLinks().getSmallThumbnail());
                           }
                       } catch (NullPointerException ex){
                            Log.e("Activity", "Null pointer in search function");
                       }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getSimpleName(), t.getMessage());
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean isVertical(Context context) {
        boolean portrait = (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        boolean landscape = (context.getResources().getConfiguration().screenLayout == Configuration.ORIENTATION_LANDSCAPE);
        return (portrait || landscape);
    }

    public static int getDisplayColumns(Activity activity) {
        int columnCount = 3;
        if (isVertical(activity)) {
            columnCount = 2;
        }
        return columnCount;
    }

    private boolean hasConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        //super.onBackPressed();
    }
}
