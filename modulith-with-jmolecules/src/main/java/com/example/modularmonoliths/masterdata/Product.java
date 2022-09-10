package com.example.modularmonoliths.masterdata;

import java.util.UUID;

import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.data.annotation.Version;

import com.example.modularmonoliths.common.type.AbstractAggregate;
import com.example.modularmonoliths.masterdata.Product.ProductIdentifier;
import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Product extends AbstractAggregate<Product, ProductIdentifier> {

    private final ProductIdentifier id;

    @Version
    private int version;

    private String name;

    private ProductState state = ProductState.ACTIVE;

    public static Product create(String name) {
        val id = ProductIdentifier.random();
        val result = new Product(id);
        result.name = name;
        result.registerEvent(ProductCreated.of(id));
        return result;
    }

    public Product rename(String newName) {
        registerEvent(ProductNameChanged.builder()
                .productId(id)
                .oldName(name)
                .newName(newName)
                .build());
        name = newName;
        return this;
    }

    public Product discontinue() {
        registerEvent(ProductDiscontinued.of(id));
        state = ProductState.DISCONTINUED;
        return this;
    }

    // --------------------------------------------------------------------------------------------

    @Value(staticConstructor = "of")
    @Accessors(fluent = true)
    public static class ProductIdentifier implements Identifier {

    	@NonNull 
    	@Column(name = "id")
    	String stringValue;
    	
    	@JsonCreator
    	ProductIdentifier(String stringValue) {
    		this.stringValue = stringValue;
    	}
    	
        public static ProductIdentifier random() {
			return ProductIdentifier.of(UUID.randomUUID().toString());
		}

		@Override
        public String toString() {
            return stringValue;
        }
    }

    @Value
    @Builder
    public static class ProductNameChanged implements DomainEvent {
        @NonNull ProductIdentifier productId;
        @NonNull String oldName;
        @NonNull String newName;
    }

    @Value(staticConstructor = "of")
    public static class ProductCreated implements DomainEvent {
        @NonNull ProductIdentifier productId;
    }

    @Value(staticConstructor = "of")
    public static class ProductDiscontinued implements DomainEvent {
        @NonNull ProductIdentifier productId;
    }

    public enum ProductState {
        ACTIVE,
        DISCONTINUED
    }

}

