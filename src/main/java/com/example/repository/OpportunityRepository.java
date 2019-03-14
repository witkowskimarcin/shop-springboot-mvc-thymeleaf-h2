package com.example.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.entity.Opportunity;

public interface OpportunityRepository extends CrudRepository<Opportunity, Long> {


}
