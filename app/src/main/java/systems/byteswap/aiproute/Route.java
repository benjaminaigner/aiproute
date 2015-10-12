package systems.byteswap.aiproute;

import android.content.Context;

/**
 * Base class to represent a route.
 * It contains all necessary/available fields
 * for using it with the "ip route" command
 *
 * It provides basic methods to parse the route to a Shell command, which is applicable
 * via the ShellCommand class.

 Copyright (C) 2015  Benjamin Aigner

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Route {
    private String address;
    private String netmask;
    private String gateway;
    private String name;
    private String iface;
    private int id;
    private int metric;
    private boolean active;
    private boolean persistent;

    public String toRouteStringDelete(Context context) {
        String res = "ip route del ";
        String iface = SettingsActivity.getInterfaceName(context);

        //assemble base address + netmask
        res += this.address;

        //if set, use netmask
        if(!this.netmask.isEmpty()) {
            res += "/" + this.netmask;
        }

        //if set, add a gateway too...
        if(!this.gateway.isEmpty()) {
            res += " via " + this.gateway;
        }

        //if metric set, add this too
        if(this.metric > 0) {
            res += " metric " + this.metric;
        }

        res += " dev " + iface;
        return res;
    }

    public String toRouteStringAdd(Context context) {
        String res = "ip route add ";
        String iface = SettingsActivity.getInterfaceName(context);

        //assemble base address + netmask
        res += this.address;

        //if set, use netmask
        if(!this.netmask.isEmpty()) {
            res += "/" + this.netmask;
        }


        //if set, add a gateway too...
        if(!this.gateway.isEmpty()) {
            res += " via " + this.gateway;
        }

        //if metric set, add this too
        if(this.metric > 0) {
            res += " metric " + this.metric;
        }

        res += " dev " + iface;
        return res;
    }

    public static String listDefaultRoutes() {
        return ShellAccess.execSuCommand("ip route list");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMetric() {
        return metric;
    }

    public void setMetric(int metric) {
        this.metric = metric;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIface() {
        return iface;
    }

    public void setIface(String iface) {
        this.iface = iface;
    }
}
