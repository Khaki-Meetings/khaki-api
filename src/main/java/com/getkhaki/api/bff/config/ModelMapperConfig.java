package com.getkhaki.api.bff.config;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import com.getkhaki.api.bff.persistence.models.FlagDao;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import lombok.val;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

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
