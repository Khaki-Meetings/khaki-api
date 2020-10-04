package com.getkhaki.api.bff.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTransformers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setDestinationNameTransformer(NameTransformers.JAVABEANS_MUTATOR)
                .setSourceNameTransformer(NameTransformers.JAVABEANS_MUTATOR);
        return modelMapper;
    }
}
