package com.getkhaki.api.bff;

import com.getkhaki.api.bff.config.modelmapper.BaseModelMapperIntegrationTests;
import org.junit.jupiter.api.Test;

public class ModelMapperValidationIntegrationTests extends BaseModelMapperIntegrationTests {
    @Test
    public void validate() {
        underTest.validate();
    }


}
