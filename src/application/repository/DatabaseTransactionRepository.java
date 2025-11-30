package application.repository;

import application.database.Database;
import application.logic.Transaction;

import java.util.List;
import java.util.Optional;

public class DatabaseTransactionRepository implements TransactionRepository {
    @Override
    public List<Transaction> loadTransactions() {
        return Database.loadTransactions();
    }

    @Override
    public Optional<Transaction> findById(long transactionId) {
        return Database.loadTransactions().stream().filter(t -> t.getTransactionId() == transactionId).findFirst();
    }

    @Override
    public long createNewTransactionId() {
        return Database.createNewTransactionId();
    }

    @Override
    public void addTransaction(Transaction t) {
        Database.addTransaction(t);
    }

    @Override
    public void editTransactionById(Transaction t) {
        Database.editTransactionById(t);
    }

    @Override
    public void deleteTransactionById(long transactionId) {
        Database.deleteTransactionById(transactionId);
    }
}
