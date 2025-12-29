# ConnectivityHack

ConnectivityHack is an LSPosed/Xposed module that spoofs Android’s reported network transport state.

The module intercepts calls to `NetworkCapabilities.hasTransport(...)` and forces the system to report:

* **Wi-Fi: OFF / unavailable**
* **Mobile data: ON / available**

This is useful for apps that change behavior depending on whether the device is on Wi-Fi or cellular data.

---

## What it does

After enabling this module:

* Apps will **not** detect Wi-Fi transport
* Apps will **always** think mobile data is available
* Actual connectivity is unchanged — only the reported transport flag is spoofed

This only affects API checks such as:

```kotlin
hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
```

It does **not**:

* turn Wi-Fi or mobile data on or off
* provide internet access
* modify APNs or carrier configuration

It only changes what apps are **told** about the connection type.

---

## How it works (short explanation)

The module:

1. Hooks into Zygote via LSPosed/Xposed
2. Hooks `android.net.NetworkCapabilities.hasTransport(int)`
3. Forces the following results:

   * `TRANSPORT_WIFI` → **false**
   * `TRANSPORT_CELLULAR` → **true**

So apps believe mobile data is active even if:

* Airplane mode is enabled
* There is no SIM inserted
* Wi-Fi is actually connected

---

## Requirements

* Rooted device
* LSPosed / Xposed / EdXposed environment
* Android 8+ (tested on newer versions too)

---

## Installation

1. Build the module (or use a prebuilt release if available)
2. Install the APK
3. Open LSPosed / EdXposed Manager
4. Enable the module for the target apps (or system-wide)
5. Reboot or soft-reboot Zygote

---

## Notes and limitations

* Some apps use additional network detection methods and may ignore this
* Bandwidth/captive portal tests will still see real connectivity
* VPN transports are not touched by default
* Use at your own risk; this is for educational/testing purposes

---

## License

This project is provided without warranty. Use responsibly for testing and research.
