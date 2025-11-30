package test.fakes;

import application.logic.Transaction;
import application.repository.TransactionRepository;

import java.util.*;

public class FakeTransactionRepository implements TransactionRepository {
    private final Map<Long, Transaction> store = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public List<Transaction> loadTransactions() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Transaction> findById(long transactionId) {
        return Optional.ofNullable(store.get(transactionId));
    }

    @Override
    public long createNewTransactionId() {
        return nextId++;
    }

    @Override
    public void addTransaction(Transaction t) {
        store.put(t.getTransactionId(), t);
    }

    @Override
    public void editTransactionById(Transaction t) {
        store.put(t.getTransactionId(), t);
    }

    @Override
    public void deleteTransactionById(long transactionId) {
        store.remove(transactionId);
    }
}
