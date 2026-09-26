package com.inventory.billing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String contactPerson;
    private String phone;
    private String email;
    private String address;

    public Supplier() {}

    public Supplier(Long id, String name, String contactPerson, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public static SupplierBuilder builder() {
        return new SupplierBuilder();
    }

    public static class SupplierBuilder {
        private Long id;
        private String name;
        private String contactPerson;
        private String phone;
        private String email;
        private String address;

        public SupplierBuilder id(Long id) { this.id = id; return this; }
        public SupplierBuilder name(String name) { this.name = name; return this; }
        public SupplierBuilder contactPerson(String contactPerson) { this.contactPerson = contactPerson; return this; }
        public SupplierBuilder phone(String phone) { this.phone = phone; return this; }
        public SupplierBuilder email(String email) { this.email = email; return this; }
        public SupplierBuilder address(String address) { this.address = address; return this; }
        public Supplier build() { return new Supplier(id, name, contactPerson, phone, email, address); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
