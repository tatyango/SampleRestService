import core.Transaction;
import exception.IdAlreadyExistsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Created by Tetiana on 7/22/2016.
 */

public class Model {

    private Map<Long, Transaction> transactions = new HashMap<>();

    /* predefined types */
    private static final String CARS = "cars";
    private static final String SHOPPING = "shopping";

    /**
     * store transaction into model
     *
     * @param id       The transaction id
     * @param type     The transaction type
     * @param amount   The amount
     * @param parentId The id of parent transaction
     */
    public void createRecord(Long id, String type, Double amount, Long parentId) {
        if (transactions.get(id) != null) {
            throw new IdAlreadyExistsException("core.Transaction with id: " + id + " already exists");
        } else {
            Transaction transaction = new Transaction();
            transaction.setId(id);
            transaction.setType(type);
            transaction.setParentId(parentId);
            transaction.setAmount(amount);
            transactions.put(id, transaction);
            if (parentId != null) {
                Transaction parent = getTransaction(transaction.getParentId());
                if (parent != null) {
                    parent.getChildIds().add(id);
                } else {
                    transactions.remove(id);
                    throw new IdAlreadyExistsException("Parent with id: " + parentId + " does not exist");
                }
            }
        }
    }

    /**
     * Gets all transactions data
     *
     * @return list of all transactions ids
     */
    public List getAllTransactions() {
        List result = (List) transactions.keySet().stream().sorted()
                .map((id) -> transactions.get(id))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * Gets all ids of transactions for a specific type
     *
     * @param type The transaction type
     * @return list of transactions ids
     */

    public List<Long> getTransactionsIdsByType(String type) {
        List<Long> ids = transactions.values().stream()
                .filter(trans -> type.equals(trans.getType()))
                .map(Transaction::getId)
                .collect(Collectors.toList());
        return ids;
    }

    /**
     * returns sum of current transaction amount and all transitive child-transactions by id of parent transactions
     *
     * @param id The transaction id
     * @return list of transactions ids
     */

    public Double getSumById(Long id) {
        Transaction root = transactions.get(id);
        Double result = null;
        if (root != null) {
            Set<Long> firstChilds = root.getChildIds();
            result = root.getAmount();
            if (root.getChildIds().size() != 0) {
                for (Long childId : firstChilds) {
                    result = result + getSumById(childId);
                }
            }
        }
        return result;
    }

    public Transaction getTransaction(Long id) {
        return transactions.get(id);
    }

}