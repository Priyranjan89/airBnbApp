package com.learn.spring.boot.airBnbApp.repository;

import com.learn.spring.boot.airBnbApp.entity.Hotel;
import com.learn.spring.boot.airBnbApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByOwner(User user);
}
