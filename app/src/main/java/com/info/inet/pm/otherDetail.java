package com.info.inet.pm;

public class otherDetail {
    public String otherVehicleImage, otherVehicleLicensePlateNo, tourType, otherVehicleType, reqStatus, userId;

    public otherDetail(){

    }

    public otherDetail(String otherVehicleImage, String otherVehicleLicensePlateNo, String tourType, String otherVehicleType, String reqStatus, String userId) {
        this.otherVehicleImage = otherVehicleImage;
        this.otherVehicleLicensePlateNo = otherVehicleLicensePlateNo;
        this.tourType = tourType;
        this.otherVehicleType = otherVehicleType;
        this.reqStatus = reqStatus;
        this.userId = userId;
    }


    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtherVehicleImage() {
        return otherVehicleImage;
    }

    public void setOtherVehicleImage(String otherVehicleImage) {
        this.otherVehicleImage = otherVehicleImage;
    }

    public String getOtherVehicleLicensePlateNo() {
        return otherVehicleLicensePlateNo;
    }

    public void setOtherVehicleLicensePlateNo(String otherVehicleLicensePlateNo) {
        this.otherVehicleLicensePlateNo = otherVehicleLicensePlateNo;
    }

    public String getTourType() {
        return tourType;
    }

    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    public String getOtherVehicleType() {
        return otherVehicleType;
    }

    public void setOtherVehicleType(String otherVehicleType) {
        this.otherVehicleType = otherVehicleType;
    }
}
