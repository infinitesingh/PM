package com.info.inet.pm;

public class vehicleDetail {

    public String vehicleType, vehicleAvail, vehiclePlateNo, vehicleImage, reqStatus, userId;


    public vehicleDetail(){

    }

    public vehicleDetail(String vehicleType, String vehicleAvail, String vehiclePlateNo, String vehicleImage, String reqStatus, String userId) {
        this.vehicleType = vehicleType;
        this.vehicleAvail = vehicleAvail;
        this.vehiclePlateNo = vehiclePlateNo;
        this.vehicleImage = vehicleImage;
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

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleAvail() {
        return vehicleAvail;
    }

    public void setVehicleAvail(String vehicleAvail) {
        this.vehicleAvail = vehicleAvail;
    }

    public String getVehiclePlateNo() {
        return vehiclePlateNo;
    }

    public void setVehiclePlateNo(String vehiclePlateNo) {
        this.vehiclePlateNo = vehiclePlateNo;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }
}
