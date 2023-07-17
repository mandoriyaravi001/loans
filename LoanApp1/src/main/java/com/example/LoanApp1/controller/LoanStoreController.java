package com.example.LoanApp1.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.LoanApp1.entity.Loan;
import com.example.LoanApp1.service.LoanStoreService;

@RestController
@RequestMapping("/loans")
public class LoanStoreController {
    private LoanStoreService loanStoreService;
    
    @Autowired
    public LoanStoreController(LoanStoreService loanStoreService) {
        this.loanStoreService = loanStoreService;
    }
    
    @PostMapping
    public void addLoan(@RequestBody Loan loan) throws Exception {
        loanStoreService.addLoan(loan);
    }
    
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanStoreService.getAllLoans();
    }
    
    @GetMapping("/aggregate/lender")
    public Map<String, Double> aggregateByLender() {
        return loanStoreService.aggregateByLender();
    }
    
    @GetMapping("/aggregate/interest")
    public Map<Double, Double> aggregateByInterest() {
        return loanStoreService.aggregateByInterest();
    }
     //remaining
    @GetMapping("/aggregate/customer")
    public Map<String, Double> aggregateByCustomerId() {
        return loanStoreService.aggregateByCustomerId();
    }
}
