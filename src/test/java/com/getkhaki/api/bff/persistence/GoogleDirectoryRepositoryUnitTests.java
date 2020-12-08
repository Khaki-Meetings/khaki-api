package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;
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

     /*   User: bob@workspacetest.dev
        Password: XKVy<g8M

        User: harris@workspacetest.dev
        Password: EUrM<2FH*/

        String adminEmail="bob@workspacetest.dev";
        User user=new User();
        user.setPrimaryEmail("harris@workspacetest.dev");
        user.setPassword("EUrM<2FH");
        List<User> users= Lists.list(user);
        try {
            when(underTest.getUsers(adminEmail)).thenReturn(users);
            for (User underTestUser : underTest.getUsers(adminEmail)) {
                assertThat(underTestUser).isNotNull();
                assertThat(users.contains(underTestUser)).isEqualTo(true);
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }




    }

    /*@Test
    public void getEvents(){

    }*/
}

