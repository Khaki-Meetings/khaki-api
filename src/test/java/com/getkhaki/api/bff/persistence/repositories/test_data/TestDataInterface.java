package com.getkhaki.api.bff.persistence.repositories.test_data;

import com.getkhaki.api.bff.persistence.models.EntityBaseDao;

import java.util.List;

public interface TestDataInterface<T extends EntityBaseDao> {
    List<T> getData();
}
