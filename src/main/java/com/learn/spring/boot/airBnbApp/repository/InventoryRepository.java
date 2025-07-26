package com.learn.spring.boot.airBnbApp.repository;

import com.learn.spring.boot.airBnbApp.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
