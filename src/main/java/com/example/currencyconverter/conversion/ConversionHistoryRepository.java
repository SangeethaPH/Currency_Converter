package com.example.currencyconverter.conversion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversionHistoryRepository extends JpaRepository<ConversionEntity, Long> {
    Page<ConversionEntity> findAllByApiClient_IdOrderByCreatedAtDesc(Long apiClientId, Pageable pageable);
}
