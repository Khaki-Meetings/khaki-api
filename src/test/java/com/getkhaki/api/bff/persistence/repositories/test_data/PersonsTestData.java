package com.getkhaki.api.bff.persistence.repositories.test_data;

import com.getkhaki.api.bff.persistence.models.PersonDao;
import org.assertj.core.util.Lists;

import java.util.List;

public class PersonsTestData implements TestDataInterface<PersonDao> {
    @Override
    public List<PersonDao> getData() {
        List<PersonDao> data = Lists.list(
                new PersonDao()
                        .setFirstName("Bob")
                        .setLastName("Jones"),
                new PersonDao()
                        .setFirstName("John")
                        .setLastName("Johnson")
        );
        return data;
    }
}
