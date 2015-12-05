# WalmartAutomateTest
This is a automation testing project for www.walmart.com. This project is build using Java, IntelliJ IDE, Maven and Selenium WebDriver. The automation testing covers the scenario: login account, search item, add item to cart and verify added item in cart. 
Copyright by Huan Chang 12/04/2015.  
Test data used in this automate test is private Walmart account not allowed to be used in any other circumstances. 

##General Usage Guide
-----------------------------------------------
1. Clone the WalmartAutomateTest repository by 
```
    git clone https://github.com/huanchang/WalmartAutomateTest
```

2. Import project walmartinterviewproject into IntelliJ IDE.
  * Open IntelliJ IDE. Probably you could also use Eclipse IDE, similar process to import maven project.
  * Select "Import Project".
  * Go to the project directory and Choose folder "walmartinterviewproject".
  * Select "Import project from external model" and then select "Maven". 
  * Checkout "Import Maven project automatically" and select "Next".
  * Checkout "huanchang.walmart::walmartassignment:1.0-SNAPSHOT" and select "NEXT".
  * Select "Next" and then "Finish".

3. Compile project ocdes
  * In header bar, click "Build" and then select "Make Project".
  * In header bar, click "Build" and then select "Make module walmart assignment". Should see everything updated.

4. Run automation testing
  * In header bar, click "Run" and then select "Run..."
  * Then, you should see only one runnable test "WalmartFrontEndTestng" in popup window. Select it or hit return button.
  * Afterward, the automate test will start to run and a chrome browser should be opened then.
  * The detail and log information will show in the Run window.

After finishing all tests, the results will shown in Run window and the chrome browser will be closed automatically by automation test.
![alt tag](https://github.com/huanchang/WalmartAutomateTest/blob/version1/Example_FinishTesting.png)

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

## Future word
-----------------------------------------------
One of the challenging for UI testing using Selenium WebDriver is time issue, like page loading, refreshing and slow responding. I used WebDriverWait to add some implicit waiting into the test. However, these implicit waiting is performed based on the response from remote webdriver and the UI element. 

This test case is implemented within two days with limited information from the webpage frame and codes. It covers several possible cases in the test scenario but not fully like if the product is sold out. If there's more time, I want to build a more complete test automation frames and encapsulate common used functions into classed.


