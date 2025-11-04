package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BarnesSpecTest {

    @DisplayName("specification-based")
    @Test
    void sums_total_for_items_in_stock_spec() {
        // arrange
        BookDatabase db = isbn -> new Book("111", 10, 5);
        BuyBookProcess process = (book, qty) -> {};
        BarnesAndNoble bn = new BarnesAndNoble(db, process);

        Map<String, Integer> order = new HashMap<>();
        order.put("111", 3);

        // act
        PurchaseSummary summary = bn.getPriceForCart(order);

        // assert
        assertEquals(30, summary.getTotalPrice());
        assertTrue(summary.getUnavailable().isEmpty());
    }

    @DisplayName("specification-based")
    @Test
    void partially_unavailable_items_are_capped_and_recorded_spec() {
        // arrange
        BookDatabase db = isbn -> new Book("222", 15, 2);
        BuyBookProcess process = (book, qty) -> {};
        BarnesAndNoble bn = new BarnesAndNoble(db, process);

        Map<String, Integer> order = new HashMap<>();
        order.put("222", 4);

        // act
        PurchaseSummary summary = bn.getPriceForCart(order);

        // assert
        assertEquals(30, summary.getTotalPrice()); // 2 * 15
        assertEquals(1, summary.getUnavailable().size());
    }
}
