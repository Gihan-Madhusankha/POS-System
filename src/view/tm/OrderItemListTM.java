package view.tm;

/**
 * @author : Gihan Madhusankha
 * 2022-06-01 12:04 AM
 **/

public class OrderItemListTM {
    private String orderID;
    private String itemCode;
    private String description;
    private String packSize;
    private double unitPrice;
    private double discount;
    private int qty;
    private double total;
    private String date;
    private int qtyOnHand;

    public OrderItemListTM() {
    }

    public OrderItemListTM(String itemCode, String description, int qty) {
        this.itemCode = itemCode;
        this.description = description;
        this.qty = qty;
    }

    public OrderItemListTM(String date, double total) {
        this.date = date;
        this.total = total;
    }

    public OrderItemListTM(String itemCode, String description, double unitPrice, int qty, double total) {
        this.itemCode = itemCode;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.total = total;
    }

    public OrderItemListTM(String itemCode, String description, String packSize, double unitPrice, double discount, int qty, double total) {
        this.itemCode = itemCode;
        this.description = description;
        this.packSize = packSize;
        this.unitPrice = unitPrice;
        this.setDiscount(discount);
        this.qty = qty;
        this.total = total;
    }

    public OrderItemListTM(String orderID, String itemCode, String description, String packSize, double unitPrice, double discount, int qty, double total, String date, int qtyOnHand) {
        this.orderID = orderID;
        this.itemCode = itemCode;
        this.description = description;
        this.packSize = packSize;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.qty = qty;
        this.total = total;
        this.date = date;
        this.qtyOnHand = qtyOnHand;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public String toString() {
        return "OrderItemListTM{" +
                "orderID='" + orderID + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", description='" + description + '\'' +
                ", packSize='" + packSize + '\'' +
                ", unitPrice=" + unitPrice +
                ", discount=" + discount +
                ", qty=" + qty +
                ", total=" + total +
                ", date='" + date + '\'' +
                '}';
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }
}
