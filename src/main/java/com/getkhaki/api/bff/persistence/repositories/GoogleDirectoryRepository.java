package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.Directory;
import com.getkhaki.api.bff.persistence.models.User;

import java.util.List;

public class GoogleDirectoryRepository {
    private Directory client;

    public GoogleDirectoryRepository(Directory client) {
        this.client = client;
    }

    public List<User> getUsers(String adminEmail){
        return List.of();
    }

}
