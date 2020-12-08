package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.BaseIntegrationTest;
import com.google.api.services.admin.directory.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@DataJpaTest
public class GoogleDirectoryRepositoryIntegrationTests extends BaseIntegrationTest {
    @Inject
    private GoogleDirectoryRepository underTest;

    @Test
    public void getUsers() throws IOException {
        List<User> result = this.underTest.getUsers("casey@s56.net");
    }
}
