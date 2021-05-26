package com.dodo.gobz.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberRole {
    OWNER(3),
    CONTRIBUTOR(2),
    VIEWER(1);

    private final int rolePower;
}
