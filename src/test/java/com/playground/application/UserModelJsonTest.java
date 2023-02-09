package com.playground.application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@JsonTest
public class UserModelJsonTest {

    @Autowired
    private JacksonTester<UserModel> json;

    @Test
    public void userDeserializationTest() throws IOException {
        String expected = """
                {
                "id":1,
                "name":"Federico"
                }
           """;

        assertThat(json.parse(expected)).isEqualTo(new UserModel(1L, "Federico"));
        assertThat(json.parseObject(expected).id()).isEqualTo(1L);
        assertThat(json.parseObject(expected).name()).isEqualTo("Federico");
    }

    public void userSerializationTest() throws IOException {

        UserModel user = new UserModel(1L, "Federico");

        assertThat(json.write(user)).isStrictlyEqualToJson("expected.json");
        assertThat(json.write(user)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(user)).extractingJsonPathStringValue("@.name").isEqualTo("Federico");
    }
}
