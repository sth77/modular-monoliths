/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2022.
 */

package com.example.modularmonoliths.productionorders;

import com.example.modularmonoliths.common.exception.EntityNotFoundException;
import com.example.modularmonoliths.common.type.Quantity;
import com.example.modularmonoliths.masterdata.Product.ProductIdentifier;
import com.example.modularmonoliths.masterdata.Products;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductionOrderService {

    private final Products products;
    private final ProductionOrders productionOrders;

    public ProductionOrder createOrder(String name, ProductIdentifier productId, Quantity quantityToProduce) {
        val product = products.findById(productId.uuidValue())
                .orElseThrow(() -> new EntityNotFoundException("Product with ID " + productId + " not found"));
        val result = ProductionOrder.create(name, product, quantityToProduce);
        return productionOrders.save(result);
    }

}
