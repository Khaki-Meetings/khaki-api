package com.getkhaki.api.bff.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class DepartmentService {
    @Autowired
    public DepartmentService() {
    }

    public void importAsync(InputStream csvInputStream) {
    }
}
