package com.bombanya.web4.points;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointsRepository extends JpaRepository<Point, Long> {
    List<Point> findAllByUsername(String username);
}
