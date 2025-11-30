package application.repository;

import application.logic.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    List<Transaction> loadTransactions();

    Optional<Transaction> findById(long transactionId);

    long createNewTransactionId();

    void addTransaction(Transaction t);

    void editTransactionById(Transaction t);

    void deleteTransactionById(long transactionId);
}
