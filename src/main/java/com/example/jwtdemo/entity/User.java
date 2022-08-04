package com.example.jwtdemo.entity;

import com.example.jwtdemo.utils.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type="uuid-char")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "UserId", nullable = false)
    private UUID userId;

    @Column(name = "Username", length = 30, nullable = false, unique = true)
    private String username;

    @Column(name = "Password", length = 100, nullable = false)
    private String password;

    @Column(name = "Email", length = 40, nullable = false, unique = true)
    private String email;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @JoinTable(name = "tbl_user_role",
               joinColumns = {
                    @JoinColumn(name = "UserId", referencedColumnName = "UserId")
               },
               inverseJoinColumns = {
                    @JoinColumn(name = "RoleId", referencedColumnName = "RoleId")
               })
    private Set<Role> roles;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "CreatedAt", nullable = false)
    private Date createdAt;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Column(name = "UpdatedAt")
    private Date updatedAt;
}
