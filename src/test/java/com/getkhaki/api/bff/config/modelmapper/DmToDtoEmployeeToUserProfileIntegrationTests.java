package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.web.models.UserProfileResponseDto;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DmToDtoEmployeeToUserProfileIntegrationTests extends BaseModelMapperIntegrationTests {

    @Test
    public void dmToDto() {
        EmployeeDm dm = new EmployeeDm()
                .setDepartment("IT")
                .setCompanyName("MyCo");
        dm .setEmail("joe@workingman.com")
                .setFirstName("Joe")
                .setLastName("Worker");

        val dto = underTest.map(dm, UserProfileResponseDto.class);

        assertThat(dto.getEmail()).isEqualTo(dm.getEmail());
        assertThat(dto.getCompanyName()).isEqualTo(dm.getCompanyName());
        assertThat(dto.getFirstName()).isEqualTo(dm.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(dm.getLastName());
    }
}
