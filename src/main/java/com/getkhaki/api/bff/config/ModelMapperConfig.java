package com.getkhaki.api.bff.config;

import com.getkhaki.api.bff.domain.models.AliasDm;
import com.getkhaki.api.bff.domain.models.CalendarEventParticipantDm;
import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.persistence.models.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        daoToDmAlias(modelMapper);
        dmToDaoAlias(modelMapper);
        daoToDmEmployee(modelMapper);
        dmToDaoEmployee(modelMapper);
        dmToDaoCalendarEventParticipant(modelMapper);

        return modelMapper;
    }

    private void daoToDmAlias(ModelMapper modelMapper) {
        modelMapper.typeMap(AliasDao.class, AliasDm.class)
                .addMappings(
                        mapper -> {
                            mapper.map(src -> src.getEmail().getEmailString(), AliasDm::setEmail);
                        }
                );
    }

    private void dmToDaoAlias(ModelMapper modelMapper) {
        modelMapper.typeMap(AliasDm.class, AliasDao.class)
                .addMappings(
                        mapper -> {
                            mapper.using(stringToEmailConverter())
                                .map(
                                    AliasDm::getEmail,
                                    AliasDao::setEmail
                                );
                        }
                );
    }

    private void daoToDmEmployee(ModelMapper modelMapper) {
        modelMapper.typeMap(EmployeeDao.class, EmployeeDm.class)
                .addMappings(
                        mapper -> {
                            mapper.map(src -> src.getDepartment().getName(), EmployeeDm::setDepartment);
                            mapper.map(src -> src.getPerson().getFirstName(), EmployeeDm::setFirstName);
                            mapper.map(src -> src.getPerson().getLastName(), EmployeeDm::setLastName);
                            mapper.map(src -> src.getPerson().getNotify(), EmployeeDm::setNotify);
                            mapper.map(src -> src.getDepartment().getOrganization().getName(), EmployeeDm::setCompanyName);

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
                                    stringToEmailListConverter()
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

    private void dmToDaoCalendarEventParticipant(ModelMapper modelMapper) {
        modelMapper.typeMap(CalendarEventParticipantDm.class, CalendarEventParticipantDao.class)
                .addMappings(
                        mapper -> mapper.using(stringToEmailConverter())
                                .map(
                                        CalendarEventParticipantDm::getEmail,
                                        CalendarEventParticipantDao::setEmail
                                )
                );
    }

    private Converter<?, ?> stringToEmailConverter() {
        return ctx -> stringToEmail((String) ctx.getSource());
    }

    private Converter<?, ?> stringToEmailListConverter() {
        return ctx -> {
            EmailDao emailDao = stringToEmail((String) ctx.getSource());
            return (emailDao == null) ? List.of() : List.of(emailDao);
        };
    }

    private EmailDao stringToEmail(String emailString) {
        String[] parts = emailString.split("@");
        if (parts.length != 2) {
            return null;
        }
        return new EmailDao()
                .setUser(parts[0])
                .setDomain(new DomainDao().setName(parts[1]));
    }
}
