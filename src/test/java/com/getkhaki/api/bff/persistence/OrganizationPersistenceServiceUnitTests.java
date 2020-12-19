package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrganizationPersistenceServiceUnitTests {
    @Mock
    private OrganizationRepositoryInterface organizationRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrganizationPersistenceService underTest;

    @Test
    public void success_getImportEnabledOrganizations() {
        val daoOrgSet = Stream.of(
                new OrganizationDao().setName("Billy"),
                new OrganizationDao().setName("Red Barchetta")
        ).collect(Collectors.toSet());
        val dmOrgSet = Stream.of(
                new OrganizationDm().setName("Billy"),
                new OrganizationDm().setName("Red Barchetta")
        ).collect(Collectors.toSet());

        val dmSetItr = dmOrgSet.iterator();
//        when(organizationRepository.findAll();

        daoOrgSet.forEach(
                organizationDao -> when(modelMapper.map(organizationDao, OrganizationDm.class))
                        .thenReturn(dmSetItr.next())
        );

        val orgs = underTest.getImportEnabledOrganizations();


        assertThat(orgs).hasSize(2);
        assertThat(orgs).extracting(OrganizationDm::getName).contains("Billy");
        assertThat(orgs).extracting(OrganizationDm::getName).contains("Red Barchetta");
    }

}
