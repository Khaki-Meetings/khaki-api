package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.FlagDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlagRepositoryInterface extends JpaRepository<FlagDao, UUID> {
    Optional<FlagDao> findDistinctByName(String name);
}
