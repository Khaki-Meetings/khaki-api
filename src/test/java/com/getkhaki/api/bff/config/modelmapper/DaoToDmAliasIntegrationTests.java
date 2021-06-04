package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.AliasDm;
import com.getkhaki.api.bff.persistence.models.*;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoToDmAliasIntegrationTests  extends BaseModelMapperIntegrationTests {

    @Test
    public void success() {
        val emailDao = new EmailDao().setDomain(new DomainDao().setName("test.com")).setUser("test");
        val aliasDao = new AliasDao()
                .setAlias("testalias")
                .setEmail(emailDao);

        val aliasDm = underTest.map(aliasDao, AliasDm.class);

        assertThat(aliasDm.getAlias()).isEqualTo(aliasDao.getAlias());
        assertThat(aliasDm.getEmail()).isEqualTo(emailDao.getEmailString());
    }
}
