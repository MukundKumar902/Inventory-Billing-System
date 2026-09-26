package com.inventory.billing.config;

import com.inventory.billing.entity.Category;
import com.inventory.billing.entity.Product;
import com.inventory.billing.entity.Role;
import com.inventory.billing.entity.User;
import com.inventory.billing.repository.CategoryRepository;
import com.inventory.billing.repository.ProductRepository;
import com.inventory.billing.repository.RoleRepository;
import com.inventory.billing.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, CategoryRepository categoryRepository, ProductRepository productRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));

        Role cashierRole = roleRepository.findByName("ROLE_CASHIER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_CASHIER").build()));

        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Store Owner (Admin)")
                    .active(true)
                    .roles(Set.of(adminRole))
                    .build();
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("cashier")) {
            User cashier = User.builder()
                    .username("cashier")
                    .password(passwordEncoder.encode("cashier123"))
                    .fullName("Main Cashier")
                    .active(true)
                    .roles(Set.of(cashierRole))
                    .build();
            userRepository.save(cashier);
        }

        Category grocery = categoryRepository.findByName("Grocery")
                .orElseGet(() -> categoryRepository.save(Category.builder().name("Grocery").description("Daily essential food & household items").build()));
        
        Category medicines = categoryRepository.findByName("Medicines")
                .orElseGet(() -> categoryRepository.save(Category.builder().name("Medicines").description("Healthcare & pharmaceutical products").build()));
        
        Category electronics = categoryRepository.findByName("Electronics")
                .orElseGet(() -> categoryRepository.save(Category.builder().name("Electronics").description("Mobile accessories & gadgets").build()));

        if (productRepository.count() == 0) {
            productRepository.save(Product.builder()
                    .name("Fortune Sunflower Oil 1L")
                    .barcode("8901234567890")
                    .price(145.0)
                    .gstPercent(5.0)
                    .currentStock(50)
                    .minStockAlert(10)
                    .unit("Pouch")
                    .imageUrl("https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=500&auto=format&fit=crop&q=60")
                    .category(grocery)
                    .build());

            productRepository.save(Product.builder()
                    .name("Paracetamol 650mg")
                    .barcode("8909876543210")
                    .price(32.5)
                    .gstPercent(12.0)
                    .currentStock(100)
                    .minStockAlert(20)
                    .unit("Strip")
                    .imageUrl("https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=500&auto=format&fit=crop&q=60")
                    .category(medicines)
                    .build());

            productRepository.save(Product.builder()
                    .name("USB-C Fast Charging Cable")
                    .barcode("8905555444333")
                    .price(299.0)
                    .gstPercent(18.0)
                    .currentStock(4)
                    .minStockAlert(10)
                    .unit("Pcs")
                    .imageUrl("https://images.unsplash.com/photo-1588508065123-287b28e013da?w=500&auto=format&fit=crop&q=60")
                    .category(electronics)
                    .build());
        } else {
            // Update existing products if imageUrl is missing
            List<Product> products = productRepository.findAll();
            for (Product p : products) {
                if (p.getImageUrl() == null || p.getImageUrl().isEmpty()) {
                    if (p.getName().toLowerCase().contains("oil")) {
                        p.setImageUrl("https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=500&auto=format&fit=crop&q=60");
                    } else if (p.getName().toLowerCase().contains("paracetamol")) {
                        p.setImageUrl("https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=500&auto=format&fit=crop&q=60");
                    } else if (p.getName().toLowerCase().contains("cable") || p.getName().toLowerCase().contains("usb")) {
                        p.setImageUrl("https://images.unsplash.com/photo-1588508065123-287b28e013da?w=500&auto=format&fit=crop&q=60");
                    } else {
                        p.setImageUrl("https://images.unsplash.com/photo-1542838132-92c53300491e?w=500&auto=format&fit=crop&q=60");
                    }
                    productRepository.save(p);
                }
            }
        }
    }
}
