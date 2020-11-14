package no.nav.pensjon.vtp.testmodell;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootConfiguration
@AutoConfigurationPackage
@EnableMongoRepositories
@ComponentScan
public interface ModelTestConfiguration {
}
