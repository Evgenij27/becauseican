package org.nashorn.server.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class CommonUtils {

    public static String getCode(HttpServletRequest req) {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        String data = null;
        try {
            br = req.getReader();
            while ((data = br.readLine()) != null) {
                sb.append(data);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }
}
