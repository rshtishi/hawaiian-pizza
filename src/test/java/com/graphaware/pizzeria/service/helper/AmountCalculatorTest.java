package com.graphaware.pizzeria.service.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.graphaware.pizzeria.model.Pizza;
import com.graphaware.pizzeria.repository.PizzeriaUserRepository;
import com.graphaware.pizzeria.repository.PurchaseRepository;
import com.graphaware.pizzeria.service.PurchaseService;

class AmountCalculatorTest {

	private static Stream<Arguments> provideInpoutForComputeAmount() {
		Pizza margarita = new Pizza();
		margarita.setId(1L);
		margarita.setName("Margarita");
		margarita.setPrice(10.00);
		margarita.setToppings(new ArrayList<String>(Arrays.asList("Tomato", "mozzarella")));
		Pizza pinneaple = new Pizza();
		pinneaple.setId(1L);
		pinneaple.setName("Pineapple");
		pinneaple.setPrice(20.00);
		pinneaple.setToppings(new ArrayList<String>(Arrays.asList("pineapple", "ham", "tomato", "cheese")));
		Pizza pepperoni = new Pizza();
		pepperoni.setId(1L);
		pepperoni.setName("Peperoni");
		pepperoni.setPrice(15.00);
		pepperoni.setToppings(new ArrayList<String>(Arrays.asList("Tomato", "mozzarella", "pepperoni")));
		return Stream.of(Arguments.of(new ArrayList<Pizza>(), 0.0),
				Arguments.of(new ArrayList<Pizza>(Arrays.asList(margarita)), margarita.getPrice()),
				Arguments.of(new ArrayList<Pizza>(Arrays.asList(margarita, pinneaple)),
						(margarita.getPrice() * 0.9 + pinneaple.getPrice())),
				Arguments.of(new ArrayList<Pizza>(Arrays.asList(margarita, pinneaple, pepperoni)),
						(pepperoni.getPrice() * 0.9 + pinneaple.getPrice())),
				Arguments.of(new ArrayList<Pizza>(Arrays.asList(margarita, margarita, margarita)),
						(2 * margarita.getPrice())),
				Arguments.of(new ArrayList<Pizza>(Arrays.asList(margarita, margarita, pinneaple)),
						(1 * margarita.getPrice() * 0.9 + pinneaple.getPrice())),
				Arguments.of(new ArrayList<Pizza>(Arrays.asList(margarita, margarita, margarita, pinneaple)),
						(2 * margarita.getPrice() * 0.9 + pinneaple.getPrice())),
				Arguments.of(new ArrayList<Pizza>(Arrays.asList(margarita, margarita, margarita, pepperoni, pepperoni)),
						(2 * margarita.getPrice() + 2 * pepperoni.getPrice())));
	}

	@ParameterizedTest
	@MethodSource("provideInpoutForComputeAmount")
	void shouldComputeCorrectAmountForProvidedInput(List<Pizza> pizzas, Double amount) {
		// setup
		PurchaseService purchaseService = mock(PurchaseService.class);
		// execute
		Double amountComputed = new AmountCalculator(purchaseService, pizzas).compute();
		// verify
		assertThat(amountComputed).isEqualTo(amount);
	}
}
