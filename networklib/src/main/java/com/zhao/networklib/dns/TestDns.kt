package com.zhao.networklib.dns

import okhttp3.Dns
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*

class TestDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        val inetAddresses: MutableList<InetAddress> = ArrayList()
        var hostNameInetAddresses: List<InetAddress>? = null
        try {
            hostNameInetAddresses = Dns.SYSTEM.lookup(hostname)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
        if (hostNameInetAddresses != null && hostNameInetAddresses.isNotEmpty()) {
            inetAddresses.addAll(hostNameInetAddresses)
        }
        try {
            inetAddresses.add(InetAddress.getByName("111.231.97.251"))
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
        return inetAddresses
    }
}