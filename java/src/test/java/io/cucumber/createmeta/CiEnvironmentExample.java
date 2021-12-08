package io.cucumber.createmeta;

import static io.cucumber.createmeta.DetectCiEnvironment.detectCiEnvironment;

public class CiEnvironmentExample {
    public static void main(String[] args) {
        CiEnvironment ciEnvironment = detectCiEnvironment(System.getenv());
        System.out.println("ciEnvironment = " + ciEnvironment);
    }
}
