package com.example.railyatri.RestApi;


import com.example.railyatri.Model.CommonModel;
import com.example.railyatri.Model.CoolieListModel;
import com.example.railyatri.Model.CoolieOrderListModel;
import com.example.railyatri.Model.FoodCategoryModel;
import com.example.railyatri.Model.FoodListModel;
import com.example.railyatri.Model.FoodOrderModel;
import com.example.railyatri.Model.LoginModel;
import com.example.railyatri.Model.PassListModel;
import com.example.railyatri.Model.ShopListModel;
import com.example.railyatri.Model.TicketListModel;
import com.example.railyatri.Model.UpdateUserModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("getUserLogin.php")
    Call<LoginModel> login(@Field("mobile") String mobile, @Field("password") String password, @Field("type") String type);

    @Multipart
    @POST("getUserRegistration.php")
    Call<CommonModel> registration(@Part("first_name") RequestBody first_name, @Part("last_name") RequestBody last_name,
                                   @Part("mobile") RequestBody mobile, @Part("password") RequestBody password,
                                   @Part("address") RequestBody address, @Part("photo\";filename=\"photo.jpg\"") RequestBody userImage);

    @Multipart
    @POST("updateUserDetails.php")
    Call<UpdateUserModel> updateUser(@Part("user_id") RequestBody userId, @Part("type") RequestBody userType,
                                     @Part("first_name") RequestBody userFName, @Part("last_name") RequestBody userLName,
                                     @Part("address") RequestBody userAddress, @Part("photo\";filename=\"photo.jpg\"") RequestBody userImage);

    @FormUrlEncoded
    @POST("viewOwnFood.php")
    Call<FoodListModel> getShopFood(@Field("shop_id") String shopId);

    @Multipart
    @POST("addFood.php")
    Call<CommonModel> addFood(@Part("shop_id") RequestBody shopId, @Part("name") RequestBody name, @Part("description") RequestBody description,
                              @Part("price") RequestBody price, @Part("price2") RequestBody price2, @Part("price3") RequestBody price3,
                              @Part("category_id") RequestBody categoryId, @Part("photo\";filename=\"photo.jpg\"") RequestBody photo);

    @Multipart
    @POST("updateFood.php")
    Call<CommonModel> updateFood(@Part("food_id") RequestBody foodId, @Part("name") RequestBody name, @Part("description") RequestBody description,
                                 @Part("price") RequestBody price, @Part("price2") RequestBody price2, @Part("price3") RequestBody price3,
                                 @Part("category_id") RequestBody categoryId, @Part("photo\";filename=\"photo.jpg\"") RequestBody photo);

    @FormUrlEncoded
    @POST("deleteFood.php")
    Call<CommonModel> deleteFood(@Field("food_id") String foodId);

    @FormUrlEncoded
    @POST("getFoodShopOrder.php")
    Call<FoodOrderModel> getFoodShopOrder(@Field("shop_id") String shopId);

    @FormUrlEncoded
    @POST("updateFoodOrderStatus.php")
    Call<CommonModel> updateFoodOrderStatus(@Field("order_id") String orderId, @Field("status") String status);

    @FormUrlEncoded
    @POST("collagePassRequest.php")
    Call<CommonModel> applyCollagePass(@Field("enroll") String enroll, @Field("collage_id") String collage, @Field("to") String to,
                                       @Field("from") String from, @Field("duration") String month, @Field("pass_type") String type,
                                       @Field("payment") String payment, @Field("file_name") String fileName);

    @Multipart
    @POST("normalPassRequest.php")
    Call<CommonModel> applyNormalPass(@Part("user_id") RequestBody user_id, @Part("user_type") RequestBody user_type,
                                      @Part("to") RequestBody to, @Part("from") RequestBody from,
                                      @Part("name") RequestBody name, @Part("duration") RequestBody month,
                                      @Part("pass_type") RequestBody pass_type, @Part("payment") RequestBody payment,
                                      @Part("age") RequestBody age, @Part("gender") RequestBody gender,
                                      @Part("photo\";filename=\"photo.jpg\"") RequestBody photo,
                                      @Part("id_proof\";filename=\"id_proof.jpg\"") RequestBody id_proof);

    @FormUrlEncoded
    @POST("getPass.php")
    Call<PassListModel> getPass(@Field("user_id") String userId, @Field("user_type") String userType);

    @FormUrlEncoded
    @POST("getTicket.php")
    Call<TicketListModel> getTicket(@Field("user_id") String userId, @Field("user_type") String userType);

    @FormUrlEncoded
    @POST("addTicket.php")
    Call<CommonModel> addTicket(@Field("user_id") String userId, @Field("user_type") String userType, @Field("ticket_type") String ticketType,
                                @Field("from_station") String from, @Field("to_station") String to, @Field("qty") String qty,
                                @Field("price") String price, @Field("total") String total);

    @FormUrlEncoded
    @POST("getCoolieOrder.php")
    Call<CoolieOrderListModel> getCoolieOrder(@Field("coolie_id") String coolieId);

    @FormUrlEncoded
    @POST("getCoolieOrderHistory.php")
    Call<CoolieOrderListModel> getCoolieOrderHistory(@Field("user_id") String userId);

    @POST("getCoolie.php")
    Call<CoolieListModel> getCoolie();

    @FormUrlEncoded
    @POST("bookCoolie.php")
    Call<CommonModel> bookCoolie(@Field("coolie_id") String coolieId, @Field("user_id") String userId, @Field("station") String station,
                                 @Field("place") String place);

    @FormUrlEncoded
    @POST("updateCoolieAvailableStatus.php")
    Call<CommonModel> updateCoolieAvailableStatus(@Field("coolie_id") String coolieId, @Field("status") String status);

    @FormUrlEncoded
    @POST("updateCoolieOrderStatus.php")
    Call<CommonModel> updateCoolieOrderStatus(@Field("order_id") String coolieId, @Field("status") String status);

    @FormUrlEncoded
    @POST("getFoodOrder.php")
    Call<FoodOrderModel> getFoodOrder(@Field("user_id") String id, @Field("user_type") String user_type);

    @POST("getFoodShopList.php")
    Call<ShopListModel> getShopList();

    @FormUrlEncoded
    @POST("getFoodCategory.php")
    Call<FoodCategoryModel> getFoodCategory(@Field("shop_id") String id);

    @FormUrlEncoded
    @POST("getFoodList.php")
    Call<FoodListModel> getFoodList(@Field("shop_id") String id, @Field("category_id") String category_id);

    @FormUrlEncoded
    @POST("addFoodOrder.php")
    Call<CommonModel> addFoodOrder(@Field("user_id") String userId, @Field("user_type") String userType,
                                   @Field("food_name") String foodName, @Field("category_name") String categoryName,
                                   @Field("shop_id") String shopId, @Field("size") String size, @Field("qty") String qty,
                                   @Field("total") String total, @Field("file_name") String fileName);

}