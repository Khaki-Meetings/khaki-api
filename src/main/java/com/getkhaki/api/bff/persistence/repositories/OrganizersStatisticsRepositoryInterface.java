package com.getkhaki.api.bff.persistence.repositories;
import com.getkhaki.api.bff.persistence.models.OrganizersStatisticsDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrganizersStatisticsRepositoryInterface extends JpaRepository<OrganizersStatisticsDao, UUID> {
    @Query("")
    OrganizersStatisticsDao findOrganizerStatisticsByEmail(String email);
}
