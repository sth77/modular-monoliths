package com.example.modularmonoliths.masterdata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface Products extends CrudRepository<Product, UUID> {

}
