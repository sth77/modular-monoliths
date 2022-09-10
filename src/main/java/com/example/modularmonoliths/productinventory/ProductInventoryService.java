/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2022.
 */

package com.example.modularmonoliths.productinventory;

import com.example.modularmonoliths.common.exception.EntityNotFoundException;
import com.example.modularmonoliths.common.type.Source;
import com.example.modularmonoliths.masterdata.Product.ProductIdentifier;
import com.example.modularmonoliths.masterdata.Products;
import com.example.modularmonoliths.productionorders.event.ProductionOrderCompleted;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInventoryService {

    private final ProductInventories productInventories;
    private final Products products;

    @EventListener
    void on(ProductionOrderCompleted event) {
        val inventory = productInventories.findById(event.getProductIdentifier().getId())
                .orElseGet(() -> this.createInventory(event.getProductIdentifier()));
        productInventories.save(inventory.replenish(
                event.getProducedQuantity(),
                Source.of("ProductionOrder " + event.getProductionOrderId())));
    }

    private ProductInventory createInventory(ProductIdentifier productId) {
        return products.findById(productId.uuidValue())
                .map(ProductInventory::createFor)
                .map(productInventories::save)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID " + productId + " not found"));
    }

}
