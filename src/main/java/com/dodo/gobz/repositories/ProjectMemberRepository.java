package com.dodo.gobz.repositories;

import com.dodo.gobz.models.ProjectMember;
import com.dodo.gobz.models.User;
import com.dodo.gobz.models.enums.MemberRole;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ProjectMemberRepository extends CrudRepository<ProjectMember, Long> {
    List<ProjectMember> findAllByUserAndRoleIn(User user, Collection<MemberRole> role);
}
