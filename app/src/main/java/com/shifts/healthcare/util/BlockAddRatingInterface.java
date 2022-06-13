package com.shifts.healthcare.util;


public interface BlockAddRatingInterface {

    public void block(String worker_id,String status);
    public void addRating(String worker_id,String review,String rating);

}
