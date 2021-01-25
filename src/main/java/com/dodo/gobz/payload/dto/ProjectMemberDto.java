package com.dodo.gobz.payload.dto;

import com.dodo.gobz.model.common.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberDto {
    private long id;
    private String name;
    private String imageUrl;
    private MemberRole role;
}
