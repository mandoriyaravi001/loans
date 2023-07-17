package com.example.LoanApp1.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LoanApp1.entity.Loan;

@Service
public class LoanStoreService {
	@Autowired
    private Map<String, Loan> loans;
    private Logger logger;
    
    public LoanStoreService() {
        loans = new HashMap<>();
        logger = Logger.getLogger(LoanStoreService.class.getName());
    }
    
    public void addLoan(Loan loan) throws Exception {
        if (loan.getPaymentDate().isAfter(loan.getDueDate())) {
            throw new Exception("Payment date cannot be greater than the due date.");
        }
        
        loans.put(loan.getLoanId()+1, loan);
       // loanRepo.save(loan);
        
        if (LocalDate.now().isAfter(loan.getDueDate())) {
            logger.warning("Loan with ID " + loan.getLoanId() + " has crossed the due date.");
        }
    }
    
    public List<Loan> getAllLoans() {
        return List.copyOf(loans.values());
    }
    
    public Map<String, Double> aggregateByLender() {
        Map<String, Double> lenderAggregation = new HashMap<>();
        
        for (Loan loan : loans.values()) {
            double aggregatedAmount = lenderAggregation.getOrDefault(loan.getLenderId(), 0.0);
            aggregatedAmount += loan.getRemainingAmount();
            lenderAggregation.put(loan.getLenderId(), aggregatedAmount);
        }
        
        return lenderAggregation;
    }
    
    public Map<Double, Double> aggregateByInterest() {
        Map<Double, Double> interestAggregation = new HashMap<>();
        
        for (Loan loan : loans.values()) {
            double aggregatedAmount = interestAggregation.getOrDefault(loan.getInterestPerDay(), 0.0);
            aggregatedAmount += loan.getRemainingAmount();
            interestAggregation.put(loan.getInterestPerDay(), aggregatedAmount);
        }
        
        return interestAggregation;
    }
    
    public Map<String, Double> aggregateByCustomerId() {
        Map<String, Double> customerIdAggregation = new HashMap<>();
        
        for (Loan loan : loans.values()) {
            double aggregatedAmount = customerIdAggregation.getOrDefault(loan.getCustomerId(), 0.0);
            aggregatedAmount += loan.getRemainingAmount();
            customerIdAggregation.put(loan.getCustomerId(), aggregatedAmount);
        }
        
        return customerIdAggregation;
    }
}
