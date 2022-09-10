package com.example.modularmonoliths.productionorders;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface ProductionOrders extends CrudRepository<ProductionOrder, UUID> {

}
