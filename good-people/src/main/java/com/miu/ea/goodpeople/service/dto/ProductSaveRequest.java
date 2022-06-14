package com.miu.ea.goodpeople.service.dto;

public class ProductSaveRequest {

    private Long requestId;

    private String products;

    public ProductSaveRequest() {
    }
    
    

    public ProductSaveRequest(Long requestId, String products) {
		super();
		this.requestId = requestId;
		this.products = products;
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
