package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Card;
import data.Generator;
import data.Storage;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.LandingPage;
import pages.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPaymentPage {
    @BeforeEach
    public void openPage() {
        Storage.clearTables();
        String url = System.getProperty("sut.url");
        open(url);
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should send request by approved card, payment page")
    void shouldSendRequestSuccessForPayment() {
        Card approvedCard = Generator.getApprovedCard();
        LandingPage startPage = new LandingPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.inputData(approvedCard);
        paymentPage.notificationOkIsVisible();
        assertEquals("APPROVED", Storage.findPaymentStatus());
    }

    @Test
    @DisplayName("Should not send request by declined card, payment page")
    void shouldNotSendRequestDeclinedCardForPayment() {
        Card declinedCard = Generator.getDeclinedCard();
        LandingPage startPage = new LandingPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.inputData(declinedCard);
        paymentPage.notificationErrorIsVisible();
        assertEquals("DECLINED", Storage.findPaymentStatus());
    }

    @Test
    @DisplayName("Should not send request by random card, payment page")
    void shouldNotSendRequestWithWrongCardNumberForPayment() {
        Storage.clearTables();
        Card wrongRandomCardNumber = Generator.getWrongRandomCardNumber();
        LandingPage startPage = new LandingPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.inputData(wrongRandomCardNumber);
        paymentPage.inputInvalidIsVisible();
        assertEquals("0", Storage.countRecords());
    }

    @Test
    @DisplayName("Should show warning if card expired, payment page")
    void shouldShowWarningIfCardIsExpiredForPayment() {
        Card invalidExpDateCard = Generator.getInvalidExpDateCard();
        LandingPage startPage = new LandingPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.inputData(invalidExpDateCard);
        paymentPage.inputInvalidIsVisibleExpiredDate();
    }

    @Test
    @DisplayName("Should show warning if expiration date more than 5 years, payment page")
    void shouldShowWarningIfExpirationDateMoreThan5YearsForPayment() {
        Card invalidExpDateCard = Generator.getInvalidExpDateCardMoreThan5Years();
        LandingPage startPage = new LandingPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.inputData(invalidExpDateCard);
        paymentPage.inputInvalidIsVisibleMore5Years();
    }

    @Test
    @DisplayName("Should show warning if month 00, payment page")
    void shouldShowWarningIfMonth00ForPayment() {
        Card invalidMonthCard = Generator.getInvalidMonth00();
        LandingPage startPage = new LandingPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.inputData(invalidMonthCard);
        paymentPage.inputInvalidIsVisible();
    }

    @Test
    @DisplayName("Should show warning if month expired, payment page")
    void shouldShowWarningIfMonthExpiredForPayment() {
        Card invalidExpiredMonthCard = Generator.getExpiredMonth();
        LandingPage startPage = new LandingPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.inputData(invalidExpiredMonthCard);
        paymentPage.inputInvalidIsVisibleExpiredDate();
    }

    @Test
    @DisplayName("Should show warning if year 1 number, payment page")
    void shouldShowWarningIfYear1numberForPayment() {
        Card invalidYear1number = Generator.getInvalidYear1number();
        LandingPage startPage = new LandingPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.inputData(invalidYear1number);
        paymentPage.inputInvalidIsVisible();
    }

    @Test
    @DisplayName("Should show warning if CVV 1 number, payment page")
    void shouldShowWarningIfCVV1numberForPayment() {
        Card invalidCVV1number = Generator.getInvalidCVV1number();
        LandingPage startPage = new LandingPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.inputData(invalidCVV1number);
        paymentPage.inputInvalidIsVisible();
    }

    @Test
    @DisplayName("Should show warning if CVV 2 number, payment page")
    void shouldShowWarningIfCVV2numberForPayment() {
        Card invalidCVV2numbers = Generator.getInvalidCVV2numbers();
        LandingPage startPage = new LandingPage();
        PaymentPage paymentPage = startPage.goToPaymentPage();
        paymentPage.inputData(invalidCVV2numbers);
        paymentPage.inputInvalidIsVisible();
    }

}