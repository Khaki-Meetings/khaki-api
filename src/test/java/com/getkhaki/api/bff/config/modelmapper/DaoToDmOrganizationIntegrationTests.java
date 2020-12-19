package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoToDmOrganizationIntegrationTests extends BaseModelMapperIntegrationTests {
    @Test
    public void daoToDm() {
        val organizationDao = new OrganizationDao()
                .setName("Cygnus")
                .setAdminEmail(
                        new EmailDao()
                                .setUser("bob")
                                .setDomain(new DomainDao().setName("cygnus.com"))
                );

        val organizationDm = underTest.map(organizationDao, OrganizationDm.class);

        assertThat(organizationDm.getAdminEmail()).isEqualTo(organizationDao.getAdminEmail().getEmailString());
        assertThat(organizationDm.getName()).isEqualTo(organizationDao.getName());

    }
}
