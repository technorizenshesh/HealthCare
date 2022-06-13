package com.shifts.healthcare.util;

import com.shifts.healthcare.models.SuccessResGetCurrentSchedule;
import com.shifts.healthcare.models.SuccessResGetPost;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Ravindra Birla on 21,June,2021
 */
public interface DeleteShifts {

    public void onClick(String shiftsId, String userId, List<SuccessResGetPost.PostshiftTime> postshiftTime,String type) throws ParseException;
    public void rejectSHift(String shiftsId, String userId, List<SuccessResGetPost.PostshiftTime> postshiftTime,String type);
    public void deleteCurrentShiftsClick(String shiftsId,String userId,SuccessResGetCurrentSchedule.PostshiftTime dateTime);

    public void shiftConfimation(String shiftIds,String userId,String workerId,String status);

    public void userCancelRecruit(String shiftIds,String userId,String workerId,String status);


}
