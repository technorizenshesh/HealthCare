package com.shifts.healthcare.util;

import android.view.View;

import com.shifts.healthcare.models.SuccessResGetCurrentSchedule;

/**
 * Created by Ravindra Birla on 17,November,2021
 */
public interface RecruitmentShiftConfirmationInterface {

    public void recruitConfirmation(View v, String shiftIds, String userId, String workerId, String status, SuccessResGetCurrentSchedule.Result result);

    public void cancelRecruitShift(String shiftIds,String userId,String workerId, String status);

}
