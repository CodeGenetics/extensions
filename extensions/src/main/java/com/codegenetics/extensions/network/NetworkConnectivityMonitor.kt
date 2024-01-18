package com.codegenetics.extensions.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*

class NetworkConnectivityMonitor(context: Context) {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback = NetworkConnectivityCallback()
    private var networkStatusCallback: NetworkStatusCallback? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    interface NetworkStatusCallback {
        fun onNetworkAvailable()
        fun onNetworkUnavailable()
    }

    fun startMonitoring(callback: NetworkStatusCallback? = null) {
        networkStatusCallback = callback
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun stopMonitoring() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
        networkStatusCallback = null
        coroutineScope.cancel()
    }

    private inner class NetworkConnectivityCallback : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            checkInternetReachability { isReachable ->
                if (isReachable) {
                    networkStatusCallback?.onNetworkAvailable()
                } else {
                    networkStatusCallback?.onNetworkUnavailable()
                }
            }
        }

        override fun onLost(network: Network) {
            networkStatusCallback?.onNetworkUnavailable()
        }
    }

    private fun checkInternetReachability(onResult: (Boolean) -> Unit) {
        coroutineScope.launch {
            val isReachable = withContext(Dispatchers.IO) { isInternetReachable() }
            onResult(isReachable)
        }
    }

    private fun isInternetReachable(): Boolean {
        return try {
            val url = URL("http://clients3.google.com/generate_204")
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("User-Agent", "Android")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.connectTimeout = 1500
            urlConnection.connect()
            urlConnection.responseCode == 204 && urlConnection.contentLength == 0
        } catch (e: IOException) {
            false
        }
    }
}

/** ```````````````USAGE`````````````````````
 * class MainActivity : AppCompatActivity(), NetworkConnectivityMonitor.NetworkStatusCallback {
 *
 *     private lateinit var networkMonitor: NetworkConnectivityMonitor
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         setContentView(R.layout.activity_main)
 *
 *         networkMonitor = NetworkConnectivityMonitor(this)
 *         networkMonitor.startMonitoring(this)
 *     }
 *
 *     override fun onNetworkAvailable() {
 *         // Handle network available
 *     }
 *
 *     override fun onNetworkUnavailable() {
 *         // Handle network unavailable
 *     }
 *
 *     override fun onDestroy() {
 *         networkMonitor.stopMonitoring()
 *         super.onDestroy()
 *     }
 * }
 *
 * */