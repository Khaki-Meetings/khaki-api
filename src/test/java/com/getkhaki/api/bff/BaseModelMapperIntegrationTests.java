package com.getkhaki.api.bff;

import com.getkhaki.api.bff.config.ModelMapperConfig;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;

public class BaseModelMapperIntegrationTests {
    protected ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        ModelMapperConfig modelMapperConfig = new ModelMapperConfig();
        modelMapper = modelMapperConfig.modelMapper();
    }

}
