package com.financeapp.backend.controller;

import com.financeapp.backend.DTO.investment.InvestmentRequestDTO;
import com.financeapp.backend.DTO.investment.InvestmentResponseDTO;
import com.financeapp.backend.mappers.InvestmentMapper;
import com.financeapp.backend.model.InvestmentModel;
import com.financeapp.backend.services.InvestmentService;
import com.financeapp.backend.services.MonthlyHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;
    private final MonthlyHistoryService monthlyHistoryService;
    private final InvestmentMapper investmentMapper;

    public InvestmentController(InvestmentService investmentService, MonthlyHistoryService monthlyHistoryService, InvestmentMapper investmentMapper) {
        this.investmentService = investmentService;
        this.monthlyHistoryService = monthlyHistoryService;
        this.investmentMapper = investmentMapper;
    }

    @PostMapping()
    public InvestmentResponseDTO addInvestment(@RequestBody InvestmentRequestDTO dto) {
        InvestmentModel entity = investmentMapper.toEntity(dto);
        InvestmentModel saved = investmentService.addInvestment(entity);
        monthlyHistoryService.generateMonthlySummary();
        return investmentMapper.toDTO(saved);
    }

    @GetMapping
    public List<InvestmentResponseDTO> getAllInvestments() {
        return investmentService.getAllInvestments()
                .stream()
                .map(investmentMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public InvestmentResponseDTO getInvestmentById(@PathVariable Long id) {
        InvestmentModel investment = investmentService.getInvestmentById(id)
                .orElseThrow(() -> new RuntimeException("Investment not found!"));

        return investmentMapper.toDTO(investment);
    }

    @PutMapping("/{id}")
    public InvestmentResponseDTO updateInvestment(@PathVariable Long id, @RequestBody InvestmentRequestDTO dto) {
        InvestmentModel updated = investmentService.updateInvestment(id, dto);
        return investmentMapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvestment(@PathVariable Long id) {
        investmentService.deleteInvestment(id);
        monthlyHistoryService.generateMonthlySummary();
        return ResponseEntity.ok("Investment deleted successfully");
    }

    @GetMapping("/total-invested")
    public BigDecimal getTotalInvested() {
        return investmentService.getTotalInvested();
    }

    @GetMapping("/total-current")
    public BigDecimal getTotalCurrentValue() {
        return investmentService.getTotalCurrentValue();
    }

    @GetMapping("/total-roi")
    public BigDecimal getTotalRoi() {
        return investmentService.getTotalRoi();
    }
}
