package com.getkhaki.api.bff.persistence.repositories;
import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrganizersStatisticsRepositoryInterface extends JpaRepository<OrganizersStatisticsDm, UUID> {
    @Query("")
    OrganizersStatisticsDm getOrganizerStatistics(String email);
}
