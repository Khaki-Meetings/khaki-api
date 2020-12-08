package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

public class DepartmentControllerUnitTests {
    private DepartmentController underTest;
    private DepartmentService departmentService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        modelMapper = mock(ModelMapper.class);
        departmentService = mock(DepartmentService.class);
        underTest = new DepartmentController(this.departmentService, modelMapper);
    }

    @Test
    public void importAsync() throws IOException {
        var mockInputStream = mock(InputStream.class);
        var mockMultipartFile = mock(MultipartFile.class);

        when(mockMultipartFile.getInputStream())
                .thenReturn(mockInputStream);

        this.underTest.importAsync(mockMultipartFile);

        verify(this.departmentService).importAsync(mockInputStream);
    }
}
