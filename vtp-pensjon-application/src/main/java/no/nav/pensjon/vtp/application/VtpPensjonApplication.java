package no.nav.pensjon.vtp.application;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VtpPensjonApplication {
    public static void main(String[] args) {
        System.setProperty("scenarios.dir", "./model/scenarios");

        run(VtpPensjonApplication.class, args);
    }
}
