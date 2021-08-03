package com.getkhaki.api.bff.config;

import com.getkhaki.api.bff.domain.models.CalendarEventParticipantDm;
import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.domain.models.EmployeeWithStatisticsDm;
import com.getkhaki.api.bff.persistence.models.CalendarEventParticipantDao;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import com.getkhaki.api.bff.persistence.models.views.EmployeeWithStatisticsView;
import com.getkhaki.api.bff.web.models.UserProfileResponseDto;
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

        daoToDmEmployee(modelMapper);
        dmToDaoEmployee(modelMapper);
        dmToDaoCalendarEventParticipant(modelMapper);
        viewToDmEmployeeWithStatistics(modelMapper);
        employeeDaoToUserProfileResponseDto(modelMapper);

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
                            mapper.map(src -> src.getPerson().getAvatarUrl(), EmployeeDm::setAvatarUrl);
                            mapper.map(src -> src.getDepartment().getOrganization().getName(), EmployeeDm::setCompanyName);

                            mapper.map(
                                    src -> src.getPerson().getPrimaryEmail().getEmailString(),
                                    EmployeeDm::setEmail
                            );
                        }
                );
    }

    private void viewToDmEmployeeWithStatistics(ModelMapper modelMapper) {
        modelMapper.typeMap(EmployeeWithStatisticsView.class, EmployeeWithStatisticsDm.class)
                .addMappings(
                        mapper -> {
                            mapper.map(src -> src.getEmployee().getDepartment().getName(), EmployeeWithStatisticsDm::setDepartment);
                            mapper.map(src -> src.getEmployee().getPerson().getFirstName(), EmployeeWithStatisticsDm::setFirstName);
                            mapper.map(src -> src.getEmployee().getPerson().getLastName(), EmployeeWithStatisticsDm::setLastName);
                            mapper.map(src -> src.getEmployee().getPerson().getAvatarUrl(), EmployeeWithStatisticsDm::setAvatarUrl);
                            mapper.map(src -> src.getEmployee().getPerson().getId(), EmployeeWithStatisticsDm::setId);
                            mapper.map(
                                    src -> src.getEmployee().getPerson().getPrimaryEmail().getEmailString(),
                                    EmployeeWithStatisticsDm::setEmail
                            );
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

    private void employeeDaoToUserProfileResponseDto(ModelMapper modelMapper) {
        modelMapper.typeMap(EmployeeDao.class, UserProfileResponseDto.class)
                .addMappings(
                        mapper -> {
                            mapper.map(src -> src.getDepartment().getName(), UserProfileResponseDto::setDepartment);
                            mapper.map(src -> src.getDepartment().getOrganization().getName(), UserProfileResponseDto::setCompanyName);
                            mapper.map(src -> src.getPerson().getFirstName(), UserProfileResponseDto::setFirstName);
                            mapper.map(src -> src.getPerson().getLastName(), UserProfileResponseDto::setLastName);
                            mapper.map(src -> src.getPerson().getAvatarUrl(), UserProfileResponseDto::setAvatarUrl);
                            mapper.map(src -> src.getPerson().getNotify(), UserProfileResponseDto::setNotify);
                            mapper.map(
                                    src -> src.getPerson().getPrimaryEmail().getEmailString(),
                                    UserProfileResponseDto::setEmail
                            );
                        }
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
