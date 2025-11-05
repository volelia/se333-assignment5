![build](https://github.com/volelia/se333-assignment5/actions/workflows/SE333_CI.yml/badge.svg)
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
```
---
# part 2: automated testing with github actions

## overview
this part adds a github actions workflow that automatically runs static analysis and unit tests every time code is pushed to the **main** branch.

## workflow summary
the workflow file is located at:  
`.github/workflows/SE333_CI.yml`

**steps performed:**
1. check out the repository
2. set up java 17 on ubuntu
3. run checkstyle during the validate phase
4. upload the `checkstyle.xml` report as an artifact
5. run junit tests with jacoco coverage
6. upload the `jacoco.xml` report as an artifact

## generated artifacts
- `target/checkstyle.xml` → static analysis report
- `target/site/jacoco/jacoco.xml` → coverage report

## notes
- checkstyle is configured to not fail the build on warnings
- jacoco automatically runs after tests
- the workflow badge at the top updates automatically after every push
