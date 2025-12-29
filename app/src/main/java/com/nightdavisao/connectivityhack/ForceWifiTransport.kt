package com.nightdavisao.connectivityhack

import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

class ForceWifiTransport : IXposedHookZygoteInit {

    companion object {
        private const val NC_CLASS = "android.net.NetworkCapabilities"
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        try {
            val ncClass = XposedHelpers.findClass(NC_CLASS, null)

            // get constants
            val transportWifi =
                XposedHelpers.getStaticIntField(ncClass, "TRANSPORT_WIFI") as Int
            val transportCell =
                XposedHelpers.getStaticIntField(ncClass, "TRANSPORT_CELLULAR") as Int

            XposedHelpers.findAndHookMethod(
                NC_CLASS,
                null,
                "hasTransport",
                Int::class.javaPrimitiveType,
                object : XC_MethodHook() {

                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val transport = param.args[0] as Int

                        when (transport) {

                            // force WIFI -> false
                            transportWifi -> {
                                param.result = false
                                XposedBridge.log("ForceWifiTransport: WIFI forced FALSE")
                            }

                            // force CELLULAR -> true
                            transportCell -> {
                                param.result = true
                                XposedBridge.log("ForceWifiTransport: CELLULAR forced TRUE")
                            }
                        }
                    }
                }
            )

            XposedBridge.log("ForceWifiTransport: hooks installed")
        } catch (t: Throwable) {
            XposedBridge.log("ForceWifiTransport: failed: $t")
        }
    }
}
