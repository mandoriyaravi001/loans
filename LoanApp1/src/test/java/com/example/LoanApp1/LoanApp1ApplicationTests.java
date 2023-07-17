package com.example.LoanApp1;

import java.lang.System.Logger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;


import com.example.LoanApp1.entity.Loan;
import com.example.LoanApp1.service.LoanStoreService;

@SpringBootTest
class LoanApp1ApplicationTests {

	@Mock
    private Logger logger;
    
    private LoanStoreService loanStoreService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        loanStoreService = new LoanStoreService();
    }

    @Test
    void testAddLoan() throws Exception {
        Loan loan = new Loan("L1", "C1", "LEN1", 10000, 10000, LocalDate.of(2023, 5, 6),
                1, LocalDate.of(2023, 5, 7), 0.01);

        loanStoreService.addLoan(loan);

        Assertions.assertEquals(1, loanStoreService.getAllLoans().size());
        Assertions.assertEquals(loan, loanStoreService.getAllLoans().get(0));
    }

    @Test
    void testAddLoanWithInvalidPaymentDate() {
        Loan loan = new Loan("L1", "C1", "LEN1", 10000, 10000, LocalDate.of(2023, 5, 8),
                1, LocalDate.of(2023, 5, 7), 0.01);

        Assertions.assertThrows(Exception.class, () -> loanStoreService.addLoan(loan));
    }

    @Test
    void testAggregateByLender() throws Exception {
        Loan loan1 = new Loan("L1", "C1", "LEN1", 10000, 10000, LocalDate.of(2023, 5, 6),
                1, LocalDate.of(2023, 5, 7), 0.01);
        Loan loan2 = new Loan("L2", "C1", "LEN1", 20000, 5000, LocalDate.of(2023, 1, 6),
                1, LocalDate.of(2023, 5, 8), 0.01);

        loanStoreService.addLoan(loan1);
        loanStoreService.addLoan(loan2);

        Map<String, Double> lenderAggregation = loanStoreService.aggregateByLender();

        Map<String, Double> expectedAggregation = new HashMap<>();
        expectedAggregation.put("LEN1", 15000.0);

        Assertions.assertEquals(expectedAggregation, lenderAggregation);
    }

    @Test
    void testAggregateByInterest() throws Exception {
        Loan loan1 = new Loan("L1", "C1", "LEN1", 10000, 10000, LocalDate.of(2023, 5, 6),
                1, LocalDate.of(2023, 5, 7), 0.01);
        Loan loan2 = new Loan("L2", "C1", "LEN1", 20000, 5000, LocalDate.of(2023, 1, 6),
                1, LocalDate.of(2023, 5, 8), 0.02);

        loanStoreService.addLoan(loan1);
        loanStoreService.addLoan(loan2);

        Map<Double, Double> interestAggregation = loanStoreService.aggregateByInterest();

        Map<Double, Double> expectedAggregation = new HashMap<>();
        expectedAggregation.put(0.01, 10000.0);
        expectedAggregation.put(0.02, 20000.0);

        Assertions.assertEquals(expectedAggregation, interestAggregation);
    }

    @Test
    void testAggregateByCustomerId() throws Exception {
        Loan loan1 = new Loan("L1", "C1", "LEN1", 10000, 10000, LocalDate.of(2023, 5, 6),
                1, LocalDate.of(2023, 5, 7), 0.01);
        Loan loan2 = new Loan("L2", "C2", "LEN1", 20000, 5000, LocalDate.of(2023, 1, 6),
                1, LocalDate.of(2023, 5, 8), 0.02);

        loanStoreService.addLoan(loan1);
        loanStoreService.addLoan(loan2);

        Map<String, Double> customerIdAggregation = loanStoreService.aggregateByCustomerId();

        Map<String, Double> expectedAggregation = new HashMap<>();
        expectedAggregation.put("C1", 10000.0);
        expectedAggregation.put("C2", 20000.0);

        Assertions.assertEquals(expectedAggregation, customerIdAggregation);
    }
}
