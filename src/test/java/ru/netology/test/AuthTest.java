package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.AuthInfo;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldAuthActiveUserTest() {
        AuthInfo activeUser = DataGenerator.Registration.generateActiveUser();
        $("[name='login']").setValue(activeUser.getLogin());
        $("[name='password']").setValue(activeUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(withText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    void shouldAuthBlockedUserTest() {
        AuthInfo blockedUser = DataGenerator.Registration.generateBlockedUser();
        $("[name='login']").setValue(blockedUser.getLogin());
        $("[name='password']").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(withText("Пользователь заблокирован")).shouldBe(Condition.visible);
    }

    @Test
    void shouldAuthInvalidPassworUserTest() {
        AuthInfo invalidPasswordUser = DataGenerator.Registration.generateInvalidPasswordUser();
        $("[name='login']").setValue(invalidPasswordUser.getLogin());
        $("[name='password']").setValue(invalidPasswordUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldAuthInvalidLoginUserTest() {
        AuthInfo invalidLoginUser = DataGenerator.Registration.generateInvalidLoginUser();
        $("[name='login']").setValue(invalidLoginUser.getLogin());
        $("[name='password']").setValue(invalidLoginUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(".notification__content").shouldHave(text("Неверно указан логин или пароль"));
    }
}
