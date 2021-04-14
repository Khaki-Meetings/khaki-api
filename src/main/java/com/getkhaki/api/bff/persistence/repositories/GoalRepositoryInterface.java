package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.GoalDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface GoalRepositoryInterface extends JpaRepository<GoalDao, UUID> {

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
