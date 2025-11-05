package org.example.Amazon;

import org.example.Amazon.Cost.DeliveryPrice;
import org.example.Amazon.Cost.ExtraCostForElectronics;
import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.example.Amazon.Cost.RegularCost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// simple in-memory cart for unit tests (no database)
class FakeCart implements ShoppingCart {
    private final List<Item> items = new ArrayList<>();
    @Override public void add(Item item) { items.add(item); }
    @Override public List<Item> getItems() { return items; }
    @Override public int numberOfItems() { return items.size(); }
}

class AmazonUnitTest {

    @DisplayName("unit")
    @Test
    void empty_cart_cost_is_zero() {
        // arrange
        ShoppingCart cart = new FakeCart();
        List<PriceRule> rules = List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics());
        Amazon amazon = new Amazon(cart, rules);

        // act
        double total = amazon.calculate();

        // assert
        assertEquals(0.0, total, 0.0001);
    }

    @DisplayName("unit")
    @Test
    void regular_cost_is_sum_of_price_times_qty() {
        // arrange
        ShoppingCart cart = new FakeCart();
        cart.add(new Item(ItemType.OTHER, "pencils", 3, 1.50)); // 4.50
        cart.add(new Item(ItemType.OTHER, "notebook", 2, 2.00)); // 4.00
        List<PriceRule> rules = List.of(new RegularCost()); // only base price
        Amazon amazon = new Amazon(cart, rules);

        // act
        double total = amazon.calculate();

        // assert
        assertEquals(8.50, total, 0.0001);
    }

    @DisplayName("unit")
    @Test
    void delivery_price_tiers_apply_by_item_count() {
        // arrange
        ShoppingCart cart = new FakeCart();
        cart.add(new Item(ItemType.OTHER, "a", 1, 1.0));
        cart.add(new Item(ItemType.OTHER, "b", 1, 1.0));
        cart.add(new Item(ItemType.OTHER, "c", 1, 1.0)); // size = 3 -> $5 delivery
        List<PriceRule> rules = List.of(new DeliveryPrice());
        Amazon amazon = new Amazon(cart, rules);

        // act
        double fee = amazon.calculate();

        // assert
        assertEquals(5.0, fee, 0.0001);

        // 4 items -> 12.5
        cart.add(new Item(ItemType.OTHER, "d", 1, 1.0));
        assertEquals(12.5, new Amazon(cart, rules).calculate(), 0.0001);
    }

    @DisplayName("unit")
    @Test
    void extra_electronics_fee_applies_once_if_any_electronic_present() {
        // arrange
        ShoppingCart cart = new FakeCart();
        cart.add(new Item(ItemType.OTHER, "book", 1, 10.0));
        cart.add(new Item(ItemType.ELECTRONIC, "mouse", 1, 20.0));
        List<PriceRule> rules = List.of(new ExtraCostForElectronics());
        Amazon amazon = new Amazon(cart, rules);

        // act
        double fee = amazon.calculate();

        // assert
        assertEquals(7.50, fee, 0.0001);
    }

    @DisplayName("unit")
    @Test
    void combined_rules_add_up_regular_delivery_and_extra() {
        // arrange
        ShoppingCart cart = new FakeCart();
        cart.add(new Item(ItemType.ELECTRONIC, "headphones", 2, 30.0)); // regular = 60
        cart.add(new Item(ItemType.OTHER, "sticker", 5, 1.0));          // regular = 5
        // cart size = 2 items -> delivery = 5
        // has electronic -> extra = 7.5
        List<PriceRule> rules = List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics());
        Amazon amazon = new Amazon(cart, rules);

        // act
        double total = amazon.calculate();

        // assert
        assertEquals(60 + 5 + 7.5 + 5 /* five stickers counted in regular above? wait */,
                total, 0.0001);
        // fix the math: regular = (2*30) + (5*1) = 60 + 5 = 65
        // expected total = 65 + 5 (delivery) + 7.5 (electronics) = 77.5
        assertEquals(77.5, total, 0.0001);
    }
}
