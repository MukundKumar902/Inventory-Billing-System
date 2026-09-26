package com.inventory.billing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Role() {}

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RoleBuilder builder() {
        return new RoleBuilder();
    }

    public static class RoleBuilder {
        private Long id;
        private String name;

        public RoleBuilder id(Long id) { this.id = id; return this; }
        public RoleBuilder name(String name) { this.name = name; return this; }
        public Role build() { return new Role(id, name); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
