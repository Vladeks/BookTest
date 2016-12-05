package com.booklist.booktest.library;




import com.booklist.booktest.library.models.Item;
import com.booklist.booktest.library.models.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface BooksApiService {
    @GET("volumes")
    Call<Result> searchBooks(@Query("q") String terms,
                             @Query("maxResults") String maxResults,
                             @Query("startIndex") String startIndex,
                             @Query("key") String apiKey);

    @GET("volumes")
    Call<Result> searchBooksOrderBy(@Query("q") String terms,
                                    @Query("orderBy") String orderBy,
                                    @Query("maxResults") String maxResults,
                                    @Query("startIndex") String startIndex,
                                    @Query("key") String apiKey);

    @GET("volumes")
    Call<Result> searchBooksLangRestrict(@Query("q") String terms,
                                         @Query("langRestrict") String lang,
                                         @Query("maxResults") String maxResults,
                                         @Query("startIndex") String startIndex,
                                         @Query("key") String apiKey);

    @GET("volumes")
    Call<Result> searchBooksPrintType(@Query("q") String terms,
                                      @Query("printType") String printType,
                                      @Query("maxResults") String maxResults,
                                      @Query("startIndex") String startIndex,
                                      @Query("key") String apiKey);

    @GET("volumes/{volumeId}")
    Call<Item> getBookById(@Path("volumeId") String id,
                           @Query("key") String apiKey);

}
