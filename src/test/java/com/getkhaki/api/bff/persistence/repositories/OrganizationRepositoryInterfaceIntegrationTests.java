package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import com.getkhaki.api.bff.domain.models.FlagDe;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import lombok.val;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class OrganizationRepositoryInterfaceIntegrationTests extends BaseJpaIntegrationTest {
    @Inject
    private OrganizationRepositoryInterface underTest;

    @Inject
    private FlagRepositoryInterface flagRepository;

    @Test
    public void success_findDistinctByFlagsNotContaining() {
        val s56 = underTest.getOne(UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4"));
        val disableImportFlag = flagRepository.findDistinctByName(FlagDe.DisableImport.toString()).orElseThrow();

        s56.setFlags(Stream.of(disableImportFlag).collect(Collectors.toSet()));

        underTest.save(s56);

        val notS56Orgs = underTest.findDistinctByFlagsNameNotContaining(FlagDe.DisableImport.toString());

        assertThat(notS56Orgs).extracting(OrganizationDao::getName).doesNotContain("S56");
    }
}
