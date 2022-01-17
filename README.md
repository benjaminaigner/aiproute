# AIProute

Welcome to the Android IP route app!

## Remarks

* No maintenance intended!
* Rooting your Android is required!

## Introduction

This app allows you a modification of the IP routing table for your Android device.

The base of this app is the commandline tool _ip route_, which is capable of adding/removing route entries.
This app allows you to set following parameters of the command:

* Netmask
* Adress
* Gateway
* Metric

In addition, it is possible the give every route a name, to find it easier in the overview.
Each route has two additional parameters:

* **Persistent route:** A persistent route will be applied a soon as the app is started. This maybe directly at startup or if you launch the app manually. If the SSID activation is used, the route is only applied if the device is connected to the desired network.
* **Active:** This route is active now. Depending on the SSID setting the route will be applied immediately or afterwards (if the desired SSID is available)


## SSID based activation

This app follows two major philosophies of activating the routes:

Either the activation will be SSID based, which means a route gets active if the Wifi interface is connected to one of the selected SSIDs.
Or the activation is general, in this case all active routes are applied as soon as the Wifi interface is connected

You can change the activation type via  _Settings->General->Use SSID_.

## Other settings 

It is possible to change other settings for this app:

* Autostart
* Wifi devicename

The autostart setting determines, if this app launches itself (in detail: it installs a Wifi broadcast event) to receive information on the Wifi connectivity.
This is necessary if you want to have your routes applied automatically. Combined with the SSID activation, it is possible to apply all routes automatically, if you are connected to e.g. your home Wifi.


Due to the implementation of the _ip route_ it is necessary to know the device name, which the routes should be applied to. In most cases, Android devices are using *wlan0* as default Wifi interface name. This is the default value, which works on all tested phones.
If you have another device name or you want to use your routes on the GSM interface (I won't recommend that, you may break your internet connectivity), you have to replace the wifi device name with the intended device's name.


## Reason to have this app


I've programmed this app for my personal use.
The reason is a small but major missing feature of my cable provider's router:

It is not possible to connect from my home network to the public IP adress of my own router.
On every common notebook/PC it is possible to modify the route settings via the network connection manager (also depending on the SSID).
This feature was not available on my Android phone, so it was impossible to synchronize my contacts and the calendar.

I use this app to set following route avoiding the previous mentioned problem:

* Adress: <my public ip>
* All other options: empty
* SSID: <my home SSID>
* Autostart active, Use SSID option

So each time my phone connected to my home network, the route is applied. This route avoids the connection to be routed over the cable modem -> everything works out fine.


Nowadays, I changed the router's settings to "Bridge" mode. This allows my firewall to take care of all incoming connections, avoiding the problem.
