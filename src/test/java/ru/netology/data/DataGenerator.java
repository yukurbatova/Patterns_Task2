package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void sendRequest(AuthInfo user) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }


    public static class Registration {
        private Registration() {
        }

        public static String generateLogin() {
            return faker.name().username();
        }

        public static String generatePassword() {
            return faker.internet().password();
        }

        public static AuthInfo generateActiveUser() {
            AuthInfo user = new AuthInfo(generateLogin(), generatePassword(), "active");
            sendRequest(user);
            return user;
        }

        public static AuthInfo  generateBlockedUser() {
            AuthInfo user = new AuthInfo(generateLogin(), generatePassword(), "blocked");
            sendRequest(user);
            return user;
        }

        public static AuthInfo generateInvalidPasswordUser() {
            String login = generateLogin();
            sendRequest(new AuthInfo(login, generatePassword(), "active"));
            return new AuthInfo(login, generatePassword(), "active");
        }

        public static AuthInfo generateInvalidLoginUser() {
            String password = generatePassword();
            sendRequest(new AuthInfo(generateLogin(), password, "active"));
            return new AuthInfo(generateLogin(), password, "active");
        }
    }
}
