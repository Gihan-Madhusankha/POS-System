package entity;

import java.time.LocalDate;

/**
 * @author : Gihan Madhusankha
 * 2022-06-04 2:19 PM
 **/

public class Orders {
    private String orderID;
    private LocalDate orderDate;
    private String cusID;

    public Orders() {
    }

    public Orders(String orderID, LocalDate orderDate, String cusID) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.cusID = cusID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getCusID() {
        return cusID;
    }

    public void setCusID(String cusID) {
        this.cusID = cusID;
    }
}
