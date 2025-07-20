package com.planwise.repository;

import com.planwise.model.SearchFrequency;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchFrequencyRepository extends JpaRepository<SearchFrequency, String> {
}