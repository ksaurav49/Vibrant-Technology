package htkc.ebaba.ecom.api;

import java.util.List;

import htkc.ebaba.ecom.response.AddCartResponse;
import htkc.ebaba.ecom.response.CartResponse;
import htkc.ebaba.ecom.response.CategoryRetResponse;
import htkc.ebaba.ecom.response.DeliveryResponse;
import htkc.ebaba.ecom.response.MyOrderResponse;
import htkc.ebaba.ecom.response.OrderResponse;
import htkc.ebaba.ecom.response.ProbResponse;
import htkc.ebaba.ecom.response.ProductDetailResponse;
import htkc.ebaba.ecom.response.ProductRetResponse;
import htkc.ebaba.ecom.response.RegisterResponse;
import htkc.ebaba.ecom.response.ResendResponse;
import htkc.ebaba.ecom.response.SliderRetResponse;
import htkc.ebaba.ecom.response.SubCategoryRetResponse;
import htkc.ebaba.ecom.response.UpdateCartResponse;
import htkc.ebaba.ecom.response.UserResponse;
import htkc.ebaba.ecom.response.VerifyResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService
{


    @FormUrlEncoded
    @POST("verify")
    Call<VerifyResponse> verify(
            @Field("otp") String otp,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> login(
            @Field("phone") String phone,
            @Field("pass") String pass
    );

    @GET("allcategory")
    Call<List<CategoryRetResponse>> getCategory();

    @GET("allslider")
    Call<List<SliderRetResponse>> getSlider();


    @FormUrlEncoded
    @POST("cart")
    Call<List<CartResponse>> getCart(
            @Field("u_id") String u_id
    );

    @GET("delivery")
    Call<DeliveryResponse> delivery();

    @FormUrlEncoded
    @POST("register")
    Call<RegisterResponse> register(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("addtocart")
    Call<AddCartResponse> addToCart(
            @Field("uid") String uid,
            @Field("qntty") String qntty,
            @Field("pid") String pid,
            @Field("price") String price
    );
    @FormUrlEncoded
    @POST("updatecart")
    Call<UpdateCartResponse> updateCart(
            @Field("cid") String cid,
            @Field("qntty") String qntty
    );

    @FormUrlEncoded
    @POST("resendotp")
    Call<ResendResponse> resend(
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("getsubcategory")
    Call<List<SubCategoryRetResponse>> getSubCategory(
            @Field("cat_id") String cat_id
    );

    @FormUrlEncoded
    @POST("allproducts")
    Call<List<ProductRetResponse>> getAllProduct(
            @Field("sub_id") String sub_id
    );
    @FormUrlEncoded
    @POST("productdetail")
    Call<ProductDetailResponse> getProductDetail(
            @Field("p_id") String p_id
    );

    @FormUrlEncoded
    @POST("order")
    Call<OrderResponse> order(
            @Field("u_id") String u_id,
            @Field("mode") String mode,
            @Field("hno") String hno,
            @Field("area") String area,
            @Field("city") String city,
            @Field("pin") String pin,
            @Field("total") String total

    );
    @FormUrlEncoded
    @POST("myorder")
    Call<List<MyOrderResponse>> myorder(
            @Field("u_id") String u_id
    );
    @FormUrlEncoded
    @POST("prob")
    Call<ProbResponse> prob(
            @Field("oid") String oid,
            @Field("descr") String descr
    );
    @FormUrlEncoded
    @POST("forgot")
    Call<OrderResponse> forgot(
            @Field("mo") String uid
    );
    @FormUrlEncoded
    @POST("cancel")
    Call<ProbResponse> cancel(
            @Field("order_id") String order_id
    );
}
