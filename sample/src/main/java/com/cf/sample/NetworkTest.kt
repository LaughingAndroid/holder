package com.cf.sample

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build

object NetworkTest {
    val TAG = "NetworkTest"

    fun registerNetworkCallback(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val m: ConnectivityManager? = context.getSystemService(ConnectivityManager::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                m?.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network?) {
                        super.onAvailable(network)
                        Logs.d("onAvailable" + network?.toString())
                    }

                    override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
                        super.onCapabilitiesChanged(network, networkCapabilities)
                        Logs.d("onCapabilitiesChanged" + network?.toString())
                        Logs.d("onCapabilitiesChanged networkCapabilities:$networkCapabilities " + network?.toString())
                    }

                    override fun onLinkPropertiesChanged(network: Network?, linkProperties: LinkProperties?) {
                        super.onLinkPropertiesChanged(network, linkProperties)
                        Logs.d("onLinkPropertiesChanged " + network?.toString())

                    }

                    override fun onLosing(network: Network?, maxMsToLive: Int) {
                        super.onLosing(network, maxMsToLive)
                        Logs.d("onLosing " + network?.toString())

                    }

                    override fun onLost(network: Network?) {
                        super.onLost(network)
                        Logs.d("onLost " + network?.toString())

                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        Logs.d("onUnavailable")
                    }
                })
            }
        }
    }
}
