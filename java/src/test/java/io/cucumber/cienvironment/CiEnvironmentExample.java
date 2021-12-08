package io.cucumber.cienvironment;

import static io.cucumber.cienvironment.DetectCiEnvironment.detectCiEnvironment;

public class CiEnvironmentExample {
    public static void main(String[] args) {
        CiEnvironment ciEnvironment = detectCiEnvironment(System.getenv());
        System.out.println("ciEnvironment = " + ciEnvironment);
    }
}