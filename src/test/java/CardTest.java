import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardTest {

    @BeforeEach
    void setup() { open("http://localhost:9999/"); }

    @Test
    void shouldRegisterMeetingAndReschedule () {
        DataGenerator.UserInfo getUser = DataGenerator.Registration.generateUser("ru");
        int addDaysFirstMeeting = 6;
        String firstMeeting = DataGenerator.generateDate(addDaysFirstMeeting);
        int addDaysSecondMeeting = 9;
        String secondMeeting = DataGenerator.generateDate(addDaysSecondMeeting);
        $("[data-test-id=\"city\"] input").setValue(getUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeeting);
        $("[data-test-id=\"name\"] input").setValue(getUser.getName());
        $("[data-test-id=\"phone\"] input").setValue(getUser.getPhone());
        $("[data-test-id=\"agreement\"]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(10));
        $("[data-test-id=\"success-notification\"] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeeting))
                .shouldBe(Condition.visible);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeeting);
        $(byText("Запланировать")).click();
        $("[data-test-id=\"replan-notification\"] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(Condition.visible);
        $("[data-test-id=\"replan-notification\"] button").click();
        $("[data-test-id=\"success-notification\"] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeeting))
                .shouldBe(Condition.visible);


    }


}
