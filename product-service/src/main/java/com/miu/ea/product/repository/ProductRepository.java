package com.miu.ea.product.repository;

import com.miu.ea.product.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

    List<Products> findProductByRequestId(Long requestId);

}
