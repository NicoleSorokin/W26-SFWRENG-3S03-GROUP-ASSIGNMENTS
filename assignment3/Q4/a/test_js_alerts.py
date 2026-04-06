import pytest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

BASE_URL = "https://the-internet.herokuapp.com/javascript_alerts"


@pytest.fixture
def driver():
    options = Options()
    options.add_argument("--headless")
    driver = webdriver.Firefox(options=options)
    driver.implicitly_wait(5)
    yield driver
    driver.quit()


def get_result(driver):
    result = WebDriverWait(driver, 5).until(
        EC.visibility_of_element_located((By.ID, "result"))
    )
    return result.text.strip()


class TestPageLoad:

    def test_page_title(self, driver):
        driver.get(BASE_URL)
        assert "The Internet" in driver.title

    def test_three_buttons_present(self, driver):
        driver.get(BASE_URL)
        buttons = driver.find_elements(By.CSS_SELECTOR, ".example button")
        assert len(buttons) == 3


class TestJSAlert:

    def test_alert_appears(self, driver):
        driver.get(BASE_URL)
        driver.find_element(By.XPATH, "//button[text()='Click for JS Alert']").click()
        WebDriverWait(driver, 5).until(EC.alert_is_present())
        alert = driver.switch_to.alert
        assert alert is not None

    def test_alert_text(self, driver):
        driver.get(BASE_URL)
        driver.find_element(By.XPATH, "//button[text()='Click for JS Alert']").click()
        WebDriverWait(driver, 5).until(EC.alert_is_present())
        alert = driver.switch_to.alert
        assert alert.text == "I am a JS Alert"

    def test_alert_accept_updates_result(self, driver):
        driver.get(BASE_URL)
        driver.find_element(By.XPATH, "//button[text()='Click for JS Alert']").click()
        WebDriverWait(driver, 5).until(EC.alert_is_present())
        driver.switch_to.alert.accept()
        assert "You successfully clicked an alert" in get_result(driver)


class TestJSConfirm:

    def test_confirm_accept_updates_result(self, driver):
        driver.get(BASE_URL)
        driver.find_element(By.XPATH, "//button[text()='Click for JS Confirm']").click()
        WebDriverWait(driver, 5).until(EC.alert_is_present())
        driver.switch_to.alert.accept()
        assert "Ok" in get_result(driver)

    def test_confirm_dismiss_updates_result(self, driver):
        driver.get(BASE_URL)
        driver.find_element(By.XPATH, "//button[text()='Click for JS Confirm']").click()
        WebDriverWait(driver, 5).until(EC.alert_is_present())
        driver.switch_to.alert.dismiss()
        assert "Cancel" in get_result(driver)

    def test_confirm_text(self, driver):
        driver.get(BASE_URL)
        driver.find_element(By.XPATH, "//button[text()='Click for JS Confirm']").click()
        WebDriverWait(driver, 5).until(EC.alert_is_present())
        alert = driver.switch_to.alert
        assert alert.text == "I am a JS Confirm"
        alert.dismiss()


class TestJSPrompt:

    def test_prompt_accept_with_text(self, driver):
        driver.get(BASE_URL)
        driver.find_element(By.XPATH, "//button[text()='Click for JS Prompt']").click()
        WebDriverWait(driver, 5).until(EC.alert_is_present())
        alert = driver.switch_to.alert
        alert.send_keys("Hello 3S03")
        alert.accept()
        assert "Hello 3S03" in get_result(driver)

    def test_prompt_dismiss_records_null(self, driver):
        driver.get(BASE_URL)
        driver.find_element(By.XPATH, "//button[text()='Click for JS Prompt']").click()
        WebDriverWait(driver, 5).until(EC.alert_is_present())
        driver.switch_to.alert.dismiss()
        assert "null" in get_result(driver)

    def test_prompt_accept_empty_string(self, driver):
        driver.get(BASE_URL)
        driver.find_element(By.XPATH, "//button[text()='Click for JS Prompt']").click()
        WebDriverWait(driver, 5).until(EC.alert_is_present())
        alert = driver.switch_to.alert
        alert.accept()
        result = get_result(driver)
        assert "You entered:" in result

    def test_prompt_text(self, driver):
        driver.get(BASE_URL)
        driver.find_element(By.XPATH, "//button[text()='Click for JS Prompt']").click()
        WebDriverWait(driver, 5).until(EC.alert_is_present())
        alert = driver.switch_to.alert
        assert alert.text == "I am a JS prompt"
        alert.dismiss()
