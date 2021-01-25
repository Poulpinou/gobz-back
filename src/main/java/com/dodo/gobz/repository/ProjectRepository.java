package com.dodo.gobz.repository;

import com.dodo.gobz.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProjectRepository extends CrudRepository<Project, Long> {
    @Query("FROM Project AS p LEFT JOIN p.members AS pm WHERE pm.user.id = ?1")
    List<Project> getProjectsByUserId(long userId);
}
