package com.miu.ea.goodpeople.service.dto;


import java.util.List;

public class ProductResponse {

    private List<Products> products;

    public ProductResponse() {
    }

    public ProductResponse(List<Products> products) {
        this.products = products;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
}
