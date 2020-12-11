package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentDm;
import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.domain.models.csv.EmployeeCsvDm;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Service
public class CsvFileService {
    private final ModelMapper modelMapper;

    @Autowired
    public CsvFileService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Collection<DepartmentDm> read(InputStream csvInputStream) {
        CsvToBean<EmployeeCsvDm> csv = new CsvToBeanBuilder<EmployeeCsvDm>(new InputStreamReader(csvInputStream))
                .withType(EmployeeCsvDm.class)
                .build();

        var departmentMap = new HashMap<String, DepartmentDm>();

        csv.parse()
                .forEach(employeeCsvDm -> departmentMap.computeIfAbsent(
                        employeeCsvDm.getDepartment(), name -> DepartmentDm.builder()
                                .name(name)
                                .employees(new ArrayList<>())
                                .build()
                        )
                        .getEmployees()
                        .add(this.modelMapper.map(employeeCsvDm, EmployeeDm.class))
                );

        return departmentMap.values();
    }
}
