package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrganizationPersistenceServiceUnitTests {
    @Mock
    private OrganizationRepositoryInterface organizationRepository;
    @Mock
    EmailDaoService emailDaoService;
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
        when(organizationRepository.findAll()).thenReturn(new ArrayList<>(daoOrgSet));

        daoOrgSet.forEach(
                organizationDao -> when(modelMapper.map(organizationDao, OrganizationDm.class))
                        .thenReturn(dmSetItr.next())
        );

        val orgs = underTest.getImportEnabledOrganizations();

        assertThat(orgs).hasSize(2);
        assertThat(orgs).extracting(OrganizationDm::getName).contains("Billy");
        assertThat(orgs).extracting(OrganizationDm::getName).contains("Red Barchetta");
    }

    @Test
    public void upsertOrganization_notFound() {
        var mockOrganizationDm = mock(OrganizationDm.class);
        var mockEmailDao = mock(EmailDao.class);
        var mockDomainDao = mock(DomainDao.class);
        var mockOrganizationDao = mock(OrganizationDao.class);
        var mockPersistedOrganizationDao = mock(OrganizationDao.class);
        var mockMappedOrganizationDm = mock(OrganizationDm.class);

        when(this.emailDaoService.upsertByEmailString(mockOrganizationDm.getAdminEmail()))
                .thenReturn(mockEmailDao);

        when(mockEmailDao.getDomain()).thenReturn(mockDomainDao);

        when(mockDomainDao.setOrganization(mockOrganizationDao)).thenReturn(mockDomainDao);

        when(this.modelMapper.map(mockOrganizationDm, OrganizationDao.class))
                .thenReturn(mockOrganizationDao);

        when(this.organizationRepository.findDistinctByName(mockOrganizationDao.getName()))
                .thenReturn(Optional.empty());

        when(mockOrganizationDao.setAdminEmail(mockEmailDao)).thenReturn(mockOrganizationDao);
        when(mockOrganizationDao.setDomains(List.of(mockEmailDao.getDomain()))).thenReturn(mockOrganizationDao);

        when(this.organizationRepository.save(mockOrganizationDao))
                .thenReturn(mockPersistedOrganizationDao);

        when(this.modelMapper.map(mockPersistedOrganizationDao, OrganizationDm.class))
                .thenReturn(mockMappedOrganizationDm);

        OrganizationDm result = this.underTest.upsertOrganization(mockOrganizationDm);

        assertThat(result.getName()).isEqualTo(mockOrganizationDm.getName());
        assertThat(result.getAdminEmail()).isEqualTo(mockEmailDao.getEmailString());
        assertThat(result.getDepartments()).isEqualTo(mockOrganizationDm.getDepartments());
    }

    @Test
    public void upsertOrganization_found() {
        var mockOrganizationDm = mock(OrganizationDm.class);
        var mockEmailDao = mock(EmailDao.class);
        var mockDomainDao = mock(DomainDao.class);
        var mockOrganizationDao = mock(OrganizationDao.class);
        var mockFoundOrganizationDao = mock(OrganizationDao.class);
        var mockPersistedOrganizationDao = mock(OrganizationDao.class);
        var mockMappedOrganizationDm = mock(OrganizationDm.class);

        when(this.emailDaoService.upsertByEmailString(mockOrganizationDm.getAdminEmail()))
                .thenReturn(mockEmailDao);

        when(mockEmailDao.getDomain()).thenReturn(mockDomainDao);

        when(this.modelMapper.map(mockOrganizationDm, OrganizationDao.class))
                .thenReturn(mockOrganizationDao);

        when(this.organizationRepository.findDistinctByName(mockOrganizationDao.getName()))
                .thenReturn(Optional.of(mockFoundOrganizationDao));

        when(mockOrganizationDao.setAdminEmail(mockEmailDao)).thenReturn(mockOrganizationDao);
        when(mockOrganizationDao.setDomains(any(List.class))).thenReturn(mockOrganizationDao);

        when(this.organizationRepository.save(mockOrganizationDao))
                .thenReturn(mockPersistedOrganizationDao);

        when(this.modelMapper.map(mockPersistedOrganizationDao, OrganizationDm.class))
                .thenReturn(mockMappedOrganizationDm);

        OrganizationDm result = this.underTest.upsertOrganization(mockOrganizationDm);

        assertThat(result.getName()).isEqualTo(mockOrganizationDm.getName());
        assertThat(result.getAdminEmail()).isEqualTo(mockEmailDao.getEmailString());
        assertThat(result.getDepartments()).isEqualTo(mockOrganizationDm.getDepartments());
    }
}
