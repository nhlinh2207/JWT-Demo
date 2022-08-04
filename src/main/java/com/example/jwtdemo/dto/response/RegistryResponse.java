package com.example.jwtdemo.dto.response;

import com.example.jwtdemo.utils.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistryResponse {
    private UUID userId;
    private String email;
    private String username;
    private Boolean isActive;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createdAt;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updatedAt;
}
