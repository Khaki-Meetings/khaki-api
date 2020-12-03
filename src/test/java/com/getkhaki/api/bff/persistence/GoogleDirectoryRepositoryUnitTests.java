package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.persistence.models.Directory;
import com.getkhaki.api.bff.persistence.models.User;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoogleDirectoryRepositoryUnitTests {
    @Mock
    private GoogleDirectoryRepository underTest;
    @Mock
    private Directory client;




    @BeforeEach
    public void setup() {
        underTest = new GoogleDirectoryRepository(client);
    }

    @Test
    public void getUsers(){

        String adminEmail="";
        User user=new User("email");
        List<User> users= Lists.list(user);
        when(underTest.getUsers(adminEmail)).thenReturn(users);

        for (User underTestUser : underTest.getUsers(adminEmail)) {
            assertThat(underTestUser).isNotNull();
           assertThat(client.getUsers().contains(underTestUser)).isEqualTo(true);
        }


    }

    @Test
    public void getEvents(){

    }
}

