package com.droidlabs.pocketcontrol.db.paymentmode;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PaymentModeDao {
    /**
     * Insert new payment mode into the database.
     * @param paymentMode payment mode to be saved.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(PaymentMode paymentMode);

    /**
     * Delete all payment modes from the database.
     */
    @Query("DELETE FROM payment_modes")
    void deleteAll();

    /**
     * Retrieve all payment modes from the database.
     * @return all payment modes.
     */
    @Query("SELECT * FROM payment_modes")
    List<PaymentMode> getAllPaymentModes();
}
