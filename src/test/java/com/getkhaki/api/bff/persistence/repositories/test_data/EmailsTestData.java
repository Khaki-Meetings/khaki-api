package com.getkhaki.api.bff.persistence.repositories.test_data;

import com.getkhaki.api.bff.persistence.models.EmailDao;
import org.assertj.core.util.Lists;

import java.util.List;

public class EmailsTestData implements TestDataInterface<EmailDao> {
    @Override
    public List<EmailDao> getData() {
        return Lists.list(
                new EmailDao()
                        .setDomain(new DomainsTestData().getData().get(0))
                        .setUser("bob")
                        .setPerson(new PersonsTestData().getData().get(0)),
                new EmailDao()
                        .setDomain(new DomainsTestData().getData().get(0))
                        .setUser("ted")
                        .setPerson(new PersonsTestData().getData().get(1))

        );
    }
}
