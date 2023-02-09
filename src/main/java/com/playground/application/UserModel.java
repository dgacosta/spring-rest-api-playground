package com.playground.application;

import org.springframework.data.annotation.Id;

public record UserModel(@Id Long id, String name) {
}
