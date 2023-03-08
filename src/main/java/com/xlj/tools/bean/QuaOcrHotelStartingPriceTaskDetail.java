package com.xlj.tools.bean;


public class QuaOcrHotelStartingPriceTaskDetail {

    public static final String COUNTRY_PATH = "country";
    public static final String PROVINCE_PATH = "province";
    public static final String CITY_PATH = "city";
    public static final String AREA_PATH = "area";

    // 是否按城市等build
    private boolean needBuildByCity;
    private String buildPath;
    private String province;
    private String cityId;
    private String cityName;
    private String hotelId;
    private String hotelName;
    private String checkInDate;
    private String checkOutDate;
    private String hotelAddress;
    private String shareShortLink;
    private String sharePassword;
    public String type = QuaOcrHotelStartingPriceTaskDetail.class.getSimpleName();
    public String resultType = "QuaOcrHotelStartingPriceResultDetail";
    private int isundercarriage = -1;// 是否下架，是1，否0
    private int canBooking = -1;// 是否可订，是1，否0，判断暂无报价
    private int isLinkInvalidate = -1;// 短链接是否失效，是1，否0
    private String hotelStatusDesc;// 可订状态描述
    private String laterPay;// 信用住

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public boolean isNeedBuildByCity() {
        return needBuildByCity;
    }

    public void setNeedBuildByCity(boolean needBuildByCity) {
        this.needBuildByCity = needBuildByCity;
    }

    public String getBuildPath() {
        return buildPath;
    }

    public void setBuildPath(String buildPath) {
        this.buildPath = buildPath;
    }

    public String getLaterPay() {
        return laterPay;
    }

    public void setLaterPay(String laterPay) {
        this.laterPay = laterPay;
    }

    public String getHotelStatusDesc() {
        return hotelStatusDesc;
    }

    public void setHotelStatusDesc(String hotelStatusDesc) {
        this.hotelStatusDesc = hotelStatusDesc;
    }

    public int getIsundercarriage() {
        return isundercarriage;
    }

    public void setIsundercarriage(int isundercarriage) {
        this.isundercarriage = isundercarriage;
    }

    public int getCanBooking() {
        return canBooking;
    }

    public void setCanBooking(int canBooking) {
        this.canBooking = canBooking;
    }

    public int getIsLinkInvalidate() {
        return isLinkInvalidate;
    }

    public void setIsLinkInvalidate(int isLinkInvalidate) {
        this.isLinkInvalidate = isLinkInvalidate;
    }


    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getShareShortLink() {
        return shareShortLink;
    }

    public void setShareShortLink(String shareShortLink) {
        this.shareShortLink = shareShortLink;
    }

    public String getSharePassword() {
        return sharePassword;
    }

    public void setSharePassword(String sharePassword) {
        this.sharePassword = sharePassword;
    }

    @Override
    public String toString() {
        return "QuaOcrHotelStartingPriceTaskDetail{" +
                "needBuildByCity=" + needBuildByCity +
                ", buildPath='" + buildPath + '\'' +
                ", province='" + province + '\'' +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", hotelId='" + hotelId + '\'' +
                ", hotelName='" + hotelName + '\'' +
                ", checkInDate='" + checkInDate + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", hotelAddress='" + hotelAddress + '\'' +
                ", shareShortLink='" + shareShortLink + '\'' +
                ", sharePassword='" + sharePassword + '\'' +
                ", type='" + type + '\'' +
                ", resultType='" + resultType + '\'' +
                ", isundercarriage=" + isundercarriage +
                ", canBooking=" + canBooking +
                ", isLinkInvalidate=" + isLinkInvalidate +
                ", hotelStatusDesc='" + hotelStatusDesc + '\'' +
                ", laterPay='" + laterPay + '\'' +
                '}';
    }
}
