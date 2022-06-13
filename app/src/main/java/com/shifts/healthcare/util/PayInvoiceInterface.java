package com.shifts.healthcare.util;

import android.view.View;

public interface PayInvoiceInterface {

    public void payInvoice(View v, String invoceId, String amount, String userId, String workerId, String shiftId);

}
