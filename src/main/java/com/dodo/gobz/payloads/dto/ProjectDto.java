package com.dodo.gobz.payloads.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProjectDto {
    private long id;
    private String name;
    private String description;
    @JsonProperty("isShared")
    private boolean shared;
    private Date createdAt;
    private Date updatedAt;
}
