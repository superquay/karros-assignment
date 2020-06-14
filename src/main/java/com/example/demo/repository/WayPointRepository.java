package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.WayPoint;

public interface WayPointRepository extends JpaRepository<WayPoint, Long> {
	List<WayPoint> findByGpsId(Long gpsId);
}
