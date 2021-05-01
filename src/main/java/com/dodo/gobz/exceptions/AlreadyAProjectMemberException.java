package com.dodo.gobz.exceptions;

import com.dodo.gobz.models.Project;
import com.dodo.gobz.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AlreadyAProjectMemberException extends RuntimeException {
    public AlreadyAProjectMemberException(Project project, User user) {
        super(String.format("User %s is already a member of Project %s", user.getId(), project.getId()));
    }
}
