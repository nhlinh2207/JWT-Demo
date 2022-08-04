package com.example.jwtdemo.entity;

import com.example.jwtdemo.utils.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tbl_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleId")
    private Long roleId;

    @Column(name = "RoleName", unique = true, nullable = false)
    private String roleName;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude // không sử dụng trường này trong equals và hashcode
    @ToString.Exclude // Không sử dụng trong toString()
    @JoinTable(name = "tbl_role_permission", joinColumns = {
            @JoinColumn(name = "RoleId", referencedColumnName = "RoleId")},
            inverseJoinColumns = {
                    @JoinColumn(name = "PermissionId", referencedColumnName = "PermissionId")})
    private Set<Permission> permissions;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> users;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "CreatedAt", nullable = false)
    private Date createdAt;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "UpdatedAt")
    private Date updatedAt;
}
