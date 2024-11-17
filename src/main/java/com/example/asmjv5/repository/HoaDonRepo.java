package com.example.asmjv5.repository;

import com.example.asmjv5.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HoaDonRepo extends JpaRepository<HoaDon, Integer> {
    @Query(value = "SELECT * from hoa_don where trang_thai='Chua thanh toan'", nativeQuery = true)
    List<HoaDon> getList();
}
