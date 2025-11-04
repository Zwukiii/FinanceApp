package com.financeapp.backend.controller;


import com.financeapp.backend.DTO.MonthlyHistoryResponseDTO;
import com.financeapp.backend.mappers.MonthlyHistoryMapper;
import com.financeapp.backend.repository.MonthlyHistoryRepository;
import com.financeapp.backend.repository.TransactionRepository;
import com.financeapp.backend.services.MonthlyHistoryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/history")
@CrossOrigin(origins = "http://localhost:8081")
public class MonthlyHistoryController {

    private final MonthlyHistoryService monthlyHistoryService;
    private final MonthlyHistoryRepository monthlyHistoryRepository;
    private final TransactionRepository transactionRepository;

    public MonthlyHistoryController(MonthlyHistoryService monthlyHistoryService, MonthlyHistoryRepository monthlyHistoryRepository, TransactionRepository transactionRepository) {
        this.monthlyHistoryService = monthlyHistoryService;
        this.monthlyHistoryRepository = monthlyHistoryRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/generate")
    public void generateMonthlySummary() {
        monthlyHistoryService.generateMonthlySummary();
    }

    @GetMapping
    public List<MonthlyHistoryResponseDTO> getAllHistory() {
        return monthlyHistoryRepository.findAll()
                .stream()
                .map(MonthlyHistoryMapper::toDTO)
                .toList();


    }


    @Scheduled(cron = "0 0 0 1 * ?")
    public void autoGenerateMonthlySummary() {
        monthlyHistoryService.generateMonthlySummary();
    }


}
