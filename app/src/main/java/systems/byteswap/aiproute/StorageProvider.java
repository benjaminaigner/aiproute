package systems.byteswap.aiproute;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This file provides storage access to insert/delete/update any route associated
 * data.
 * There are 2 tables:
 * AIPRoute_Routes
 *
 * contains all routes (also attached via CursorAdapter to the main window) and all
 * necessary data.
 *
 * AIPRoute_SSIDs
 * In order to provide the possibility to attach one route to a specific SSID, the second table is
 * used. There is a 1 to n relation between routes and ssids
 *

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
public class StorageProvider extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AIPRoute";
    private static final String ROUTE_TABLE_NAME = "AIPRoute_Routes";
    private static final String ROUTE2_TABLE_NAME = "AIPRoute_SSIDs";
    private static final int DATABASE_VERSION = 2;

    //database for all available routes
    public static final String KEY_ROWID = "_id";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_NETMASK = "netmask";
    public static final String KEY_GATEWAY = "gateway";
    public static final String KEY_NAME= "name";
    public static final String KEY_METRIC= "metric";
    public static final String KEY_ACTIVE= "active";
    public static final String KEY_PERSISTENT= "persistent";
    public static final String KEY_INTERFACE= "interface";

    //database for SSIDs (corresponding to one route)
    public static final String KEY2_ROWID = "_id";
    public static final String KEY2_ROUTEID = "routeid";
    public static final String KEY2_SSID = "ssid";

    private static final String ROUTE_TABLE_CREATE =
            "CREATE TABLE " + ROUTE_TABLE_NAME + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY ASC," +
                    KEY_ADDRESS + " TEXT," +
                    KEY_NETMASK + " TEXT," +
                    KEY_GATEWAY + " TEXT," +
                    KEY_NAME + " TEXT," +
                    KEY_METRIC + " INTEGER," +
                    KEY_ACTIVE + " TEXT," +
                    KEY_PERSISTENT + " TEXT," +
                    KEY_INTERFACE + " TEXT" +
                    ");";

    private static final String ROUTE2_TABLE_CREATE =
            "CREATE TABLE " + ROUTE2_TABLE_NAME + " (" +
                    KEY2_ROWID + " INTEGER PRIMARY KEY ASC," +
                    KEY2_ROUTEID + " TEXT," +
                    KEY2_SSID + " TEXT" +
                    ");";


    StorageProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public Route getRoute(long id) {
        Route route = new Route();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ROUTE_TABLE_NAME, new String[]{KEY_ROWID,
                        KEY_ADDRESS, KEY_NETMASK, KEY_GATEWAY, KEY_NAME, KEY_METRIC, KEY_ACTIVE, KEY_PERSISTENT, KEY_INTERFACE},
                KEY_ROWID + "='" + id + "'", null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            route.setId(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
            route.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            route.setNetmask(cursor.getString(cursor.getColumnIndex(KEY_NETMASK)));
            route.setGateway(cursor.getString(cursor.getColumnIndex(KEY_GATEWAY)));
            route.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            route.setIface(cursor.getString(cursor.getColumnIndex(KEY_INTERFACE)));
            route.setMetric(cursor.getInt(cursor.getColumnIndex(KEY_METRIC)));
            String temp = cursor.getString(cursor.getColumnIndex(KEY_ACTIVE));
            if(temp.equals("1")) {
                route.setActive(true);
            } else {
                route.setActive(false);
            }
            temp = cursor.getString(cursor.getColumnIndex(KEY_PERSISTENT));
            if(temp.equals("1")) {
                route.setPersistent(true);
            } else {
                route.setPersistent(false);
            }

            cursor.close();
        }

        return route;
    }

    public Route[] getAllRoutes() {
        Route route[] = new Route[0];
        Route oneRoute;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.query(ROUTE_TABLE_NAME,null,"",null,null,null,null);
        boolean iterate = true;
        int index = 0;

        if (result != null && result.getCount() > 0) {
            result.moveToFirst();

            while (iterate) {
                oneRoute = new Route();

                oneRoute.setId(result.getInt(result.getColumnIndex(KEY_ROWID)));
                oneRoute.setAddress(result.getString(result.getColumnIndex(KEY_ADDRESS)));
                oneRoute.setNetmask(result.getString(result.getColumnIndex(KEY_NETMASK)));
                oneRoute.setGateway(result.getString(result.getColumnIndex(KEY_GATEWAY)));
                oneRoute.setName(result.getString(result.getColumnIndex(KEY_NAME)));
                oneRoute.setMetric(result.getInt(result.getColumnIndex(KEY_METRIC)));
                oneRoute.setIface(result.getString(result.getColumnIndex(KEY_INTERFACE)));

                String temp = result.getString(result.getColumnIndex(KEY_ACTIVE));
                if(temp.equals("1")) {
                    oneRoute.setActive(true);
                } else {
                    oneRoute.setActive(false);
                }

                temp = result.getString(result.getColumnIndex(KEY_PERSISTENT));
                if(temp.equals("1")) {
                    oneRoute.setPersistent(true);
                } else {
                    oneRoute.setPersistent(false);
                }

                route[index] = oneRoute;
                index++;

                if (result.isLast()) {
                    iterate = false;
                } else {
                    result.moveToNext();
                }
            }
        }
        if(result != null) {
            result.close();
        }
        return route;
    }

    public int updateRoute(long id, Route newRoute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(KEY_ADDRESS,newRoute.getAddress());
        content.put(KEY_NETMASK,newRoute.getNetmask());
        content.put(KEY_GATEWAY,newRoute.getGateway());
        content.put(KEY_NAME,newRoute.getName());
        content.put(KEY_METRIC,newRoute.getMetric());
        content.put(KEY_ACTIVE,newRoute.isActive());
        content.put(KEY_PERSISTENT,newRoute.isPersistent());
        content.put(KEY_INTERFACE, newRoute.getIface());
        return db.update(ROUTE_TABLE_NAME,content,KEY_ROWID + "='" + id + "'",null);
    }

    public void deleteRoute(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ROUTE_TABLE_NAME, KEY_ROWID + "='" + id + "'", null);
        //db.rawQuery("DELETE FROM " + ROUTE_TABLE_NAME + " WHERE " + KEY_ROWID + "='" + id + "'",null);
    }

    public long insertRoute(Route newRoute) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(KEY_ADDRESS,newRoute.getAddress());
        content.put(KEY_NETMASK,newRoute.getNetmask());
        content.put(KEY_GATEWAY,newRoute.getGateway());
        content.put(KEY_NAME,newRoute.getName());
        content.put(KEY_METRIC,newRoute.getMetric());
        content.put(KEY_INTERFACE,newRoute.getIface());
        content.put(KEY_ACTIVE, newRoute.isActive());
        content.put(KEY_PERSISTENT, newRoute.isPersistent());
        return db.insert(ROUTE_TABLE_NAME,null,content);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ROUTE_TABLE_CREATE);
        db.execSQL(ROUTE2_TABLE_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor fetchSSID(long routeid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(ROUTE2_TABLE_NAME, new String[] {KEY2_ROWID, KEY2_SSID},
                KEY2_ROUTEID + "='" + routeid +"'", null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void deleteSSIDforRoute(long routeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ROUTE2_TABLE_NAME, KEY2_ROUTEID + "='" + routeid + "'", null);
    }

    public Cursor fetchRouteForSSID(String SSID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(ROUTE2_TABLE_NAME, new String[] {KEY2_ROWID, KEY2_SSID, KEY2_ROUTEID},
                KEY2_SSID + "='" + SSID +"'", null, null, null, null);
        String where = "";
        long id;

        if (mCursor != null) {
            mCursor.moveToFirst();
            if(mCursor.getCount()>0) {
                //Assemble an additional where statement for the second query of the actual routes...
                int index = mCursor.getColumnIndex(StorageProvider.KEY2_ROUTEID);
                id = Long.valueOf(mCursor.getString(index));
                where += KEY_ROWID + "='" + id + "'";
                if (!mCursor.isLast()) {
                    where += " OR ";
                }

                while (mCursor.moveToNext()) {
                    id = mCursor.getInt(mCursor.getColumnIndex(StorageProvider.KEY2_ROUTEID));
                    where += KEY_ROWID + "='" + id + "'";
                    if (!mCursor.isLast()) {
                        where += " OR ";
                    }
                }
                mCursor.close();
            } else {
                return mCursor;
            }
        }

        //Requery cursor on the route table
        mCursor = db.query(ROUTE_TABLE_NAME, new String[] {KEY_ROWID,
                        KEY_ADDRESS, KEY_NETMASK, KEY_GATEWAY, KEY_NAME, KEY_METRIC, KEY_ACTIVE, KEY_PERSISTENT, KEY_INTERFACE},
                where, null, null, null, null);

        if(mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public int updateSSID(int id, String SSID, int routeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(KEY2_ROUTEID,routeid);
        content.put(KEY2_SSID,SSID);
        return db.update(ROUTE2_TABLE_NAME,content,KEY2_ROWID + "='" + id + "'",null);
    }

    public void deleteSSID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ROUTE2_TABLE_NAME, KEY2_ROWID + "='" + id + "'", null);
    }

    public boolean isSSIDactiveForRoute(long routeid, String SSID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(ROUTE2_TABLE_NAME, new String[]{KEY2_ROWID}, KEY2_ROUTEID + "='" + routeid + "' AND " +
                KEY2_SSID + "='" + SSID + "'", null, null, null, null);
        boolean ret = mCursor.getCount() > 0;
        mCursor.close();
        return ret;
    }

    public long insertSSID(String SSID, long routeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(KEY2_ROUTEID,routeid);
        content.put(KEY2_SSID,SSID);
        return db.insert(ROUTE2_TABLE_NAME,null,content);
    }


    public Cursor fetchAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(ROUTE_TABLE_NAME, new String[] {KEY_ROWID,
                        KEY_ADDRESS, KEY_NETMASK, KEY_GATEWAY, KEY_NAME, KEY_METRIC, KEY_ACTIVE, KEY_PERSISTENT, KEY_INTERFACE},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
