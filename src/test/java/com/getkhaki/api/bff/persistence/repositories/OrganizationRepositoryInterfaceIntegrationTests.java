package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;

@DataJpaTest
public class OrganizationRepositoryInterfaceIntegrationTests extends BaseJpaIntegrationTest {
    @Inject
    private OrganizationRepositoryInterface underTest;

    @Inject
    private FlagRepositoryInterface flagRepository;

    @Test
    public void success_findDistinctByFlagsNotContaining() {
    }
}
