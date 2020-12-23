package com.sunset.spring.jpa.repository;

import com.sunset.spring.jpa.model.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Long> {
}
