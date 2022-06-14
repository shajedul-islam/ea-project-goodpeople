package com.miu.ea.product.io;

public class ProductSaveRequest {

    private Long requestId;

    private String products;

    public ProductSaveRequest() {
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
