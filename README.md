# WalmartAutomateTest
This is a automation testing project for www.walmart.com. This project is build using Java, IntelliJ IDE, Maven and Selenium WebDriver. The automation testing covers the scenario: login account, search item, add item to cart and verify added item in cart. 
Copyright by Huan Chang 12/04/2015.  
Test data used in this automate test is private Walmart account not allowed to be used in any other circumstances. 

##General Usage Guide
-----------------------------------------------



##Approach
-----------------------------------------------
My approach is to first run the software to be tested for sets of input files and command-line arguments, and save the output files. These output files serve as a test oracle, a means of determining whether a test has passed or failed.

Then, I write the automated test covering the following scenario:
* Login using existing account
* Perform a search on home page from a pool of key words given below
* Identify an item from the result set that you can add to cart
* Add the item to cart
* Validate that item added is present in the cart and is the only item in the cart

##Project Structure
-----------------------------------------------
This project is constructed using Maven. Here's an brief introduction of main files. 

* WalmartFrontEndTestng.java: core code of this automate test.
* Util.java: reusable codes for handling web element operations
* WaitEvents.java: functions used to wait for the expected conditions to be satisfied. 
* pom.xml: the configuration file of this maven project
* log4j.xml: the configuration file of logger


##Test Process
-----------------------------------------------
Preparation before test:
  Before all tests, I create a new chrome web driver and navigate to walmart default page(www.walmart.com). Then, I call the login() function to log in with my test account.
  
Test:
* Enter the search item in header search field and click search button.
* Select the first item in searching result list. Browser will navigate to the product page after clicking the title of product.
* In the product page, get the title of the product for verifying product in cart.
* Check if there's any variants needed to be selected. If so, then select the first choice in each variant list. Finally, click the "add to cart" button. Check if the product is added successfully and click "view cart" button in the hover modal. 
* Navigate to cart page if current page is not cart page. Get all cart items, fail the test case under these cases:
  -  Cart is empty
  -  Found more than one types of items in cart
  -  Found an item with the title which is not the same as added product
  -  The quantity of cart item is not equal to 1

Totally, there are 6 search items to be tested, so each test covers one from the search items pool. BeforeMethod and AfterMethod function update and show the index of current invocation. 

The added item is removed in AfterMethod function to make sure the cart is empty before next test.

##Test Data
-----------------------------------------------
* Walmart account: see details in WalmartFrontEndTestng.java.
* Search items pool: test runs once for each item
```
  private static final String[] TEST_SEARCH_ITEMS = {"iPhone", "dvd", "socks", "tv", "toys", "iPhone"}
```


