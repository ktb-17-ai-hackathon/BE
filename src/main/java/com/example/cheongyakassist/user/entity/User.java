package com.example.cheongyakassist.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")

public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String email;
        private String password;
        private String name;

        @Column(name = "is_deleted")
        private boolean isDeleted;

        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        public User() {}

        public User( String email, String password, String name) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.isDeleted = false;
            this.deletedAt = null;
            this.createdAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }


}
