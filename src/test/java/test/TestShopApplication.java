package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Card;
import data.APIHelpers;
import data.Generator;
import data.Storage;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.CardPage;
import pages.LandingPage;
import pages.PaymentPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestShopApplication {
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

    // CREDIT PAGE
    @Test
    @DisplayName("Happy path. Should send request by approved card, credit page")
    void shouldSendRequestSuccessForCredit() {
        Card approvedCard = Generator.getApprovedCard();
        LandingPage LandingPage = new LandingPage();
        CardPage creditCardPage = LandingPage.goToCreditPage();
        creditCardPage.fillData(approvedCard);
        creditCardPage.notificationOkIsVisible();
        assertEquals("APPROVED", Storage.findCreditStatus());
    }

    @Test
    @DisplayName("Should not send request by declined card, credit page")
    void shouldNotSendRequestDeclinedCardForCredit() {
        Card declinedCard = Generator.getDeclinedCard();
        LandingPage LandingPage = new LandingPage();
        CardPage creditPage = LandingPage.goToCreditPage();
        creditPage.fillData(declinedCard);
        creditPage.notificationErrorIsVisible();
        assertEquals("DECLINED", Storage.findPaymentStatus());
    }

    @Test
    @DisplayName("Should not send request by random card, credit page")
    void shouldNotSendRequestWithWrongCardNumberForCredit() {
        Storage.clearTables();
        Card wrongRandomCardNumber = Generator.getWrongRandomCardNumber();
        LandingPage LandingPage = new LandingPage();
        CardPage creditPage = LandingPage.goToCreditPage();
        creditPage.fillData(wrongRandomCardNumber);
        creditPage.inputInvalidIsVisible();
        assertEquals("0", Storage.countRecords());
    }

    @Test
    @DisplayName("Should show warning if card expired, credit page")
    void shouldShowWarningIfCardIsExpiredForCredit() {
        Card invalidExpDateCard = Generator.getInvalidExpDateCard();
        LandingPage LandingPage = new LandingPage();
        CardPage creditPage = LandingPage.goToCreditPage();
        creditPage.fillData(invalidExpDateCard);
        creditPage.inputInvalidIsVisibleExpiredDate();
    }

    @Test
    @DisplayName("Should show warning if expiration date more than 5 years, credit page")
    void shouldShowWarningIfExpirationDateMoreThan5YearsForCredit() {
        Card invalidExpDateCard = Generator.getInvalidExpDateCardMoreThan5Years();
        LandingPage LandingPage = new LandingPage();
        CardPage creditPage = LandingPage.goToCreditPage();
        creditPage.fillData(invalidExpDateCard);
        creditPage.inputInvalidIsVisibleMore5Years();
    }

    @Test
    @DisplayName("Should show warning if month 00, credit page")
    void shouldShowWarningIfMonth00ForCredit() {
        Card invalidMonthCard = Generator.getInvalidMonth00();
        LandingPage LandingPage = new LandingPage();
        CardPage creditPage = LandingPage.goToCreditPage();
        creditPage.fillData(invalidMonthCard);
        creditPage.inputInvalidIsVisible();
    }

    @Test
    @DisplayName("Should show warning if month expired, credit page")
    void shouldShowWarningIfMonthExpiredForCredit() {
        Card invalidExpiredMonthCard = Generator.getExpiredMonth();
        LandingPage LandingPage = new LandingPage();
        CardPage creditPage = LandingPage.goToCreditPage();
        creditPage.fillData(invalidExpiredMonthCard);
        creditPage.inputInvalidIsVisibleExpiredDate();
    }

    @Test
    @DisplayName("Should show warning if year 1 number, credit page")
    void shouldShowWarningIfYear1numberForCredit() {
        Card invalidYear1number = Generator.getInvalidYear1number();
        LandingPage LandingPage = new LandingPage();
        CardPage creditPage = LandingPage.goToCreditPage();
        creditPage.fillData(invalidYear1number);
        creditPage.inputInvalidIsVisible();
    }

    @Test
    @DisplayName("Should show warning if CVV 1 number, credit page")
    void shouldShowWarningIfCVV1numberForCredit() {
        Card invalidCVV1number = Generator.getInvalidCVV1number();
        LandingPage LandingPage = new LandingPage();
        CardPage creditPage = LandingPage.goToCreditPage();
        creditPage.fillData(invalidCVV1number);
        creditPage.inputInvalidIsVisible();
    }

    @Test
    @DisplayName("Should show warning if CVV 2 number, credit page")
    void shouldShowWarningIfCVV2numberForCredit() {
        Card invalidCVV2numbers = Generator.getInvalidCVV2numbers();
        LandingPage LandingPage = new LandingPage();
        CardPage creditPage = LandingPage.goToCreditPage();
        creditPage.fillData(invalidCVV2numbers);
        creditPage.inputInvalidIsVisible();
    }
}