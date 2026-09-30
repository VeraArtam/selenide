import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RegistrationTest {
    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldRegisterDeliveryCard() {
        String planningDate = generateDate(4,"dd.MM.yyyy");

        Selenide.open("http://localhost:9999");
        SelenideElement form = $$("form").find(visible);
        form.$("[data-test-id='city'] input").setValue("Москва");
        form.$("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME))
                .press(Keys.DELETE)
                .setValue(planningDate);
        form.$("[data-test-id='name'] input").setValue("Иванов Иван");
        form.$("[data-test-id='phone'] input").setValue("+79999999999");
        form.$("[data-test-id='agreement']").click();
        form.$(Selectors.byText("Забронировать")).click();
        SelenideElement body = $("body");
        body.$("[data-test-id='notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно забронирована на " + planningDate));
    }
}
