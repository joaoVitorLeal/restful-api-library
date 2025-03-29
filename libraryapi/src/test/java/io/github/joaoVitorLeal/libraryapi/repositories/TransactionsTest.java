package io.github.joaoVitorLeal.libraryapi.repositories;

import io.github.joaoVitorLeal.libraryapi.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests transaction behavior demonstration methods
 * @see TransactionService
 */

@SpringBootTest
public class TransactionsTest {

    @Autowired
    TransactionService transactionService;

    /**
     * Tests atomic transaction with potential rollback
     */
    @Test
    void simpleTransaction() {
        transactionService.execute();
    }

    /**
     * Demonstrates JPA's automatic dirty checking
     */
    @Test
    void transacaoEstadoManeged() {
        transactionService.updateWithoutUpdating();
    }
}
