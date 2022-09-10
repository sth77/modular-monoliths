package com.example.modularmonoliths.productionorder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.moduliths.test.ModuleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.modularmonoliths.common.type.Quantity;
import com.example.modularmonoliths.masterdata.Product;
import com.example.modularmonoliths.masterdata.Products;
import com.example.modularmonoliths.productionorder.ProductionOrder.ProductionOrderState;

import lombok.val;

@ModuleTest
@SpringBootTest
@AutoConfigureMockMvc
class ProductionOrderModuleTests {

	@Autowired
	MockMvc mvc;

	@Autowired
	ProductionOrders productionOrders;

	@MockBean
	Products products;

	Product product1 = Product.create("Product 1");
	Product product2 = Product.create("Product 2");

	@BeforeEach
	void setupMocks() {
		lenient().when(products.findAll()).thenReturn(List.of(product1, product2));
		Stream.of(product1, product2).forEach(product -> lenient().when(products.findById(product.getId().uuidValue()))
				.thenReturn(Optional.of(product)));
	}

	@Test
	void getAllProductionOrders() throws Exception {
		val initialCount = (int) productionOrders.count();

		val productionOrder1 = createOrder("ProductionOrder 1", product1, Quantity.of(5));
		productionOrders.save(productionOrder1);

		val productionOrder2 = createOrder("ProductionOrder 2", product2, Quantity.of(10));
		productionOrders.save(productionOrder2);

		// act & assert
		mvc.perform(MockMvcRequestBuilders.get("/api/productionOrders").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded['productionOrders']", Matchers.hasSize(initialCount + 2)));
	}

	@Test
	public void createOrder() throws Exception {
		val body = """
				    {
				        "name": "Test-Order",
				        "productIdentifier": "pid",
				        "quantityToProduce": 5
				    }
				""".replace("pid", product1.getId().uuidValue().toString());
		productionOrders.deleteAll();
		
		// act
		mvc.perform(post("/api/productionOrders")
				.contentType("application/json")
				.content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links['self']").exists())
				.andExpect(jsonPath("$._links['submit'].href").value(endsWith("/submit")))
				.andExpect(jsonPath("$._links['accept']").doesNotExist());
		
		// assert
		assertThat(productionOrders.findAll())
			.hasSize(1)
			.first()
				.hasFieldOrPropertyWithValue("name", "Test-Order")
				.hasFieldOrPropertyWithValue("product", AggregateReference.to(product1.getId().uuidValue()))
				.hasFieldOrPropertyWithValue("quantityToProduce", Quantity.of(5))
				.hasFieldOrPropertyWithValue("state", ProductionOrderState.DRAFT);

	}

	private static ProductionOrder createOrder(String name, Product product, Quantity quantityToProduce) {
		return ProductionOrder.create(name, product, quantityToProduce);
	}
}
