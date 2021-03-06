/*
 * Copyright (c) 2020
 * @ uvin6667
 *
 */

package lk.ijse.dep.web.model;

public class OrderItem {
    private String orderId;
    private String customerId;
    private String itemCode;
    private String qty;
    private String unitPrice;
    private String subTotal;

    public OrderItem() {
    }

    public OrderItem(String orderId, String customerId, String itemCode, String qty, String unitPrice, String subTotal) {
        this.setOrderId(orderId);
        this.setCustomerId(customerId);
        this.setItemCode(itemCode);
        this.setQty(qty);
        this.setUnitPrice(unitPrice);
        this.setSubTotal(subTotal);
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", qty='" + qty + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", subTotal='" + subTotal + '\'' +
                '}';
    }
}
