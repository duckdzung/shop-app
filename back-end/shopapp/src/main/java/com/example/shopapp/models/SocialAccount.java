package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    private String name;

    private String email;
}
