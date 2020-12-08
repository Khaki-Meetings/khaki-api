package com.getkhaki.api.bff.config;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(EmployeeDao.class, EmployeeDm.class)
                .addMappings(
                        mapper -> {
                            mapper.map(src -> src.getDepartment().getName(), EmployeeDm::setDepartment);
                            mapper.map(src -> src.getPerson().getFirstName(), EmployeeDm::setFirstName);
                            mapper.map(src -> src.getPerson().getLastName(), EmployeeDm::setLastName);
                            mapper.map(src -> src.getPerson().getNotify(), EmployeeDm::setNotify);

                            mapper.map(
                                    src -> src.getPerson().getPrimaryEmail().getEmailString(),
                                    EmployeeDm::setEmail
                            );

                            mapper.skip(EmployeeDm::setAvatarUrl);
                        }
                );

        return modelMapper;
    }
}
