package com.technorizen.healthcare.retrofit;



import com.technorizen.healthcare.models.SuccessResAcceptShift;
import com.technorizen.healthcare.models.SuccessResAddAddress;
import com.technorizen.healthcare.models.SuccessResDeleteShifts;
import com.technorizen.healthcare.models.SuccessResForgetPassword;
import com.technorizen.healthcare.models.SuccessResGetAddress;
import com.technorizen.healthcare.models.SuccessResGetCountries;
import com.technorizen.healthcare.models.SuccessResGetJobPositions;
import com.technorizen.healthcare.models.SuccessResGetPost;
import com.technorizen.healthcare.models.SuccessResGetProfile;
import com.technorizen.healthcare.models.SuccessResGetStates;
import com.technorizen.healthcare.models.SuccessResGetWorkerProfile;
import com.technorizen.healthcare.models.SuccessResSignIn;
import com.technorizen.healthcare.models.SuccessResSignup;
import com.technorizen.healthcare.models.SuccessResUpdateProfile;
import com.technorizen.healthcare.models.SuccessResWorkerSignup;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HealthInterface {

    @FormUrlEncoded
    @POST("signup")
    Call<SuccessResSignup> signup(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_country")
    Call<SuccessResGetCountries> getCountries(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_state")
    Call<SuccessResGetStates> getStates(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("login")
    Call<SuccessResSignIn> login(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_profile")
    Call<SuccessResGetProfile> getProfile(@FieldMap Map<String, String> paramHashMap);



    @Multipart
    @POST("update_profile")
    Call<SuccessResUpdateProfile> updateProfile (@Part("user_id") RequestBody userId,
                                                 @Part("account_type") RequestBody accountType,
                                                 @Part("first_name") RequestBody firstname,
                                                 @Part("last_name") RequestBody lastName,
                                                 @Part("company") RequestBody company,
                                                 @Part("company_website") RequestBody website,
                                                 @Part("street_no") RequestBody streetNo,
                                                 @Part("street_name") RequestBody streetName,
                                                 @Part("couttry_code") RequestBody countryCode,
                                                 @Part("country") RequestBody country,
                                                 @Part("state") RequestBody state,
                                                 @Part("city") RequestBody city,
                                                 @Part("zipcode") RequestBody zipCode,
                                                 @Part("email") RequestBody email,
                                                 @Part("phone") RequestBody phone,
                                                 @Part("description") RequestBody descrption,
                                                 @Part("address") RequestBody address,
                                                 @Part("lat") RequestBody lat,
                                                 @Part("long") RequestBody lon,
                                                 @Part("city_new") RequestBody cityNew,
                                                 @Part("state_new") RequestBody stateNew,
                                                 @Part("country_new") RequestBody countryNew,
                                                 @Part("location_id") RequestBody locationID,
                                                 @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<SuccessResForgetPassword> forgotPassword(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("change_password")
    Call<SuccessResForgetPassword> changePass(@FieldMap Map<String, String> paramsHashMap);


    @FormUrlEncoded
    @POST("get_designation")
    Call<SuccessResGetJobPositions> getJobPositions(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("worker_signup")
    Call<SuccessResWorkerSignup> workerSignup(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("postshifts")
    Call<SuccessResSignIn> directPost(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("add_location")
    Call<SuccessResAddAddress> addAddress(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_location")
    Call<SuccessResGetAddress> getAddress(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("get_user_shifts")
    Call<SuccessResGetPost> getPostedShifts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_posted_shifts")
    Call<SuccessResGetPost> getUserPostedShifts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_shift")
    Call<SuccessResDeleteShifts> deleteShifts(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("update_worker_profile")
    Call<SuccessResUpdateProfile> updateWorkerProfile (@Part("worker_id") RequestBody userId,
                                                 @Part("first_name") RequestBody firstname,
                                                 @Part("last_name") RequestBody lastName,
                                                 @Part("street_no") RequestBody streetNo,
                                                 @Part("street_name") RequestBody streetName,
                                                 @Part("apartment_no") RequestBody appartNo,
                                                 @Part("couttry_code") RequestBody countryCode,
                                                 @Part("country") RequestBody country,
                                                 @Part("state") RequestBody state,
                                                 @Part("city") RequestBody city,
                                                 @Part("zipcode") RequestBody zipCode,
                                                 @Part("email") RequestBody email,
                                                 @Part("phone") RequestBody phone,
                                                 @Part("worker_designation") RequestBody designation,
                                                 @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("shiftsforworker")
    Call<SuccessResGetPost> getWorkerShift(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("get_worker_profile")
    Call<SuccessResGetWorkerProfile> getWorkerProfile(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("accept_reject_shifts")
    Call<SuccessResAcceptShift> acceptShift(@FieldMap Map<String, String> paramHashMap);


/*

https://www.app.careshifts.net/webservice/accept_reject_shifts
shift_id:11
worker_id:11
status:Accepted

status Accepted and Rejected

https://www.app.careshifts.net/webservice/shiftsforworker

worker_id:11

https://www.app.careshifts.net/webservice/get_posted_shifts

https://www.app.careshifts.net/webservice/delete_shift

https://www.app.careshifts.net/webservice/update_worker_profile


worker_id:15
first_name:Sagar
last_name:Panse
street_no:110
street_name:Test
apartment_no:12
couttry_code:1
country:38
state:1025
city:Indore
zipcode:452006
email:panse@gmail,com
phone:897313113
worker_designation:10










    @Multipart
    @POST(LOGIN_API)
    Call<SuccessResSignIn> login (@Part("email") RequestBody last_name,
                                   @Part("password") RequestBody email,
                                   @Part("register_id") RequestBody mobile);
*/

/*

    @FormUrlEncoded
    @POST(LOGIN_API)
    Call<SuccessResSignIn> login(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(FORGET_PASSWORD)
    Call<SuccessResForgetPassword> forgotPassword(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(CATEGORY_LIST)
    Call<SuccessResAllCategories> getAllCategories(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(GET_SUB_CATEGORIES)
    Call<SuccessResGetAllSubCategories> getAllSubCategories(@FieldMap Map<String, String> paramHashMap);

    */



/*

    @Multipart
    @POST(ADD_ITEM)
    Call<SuccessResItemAdded> addItem (@Part("user_id") RequestBody userId,
                                       @Part("category_id") RequestBody categoryId,
                                       @Part("sub_category_id") RequestBody subCategoryId,
                                       @Part("conditions") RequestBody condition,
                                       @Part("title") RequestBody title,
                                       @Part("description") RequestBody description,
                                       @Part("price") RequestBody price,
                                       @Part("address") RequestBody address,
                                       @Part("lat") RequestBody latitue,
                                       @Part("lon") RequestBody lon,
                                       @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(GET_ITEM)
    Call<SuccessResGetMyItems> getAllMyItems(@FieldMap Map<String, String> paramHashMap);

    @GET(GET_BANNERS)
    Call<SuccessResBannersList> getBanners();

    @FormUrlEncoded
    @POST(GET_PROFILE)
    Call<SuccessResProfileData> getSellerDetails(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(GET_PRODUCT_BY_CATEGORY)
    Call<SuccessResGetProductByCategory> getProductsByCategory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(GET_PRODUCT_DETAIL)
    Call<SuccessResProductDetail> getProductDetail(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(GET_ALL_PRODUCT)
    Call<SuccessResGetMyItems> getAllProducts(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST(UPDATE_PROFILE)
    Call<SuccessResUpdateProfile> updateProfile (@Part("user_id") RequestBody userId,
                                                 @Part("name") RequestBody first_name,
                                                 @Part("email") RequestBody last_name,
                                                 @Part("address") RequestBody address,
                                                 @Part("lat") RequestBody lat,
                                                 @Part("lon") RequestBody lng,
                                                 @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(DELETE_ITEM)
    Call<SuccessResDeleteItem> deleteItem(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST(UPDATE_ITEM)
    Call<SuccessResUpdateItem> updateItem (@Part("item_id") RequestBody itemId,
                                           @Part("title") RequestBody title,
                                           @Part("price") RequestBody price,
                                           @Part("conditions") RequestBody conditions,
                                           @Part("address") RequestBody address,
                                           @Part("lat") RequestBody lat,
                                           @Part("lon") RequestBody lng,
                                           @Part("description") RequestBody description,
                                           @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST(ADD_FAVORITE)
    Call<SuccessResAddFavourite> addFavorite(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(CHAT_REQUEST)
    Call<SuccessResChatRequest> chatRequest(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(GET_NOTIFICATION)
    Call<SuccessResGetNotifications> getNotifications(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(UPDATE_CHAT_STATUS)
    Call<SuccessResRequestStatus> updateRequestStatus(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST(INSERT_CHAT)
    Call<SuccessResInsertChat> insertChat (@Part("sender_id") RequestBody senderId,
                                           @Part("receiver_id") RequestBody receiverId,
                                           @Part("chat_message") RequestBody message,
                                           @Part("item_id") RequestBody itemId,
                                           @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST(GET_CHAT)
    Call<SuccessResGetChat> getChat(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(GET_CONVERSATION)
    Call<SuccessResGetConversation> getConversation(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(GET_CHAT_REQUEST)
    Call<SuccessResGetChatRequest> getChatRequest(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(GET_AVAILABLE_CHAT_REQUEST)
    Call<SuccessResAvialableChatRequest> getAvailableChatRequest(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(ITEM_SEARCH)
    Call<SuccessResGetMyItems> searchItem(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(ADD_ITEM_OFFER)
    Call<SuccessResAddOfferItem> addItemOffer(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(PRIVACY_POLICY)
    Call<SuccessResPrivacyPolicy> getPrivacyPolicy(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(TERMS_AND_CONDITION)
    Call<SuccessResPrivacyPolicy> getTermsAndCondition(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(USER_PRODUCT_BY_CATEGORY)
    Call<SuccessResGetMyItems> userProductByCategory(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(SEARCH_ITEM_BY_CATEGORY)
    Call<SuccessResGetMyItems> searchWithCategory(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST(SOCIAL_LOGIN)
    Call<SuccessResSocialLogin> socialLogin (@Part("first_name") RequestBody first_name,
                                             @Part("email") RequestBody last_name,
                                             @Part("address") RequestBody address,
                                             @Part("lat") RequestBody lat,
                                             @Part("lon") RequestBody lng,
                                             @Part("register_id") RequestBody registerId,
                                             @Part("social_id") RequestBody socialId,
                                             @Part("image") RequestBody image);

    @FormUrlEncoded
    @POST(CONTACT_US)
    Call<SuccessResContactUs> contactUs(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(USER_ITEM_SEARCH)
    Call<SuccessResGetMyItems> seachSellerItem(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(UPDATE_LANGUAGE)
    Call<SuccessResRequestStatus> changeLanguage(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(CHANGE_PASSWORD)
    Call<SuccessResRequestStatus> changePassword(@FieldMap Map<String, String> paramHashMap);
*/


/*

    @POST(SIGN_UP_API)
    Call<SuccessResSignUp> signUp(@Body Map<String, String> paramHashMap);
*/

/*
    @POST(SIGN_UP_API)
    Call<SuccessResSignUp> signUp(@Body Map<String, String> paramHashMap);
*/

/*
    @FormUrlEncoded
    @POST("login")
    Call<SignupModel> userLogin (@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<Map<String,String>> forgotPass (@FieldMap Map<String,String> params);

    @Multipart
    @POST("update_profile")
    Call<SignupModel> editprofile(
            @Part("user_id") RequestBody id,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("phone_code") RequestBody phone_code,
            @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("change_password")
    Call<SignupModel> changePassword (@FieldMap Map<String,String> params);*/

/*

    @GET("car_list")
    Call<CarListModel> getCarList();

    @FormUrlEncoded
    @POST("brand_list")
    Call<BrandListModel> cardBrandList(@FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("model_list")
    Call<ModListModel> modelList(@FieldMap Map<String,String> params);


    @Multipart
    @POST("driver_signup2")
    Call<SignupModel> addVehicle (@Part("user_id") RequestBody user_id,
                                    @Part("car_type_id") RequestBody car_type_id,
                                    @Part("brand") RequestBody brand,
                                    @Part("car_model") RequestBody car_model,
                                    @Part("year_of_manufacture") RequestBody year_of_manufacture,
                                    @Part("car_number") RequestBody car_number,
                                    @Part("car_color") RequestBody car_color,
                                    @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("driver_signup3")
    Call<SignupModel> addBank(@FieldMap Map<String,String> params);

*/


}