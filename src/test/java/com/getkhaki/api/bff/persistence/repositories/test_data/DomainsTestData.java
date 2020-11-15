package com.getkhaki.api.bff.persistence.repositories.test_data;

import com.getkhaki.api.bff.persistence.models.DomainDao;
import org.assertj.core.util.Lists;

import java.util.List;

public class DomainsTestData implements TestDataInterface<DomainDao> {
    @Override
    public List<DomainDao> getData() {
        return Lists.list(
                new DomainDao().setName("2112.com"),
                new DomainDao().setName("s56.com")
        );
    }
}
