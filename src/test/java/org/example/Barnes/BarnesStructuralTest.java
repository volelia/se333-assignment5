package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BarnesStructuralTest {

    @DisplayName("structural-based")
    @Test
    void null_order_returns_null_branch() {
        // arrange
        BarnesAndNoble bn = new BarnesAndNoble(isbn -> new Book("x", 1, 1), (b, q) -> {});
        // act + assert
        assertNull(bn.getPriceForCart(null));
    }

    @DisplayName("structural-based")
    @Test
    void request_exceeding_stock_calls_process_with_capped_qty_branch() {
        // arrange
        final int[] calls = {0};
        BookDatabase db = isbn -> new Book("333", 20, 1);
        BuyBookProcess process = (book, qty) -> calls[0] = qty;
        BarnesAndNoble bn = new BarnesAndNoble(db, process);

        Map<String, Integer> order = new HashMap<>();
        order.put("333", 3);

        // act
        PurchaseSummary summary = bn.getPriceForCart(order);

        // assert
        assertEquals(1, calls[0]);
        assertEquals(20, summary.getTotalPrice());
    }
}
