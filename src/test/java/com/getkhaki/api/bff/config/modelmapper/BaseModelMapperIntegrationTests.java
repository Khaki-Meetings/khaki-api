package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.config.ModelMapperConfig;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;

public class BaseModelMapperIntegrationTests {
    protected ModelMapper underTest;

    @BeforeEach
    public void setup() {
        ModelMapperConfig modelMapperConfig = new ModelMapperConfig();
        underTest = modelMapperConfig.modelMapper();
    }

}
