package com.planwise.repository;

import com.planwise.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    // Long is the type of your 'id' primary key in plans table.
}