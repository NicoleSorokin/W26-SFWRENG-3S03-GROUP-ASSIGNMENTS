import pytest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

BASE_URL = "https://the-internet.herokuapp.com/checkboxes"


@pytest.fixture
def driver():
    options = Options()
    options.add_argument("--headless")
    driver = webdriver.Firefox(options=options)
    driver.implicitly_wait(5)
    yield driver
    driver.quit()


def get_checkboxes(driver):
    return driver.find_elements(By.CSS_SELECTOR, "input[type='checkbox']")


class TestPageLoad:

    def test_page_title(self, driver):
        driver.get(BASE_URL)
        assert "The Internet" in driver.title

    def test_two_checkboxes_present(self, driver):
        driver.get(BASE_URL)
        checkboxes = get_checkboxes(driver)
        assert len(checkboxes) == 2

    def test_checkboxes_visible(self, driver):
        driver.get(BASE_URL)
        checkboxes = get_checkboxes(driver)
        for checkbox in checkboxes:
            assert checkbox.is_displayed()


class TestInitialState:

    def test_checkbox1_initially_unchecked(self, driver):
        driver.get(BASE_URL)
        checkboxes = get_checkboxes(driver)
        assert not checkboxes[0].is_selected()

    def test_checkbox2_initially_checked(self, driver):
        driver.get(BASE_URL)
        checkboxes = get_checkboxes(driver)
        assert checkboxes[1].is_selected()


class TestCheckboxInteractions:

    def test_check_checkbox1(self, driver):
        driver.get(BASE_URL)
        checkboxes = get_checkboxes(driver)
        checkboxes[0].click()
        assert checkboxes[0].is_selected()

    def test_uncheck_checkbox2(self, driver):
        driver.get(BASE_URL)
        checkboxes = get_checkboxes(driver)
        checkboxes[1].click()
        assert not checkboxes[1].is_selected()

    def test_toggle_checkbox1_twice(self, driver):
        driver.get(BASE_URL)
        checkboxes = get_checkboxes(driver)
        checkboxes[0].click()
        checkboxes[0].click()
        assert not checkboxes[0].is_selected()

    def test_toggle_checkbox2_twice(self, driver):
        driver.get(BASE_URL)
        checkboxes = get_checkboxes(driver)
        checkboxes[1].click()
        checkboxes[1].click()
        assert checkboxes[1].is_selected()

    def test_both_checkboxes_checked(self, driver):
        driver.get(BASE_URL)
        checkboxes = get_checkboxes(driver)
        checkboxes[0].click()
        assert checkboxes[0].is_selected()
        assert checkboxes[1].is_selected()

    def test_both_checkboxes_unchecked(self, driver):
        driver.get(BASE_URL)
        checkboxes = get_checkboxes(driver)
        checkboxes[1].click()
        assert not checkboxes[0].is_selected()
        assert not checkboxes[1].is_selected()
