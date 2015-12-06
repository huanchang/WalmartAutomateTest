
import org.openqa.selenium.*;
import org.testng.Assert;

import org.apache.log4j.Logger;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by huanchang on 12/3/15.
 * Test scenario:
 * 1. Login using existing account
 * 2. Perform a search on home page from a pool of key words given below
 * 3. Identify an item from the result set that you can add to cart
 * 4. Add the item to cart
 * 5. Validate that item added is present in the cart and is the only item in the cart

 */


public class WalmartFrontEndTestng  {

    // WebElement ID
    private static final String LOGIN_EMAIL_TEXTFIELD_ID = "login-username";
    private static final String LOGIN_PWD_TEXTFIELD_ID = "login-password";
    private static final String SEARCH_TEXTFIELD_ID = "search";
    private static final String PAC_MOCAL_ID = "spa-layout";

    // WebElement ClassName
    private static final String LOGIN_BUTTON_CLASSNAME = "header-account-signin";
    private static final String LOGIN_SUBMIT_BUTTON_CLASSNAME = "login-sign-in-btn";
    private static final String SEARCHBAR_SUBMIT_BUTTON_CLASSNAME = "searchbar-submit";
    private static final String SEARCH_RESULTS_CLASSNAME = "js-product-title";
    private static final String PRODUCT_TITLE_CLASSNAME = "js-product-heading";

    private static final String ADDTOCART_BUTTON_CLASSNAME = "js-add-to-cart";
    private static final String CART_ITEM_REMOVE_CLASSNAME = "js-cart-item-remove";
    private static final String PAC_VIEWCART_CLASSNAME = "js-pac-view-cart";
    private static final String CART_ITEM_ROW_CLASSNAME = "cart-item-row";
    private static final String CART_LIST_CLASSNAME = "cart-list";
    private static final String CART_EMPTY_ITEM_CLASSNAME = "cart-list-empty";
    private static final String CART_QUANTITY_CLASSNAME = "chooser-option-current";
    private static final String VARIANTS_CLASSNAME = "js-variants";
    private static final String VARIANT_CLASSNAME = "js-variant";

    private static final String HEADER_CART_ICON_CLASSNAME = "header-cart";



    // Test account
    private static final String TEST_EMAIL = "hhuanchang@gmail.com";
    private static final String TEST_PWD = "h5201988";
    private static final String[] TEST_SEARCH_ITEMS = {"iPhone", "dvd", "socks", "tv", "toys", "iPhone"};

    // Logger
    private static final Logger log = Logger.getLogger(WalmartFrontEndTestng.class);



    private WebDriver driver;

    // Count invocation times
    private int counter;

    public void login() {
        log.info("Login with test account:" + TEST_EMAIL + ":" + TEST_PWD);

        // Find header login button and click it
        Assert.assertTrue(Util.clickElement(driver, By.className(LOGIN_BUTTON_CLASSNAME)), "Failed to click header login button.");

        // Input email and passeword to loging
        Assert.assertTrue(Util.sendKeyToTextField(driver, By.id(LOGIN_EMAIL_TEXTFIELD_ID), TEST_EMAIL), "Failed to input loging email.");
        Assert.assertTrue(Util.sendKeyToTextField(driver, By.id(LOGIN_PWD_TEXTFIELD_ID), TEST_PWD), "Failed to input password");

        // Click login SUBMIT button
        Assert.assertTrue(Util.clickElement(driver, By.className(LOGIN_SUBMIT_BUTTON_CLASSNAME)), "Failed to submit login.");

        // TODO: Failed to login, invalid pair of email and pwd
        log.info("Successfully logged in.");

        // Wait for page reloading
        WaitEvents.waitForInvisibilityOfElement(driver, By.id(LOGIN_EMAIL_TEXTFIELD_ID));

    }

    public void searchItem(String textToBeSearched) {

        log.info("Search for:" + textToBeSearched);
        // Send searching text into textfield and click search button
        Assert.assertTrue(Util.sendKeyToTextField(driver, By.id(SEARCH_TEXTFIELD_ID), textToBeSearched));
        Assert.assertTrue(Util.clickElement(driver, By.className(SEARCHBAR_SUBMIT_BUTTON_CLASSNAME)));

        log.info("Clicked search button.");

        // TODO: Handle the case that no product found by the searching string
        if (driver.getCurrentUrl().contains(textToBeSearched)) {
            log.info("Go to search result page.");
        } else {
            log.warn("Not found " + textToBeSearched + "in current url:" + driver.getCurrentUrl());
        }

    }

    public void selectAndNavigateToProductPage(int index) {

        // Find the title elements of all listed products
        List<WebElement> foundProductTitles = Util.findElementsBy(driver, By.className(SEARCH_RESULTS_CLASSNAME));

        Assert.assertNotNull(foundProductTitles, "Not found products.");

        // TODO: Navigate to the kth page of products if the given index extends the number of products listed in one page
        Assert.assertTrue(foundProductTitles.size() > index, "Found less products than requested.");

        // Get the title element of the product and click it to enter product information page
        WebElement selectedProduct = foundProductTitles.get(index);

        Util.clickElement(driver, selectedProduct);
        // Wait for page loading until the element in the searching page become stale reference
        WaitEvents.waitForStaleElement(driver, selectedProduct);

    }

    private String getProductTitle() {
        // Get product title
        WebElement productTitleElement = Util.findElementBy(driver, By.className(PRODUCT_TITLE_CLASSNAME));
        Assert.assertNotNull(productTitleElement, "Failed to get product title element.");
        WebElement titleSpan = Util.findElementByElement(productTitleElement, By.cssSelector("span"));
        Assert.assertNotNull(titleSpan, "Failed to get product title span.");
        String productTitle = titleSpan.getText();
        log.info("In product page:" + productTitle);

        return productTitle;
    }


    private void addProductIntoChart() {

        // Check if there is any variant
        List<WebElement> variantLists = Util.findElementsBy(driver, By.className(VARIANTS_CLASSNAME));

        // Wait until the the present of PAC MOCAL element which contains the information that the item has been added to cart
        if(variantLists!=null) {
            log.info("Found variants.");
            for (WebElement variantList : variantLists) {
                WebElement firstVariant = Util.findElementByElement(variantList, By.className(VARIANT_CLASSNAME));
                Assert.assertNotNull(firstVariant, "Not found a variant.");

                String variantDataId = firstVariant.getAttribute("data-id");
                Assert.assertTrue(Util.clickElement(driver, firstVariant), "Failed to select variant:" + variantDataId);
                log.info("Selected variant:"+variantDataId);
            }
        }

        // Click add to cart button
        Assert.assertTrue(Util.clickElement(driver, By.className(ADDTOCART_BUTTON_CLASSNAME)), "Failed to add item into cart.");
        log.info("Clicked product title.");
        Assert.assertTrue(WaitEvents.waitForVisibilityOfElement(driver, By.id(PAC_MOCAL_ID)), "PAC MOCAL wasn't shown after selecting variants.");

        // Click view cart button
        Util.clickElement(driver, By.className(PAC_VIEWCART_CLASSNAME));
        log.info("Clicked view cart button.");
        WaitEvents.waitForInvisibilityOfElement(driver, By.id(PAC_MOCAL_ID));

    }


    private boolean navigateToCart() {
        // Check if current url is cart page url
        if(!driver.getCurrentUrl().contains("cart")) {
            WebElement headerCartElement = Util.findElementBy(driver, By.className(HEADER_CART_ICON_CLASSNAME));
            if (Util.clickElementByElement(driver, headerCartElement, By.cssSelector("a"))) {
                if(driver.getCurrentUrl().contains("cart")) {
                    log.info("Navigate to cart page by clicking header cart icon");
                } else {
                    driver.navigate().to("http://www.walmart.com/cart");
                    log.info("Navigating to cart page by url.");
                }
                return true;
            } else {
                log.error("Failed to navigate to cart by clicking header cart icon.");
                return false;
            }
        }
        log.info("Navigated to cart page.");
        return true;
    }

    private List<WebElement> getCartItems() {
        // Check current url and navigate to cart if current url is not cart page url
        navigateToCart();

        // Get cart item list
        WebElement cartItemList = Util.findElementBy(driver, By.className(CART_LIST_CLASSNAME));
        Assert.assertNotNull(cartItemList, "Not found cart item list.");

        // Get cart-list-empty item if exists
        WebElement cartEmptyItem = null;
        try {
            cartEmptyItem = cartItemList.findElement(By.className(CART_EMPTY_ITEM_CLASSNAME));
        } catch (NoSuchElementException e) {
            // Not empty cart
        }

        // Get all cart rows in cart list
        List<WebElement> cartItemWebElements = cartItemList.findElements(By.className(CART_ITEM_ROW_CLASSNAME));

        if (cartEmptyItem == null && cartItemWebElements ==null) {
            log.error("Failed to find cart empty item or cart item.");
            Assert.fail();
        } else if (cartEmptyItem != null && cartItemWebElements.size() > 1) {
            log.error("Found both cart item and empty cart item.");
            Assert.fail();
        } else if( cartEmptyItem != null) {
            log.info("Empty cart.");
            return null;
        } else {
            return cartItemWebElements;
        }

        return null;
    }

    private void cleanChart() {
        List<WebElement> cartItemWebElements = getCartItems();

        if (cartItemWebElements == null) {
            log.info("Empty cart. No need to clean.");
        } else {
            log.info("To clean "+cartItemWebElements.size()+"item(s) in cart.");
            try {
                for (WebElement cartItemElement : cartItemWebElements) {
                    WebElement titleElement = cartItemElement.findElement(By.className(SEARCH_RESULTS_CLASSNAME));
                    String productTitle = titleElement.findElement(By.cssSelector("span")).getText();

                    if (Util.clickElementByElement(driver, cartItemElement, By.className(CART_ITEM_REMOVE_CLASSNAME))) {
                        log.info("Removed item: " + productTitle);
                    } else {
                        log.error("Failed to remove item:" + productTitle);
                    }
                }
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }

        }

    }

    // Verify the added item is the only item presented in the cart
    private void verifyAddedItemInChart(String targetProductTitle, int targetQuantity) {
        log.info("Verify added item in cart, title:"+ targetProductTitle+", quantity:"+targetQuantity);
        List<WebElement> cartItemWebElements = getCartItems();

        // Verify that the cart is not empty
        Assert.assertNotNull(cartItemWebElements, "Empty cart");

        // Verify there's only one item in cart
        Assert.assertTrue(cartItemWebElements.size()== 1, "Found more than one items in cart.");

        // Get the title of cart item
        WebElement cartItemElement = cartItemWebElements.get(0);
        WebElement titleElement = Util.findElementByElement(cartItemElement, By.className(SEARCH_RESULTS_CLASSNAME));
        Assert.assertNotNull(titleElement, "Failed to get title element of cart item.");
        String productTitle = titleElement.findElement(By.cssSelector("span")).getText();

        // Verify the title of cart item
        // Check if the product title is ended with "..." which represents that the title is cut
        // TODO: Get more information to verify the cart item
        if (productTitle.endsWith("...")) {
            // Remove "..." end string and check if it is a prefix of targetProductTitle
            Assert.assertTrue(targetProductTitle.startsWith(productTitle.substring(0,productTitle.length() - 3)), "Found other cart item:"+productTitle);
        } else {
            Assert.assertEquals(productTitle, targetProductTitle, "Found other items in cart");
        }

        // VERIFY THE QUANTITY OF PRODUCT
        WebElement quantityElement = Util.findElementByElement(cartItemElement, By.className(CART_QUANTITY_CLASSNAME));
        Assert.assertNotNull(quantityElement, "Failed to get the quantity of cart item.");

        String currentQuantityStr = quantityElement.getText();
        int currentQuantity = Integer.parseInt(currentQuantityStr.replace("\"", ""));
        Assert.assertEquals(currentQuantity, targetQuantity, "Cart item quantity doesn't match.");

        log.info("Successfully verified cart.");

    }

    @BeforeClass
    public void setUp() {
        // Set up the customized chromewebdriver file path
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        counter = 0;

        // Navigate to Walmart website and login with default account
        driver.navigate().to("http://www.walmart.com/");
        // Login with test account
        login();


        // Remove all items in cart before tests
        cleanChart();
    }

    @BeforeMethod
    public void beforeMethod() {
        log.info("Test:"+counter);
    }

    @Test(invocationCount = 5)
    public void test() {

        searchItem(TEST_SEARCH_ITEMS[counter]);

        selectAndNavigateToProductPage(0);

        String addedProductTitle = getProductTitle();
        addProductIntoChart();

        // Verify the added product information in cart
        verifyAddedItemInChart(addedProductTitle, 1);

    }

    @AfterMethod
    public void cleanUpAfterMethod() {
        // Remove added products in cart after test
        cleanChart();
        counter++;
    }

    @AfterClass
    public void afterClassMethod() {
        //Close the browser
        driver.quit();
    }


}