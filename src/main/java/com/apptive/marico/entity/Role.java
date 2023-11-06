package com.apptive.marico.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private RoleName name;

    public Role() {}

    public Role(RoleName name) {
        this.name = name;
    }

    public enum RoleName {
        ROLE_MEMBER,
        ROLE_STYLIST,
        ROLE_ADMIN
    }

    public String getName() {
        return this.name.name();
    }

    public static final Role ROLE_USER = new Role(RoleName.ROLE_MEMBER);
    public static final Role ROLE_ADMIN = new Role(RoleName.ROLE_ADMIN);
    public static final Role ROLE_STYLIST = new Role(RoleName.ROLE_STYLIST);
}
