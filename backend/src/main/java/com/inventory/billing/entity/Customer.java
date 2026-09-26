package com.inventory.billing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String email;
    private String address;

    public Customer() {}

    public Customer(Long id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public static class CustomerBuilder {
        private Long id;
        private String name;
        private String phone;
        private String email;
        private String address;

        public CustomerBuilder id(Long id) { this.id = id; return this; }
        public CustomerBuilder name(String name) { this.name = name; return this; }
        public CustomerBuilder phone(String phone) { this.phone = phone; return this; }
        public CustomerBuilder email(String email) { this.email = email; return this; }
        public CustomerBuilder address(String address) { this.address = address; return this; }
        public Customer build() { return new Customer(id, name, phone, email, address); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
