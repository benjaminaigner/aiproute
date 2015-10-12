package systems.byteswap.aiproute;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Class to provide encapsulated access to shell commands.
 * Based on:
 * https://stackoverflow.com/questions/20932102/execute-shell-command-from-android
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

public class ShellAccess {
    public static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int length;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }

    //exec any number of commands as su
    //Source: https://stackoverflow.com/questions/20932102/execute-shell-command-from-android
    public static String execSuCommand(String... strings) {
        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        try {
            Process su = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(su.getOutputStream());
            response = su.getInputStream();
            //any arguments
            for (String s : strings) {
                    outputStream.writeBytes(s+"\n");
                    outputStream.flush();
            }

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            res = readFully(response);
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Closer.closeSilently(outputStream, response);
        }

        return res;
    }
}

class Closer {
    private static final String AIPROUTE_SHELL = "MyActivity";
    public static void closeSilently(Object... xs) {
        // Note: on Android API levels prior to 19 Socket does not implement Closeable
        for (Object x : xs) {
            if (x != null) {
                try {

                    Log.d(AIPROUTE_SHELL, "closing: " + x);
                    if (x instanceof Closeable) {
                        ((Closeable)x).close();
                    } else if (x instanceof Socket) {
                        ((Socket)x).close();
                    } else if (x instanceof DatagramSocket) {
                        ((DatagramSocket)x).close();
                    } else {
                        Log.d(AIPROUTE_SHELL,"cannot close: "+x);
                        throw new RuntimeException("cannot close "+x);
                    }
                } catch (Throwable e) {
                    Log.e(AIPROUTE_SHELL,e.toString());
                }
            }
        }
    }
}