package com.example.shopapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@MappedSuperclass // cho biết lớp này không ánh xạ vào DB, nhưng được kế thừa bởi lớp con
public class Base {
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist // đánh dấu onCreate() được gọi trước khi entity lưu vào DB.
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate // đánh dấu onCreate() được gọi trước khi entity cập nhật vào DB.
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
