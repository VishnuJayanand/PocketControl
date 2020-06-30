package com.droidlabs.pocketcontrol.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public final class NetworkUtils {

    /**
     * Private constructor.
     */
    private NetworkUtils() { }

    /**
     * Get connectivity manager.
     * @param context context.
     * @return connectivity manager.
     */
    public static ConnectivityManager getConnectivityManager(final Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Get network.
     * @param context context.
     * @return network.
     */
    public static Network getNetwork(final Context context) {
        return getConnectivityManager(context).getActiveNetwork();
    }

    /**
     * Get network capabilities.
     * @param context context.
     * @return network capabilities.
     */
    public static NetworkCapabilities getNetworkCapabilities(final Context context) {
        return getConnectivityManager(context).getNetworkCapabilities(getNetwork(context));
    }

    /**
     * Check network connection.
     * @param context context.
     * @return connection available.
     */
    public static boolean isNetworkConnectionAvailable(final Context context) {
        Network activeNetwork = getNetwork(context);

        if (activeNetwork == null) {
            return false;
        }

        NetworkCapabilities networkCapabilities = getNetworkCapabilities(context);

        if (networkCapabilities == null) {
            return false;
        }

        return (
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        );
    }
}
