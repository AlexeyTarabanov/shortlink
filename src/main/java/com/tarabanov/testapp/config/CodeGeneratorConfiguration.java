package com.tarabanov.testapp.config;

import com.tarabanov.testapp.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodeGeneratorConfiguration {

    @Bean
    public CodeGenerator codeGenerator(@Value("${generator.length: 6}") Integer shortLinkLength) {
        return CodeGenerator.of(shortLinkLength);
    }

}
