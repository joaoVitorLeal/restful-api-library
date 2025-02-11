package io.github.joaoVitorLeal.libraryapi.repositories;

import io.github.joaoVitorLeal.libraryapi.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @see TransactionService
 */

@SpringBootTest
public class TransactionsTest {

    @Autowired
    TransactionService transactionService;

    /**
     * Commit \\
     * Rollback
     * @Transactional - Abre uma transação e caso uma das operações falhe ele aciona o 'Rollback'.
     */
    @Test
    void simpleTransaction() {
        transactionService.execute();
    }

    @Test
    void transacaoEstadoManeged() {
        transactionService.atualizacaoSemAtualizar();
    }
}
