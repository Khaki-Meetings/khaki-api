package com.getkhaki.api.bff.persistence.repositories;

import com.google.api.services.admin.directory.Directory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GoogleDirectoryRepositoryUnitTests {
    @Mock
    private Directory client;
    @InjectMocks
    private GoogleDirectoryRepository underTest;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void getUsers(){
    }
}
