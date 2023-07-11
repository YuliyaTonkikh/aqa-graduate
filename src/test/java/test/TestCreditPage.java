package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.Card;
import data.Generator;
import data.Storage;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.CardPage;
import pages.LandingPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCreditPage {
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