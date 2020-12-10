package com.getkhaki.api.bff.config;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        daoToDmEmployee(modelMapper);
        dmToDaoEmployee(modelMapper);

        return modelMapper;
    }

    private void daoToDmEmployee(ModelMapper modelMapper) {
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
    }

    private void dmToDaoEmployee(ModelMapper modelMapper) {
        modelMapper.typeMap(EmployeeDm.class, EmployeeDao.class)
                .addMappings(
                        mapper -> {
                            mapper.<String>map(
                                    EmployeeDm::getDepartment,
                                    (dest, val) -> dest.getDepartment().setName(val)
                            );

                            mapper.using(
                                    ctx -> {
                                        String emailString = (String) ctx.getSource();
                                        String[] parts = emailString.split("@");
                                        if (parts.length != 2) {
                                            return List.of();
                                        }
                                        return List.of(
                                                new EmailDao()
                                                        .setUser(parts[0])
                                                        .setDomain(new DomainDao().setName(parts[1]))
                                        );
                                    }
                            ).<List<EmailDao>>map(
                                    EmployeeDm::getEmail,
                                    (dest, val) -> dest.getPerson().setEmails(val)
                            );
                            mapper.<String>map(
                                    EmployeeDm::getFirstName,
                                    (dest, val) -> dest.getPerson().setFirstName(val)
                            );
                            mapper.<String>map(
                                    EmployeeDm::getLastName,
                                    (dest, val) -> dest.getPerson().setLastName(val)
                            );
                        }
                )
        ;
    }
}
