package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

public class LandingPage {
    private SelenideElement heading = $$("h2").find(text("Путешествие дня"));
    private SelenideElement buyButton = $$("button").find(exactText("Купить"));
    private SelenideElement creditButton = $$("button").find(exactText("Купить в кредит"));

    public LandingPage() {
        heading.shouldBe(visible);
    }

    public PaymentPage goToPaymentPage() {
        buyButton.click();
        return new PaymentPage();
    }

    public CardPage goToCreditPage() {
        creditButton.click();
        return new CardPage();
    }
}