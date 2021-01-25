package com.dodo.gobz.repository;

import com.dodo.gobz.model.ProjectMember;
import com.dodo.gobz.model.User;
import com.dodo.gobz.model.common.MemberRole;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ProjectMemberRepository extends CrudRepository<ProjectMember, Long> {
    List<ProjectMember> findAllByUserAndRoleIn(User user, Collection<MemberRole> role);
}
