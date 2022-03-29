package co.com.emorae.adndb;


import co.com.emorae.model.sequence.gateways.SequenceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DataBaseConfig {

    @Bean
    public SequenceRepository dnaDataBase(){
        return new DnaDataBase(new ConcurrentHashMap<>());
    }
}
