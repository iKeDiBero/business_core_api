package com.kedibero.business_core_api.repository;

import com.kedibero.business_core_api.entity.DbConnectionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbConnectionTestRepository extends JpaRepository<DbConnectionTest, Long> {
}

