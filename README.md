# se333 assignment 5  part 1: project setup + testing practice

**student:** jillian mendez  
**course:** se 333 - software testing  
**quarter:** fall 2025

## overview
this project practices writing and organizing unit tests for the barnes and noble system. i wrote both specification-based (black-box) tests and structural-based (white-box) tests.

## testing summary
### specification-based
- verifies total price is calculated correctly when books are in stock
- verifies partial availability when requested > stock, with correct total and unavailable record

### structural-based
- checks that a `null` order returns `null`
- checks that when requested > stock, the purchase caps at available qty and calls `buyBook` once

each test is labeled with
```java
@DisplayName("specification-based")
@DisplayName("structural-based")
