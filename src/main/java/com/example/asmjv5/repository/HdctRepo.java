package com.example.asmjv5.repository;

import com.example.asmjv5.model.Hdct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HdctRepo extends JpaRepository<Hdct, Integer> {
    @Query("SELECT h FROM Hdct h WHERE h.hoaDon.id = :idhd")
    List<Hdct> getL(Integer idhd);
}
