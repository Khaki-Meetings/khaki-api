package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentDm;
import com.getkhaki.api.bff.domain.models.csv.EmployeeCsvDm;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvFileService {
    private final ModelMapper modelMapper;

    @Autowired
    public CsvFileService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<DepartmentDm> read(InputStream csvInputStream) {
        CsvToBean<EmployeeCsvDm> csv = new CsvToBeanBuilder<EmployeeCsvDm>(new InputStreamReader(csvInputStream))
                .withType(EmployeeCsvDm.class)
                .build();

        return csv.parse()
                .stream()
                .map(row -> modelMapper.map(row, DepartmentDm.class))
                .collect(Collectors.toList());
    }
}
