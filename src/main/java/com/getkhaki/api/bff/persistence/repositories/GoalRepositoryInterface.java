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
            value = "select gd.id as id, " +
                    "       gd.name as name, " +
                    "       gd.greater_than_or_equal_to as greaterThanOrEqualTo, " +
                    "       gd.less_than_or_equal_to as lessThanOrEqualTo, " +
                    "       gd.department_id as departmentId, " +
                    "       gd.organization_id as organizationId " +
                    "  from goal_dao gd " +
                    " where gd.organization_id is null " +
                    "   and gd.department_id is null ",
            nativeQuery = true
    )
    List<GoalDao> findDefaultGoals();

    @Query(
            value = "select gd.id as id, " +
                    "       gd.name as name, " +
                    "       gd.greater_than_or_equal_to as greaterThanOrEqualTo, " +
                    "       gd.less_than_or_equal_to as lessThanOrEqualTo, " +
                    "       gd.department_id as departmentId, " +
                    "       gd.organization_id as organizationId " +
                    "  from goal_dao gd " +
                    " where gd.organization_id = :tenantId " +
                    "   and gd.department_id is null ",
            nativeQuery = true
    )
    List<GoalDao> findDefaultOrganizationGoals(UUID tenantId);

}
