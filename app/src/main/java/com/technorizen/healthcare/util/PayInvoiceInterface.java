package com.technorizen.healthcare.util;

import android.view.View;

import com.technorizen.healthcare.models.SuccessResGetPost;

import java.util.List;

public interface PayInvoiceInterface {

    public void payInvoice(View v, String invoceId, String amount, String userId, String workerId, String shiftId);

}
