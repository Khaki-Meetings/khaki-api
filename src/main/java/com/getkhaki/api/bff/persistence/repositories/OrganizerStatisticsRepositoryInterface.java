package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.OrganizerStatisticsViewDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizerStatisticsRepositoryInterface extends JpaRepository<OrganizerStatisticsViewDao, UUID> {

}
