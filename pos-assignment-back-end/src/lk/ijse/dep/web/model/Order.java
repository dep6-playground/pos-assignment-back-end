package lk.ijse.dep.web.model;

public class Order {
    private String orderId;
    private String customerId;
    private String orderTotal;

    public Order() {
    }

    public Order(String orderId, String customerId, String orderTotal) {
        this.setOrderId(orderId);
        this.setCustomerId(customerId);
        this.setOrderTotal(orderTotal);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", orderTotal='" + orderTotal + '\'' +
                '}';
    }
}
