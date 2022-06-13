package com.shifts.healthcare.retrofit;

import com.shifts.healthcare.models.SuccessResAcceptRejectRecruitment;
import com.shifts.healthcare.models.SuccessResAcceptShift;
import com.shifts.healthcare.models.SuccessResAccountDetails;
import com.shifts.healthcare.models.SuccessResAddAddress;
import com.shifts.healthcare.models.SuccessResAddCardDetails;
import com.shifts.healthcare.models.SuccessResAddGetWorkerAvail;
import com.shifts.healthcare.models.SuccessResAddRating;
import com.shifts.healthcare.models.SuccessResAddReference;
import com.shifts.healthcare.models.SuccessResBlockUnblock;
import com.shifts.healthcare.models.SuccessResDeleteCurrentSchedule;
import com.shifts.healthcare.models.SuccessResDeleteShifts;
import com.shifts.healthcare.models.SuccessResEditShift;
import com.shifts.healthcare.models.SuccessResForgetPassword;
import com.shifts.healthcare.models.SuccessResGetAddress;
import com.shifts.healthcare.models.SuccessResGetAppInfo;
import com.shifts.healthcare.models.SuccessResGetCardDetails;
import com.shifts.healthcare.models.SuccessResGetChat;
import com.shifts.healthcare.models.SuccessResGetConversation;
import com.shifts.healthcare.models.SuccessResGetCountries;
import com.shifts.healthcare.models.SuccessResGetCurrentSchedule;
import com.shifts.healthcare.models.SuccessResGetDocuments;
import com.shifts.healthcare.models.SuccessResGetFaqs;
import com.shifts.healthcare.models.SuccessResGetHiredWorker;
import com.shifts.healthcare.models.SuccessResGetHrRate;
import com.shifts.healthcare.models.SuccessResGetInvoices;
import com.shifts.healthcare.models.SuccessResGetJobPositions;
import com.shifts.healthcare.models.SuccessResGetPost;
import com.shifts.healthcare.models.SuccessResGetProfile;
import com.shifts.healthcare.models.SuccessResGetReference;
import com.shifts.healthcare.models.SuccessResGetShiftInProgress;
import com.shifts.healthcare.models.SuccessResGetStates;
import com.shifts.healthcare.models.SuccessResGetToken;
import com.shifts.healthcare.models.SuccessResGetTransactionHistory;
import com.shifts.healthcare.models.SuccessResGetTransactionOverview;
import com.shifts.healthcare.models.SuccessResGetUnseenMessageCount;
import com.shifts.healthcare.models.SuccessResGetWorkerProfile;
import com.shifts.healthcare.models.SuccessResInsertChat;
import com.shifts.healthcare.models.SuccessResInvoiceSummaryUser;
import com.shifts.healthcare.models.SuccessResPrivacyPolicy;
import com.shifts.healthcare.models.SuccessResShiftCompleted;
import com.shifts.healthcare.models.SuccessResSignIn;
import com.shifts.healthcare.models.SuccessResSignup;
import com.shifts.healthcare.models.SuccessResUpdateCards;
import com.shifts.healthcare.models.SuccessResUpdateInstantPay;
import com.shifts.healthcare.models.SuccessResUpdateProfile;
import com.shifts.healthcare.models.SuccessResUpdateRate;
import com.shifts.healthcare.models.SuccessResUpdateRehireShift;
import com.shifts.healthcare.models.SuccessResUpdateScheduleTime;
import com.shifts.healthcare.models.SuccessResUpdateShiftInProgressTime;
import com.shifts.healthcare.models.SuccessResUploadDocument;
import com.shifts.healthcare.models.SuccessResWorkerAcceptedShift;
import com.shifts.healthcare.models.SuccessResWorkerSignup;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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
                                                 @Part("address") RequestBody address,
                                                 @Part("lat") RequestBody latitude,
                                                 @Part("lon") RequestBody lonitude,
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

    @FormUrlEncoded
    @POST("get_current_shift")
    Call<SuccessResGetCurrentSchedule> getCurrentShifts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("worker_current_shift")
    Call<SuccessResGetCurrentSchedule> getWorkerCurrentShifts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_cancel_current_shift")
    Call<SuccessResDeleteCurrentSchedule> deleteCurrentShifts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_worker_rate")
    Call<SuccessResUpdateRate> setRate(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("update_worker_distance")
    Call<SuccessResUpdateRate> updateDistance(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("update_notification_by")
    Call<SuccessResUpdateRate> updateWorkerNoti(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("worker_progress_shift")
    Call<SuccessResGetShiftInProgress> getShiftsInProgress(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_working_time")
    Call<SuccessResUpdateShiftInProgressTime> updateShiftInProgressTime(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("user_progress_shift")
    Call<SuccessResGetShiftInProgress> getUserShiftsInProgress(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_status_completed")
    Call<SuccessResShiftCompleted> shiftCompleted(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("user_completed_shift")
    Call<SuccessResGetShiftInProgress> getUserShiftsHistory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("worker_completed_shift")
    Call<SuccessResGetShiftInProgress> getWorkerShiftsHistory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("shift_by_company_name")
    Call<SuccessResGetPost> getShiftsByCompanyName(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("worker_shift_invoice")
    Call<SuccessResGetInvoices> getWorkerInvoice(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("user_shift_invoice")
    Call<SuccessResGetInvoices> getUserInvoice(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("add_worker_document")
    Call<SuccessResUploadDocument> uploadDocuments (@Part("worker_id") RequestBody userId,
                                                    @Part("type") RequestBody type,
                                                    @Part MultipartBody.Part fileGovId
                                                   );


    @Multipart
    @POST("add_user_document")
    Call<SuccessResUploadDocument> uploadUserDocuments (@Part("user_id") RequestBody userId,
                                                    @Part("type") RequestBody type,
                                                    @Part MultipartBody.Part fileGovId
    );

    @FormUrlEncoded
    @POST("get_user_document")
    Call<SuccessResGetDocuments> getUserDocuments(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("get_worker_document")
    Call<SuccessResGetDocuments> getWorkerDocuments(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("worker_shift_invoice_by_invoice")
    Call<SuccessResGetInvoices> searchInvoice(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("user_shift_invoice_by_invoice")
    Call<SuccessResGetInvoices> searchUserInvoice(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_card")
    Call<SuccessResAddCardDetails> addCard(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_card")
    Call<SuccessResGetCardDetails> getCards(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_hired_worker")
    Call<SuccessResGetHiredWorker> getHiredWorker(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("block_unblock_user")
    Call<SuccessResBlockUnblock> addBlockUnblock(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_review_rating")
    Call<SuccessResAddRating> addRating(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_token")
    Call<SuccessResGetToken> getToken(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("stripe_payment")
    Call<ResponseBody> stripePayment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_wallet")
    Call<ResponseBody> makePayment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("add_reference")
    Call<SuccessResAddReference> addReference(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_reference")
    Call<SuccessResGetReference> getReference(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("user_payment_history")
    Call<SuccessResGetTransactionHistory> getPaymentsHistory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("payment_history_by_date")
    Call<SuccessResGetTransactionHistory> searchPaymentHistory(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_app_info")
    Call<SuccessResGetAppInfo> getAppInfo(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_worker_availability")
    Call<SuccessResUpdateScheduleTime> updateScheduleTime(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_user_faq")
    Call<SuccessResGetFaqs> getUserFaqs(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_worker_faq")
    Call<SuccessResGetFaqs> getWorkerFaqs(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_worker_availability")
    Call<SuccessResAddGetWorkerAvail> getWorkerAvailability(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_conversation")
    Call<SuccessResGetConversation> getConversations(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_chat")
    Call<SuccessResGetChat> getChat(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("insert_chat")
    Call<SuccessResInsertChat> insertChat(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_privacy_policy")
    Call<SuccessResPrivacyPolicy> getPrivacyPolicy(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_terms_service")
    Call<SuccessResPrivacyPolicy> getTermsOfUse(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("payment_overview")
    Call<SuccessResGetTransactionOverview> getTransactionOverview(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("insert_admin_chat")
    Call<SuccessResInsertChat> insertAdminChat(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_admin_chat")
    Call<SuccessResGetChat> getAdminChat(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_recruitmentshift_request")
    Call<SuccessResGetCurrentSchedule> getPendingRecruitmentShifts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("accept_reject_recruitmentshift")
    Call<SuccessResAcceptRejectRecruitment> acceptRejectRecruitment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("rehire_postshifts")
    Call<SuccessResSignIn> rehireWorker(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("rehireshiftsforworker")
    Call<SuccessResGetPost> getRehireShiftForWorker(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_rehire_rejected_request")
    Call<SuccessResGetPost> getCancelledRehiredShifts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_rehire_rejected_shifts")
    Call<SuccessResUpdateRehireShift> updateRehiredShifts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_worker_recruitmentshift_request")
    Call<SuccessResGetCurrentSchedule> getPendingRecruitmentShiftForWorker(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("change_rehire_to_directshifts")
    Call<SuccessResAcceptRejectRecruitment> changeRehireToRecruitment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("change_rehire_to_directshifts")
    Call<SuccessResGetPost> convertRehireToDirectShifts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("updateshifts")
    Call<SuccessResEditShift> editPost(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("user_shift_invoice")
    Call<ResponseBody> downloadInvoicePdf(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_instant_pay")
    Call<SuccessResUpdateInstantPay> updateInstant(@FieldMap Map<String, String> paramsHashMap);

    @FormUrlEncoded
    @POST("acceptedshiftworker")
    Call<SuccessResWorkerAcceptedShift> getWorkerAcceptedShifts(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("worker_shift_invoice_year")
    Call<SuccessResGetTransactionHistory> getPaymentsHistoryByYear(@FieldMap Map<String, String> paramHashMap);

//    @FormUrlEncoded
//    @POST("get_user_invoice_summary")
//    Call<SuccessResInvoiceSummaryUser> getInvoiceSummary(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_user_invoice_summary_payment_status")
    Call<SuccessResInvoiceSummaryUser> getInvoiceSummary(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_worker_invoice_summary_payment_status")
    Call<SuccessResInvoiceSummaryUser> getWorkerInvoiceSummary(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_careshifts_account_details")
    Call<SuccessResAccountDetails> getAccountDetail(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("user_shift_invoice")
    Call<SuccessResGetInvoices> getInvoiceURL(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_user_card")
    Call<ResponseBody> deleteCard(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("set_default_card")
    Call<SuccessResUpdateCards> updateCard(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("document_verification_payment")
    Call<ResponseBody> workerPayment(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_unseen_message")
    Call<SuccessResGetUnseenMessageCount> getUnseenMessage(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_hourly_rates")
    Call<SuccessResGetHrRate> getHourlyRate(@FieldMap Map<String, String> paramHashMap);

}
