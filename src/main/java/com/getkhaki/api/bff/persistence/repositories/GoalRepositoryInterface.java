package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.GoalDao;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepositoryInterface extends JpaRepository<GoalDao, UUID> {

    Optional<GoalDao> findById(UUID uuid);

    Optional<GoalDao> findDistinctByOrganizationIdAndName(UUID organizationId, String name);

    @Query(
            value = "select g from GoalDao g " +
                    " where g.organization is null " +
                    " and g.department is null "
    )
    List<GoalDao> findDefaultGoals();

    @Query(
            value = "select g from GoalDao g " +
                    "   inner join g.organization organization" +
                    " where organization.id = :tenantId  " +
                    " and g.department is null "
    )
    List<GoalDao> findDefaultOrganizationGoals(UUID tenantId);

}
