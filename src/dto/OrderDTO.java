package dto;

import java.time.LocalDate;

/**
 * @author : Gihan Madhusankha
 * 2022-05-31 9:24 AM
 **/

public class OrderDTO {
    private String orderID;
    private LocalDate orderDate;
    private String id;

    public OrderDTO() {
    }

    public OrderDTO(String orderID, LocalDate orderDate, String id) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderID='" + orderID + '\'' +
                ", orderDate=" + orderDate +
                ", id='" + id + '\'' +
                '}';
    }
}
