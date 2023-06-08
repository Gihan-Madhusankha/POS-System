package entity;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 2:17 PM
 **/

public class Customer {
    private String cusID;
    private String cusTitle;
    private String cusName;
    private String cusAddress;
    private String city;
    private String province;
    private String postalCode;

    public Customer() {
    }

    public Customer(String cusID, String cusTitle, String cusName, String cusAddress, String city, String province, String postalCode) {
        this.cusID = cusID;
        this.cusTitle = cusTitle;
        this.cusName = cusName;
        this.cusAddress = cusAddress;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }

    public String getCusID() {
        return cusID;
    }

    public void setCusID(String cusID) {
        this.cusID = cusID;
    }

    public String getCusTitle() {
        return cusTitle;
    }

    public void setCusTitle(String cusTitle) {
        this.cusTitle = cusTitle;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
