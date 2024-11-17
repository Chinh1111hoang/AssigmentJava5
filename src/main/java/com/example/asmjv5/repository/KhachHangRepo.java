package com.example.asmjv5.repository;

import com.example.asmjv5.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KhachHangRepo extends JpaRepository<KhachHang, Integer> {
    Optional<KhachHang> findBySdt(String sdt);
}
