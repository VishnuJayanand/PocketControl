package com.droidlabs.pocketcontrol.db.paymentmode;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.droidlabs.pocketcontrol.db.PocketControlDB;

import java.util.List;

public class PaymentModeRepository {

    private PaymentModeDao paymentModeDao;
    private LiveData<List<PaymentMode>> allPaymentModes;

    /**
     * Payment mode repository constructor.
     * @param application application to be used.
     */
    PaymentModeRepository(final Application application) {
        PocketControlDB db = PocketControlDB.getDatabase(application);
        paymentModeDao = db.paymentModeDao();
        allPaymentModes = paymentModeDao.getAllPaymentModes();
    }

    /**
     * Retrieve all payment modes.
     * @return all payment modes.
     */
    public LiveData<List<PaymentMode>> getAllPaymentModes() {
        return allPaymentModes;
    }

    /**
     * Add a new payment mode.
     * @param paymentMode payment mode to be saved.
     */
    public void insert(final PaymentMode paymentMode) {
        PocketControlDB.DATABASE_WRITE_EXECUTOR.execute(() -> {
            paymentModeDao.insert(paymentMode);
        });
    }
}
