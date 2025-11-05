package org.example.Amazon;

import org.example.Amazon.Cost.DeliveryPrice;
import org.example.Amazon.Cost.ExtraCostForElectronics;
import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.example.Amazon.Cost.RegularCost;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// integration test using the real Database + ShoppingCartAdaptor (HSQLDB in-memory)
class AmazonIntegrationTest {

    private Database db;
    private ShoppingCartAdaptor cart;

    @BeforeEach
    void setUp() {
        db = new Database();
        db.resetDatabase();
        cart = new ShoppingCartAdaptor(db);
    }

    @AfterEach
    void tearDown() {
        db.close();
    }

    @DisplayName("integration")
    @Test
    void full_flow_with_real_database_and_rules() {
        // arrange
        cart.add(new Item(ItemType.OTHER, "notebook", 2, 2.00));          // 4.00
        cart.add(new Item(ItemType.ELECTRONIC, "keyboard", 1, 25.00));    // 25.00
        // cart size = 2 items -> delivery = 5.0
        // extra electronics = 7.5
        List<PriceRule> rules = List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics());
        Amazon amazon = new Amazon(cart, rules);

        // act
        double total = amazon.calculate();

        // assert
        assertEquals(4.00 + 25.00 + 5.0 + 7.5, total, 0.0001);

        // also verify data came from DB-backed cart
        assertEquals(2, cart.getItems().size());
        // NOTE: ShoppingCartAdaptor.numberOfItems() uses getFetchSize() (not row count).
        // It may not return the actual count. So we assert using getItems().size().
    }

    @DisplayName("integration")
    @Test
    void empty_cart_in_db_gives_zero_total() {
        // arrange
        List<PriceRule> rules = List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics());
        Amazon amazon = new Amazon(cart, rules);

        // act
        double total = amazon.calculate();

        // assert
        assertEquals(0.0, total, 0.0001);
        assertEquals(0, cart.getItems().size());
    }
}
