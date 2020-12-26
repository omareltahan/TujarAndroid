package com.SB.SBtugar.SeverData;


import com.SB.SBtugar.AllModels.AllOffersResponse;
import com.SB.SBtugar.AllModels.AreaResponse;
import com.SB.SBtugar.AllModels.CategoriesResponse;
import com.SB.SBtugar.AllModels.LocationAddressResult;
import com.SB.SBtugar.AllModels.MainModelSearchProducts;
import com.SB.SBtugar.AllModels.NotificationRegResponse;
import com.SB.SBtugar.AllModels.Orders.OrderRequest;
import com.SB.SBtugar.AllModels.Orders.OrderResponse;
import com.SB.SBtugar.AllModels.PointsMain;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.AllModels.RatingViewMainResponse;
import com.SB.SBtugar.AllModels.RequestCreateCoupon;
import com.SB.SBtugar.AllModels.ResponseChangeStatus;
import com.SB.SBtugar.AllModels.SearchResponse;
import com.SB.SBtugar.AllModels.SliderImageModelResponse;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarketsResponse;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarketsResponseSingle;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarketsStatusResponse;
import com.SB.SBtugar.AllModels.SuperMarket.Vendor;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.UserDataResponse;
import com.SB.SBtugar.AllModels.UserUpdateRequest;
import com.SB.SBtugar.AllModels.ValidateTokenResponse;
import com.SB.SBtugar.AllModels.VariationModel;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface StoreAPIInterface {
    @GET("product_search")
    Call<AllOffersResponse> SearchAllProducts(@Query("text")String text);
    @GET("product")
    Call<AllOffersResponse> GetAllOffers();




    @DELETE("coupons/{ID}")
    Call<String> DeleteCopounFromAll( @Path("ID")int ID);

    @GET("coupons")
    Call<List<RequestCreateCoupon>> GetCouponByCode(@Query("code")String code);


    @GET("orders/{ID}")
    Call<OrderResponse> GetOrderById(@Path("ID") String ID);

    @GET("loyalty/{ID}")
    Call<PointsMain> getLoyalityPoints(@Path("ID")String user_id);

    @FormUrlEncoded
    @POST("wallet/{ID}")
    Call<String> AddWalletValue(@Path("ID") int ID, @Field("add_points") int points);

    @GET("vendors")
    @Headers("Content-Type: application/json")
    Call<SuperMarketsResponse> getSuperMarkets(@Query("cat") String cat,@Query("dist") String dist,@Query("city") String city);

   @GET("market_status/{ID}")
   Call<SuperMarketsStatusResponse> getSuperMarketStatus(@Path("ID") String ID);

    @GET("reviews/store_reviews/{ID}")
    Call<RatingViewMainResponse> getRatingsOfShop(@Path("ID") int user_id);
    @GET("reviews/store_reviews/{ID}")
    Call<RatingViewMainResponse> getRatingsOfShopOfUser(@Path("ID") int shop_id, @Query("customer") int user_id);

    @FormUrlEncoded
    @POST("reviews/store_reviews/{ID}")
    Call<RatingViewMainResponse> SetRatingsOfShopOfUser(@Path("ID") int shop_id,
                                                        @Field("rate") int rate, @Field("time") String time,
                                                        @Field("desc") String desc, @Field("date") String date,
                                                        @Field("customer") int customer);

    @GET("user/meta/{ID}")
    Call<UserDataResponse> getFav_Markets(@Path("ID") String user_id);


 @GET("products")
    Call<List<Product>> getShopOffers(@Query("on_sale") Boolean on_sale,
                                      @Query("author") String author, @Query("status") String status);

    @GET("supermarket/{ID}")
    Call<SuperMarketsResponseSingle> getSuperMarketByID(@Path("ID") String marketId);

    @GET("supermarket/{ID}/categories")
    Call<CategoriesResponse> getSuperMarketCategories(@Path("ID") String marketId, @Query("lang") String lang);

    @GET("stores")
    Call<List<Vendor>> getVendorList();

    @GET("products")
    Call<List<Product>> getProducts(@Query("category") String catId,
                                    @Query("author") String author,
                                    @Query("page") int page, @Query("status") String status);

    @GET("products")
    Call<List<Product>> getFeaturedProducts(@Query("author") String author,
                                            @Query("featured") boolean featured,
                                            @Query("per_page") int noOfProducts, @Query("status") String status);
    @GET("products")
    Call<List<Product>> getAllProducts(@Query("author") String author,@Query("per_page") int noOfProducts, @Query("status") String status);
    @GET("products")
    Call<List<Product>> getAllProducts(@Query("per_page") int noOfProducts, @Query("status") String status);
    @GET("ads/slideshow")
    Call<SliderImageModelResponse> getSlideData();

    @GET("products/{ID}")
    Call<Product> getProduct(@Path("ID") int productId, @Query("status") String status);

    @GET("user/validateToken")
    Call<ValidateTokenResponse> validateToken();

    @POST("orders")
    Call<OrderResponse> placeOrder(@Body OrderRequest request);

    @POST("orders/{ID}")
    Call<OrderResponse> changeStatusToCancel(@Path("ID") int order_id, @Body OrderRequest request);


    @POST("setorderstatus")
    @FormUrlEncoded
    Call<List<ResponseChangeStatus>> SetOrderChangeStatus(@Field("order_id") int order_id, @Field("status") String status, @Field("time") String time, @Field("customer_id") int customer_id);



    @GET("orders")
    Call<List<OrderResponse>> getOrders(@Query("customer") int id, @Query("page") int page);

    @POST("customers/{ID}")
    Call<UserData> updateUserData(@Path("ID") String userId, @Body UserUpdateRequest request);

    @POST("registervendordata")
    @FormUrlEncoded
    Call<NotificationRegResponse> registerVendor(@Field("vendorid") int vendorID, @Field("playerid") String playerId);

    @GET("product_query")
    Call<SearchResponse>searchProducts(@Query("vendorId") int marketId, @Query("query") String query);

    @GET("areas")
    Call<AreaResponse>getAreas(@Query("lang") String lang);



//    @POST("user/meta/{ID}")
//    Call<UserData> SetFavouriteToServer(@Path("ID")String userId,@Body request_market_fav request);

    @Multipart
    @POST("user/meta/{ID}")
    Call<UserDataResponse> SetFavouriteToServer(@Path("ID") String userId, @Part("fav_markets") RequestBody request);

    @Multipart
    @POST("media")
    Call<String> SetImageToServer(@Part("file") RequestBody request);

//    @POST("user/meta/{ID}")
//    Call<UserData> SetFavouriteToServerMultipart2(@Path("ID")String userId,@Body RequestBody request);

    @Multipart
    @POST("user/meta/{ID}")
    Call<UserDataResponse> RemoveFavouriteFromServer(@Path("ID") String userId, @Part("del_fav_markets") RequestBody request);

    @Multipart
    @POST("user/meta/{ID}")
    Call<UserData> AddToShoppingListServer(@Path("ID") String userId, @Part("shopping_lists") RequestBody request);

    @Multipart
    @POST("user/meta/{ID}")
    Call<UserData> RemoveFromShoppingListServer(@Path("ID") String userId, @Part("del_shopping_lists") RequestBody request);

    @Multipart
    @POST("user/meta/{ID}")
    Call<UserData> SetPromoCodeToUser(@Path("ID") String userId, @Part("promo_codes") RequestBody request);

    @Multipart
    @POST("user/meta/{ID}")
    Call<UserData> RemoveCodeFromUser(@Path("ID") int userId, @Part("del_promo_codes") RequestBody request);

    @Multipart
    @POST("loyalty/{ID}")
    Call<UserData> SetMinusOfPoints(@Path("ID") String userId, @Part("used_points") RequestBody request);

    @GET("products/{ID}/variations")
    Call<List<VariationModel>> GetProductVariations(@Path("ID") String ID, @Query("page") int page, @Query("per_page") int per_page);

//    @GET("coupons")
//    Call<List<RequestCreateCoupon>> GetCouponByCode(@Query("code") String code);
//
//    @DELETE("coupons/{ID}")
//    Call<String> DeleteCopounFromAll(@Path("ID") int ID);
//
//    @POST("coupons")
//    Call<RequestCreateCoupon> CreateNewCoupon(@Body RequestCreateCoupon request);

    @POST("registertoken")
    @FormUrlEncoded
    Call<NotificationRegResponse> SetRegisterValue(@Field("user_id") int vendorID, @Field("token") String playerId);



}

