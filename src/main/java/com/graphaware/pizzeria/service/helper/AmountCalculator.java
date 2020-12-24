package com.graphaware.pizzeria.service.helper;

import java.util.Comparator;
import java.util.List;

import com.graphaware.pizzeria.model.Pizza;
import com.graphaware.pizzeria.service.PurchaseService;

public class AmountCalculator {

	private static final double DISCOUNT_FOR_PINNEAPLE_PIZZA_ORDER = 0.9;
	private static final int PIZZA_NO_FOR_DISCOUNT_APPLY = 3;

	private PurchaseService purchaseService;
	private List<Pizza> pizzas;
	private double totalPrice = 0;
	private boolean applyPineappleDiscount = false;
	private Pizza freePizza = null;

	public AmountCalculator(PurchaseService purchaseService, List<Pizza> pizzas) {
		this.purchaseService = purchaseService;
		this.pizzas = pizzas;
	}

	public Double compute() {
		if (pizzas == null || pizzas.isEmpty()) {
			return 0.0;
		}
		// buy a pineapple pizza, get 10% off the others
		applyPineappleDiscount = pizzas.stream().anyMatch(pizza -> pizza.getToppings().contains("pineapple"));
		pizzas.stream().forEach(pizza -> {
			if (pizza.getToppings().contains("pineapple")) {
				totalPrice += pizza.getPrice();
			} else {
				if (applyPineappleDiscount) {
					totalPrice += pizza.getPrice() * DISCOUNT_FOR_PINNEAPLE_PIZZA_ORDER;
				} else {
					totalPrice += pizza.getPrice();
				}
			}
		});
		// order 3 or more pizza and get the cheapest one free
		if (pizzas.size() >= PIZZA_NO_FOR_DISCOUNT_APPLY) {
			freePizza = pizzas.stream().min(Comparator.comparing(Pizza::getPrice)).get();
			totalPrice -= (!freePizza.getToppings().contains("pineapple") && applyPineappleDiscount)
					? freePizza.getPrice() * DISCOUNT_FOR_PINNEAPLE_PIZZA_ORDER
					: freePizza.getPrice();
		}
		return totalPrice;
	}

}
