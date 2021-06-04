package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.AliasDm;
import com.getkhaki.api.bff.persistence.models.AliasDao;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DmToDaoAliasIntegrationTests extends BaseModelMapperIntegrationTests {

    @Test
    public void dmToDao() {
        val aliasDm = buildDm();

        val aliasDao = underTest.map(aliasDm, AliasDao.class);

        EmailDao emailDao = new EmailDao().setUser("test").setDomain(new DomainDao().setName("test.com"));

        assertThat(aliasDao.getAlias()).isEqualTo(aliasDm.getAlias());
        assertThat(aliasDao.getEmail().getEmailString()).isEqualTo(emailDao.getEmailString());
    }

    private AliasDm buildDm() {
        return AliasDm.builder()
            .alias("testAlias")
            .email("test@test.com")
            .build();
    }
}
