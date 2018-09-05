package com.info.inet.pm;

public class driverDetail {

    public String driverName, driverLicense,driverAdhaar, driverCity, driverPhone, driverImage, reqStatus, userId;

    public driverDetail(String driverName, String driverLicense, String driverAdhaar, String driverCity, String driverPhone, String driverImage, String reqStatus, String userId) {
        this.driverName = driverName;
        this.driverLicense = driverLicense;
        this.driverAdhaar = driverAdhaar;
        this.driverCity = driverCity;
        this.driverPhone = driverPhone;
        this.driverImage = driverImage;
        this.reqStatus = reqStatus;
        this.userId = userId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public String getDriverAdhaar() {
        return driverAdhaar;
    }

    public void setDriverAdhaar(String driverAdhaar) {
        this.driverAdhaar = driverAdhaar;
    }

    public String getDriverCity() {
        return driverCity;
    }

    public void setDriverCity(String driverCity) {
        this.driverCity = driverCity;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
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

    public  driverDetail()
    {

    }


}
