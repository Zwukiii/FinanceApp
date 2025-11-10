package com.financeapp.backend.services;

import com.financeapp.backend.DTO.investment.InvestmentRequestDTO;
import com.financeapp.backend.mappers.InvestmentMapper;
import com.financeapp.backend.model.InvestmentModel;
import com.financeapp.backend.repository.InvestmentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class InvestmentService {
    private final InvestmentRepository InvestmentRepository;
    private final InvestmentMapper investmentMapper;

    public InvestmentService(InvestmentRepository investmentRepository, InvestmentMapper investmentMapper) {
        InvestmentRepository = investmentRepository;
        this.investmentMapper = investmentMapper;
    }

    public InvestmentModel addInvestment(InvestmentModel investmentModel) {
        investmentModel.setRoi(calculateRoi(investmentModel));
        return InvestmentRepository.save(investmentModel);
    }

    public List<InvestmentModel> getAllInvestments() {
        return InvestmentRepository.findAll();
    }

    public Optional<InvestmentModel> getInvestmentById(Long id) {
        return InvestmentRepository.findById(id);
    }

    public InvestmentModel updateInvestment(Long id, InvestmentRequestDTO dto) {
        InvestmentModel updateInvest = InvestmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investment doesn't exist by that id!"));
        investmentMapper.updateEntityFromDTO(dto, updateInvest);
        updateInvest.setRoi(calculateRoi(updateInvest));
        return InvestmentRepository.save(updateInvest);

    }

    public void deleteInvestment(Long id) {
        InvestmentRepository.deleteById(id);

    }

    public BigDecimal calculateRoi(InvestmentModel investment) {
        BigDecimal fvi = investment.getCurrentValue(); // Final value of investment
        BigDecimal ivi = investment.getAmountInvested(); // Initial value of investment

        if (fvi == null || ivi == null || ivi.compareTo(BigDecimal.ZERO) == 0) { // Small error handling.
            return BigDecimal.ZERO;
        }

        return fvi.subtract(ivi)
                .divide(ivi)
                .multiply(BigDecimal.valueOf(100));

    }

    public BigDecimal getTotalInvested() {
        return InvestmentRepository.findAll()
                .stream()
                .map(InvestmentModel::getAmountInvested)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalCurrentValue() {
        return InvestmentRepository.findAll()
                .stream()
                .map(InvestmentModel::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalRoi() {
        BigDecimal totalInvested = getTotalInvested();
        BigDecimal totalCurrentValue = getTotalCurrentValue();

        if (totalInvested.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return totalCurrentValue.subtract(totalInvested)
                .divide(totalInvested)
                .multiply(BigDecimal.valueOf(100));

    }
}
