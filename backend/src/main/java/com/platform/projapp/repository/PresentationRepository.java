package com.platform.projapp.repository;

import com.platform.projapp.model.Presentation;
import com.platform.projapp.model.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Long> {
}
