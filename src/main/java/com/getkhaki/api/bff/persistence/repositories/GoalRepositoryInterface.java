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
            value = "select id as id, " +
                    "       name as name, " +
                    "       greater_than_or_equal_to as greaterThanOrEqualTo, " +
                    "       less_than_or_equal_to as lessThanOrEqualTo, " +
                    "      department_id as departmentId, " +
                    "      organization_id as organizationId " +
                    "  from goal_dao gd " +
                    " where organization_id is null " +
                    "   and department_id is null ",
            nativeQuery = true
    )
    List<GoalDao> findDefaultGoals();

    @Query(
            value = "select id as id, " +
                    "       name as name, " +
                    "       greater_than_or_equal_to as greaterThanOrEqualTo, " +
                    "       less_than_or_equal_to as lessThanOrEqualTo, " +
                    "      department_id as departmentId, " +
                    "      organization_id as organizationId " +
                    "  from goal_dao gd " +
                    " where organization_id = :tenantId " +
                    "   and department_id is null ",
            nativeQuery = true
    )
    List<GoalDao> findDefaultOrganizationGoals(UUID tenantId);

}
