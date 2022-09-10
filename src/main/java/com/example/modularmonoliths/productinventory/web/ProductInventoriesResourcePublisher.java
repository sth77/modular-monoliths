package com.example.modularmonoliths.productinventory.web;

import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class ProductInventoriesResourcePublisher implements RepresentationModelProcessor<RepositoryLinksResource> {

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource linksResource) {
        return linksResource.add(linkTo(methodOn(ProductInventoryController.class).findAll()).withRel(ProductInventoryController.RESOURCE_REL));
    }

}
