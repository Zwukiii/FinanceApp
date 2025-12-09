package com.financeapp.backend.services;

import com.financeapp.backend.DTO.investment.InvestmentRequestDTO;
import com.financeapp.backend.mappers.InvestmentMapper;
import com.financeapp.backend.model.InvestmentModel;
import com.financeapp.backend.model.User;
import com.financeapp.backend.repository.InvestmentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final InvestmentMapper investmentMapper;

    public InvestmentService(InvestmentRepository investmentRepository, InvestmentMapper investmentMapper) {
        this.investmentRepository = investmentRepository;
        this.investmentMapper = investmentMapper;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @Transactional
    public InvestmentModel addInvestment(InvestmentModel investmentModel) {
        investmentModel.setUser(getCurrentUser());
        investmentModel.setRoi(calculateRoi(investmentModel));
        return investmentRepository.save(investmentModel);
    }

    public List<InvestmentModel> getAllInvestments() {
        return investmentRepository.findByUser(getCurrentUser());
    }

    public Optional<InvestmentModel> getInvestmentById(Long id) {
        return investmentRepository.findByIdAndUser(id, getCurrentUser());
    }

    @Transactional
    public InvestmentModel updateInvestment(Long id, InvestmentRequestDTO dto) {
        User currentUser = getCurrentUser();
        InvestmentModel updateInvest = investmentRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Investment doesn't exist by that id!"));
        investmentMapper.updateEntityFromDTO(dto, updateInvest);
        updateInvest.setRoi(calculateRoi(updateInvest));
        return investmentRepository.save(updateInvest);
    }

    @Transactional
    public void deleteInvestment(Long id) {
        User currentUser = getCurrentUser();
        investmentRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Investment doesn't exist by that id!"));
        investmentRepository.deleteByIdAndUser(id, currentUser);
    }

    public BigDecimal calculateRoi(InvestmentModel investment) {
        BigDecimal fvi = investment.getCurrentValue();
        BigDecimal ivi = investment.getAmountInvested();

        if (fvi == null || ivi == null || ivi.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return fvi.subtract(ivi)
                .divide(ivi, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getTotalInvested() {
        return investmentRepository.findByUser(getCurrentUser())
                .stream()
                .map(InvestmentModel::getAmountInvested)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalCurrentValue() {
        return investmentRepository.findByUser(getCurrentUser())
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
                .divide(totalInvested, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
