import pytest
import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import Select

# Run using the command: python -m pytest test_saucedemo.py -s
# You must activate the venv first and ensure you have required packages (selenium, pytest) installed.

BASE_URL = "https://www.saucedemo.com"
VALID_USER = "standard_user"
VALID_PASS = "secret_sauce"
LOCKED_USER = "locked_out_user"


class TestSauceDemo:

    def setup_method(self):
        self.driver = webdriver.Firefox()
        self.driver.maximize_window()

    def teardown_method(self):
        self.driver.quit()

    # ------------------------------------------------------------------
    # Helper: login
    # ------------------------------------------------------------------

    def login(self, username=VALID_USER, password=VALID_PASS):
        self.driver.get(BASE_URL)
        time.sleep(1)
        self.driver.find_element(By.ID, "user-name").send_keys(username)
        self.driver.find_element(By.ID, "password").send_keys(password)
        time.sleep(1)
        self.driver.find_element(By.ID, "login-button").click()
        time.sleep(1)

    # ------------------------------------------------------------------
    # 1. Authentication tests
    # ------------------------------------------------------------------

    def test_login_page_loads(self):
        self.driver.get(BASE_URL)
        time.sleep(1)

        assert self.driver.find_element(By.ID, "user-name").is_displayed(), \
            "Username field not visible!"
        assert self.driver.find_element(By.ID, "password").is_displayed(), \
            "Password field not visible!"
        assert self.driver.find_element(By.ID, "login-button").is_displayed(), \
            "Login button not visible!"

    def test_successful_login(self):
        self.login()

        WebDriverWait(self.driver, 5).until(EC.url_contains("inventory.html"))
        assert "inventory.html" in self.driver.current_url, "Login did not redirect to inventory!"

        title = self.driver.find_element(By.CLASS_NAME, "title")
        assert title.text == "Products", "Inventory page title is incorrect!"

    def test_invalid_credentials(self):
        self.login(username="wrong_user", password="wrong_pass")
        time.sleep(1)

        error = WebDriverWait(self.driver, 5).until(
            EC.visibility_of_element_located((By.CSS_SELECTOR, "[data-test='error']"))
        )
        assert "Username and password do not match" in error.text, \
            "Expected invalid credentials error message!"

    def test_empty_username(self):
        self.driver.get(BASE_URL)
        time.sleep(1)
        self.driver.find_element(By.ID, "password").send_keys(VALID_PASS)
        self.driver.find_element(By.ID, "login-button").click()
        time.sleep(1)

        error = self.driver.find_element(By.CSS_SELECTOR, "[data-test='error']")
        assert "Username is required" in error.text, "Expected 'Username is required' error!"

    def test_empty_password(self):
        self.driver.get(BASE_URL)
        time.sleep(1)
        self.driver.find_element(By.ID, "user-name").send_keys(VALID_USER)
        self.driver.find_element(By.ID, "login-button").click()
        time.sleep(1)

        error = self.driver.find_element(By.CSS_SELECTOR, "[data-test='error']")
        assert "Password is required" in error.text, "Expected 'Password is required' error!"

    def test_locked_out_user(self):
        self.login(username=LOCKED_USER, password=VALID_PASS)
        time.sleep(1)

        error = self.driver.find_element(By.CSS_SELECTOR, "[data-test='error']")
        assert "locked out" in error.text, "Expected locked-out error message!"

    def test_cannot_access_inventory_without_login(self):
        self.driver.get(f"{BASE_URL}/inventory.html")
        time.sleep(1)

        # Should be redirected back to login page
        assert self.driver.current_url.rstrip("/") == BASE_URL, \
            "Unauthenticated user should be redirected to login!"
        error = self.driver.find_element(By.CSS_SELECTOR, "[data-test='error']")
        assert error.is_displayed(), "Expected access-denied error on login page!"

    # ------------------------------------------------------------------
    # 2. Inventory / product browsing tests
    # ------------------------------------------------------------------

    def test_inventory_displays_six_products(self):
        self.login()
        time.sleep(2)

        items = self.driver.find_elements(By.CLASS_NAME, "inventory_item")
        assert len(items) == 6, f"Expected 6 products, found {len(items)}!"

    def test_products_have_name_price_image(self):
        self.login()
        time.sleep(2)

        items = self.driver.find_elements(By.CLASS_NAME, "inventory_item")
        for item in items:
            assert item.find_element(By.CLASS_NAME, "inventory_item_name").is_displayed(), \
                "Product name missing!"
            assert item.find_element(By.CLASS_NAME, "inventory_item_price").is_displayed(), \
                "Product price missing!"
            assert item.find_element(By.TAG_NAME, "img").is_displayed(), \
                "Product image missing!"

    def test_sort_by_name_a_to_z(self):
        self.login()
        time.sleep(2)

        Select(self.driver.find_element(By.CLASS_NAME, "product_sort_container")) \
            .select_by_value("az")
        time.sleep(1)

        names = [el.text for el in
                 self.driver.find_elements(By.CLASS_NAME, "inventory_item_name")]
        assert names == sorted(names), "Products are not sorted A to Z!"

    def test_sort_by_name_z_to_a(self):
        self.login()
        time.sleep(2)

        Select(self.driver.find_element(By.CLASS_NAME, "product_sort_container")) \
            .select_by_value("za")
        time.sleep(1)

        names = [el.text for el in
                 self.driver.find_elements(By.CLASS_NAME, "inventory_item_name")]
        assert names == sorted(names, reverse=True), "Products are not sorted Z to A!"

    def test_sort_by_price_low_to_high(self):
        self.login()
        time.sleep(2)

        Select(self.driver.find_element(By.CLASS_NAME, "product_sort_container")) \
            .select_by_value("lohi")
        time.sleep(1)

        prices = [float(el.text.replace("$", "")) for el in
                  self.driver.find_elements(By.CLASS_NAME, "inventory_item_price")]
        assert prices == sorted(prices), "Products are not sorted by price low to high!"

    def test_sort_by_price_high_to_low(self):
        self.login()
        time.sleep(2)

        Select(self.driver.find_element(By.CLASS_NAME, "product_sort_container")) \
            .select_by_value("hilo")
        time.sleep(1)

        prices = [float(el.text.replace("$", "")) for el in
                  self.driver.find_elements(By.CLASS_NAME, "inventory_item_price")]
        assert prices == sorted(prices, reverse=True), "Products are not sorted by price high to low!"

    def test_product_detail_page(self):
        self.login()
        time.sleep(2)

        product_name = self.driver.find_element(By.CLASS_NAME, "inventory_item_name").text
        self.driver.find_element(By.CLASS_NAME, "inventory_item_name").click()
        time.sleep(2)

        assert "inventory-item.html" in self.driver.current_url, \
            "Did not navigate to product detail page!"
        detail_name = self.driver.find_element(By.CLASS_NAME, "inventory_details_name").text
        assert detail_name == product_name, "Product name on detail page does not match!"
        assert self.driver.find_element(By.CLASS_NAME, "inventory_details_price").is_displayed(), \
            "Product price missing on detail page!"

    def test_back_to_products_from_detail(self):
        self.login()
        time.sleep(2)

        self.driver.find_element(By.CLASS_NAME, "inventory_item_name").click()
        time.sleep(2)
        self.driver.find_element(By.ID, "back-to-products").click()
        time.sleep(1)

        assert "inventory.html" in self.driver.current_url, \
            "Back to products did not navigate to inventory!"

    # ------------------------------------------------------------------
    # 3. Shopping cart tests
    # ------------------------------------------------------------------

    def test_add_item_increments_cart_badge(self):
        self.login()
        time.sleep(2)

        self.driver.find_elements(By.CLASS_NAME, "btn_inventory")[0].click()
        time.sleep(1)

        badge = self.driver.find_element(By.CLASS_NAME, "shopping_cart_badge")
        assert badge.text == "1", "Cart badge should show 1 after adding one item!"

    def test_add_multiple_items(self):
        self.login()
        time.sleep(2)

        buttons = self.driver.find_elements(By.CLASS_NAME, "btn_inventory")
        buttons[0].click()
        time.sleep(1)
        buttons[1].click()
        time.sleep(1)

        badge = self.driver.find_element(By.CLASS_NAME, "shopping_cart_badge")
        assert badge.text == "2", "Cart badge should show 2 after adding two items!"

    def test_add_button_changes_to_remove(self):
        self.login()
        time.sleep(2)

        btn = self.driver.find_elements(By.CLASS_NAME, "btn_inventory")[0]
        btn.click()
        time.sleep(1)

        assert btn.text == "Remove", "Button label should change to 'Remove' after adding!"

    def test_remove_item_from_inventory(self):
        self.login()
        time.sleep(2)

        btn = self.driver.find_elements(By.CLASS_NAME, "btn_inventory")[0]
        btn.click()   # Add
        time.sleep(1)
        btn.click()   # Remove
        time.sleep(1)

        badges = self.driver.find_elements(By.CLASS_NAME, "shopping_cart_badge")
        assert len(badges) == 0, "Cart badge should disappear after removing the only item!"

    def test_cart_page_shows_added_item(self):
        self.login()
        time.sleep(2)

        product_name = self.driver.find_element(By.CLASS_NAME, "inventory_item_name").text
        self.driver.find_elements(By.CLASS_NAME, "btn_inventory")[0].click()
        time.sleep(1)

        self.driver.find_element(By.CLASS_NAME, "shopping_cart_link").click()
        time.sleep(2)

        assert "cart.html" in self.driver.current_url, "Did not navigate to cart page!"
        cart_item_name = self.driver.find_element(By.CLASS_NAME, "inventory_item_name").text
        assert cart_item_name == product_name, "Cart item name does not match added product!"

    def test_remove_item_from_cart(self):
        self.login()
        time.sleep(2)

        self.driver.find_elements(By.CLASS_NAME, "btn_inventory")[0].click()
        time.sleep(1)
        self.driver.find_element(By.CLASS_NAME, "shopping_cart_link").click()
        time.sleep(2)

        self.driver.find_element(By.CLASS_NAME, "cart_button").click()
        time.sleep(1)

        cart_items = self.driver.find_elements(By.CLASS_NAME, "cart_item")
        assert len(cart_items) == 0, "Cart should be empty after removing the item!"

    def test_continue_shopping_returns_to_inventory(self):
        self.login()
        time.sleep(2)

        self.driver.find_elements(By.CLASS_NAME, "btn_inventory")[0].click()
        time.sleep(1)
        self.driver.find_element(By.CLASS_NAME, "shopping_cart_link").click()
        time.sleep(2)

        self.driver.find_element(By.ID, "continue-shopping").click()
        time.sleep(1)

        assert "inventory.html" in self.driver.current_url, \
            "Continue Shopping should return to inventory!"

    # ------------------------------------------------------------------
    # 4. Checkout tests
    # ------------------------------------------------------------------

    def go_to_checkout_step_one(self):
        self.login()
        time.sleep(2)
        self.driver.find_elements(By.CLASS_NAME, "btn_inventory")[0].click()
        time.sleep(1)
        self.driver.find_element(By.CLASS_NAME, "shopping_cart_link").click()
        time.sleep(2)
        self.driver.find_element(By.ID, "checkout").click()
        time.sleep(1)

    def test_checkout_step_one_loads(self):
        self.go_to_checkout_step_one()

        assert "checkout-step-one.html" in self.driver.current_url, \
            "Did not navigate to checkout step one!"
        assert self.driver.find_element(By.ID, "first-name").is_displayed(), \
            "First name field missing!"
        assert self.driver.find_element(By.ID, "last-name").is_displayed(), \
            "Last name field missing!"
        assert self.driver.find_element(By.ID, "postal-code").is_displayed(), \
            "Postal code field missing!"

    def test_checkout_requires_first_name(self):
        self.go_to_checkout_step_one()
        self.driver.find_element(By.ID, "last-name").send_keys("Doe")
        self.driver.find_element(By.ID, "postal-code").send_keys("12345")
        self.driver.find_element(By.ID, "continue").click()
        time.sleep(1)

        error = self.driver.find_element(By.CSS_SELECTOR, "[data-test='error']")
        assert "First Name is required" in error.text, \
            "Expected 'First Name is required' error!"

    def test_checkout_requires_last_name(self):
        self.go_to_checkout_step_one()
        self.driver.find_element(By.ID, "first-name").send_keys("John")
        self.driver.find_element(By.ID, "postal-code").send_keys("12345")
        self.driver.find_element(By.ID, "continue").click()
        time.sleep(1)

        error = self.driver.find_element(By.CSS_SELECTOR, "[data-test='error']")
        assert "Last Name is required" in error.text, \
            "Expected 'Last Name is required' error!"

    def test_checkout_requires_postal_code(self):
        self.go_to_checkout_step_one()
        self.driver.find_element(By.ID, "first-name").send_keys("John")
        self.driver.find_element(By.ID, "last-name").send_keys("Doe")
        self.driver.find_element(By.ID, "continue").click()
        time.sleep(1)

        error = self.driver.find_element(By.CSS_SELECTOR, "[data-test='error']")
        assert "Postal Code is required" in error.text, \
            "Expected 'Postal Code is required' error!"

    def test_checkout_step_two_summary(self):
        self.go_to_checkout_step_one()
        self.driver.find_element(By.ID, "first-name").send_keys("John")
        self.driver.find_element(By.ID, "last-name").send_keys("Doe")
        self.driver.find_element(By.ID, "postal-code").send_keys("12345")
        self.driver.find_element(By.ID, "continue").click()
        time.sleep(2)

        assert "checkout-step-two.html" in self.driver.current_url, \
            "Did not navigate to checkout step two!"
        assert len(self.driver.find_elements(By.CLASS_NAME, "cart_item")) == 1, \
            "Expected one item in order summary!"
        assert self.driver.find_element(By.CLASS_NAME, "summary_info").is_displayed(), \
            "Order summary info missing!"

    def test_order_total_is_displayed(self):
        self.go_to_checkout_step_one()
        self.driver.find_element(By.ID, "first-name").send_keys("Jane")
        self.driver.find_element(By.ID, "last-name").send_keys("Smith")
        self.driver.find_element(By.ID, "postal-code").send_keys("90210")
        self.driver.find_element(By.ID, "continue").click()
        time.sleep(2)

        total_text = self.driver.find_element(By.CLASS_NAME, "summary_total_label").text
        assert "Total:" in total_text, "Order total label missing!"
        assert "$" in total_text, "Order total should contain a dollar amount!"

    def test_cancel_checkout_returns_to_inventory(self):
        self.go_to_checkout_step_one()
        self.driver.find_element(By.ID, "cancel").click()
        time.sleep(1)

        assert "inventory.html" in self.driver.current_url, \
            "Cancelling checkout step one should return to inventory!"

    # ------------------------------------------------------------------
    # 5. Order completion tests
    # ------------------------------------------------------------------

    def complete_checkout(self):
        self.go_to_checkout_step_one()
        self.driver.find_element(By.ID, "first-name").send_keys("John")
        self.driver.find_element(By.ID, "last-name").send_keys("Doe")
        self.driver.find_element(By.ID, "postal-code").send_keys("12345")
        self.driver.find_element(By.ID, "continue").click()
        time.sleep(2)
        self.driver.find_element(By.ID, "finish").click()
        time.sleep(2)

    def test_order_confirmation_page(self):
        self.complete_checkout()

        assert "checkout-complete.html" in self.driver.current_url, \
            "Did not reach the order confirmation page!"

    def test_confirmation_message_displayed(self):
        self.complete_checkout()

        header = self.driver.find_element(By.CLASS_NAME, "complete-header")
        assert header.text == "Thank you for your order!", \
            "Confirmation message is incorrect!"

    def test_cart_is_empty_after_order(self):
        self.complete_checkout()

        badges = self.driver.find_elements(By.CLASS_NAME, "shopping_cart_badge")
        assert len(badges) == 0, "Cart badge should be gone after completing an order!"

    def test_back_home_from_confirmation(self):
        self.complete_checkout()

        self.driver.find_element(By.ID, "back-to-products").click()
        time.sleep(1)

        assert "inventory.html" in self.driver.current_url, \
            "Back Home should navigate to the inventory page!"

    # ------------------------------------------------------------------
    # 6. Logout tests
    # ------------------------------------------------------------------

    def test_logout_redirects_to_login(self):
        self.login()
        time.sleep(2)

        self.driver.find_element(By.ID, "react-burger-menu-btn").click()
        time.sleep(1)

        WebDriverWait(self.driver, 5).until(
            EC.visibility_of_element_located((By.ID, "logout_sidebar_link"))
        )
        self.driver.find_element(By.ID, "logout_sidebar_link").click()
        time.sleep(1)

        assert self.driver.current_url.rstrip("/") == BASE_URL, \
            "Logout should redirect to the login page!"
        assert self.driver.find_element(By.ID, "login-button").is_displayed(), \
            "Login button should be visible after logout!"

    def test_cannot_access_inventory_after_logout(self):
        self.login()
        time.sleep(2)

        self.driver.find_element(By.ID, "react-burger-menu-btn").click()
        time.sleep(1)
        WebDriverWait(self.driver, 5).until(
            EC.visibility_of_element_located((By.ID, "logout_sidebar_link"))
        )
        self.driver.find_element(By.ID, "logout_sidebar_link").click()
        time.sleep(1)

        self.driver.get(f"{BASE_URL}/inventory.html")
        time.sleep(1)

        assert self.driver.current_url.rstrip("/") == BASE_URL, \
            "Logged-out user should be redirected to login!"
        assert self.driver.find_element(By.CSS_SELECTOR, "[data-test='error']").is_displayed(), \
            "Expected access error after logout!"

    # ------------------------------------------------------------------
    # 7. Full end-to-end user journey
    # ------------------------------------------------------------------

    def test_complete_e2e_purchase(self):
        # Step 1: Login
        self.login()
        WebDriverWait(self.driver, 5).until(EC.url_contains("inventory.html"))
        assert "inventory.html" in self.driver.current_url, "Login failed!"

        # Step 2: Sort products by price low to high
        Select(self.driver.find_element(By.CLASS_NAME, "product_sort_container")) \
            .select_by_value("lohi")
        time.sleep(1)

        # Step 3: Add two cheapest products to cart
        buttons = self.driver.find_elements(By.CLASS_NAME, "btn_inventory")
        buttons[0].click()
        time.sleep(1)
        buttons[1].click()
        time.sleep(1)

        badge = self.driver.find_element(By.CLASS_NAME, "shopping_cart_badge")
        assert badge.text == "2", "Cart badge should show 2!"

        # Step 4: Open cart and verify item count
        self.driver.find_element(By.CLASS_NAME, "shopping_cart_link").click()
        time.sleep(2)
        assert "cart.html" in self.driver.current_url, "Did not navigate to cart!"
        cart_items = self.driver.find_elements(By.CLASS_NAME, "cart_item")
        assert len(cart_items) == 2, "Cart should contain 2 items!"

        # Step 5: Proceed to checkout
        self.driver.find_element(By.ID, "checkout").click()
        time.sleep(1)
        assert "checkout-step-one.html" in self.driver.current_url, \
            "Did not navigate to checkout step one!"

        # Step 6: Fill in customer info and click Continue
        self.driver.find_element(By.ID, "first-name").send_keys("John")
        time.sleep(1)
        self.driver.find_element(By.ID, "last-name").send_keys("Doe")
        time.sleep(1)
        self.driver.find_element(By.ID, "postal-code").send_keys("12345")
        time.sleep(1)
        self.driver.find_element(By.ID, "continue").click()
        time.sleep(2)
        assert "checkout-step-two.html" in self.driver.current_url, \
            "Did not navigate to order summary!"

        # Step 7: Verify order summary
        assert len(self.driver.find_elements(By.CLASS_NAME, "cart_item")) == 2, \
            "Order summary should show 2 items!"
        total_text = self.driver.find_element(By.CLASS_NAME, "summary_total_label").text
        assert "$" in total_text, "Total price should be displayed!"

        # Step 8: Finish order
        self.driver.find_element(By.ID, "finish").click()
        time.sleep(2)
        assert "checkout-complete.html" in self.driver.current_url, \
            "Did not reach confirmation page!"
        header = self.driver.find_element(By.CLASS_NAME, "complete-header")
        assert header.text == "Thank you for your order!", "Confirmation message incorrect!"

        # Step 9: Logout
        self.driver.find_element(By.ID, "react-burger-menu-btn").click()
        time.sleep(1)
        WebDriverWait(self.driver, 5).until(
            EC.visibility_of_element_located((By.ID, "logout_sidebar_link"))
        )
        self.driver.find_element(By.ID, "logout_sidebar_link").click()
        time.sleep(1)
        assert self.driver.current_url.rstrip("/") == BASE_URL, \
            "Should be back at login page after logout!"
