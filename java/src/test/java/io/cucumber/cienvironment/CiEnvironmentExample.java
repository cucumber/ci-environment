package io.cucumber.cienvironment;

import java.util.Optional;

import static io.cucumber.cienvironment.DetectCiEnvironment.detectCiEnvironment;

public class CiEnvironmentExample {
    public static void main(String[] args) {
        Optional<CiEnvironment> ciEnvironment = detectCiEnvironment(System.getenv());
        System.out.println("ciEnvironment = " + ciEnvironment);
    }
}
