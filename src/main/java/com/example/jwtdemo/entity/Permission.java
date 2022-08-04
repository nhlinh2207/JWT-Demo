package com.example.jwtdemo.entity;

import com.example.jwtdemo.utils.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_permission")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PermissionId")
    private Long permissionId;

    @Column(name = "ActionCode", nullable = false, unique = true)
    private String actionCode;

    @Column(name = "ActionName", nullable = false, unique = true)
    private String actionName;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "CreatedAt", nullable = false)
    private Date createdAt;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "UpdatedAt")
    private Date updatedAt;
}
