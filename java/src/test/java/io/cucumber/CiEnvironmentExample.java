package io.cucumber;

import io.cucumber.cienvironment.CiEnvironment;

import java.util.Optional;

import static io.cucumber.cienvironment.DetectCiEnvironment.detectCiEnvironment;

public class CiEnvironmentExample {
    public static void main(String[] args) {
        CiEnvironment ciEnvironment = detectCiEnvironment(System.getenv()).orElseThrow(() -> new RuntimeException("No CI environment detected"));
        System.out.println("ciEnvironment = " + ciEnvironment);
    }
}
