package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.APIHelpers;
import data.Card;
import data.Generator;
import data.Storage;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestShopAPI {
    Card invalidHolderCard = Generator.getInvalidHolderCard();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should send payment request with approved card")
    void shouldSendPaymentRequestWithApprovedCard() {
        int statusCode = APIHelpers.getRequestStatusCode(Generator.getApprovedCard(), "/api/v1/pay");
        assertEquals(200, statusCode);
        assertEquals("APPROVED", Storage.findPaymentStatus());
    }

    @Test
    @DisplayName("Shouldn't send payment request with declined card")
    void shouldNotSendPaymentWithDeclinedCard() {
        int statusCode = APIHelpers.getRequestStatusCode(Generator.getDeclinedCard(), "/api/v1/pay");
        assertEquals(400, statusCode);
        assertEquals("DECLINED", Storage.findPaymentStatus());
    }

    @Test
    @DisplayName("Shouldn't send payment request with random card")
    void shouldNotSendPaymentRequestWithIncorrectName() {
        Storage.clearTables();
        int statusCode = APIHelpers.getRequestStatusCode(Generator.getFakeCard(), "/api/v1/pay");
        assertNotEquals(200, statusCode);
        assertEquals("0", Storage.countRecords());
    }

    @Test
    @DisplayName("Should send credit request with approved card")
    void shouldSendCreditRequestWithApprovedCard() {
        int statusCode = APIHelpers.getRequestStatusCode(Generator.getApprovedCard(), "/api/v1/credit");
        assertEquals(200, statusCode);
        assertEquals("APPROVED", Storage.findCreditStatus());
    }

    @Test
    @DisplayName("Shouldn't send credit request with declined card")
    void shouldNotSendCreditWithDeclinedCard() {
        int statusCode = APIHelpers.getRequestStatusCode(Generator.getDeclinedCard(), "/api/v1/credit");
        assertEquals(400, statusCode);
        assertEquals("DECLINED", Storage.findCreditStatus());
    }

    @Test
    @DisplayName("Shouldn't send credit request with random card")
    void shouldNotSendCreditRequestWithIncorrectName() {
        Storage.clearTables();
        int statusCode = APIHelpers.getRequestStatusCode(Generator.getFakeCard(), "/api/v1/credit");
        assertNotEquals(200, statusCode);
        assertEquals("0", Storage.countRecords());
    }
}