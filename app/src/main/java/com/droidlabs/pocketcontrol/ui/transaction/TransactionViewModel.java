package com.droidlabs.pocketcontrol.ui.transaction;

import android.app.Application;
import android.util.Log;

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
            boolean amountFilterEnabled = filters.getAmountFilterEnabled();

            if (categoryFilterEnabled && dateFilterEnabled && amountFilterEnabled) {
                return repository.filterTransactionsByCategoryAndDateAndAmount(
                        filters.getCategoryId(),
                        filters.getDateLowerBound(),
                        filters.getDateUpperBound(),
                        filters.getAmountLowerBound(),
                        filters.getAmountUpperBound()
                );
            }

            if (categoryFilterEnabled && dateFilterEnabled && !amountFilterEnabled) {
                return repository.filterTransactionsByCategoryAndDate(
                        filters.getCategoryId(),
                        filters.getDateLowerBound(),
                        filters.getDateUpperBound()
                );
            }

            if (categoryFilterEnabled && !dateFilterEnabled && amountFilterEnabled) {
                return repository.filterTransactionsByAmountAndCategory(
                        filters.getCategoryId(),
                        filters.getAmountLowerBound(),
                        filters.getAmountUpperBound()
                );
            }

            if (categoryFilterEnabled && !dateFilterEnabled && !amountFilterEnabled) {
                return repository.filterTransactionsByCategoryId(filters.getCategoryId());
            }

            if (!categoryFilterEnabled && dateFilterEnabled && amountFilterEnabled) {
                return repository.filterTransactionsByAmountAndDate(
                        filters.getDateLowerBound(),
                        filters.getDateUpperBound(),
                        filters.getAmountLowerBound(),
                        filters.getAmountUpperBound()
                );
            }

            if (!categoryFilterEnabled && dateFilterEnabled && !amountFilterEnabled) {
                return repository.filterTransactionsByDate(
                        filters.getDateLowerBound(),
                        filters.getDateUpperBound()
                );
            }

            if (!categoryFilterEnabled && !dateFilterEnabled && amountFilterEnabled) {
                Log.v("HERE", "HERE");
                return repository.filterTransactionsByAmount(
                        filters.getAmountLowerBound(),
                        filters.getAmountUpperBound()
                );
            }

            return repository.getAllTransactions();
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
     * Get all transactions.
     * @param catId category id
     * @return LiveData of transaction list.
     */
    public Float getTotalIAmountByCategoryId(final String catId) {

        Float totalIncome = repository.getTotalIncomeByCategoryId(catId);
        Float totalExpenses = repository.getTotalIExpenseByCategoryId(catId);

        return totalExpenses - totalIncome;
    }

    /**
     * Get single transaction by is.
     * @param id id.
     * @return transaction
     */
    public Transaction getTransactionById(final long id) {
        return repository.getTransactionById(id);
    }

    /**
     * Insert a new transaction in the database.
     * @param transaction transaction to be added.
     * @return id of inserted transaction.
     */
    public long insert(final Transaction transaction) {
        return repository.insert(transaction);
    }

    /**
     * Update transaction recurring fields.
     * @param transactionId id.
     * @param isRecurring flag.
     * @param recurringIntervalType type.
     * @param recurringIntervalDays interval in days.
     */
    public void updateTransactionRecurringFields(
            final int transactionId,
            final Boolean isRecurring,
            final Integer recurringIntervalType,
            final Integer recurringIntervalDays
    ) {
        repository.updateTransactionRecurringFields(
                transactionId,
                isRecurring,
                recurringIntervalType,
                recurringIntervalDays
        );
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
            newFilters.setAmountFilterEnabled(oldFilters.getAmountFilterEnabled());
            newFilters.setAmountLowerBound(oldFilters.getAmountLowerBound());
            newFilters.setAmountUpperBound(oldFilters.getAmountUpperBound());
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
            newFilters.setAmountFilterEnabled(oldFilters.getAmountFilterEnabled());
            newFilters.setAmountLowerBound(oldFilters.getAmountLowerBound());
            newFilters.setAmountUpperBound(oldFilters.getAmountUpperBound());
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

    /**
     * Setter for amount filter.
     * @param filterEnabled whether filter is enabled.
     * @param lowerBound amount lower bound.
     * @param upperBound amount upper bound.
     */
    public void setAmountFilter(final boolean filterEnabled, final float lowerBound, final float upperBound) {
        TransactionFilters newFilters = new TransactionFilters();
        TransactionFilters oldFilters = transactionFilters.getValue();

        if (oldFilters != null) {
            newFilters.setCategoryFilterEnabled(oldFilters.getCategoryFilterEnabled());
            newFilters.setCategoryId(oldFilters.getCategoryId());
            newFilters.setDateFilterEnabled(oldFilters.getDateFilterEnabled());
            newFilters.setDateLowerBound(oldFilters.getDateLowerBound());
            newFilters.setDateUpperBound(oldFilters.getDateUpperBound());
        }

        if (filterEnabled) {
            newFilters.setAmountFilterEnabled(true);
            newFilters.setAmountLowerBound(lowerBound);
            newFilters.setAmountUpperBound(upperBound);
        } else {
            newFilters.setAmountFilterEnabled(false);
            newFilters.setAmountLowerBound(Float.MIN_VALUE);
            newFilters.setAmountUpperBound(Float.MAX_VALUE);
        }

        if (Objects.equals(oldFilters, newFilters)) {
            return;
        }

        transactionFilters.setValue(newFilters);
    }

    class TransactionFilters {
        private Boolean categoryFilterEnabled;
        private Boolean dateFilterEnabled;
        private Boolean amountFilterEnabled;

        private String categoryId;
        private Long dateLowerBound;
        private Long dateUpperBound;
        private Float amountLowerBound;
        private Float amountUpperBound;

        /**
         * Class constructor.
         */
        TransactionFilters() {
            categoryFilterEnabled = false;
            dateFilterEnabled = false;
            amountFilterEnabled = false;

            categoryId = "";
            dateLowerBound = Long.MIN_VALUE;
            dateUpperBound = Long.MAX_VALUE;
            amountLowerBound = Float.MIN_VALUE;
            amountUpperBound = Float.MAX_VALUE;
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
         * Setter for amount filter enabled.
         * @param filterEnabled whether filter is enabled.
         */
        private void setAmountFilterEnabled(final Boolean filterEnabled) {
            this.amountFilterEnabled = filterEnabled;
        }

        /**
         * Setter for lower bound amount filter.
         * @param lowerBound filter amount lower bound.
         */
        private void setAmountLowerBound(final Float lowerBound) {
            this.amountLowerBound = lowerBound;
        }

        /**
         * Setter for upper bound amount filter.
         * @param upperBound filter amount upper bound.
         */
        private void setAmountUpperBound(final Float upperBound) {
            this.amountUpperBound = upperBound;
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

        /**
         * Getter for amount filter enabled.
         * @return whether amount filter is enabled.
         */
        private Boolean getAmountFilterEnabled() {
            return amountFilterEnabled;
        }

        /**
         * Getter for lower bound amount filter.
         * @return amount lower bound.
         */
        private Float getAmountLowerBound() {
            return amountLowerBound;
        }

        /**
         * Getter for upper bound amount filter.
         * @return amount upper bound.
         */
        private Float getAmountUpperBound() {
            return amountUpperBound;
        }
    }
}
