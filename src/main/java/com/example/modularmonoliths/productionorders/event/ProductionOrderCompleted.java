/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2022.
 */

package com.example.modularmonoliths.productionorders.event;

import com.example.modularmonoliths.common.event.DomainEvent;
import com.example.modularmonoliths.masterdata.Product.ProductIdentifier;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ProductionOrderCompleted implements DomainEvent {

    @NonNull
    UUID productionOrderId;

    @NonNull
    ProductIdentifier productIdentifier;

    @NonNull
    int producedQuantity;

}
