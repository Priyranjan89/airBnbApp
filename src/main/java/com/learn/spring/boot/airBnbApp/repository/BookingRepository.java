package com.learn.spring.boot.airBnbApp.repository;

import com.learn.spring.boot.airBnbApp.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Loads the associated user eagerly
    @EntityGraph(attributePaths = {"user"})
    Optional<Booking> findById(Long id);

    @EntityGraph(attributePaths = {"user", "guests"})
    Optional<Booking> findWithUserAndGuestsById(Long id);

    @EntityGraph(attributePaths = {"user", "guests"})
    Page<Booking> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "guests"})
    Page<Booking> findByUserId(Long userId, Pageable pageable);
}