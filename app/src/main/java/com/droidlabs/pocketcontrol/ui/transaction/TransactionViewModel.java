package com.droidlabs.pocketcontrol.ui.transaction;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.droidlabs.pocketcontrol.db.transaction.Transaction;
import com.droidlabs.pocketcontrol.db.transaction.TransactionRepository;

import java.util.List;
import java.util.Objects;

public class TransactionViewModel extends AndroidViewModel {

    private LiveData<List<Transaction>> transactions;
    private TransactionRepository repository;

    private final MutableLiveData<TransactionFilters> transactionFilters;

    /**
     * View model constructor.
     * @param application application to be used.
     */
    public TransactionViewModel(final Application application) {
        super(application);
        this.repository = new TransactionRepository(application);

        transactionFilters = new MutableLiveData<>();

        transactions = Transformations.switchMap(transactionFilters, filters -> {
            boolean categoryFilterEnabled = filters.getCategoryFilterEnabled();
            boolean dateFilterEnabled = filters.getDateFilterEnabled();

            if (categoryFilterEnabled) {
                if (dateFilterEnabled) {
                    return repository.filterTransactionsByCategoryAndDate(
                            filters.categoryId,
                            filters.dateLowerBound,
                            filters.dateUpperBound
                    );
                }
                return repository.filterTransactionsByCategoryId(filters.categoryId);
            } else {
                if (dateFilterEnabled) {
                    return repository.filterTransactionsByDate(
                            filters.dateLowerBound,
                            filters.dateUpperBound
                    );
                }
                return repository.getAllTransactions();
            }
        });
    }

    /**
     * Get all transactions.
     * @return LiveData of transaction list.
     */
    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }

    /**
     * Insert a new transaction in the database.
     * @param transaction transaction to be added.
     */
    public void insert(final Transaction transaction) {
        repository.insert(transaction);
    }

    /**
     * Setter for category filter.
     * @param filterEnabled whether filter is enabled.
     * @param categoryId category ID to filter.
     */
    public void setCategoryFilter(final boolean filterEnabled, final String categoryId) {
        TransactionFilters newFilters = new TransactionFilters();
        TransactionFilters oldFilters = transactionFilters.getValue();

        if (oldFilters != null) {
            newFilters.setDateFilterEnabled(oldFilters.getDateFilterEnabled());
            newFilters.setDateLowerBound(oldFilters.getDateLowerBound());
            newFilters.setDateUpperBound(oldFilters.getDateUpperBound());
        }

        if (filterEnabled) {
            newFilters.setCategoryFilterEnabled(true);
            newFilters.setCategoryId(categoryId);
        } else {
            newFilters.setCategoryFilterEnabled(false);
            newFilters.setCategoryId("");
        }

        if (Objects.equals(oldFilters, newFilters)) {
            return;
        }

        transactionFilters.setValue(newFilters);
    }

    /**
     * Setter for date filter.
     * @param filterEnabled whether filter is enabled.
     * @param lowerBound date lower bound.
     * @param upperBound date upper bound.
     */
    public void setDateFilter(final boolean filterEnabled, final long lowerBound, final long upperBound) {
        TransactionFilters newFilters = new TransactionFilters();
        TransactionFilters oldFilters = transactionFilters.getValue();

        if (oldFilters != null) {
            newFilters.setCategoryFilterEnabled(oldFilters.getCategoryFilterEnabled());
            newFilters.setCategoryId(oldFilters.getCategoryId());
        }

        if (filterEnabled) {
            newFilters.setDateFilterEnabled(true);
            newFilters.setDateLowerBound(lowerBound);
            newFilters.setDateUpperBound(upperBound);
        } else {
            newFilters.setDateFilterEnabled(false);
            newFilters.setDateLowerBound(Long.MIN_VALUE);
            newFilters.setDateUpperBound(Long.MAX_VALUE);
        }

        if (Objects.equals(oldFilters, newFilters)) {
            return;
        }

        transactionFilters.setValue(newFilters);
    }

    class TransactionFilters {
        private Boolean categoryFilterEnabled;
        private Boolean dateFilterEnabled;

        private String categoryId;
        private Long dateLowerBound;
        private Long dateUpperBound;

        /**
         * Class constructor.
         */
        TransactionFilters() {
            categoryFilterEnabled = false;
            dateFilterEnabled = false;
            categoryId = "";
            dateLowerBound = Long.MIN_VALUE;
            dateUpperBound = Long.MAX_VALUE;
        }

        /**
         * Setter for category filter enabled.
         * @param filterEnabled whether category filter is enabled.
         */
        private void setCategoryFilterEnabled(final Boolean filterEnabled) {
            this.categoryFilterEnabled = filterEnabled;
        }

        /**
         * Setter for category id filter.
         * @param id category id.
         */
        private void setCategoryId(final String id) {
            this.categoryId = id;
        }

        /**
         * Setter for date filter enabled.
         * @param filterEnabled whether filter is enabled.
         */
        private void setDateFilterEnabled(final Boolean filterEnabled) {
            this.dateFilterEnabled = filterEnabled;
        }

        /**
         * Setter for lower bound date filter.
         * @param lowerBound filter date lower bound.
         */
        private void setDateLowerBound(final Long lowerBound) {
            this.dateLowerBound = lowerBound;
        }

        /**
         * Setter for upper bound date filter.
         * @param upperBound filter date upper bound.
         */
        private void setDateUpperBound(final Long upperBound) {
            this.dateUpperBound = upperBound;
        }

        /**
         * Getter for category filter enabled.
         * @return whether category filter is enabled.
         */
        private Boolean getCategoryFilterEnabled() {
            return categoryFilterEnabled;
        }

        /**
         * Getter for category id filter.
         * @return category id.
         */
        private String getCategoryId() {
            return categoryId;
        }

        /**
         * Getter for date filter enabled.
         * @return whether date filter is enabled.
         */
        private Boolean getDateFilterEnabled() {
            return dateFilterEnabled;
        }

        /**
         * Getter for lower bound date filter.
         * @return date lower bound.
         */
        private Long getDateLowerBound() {
            return dateLowerBound;
        }

        /**
         * Getter for upper bound date filter.
         * @return date upper bound.
         */
        private Long getDateUpperBound() {
            return dateUpperBound;
        }
    }
}
