package io.cucumber;

import io.cucumber.cienvironment.CiEnvironment;

import static io.cucumber.cienvironment.DetectCiEnvironment.detectCiEnvironment;

public final class CiEnvironmentExample {

    private CiEnvironmentExample(){
        // Demo
    }
    
    public static void main(String[] args) {
        CiEnvironment ciEnvironment = detectCiEnvironment(System.getenv()).orElseThrow(() -> new RuntimeException("No CI environment detected"));
        System.out.println("ciEnvironment = " + ciEnvironment);
    }
}
