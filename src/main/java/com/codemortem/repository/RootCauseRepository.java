package com.codemortem.repository;

import com.codemortem.entity.RootCause;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RootCauseRepository extends JpaRepository<RootCause, Long> {
}