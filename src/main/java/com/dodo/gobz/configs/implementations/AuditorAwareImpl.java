package com.dodo.gobz.configs.implementations;

import com.dodo.gobz.security.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated() ){
            return Optional.empty();
        }

        try {
            final UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
            return Optional.of(userPrincipal.getId());
        }catch (Exception e){
            return Optional.empty();
        }

    }
}
