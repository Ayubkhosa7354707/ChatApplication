package com.ayub.khosa.chatapplication;


import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AccessToken {
    private static final String firebaseMessagingScope = "https://www.googleapis.com/";

    public String getAccessToken() {
        try {

            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"chatapplication-563d0\",\n" +
                    "  \"private_key_id\": \"b83aa7ab841164b775ddf95a8d16d85444fb2679\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCsJqn/WE1igC4V\\npQ+eFD5EBCU1KZ6Gv1EEntoSS02gvn8lCJKupMzqixbz2U0s6Dyx7k58OstovHbu\\nidQ/fVylJsavERvllW7UbGJ7xPEwzthmaCaGD1/mEWsDAHTv3xin1Sqe5lxGy/2S\\n0Uq6JtxVTiNirPGl1On+CTQiqKbYoPl+Ezjs0sE3nDe2AXLU/L9ZibZysfM8FXrt\\nBMN/OTiCeH5b1AG/Jrd0eHRVjWiybegtwGhTWy8y9AkhvY6JmMh1ayoErgxcqfpW\\nisMDOV9HnFYyvfAXXI49j/hK38sh/Hx6Jv5H4mRI247WyCVeXqM6g8RSlV7Q67dD\\nsX5XgVQlAgMBAAECggEASlRzaD4vQ42G2G+pm56v99w2WIr82L94ct9H10hOX06B\\nRXM7Kk8b71VY9rbnvLEVq9nyZ29XboSZcYtvam5tdx2UAfkIK4vnC680oq45WdKl\\n2ymzIIU46fSxWN10oDGayrSer3dVdngTp7XYZyHqVqbjXnK/79NLqRU+PozFZZj8\\n2a1OikZ/UOW4ydE2qE3fdX+gDhSNMA5+QxUrR+1wTYFm7EL2VHZBMKLrleXsKnrg\\naafMqD8KKNVST+xAR79H8GT32rsI7woHeRmnoKyodImGoKdCgJ9KWg/En2Uq6i5y\\nQzfQT2DvZe+VhpUuH6mf0tZiUNofDpcWzDKN5TH7zQKBgQDv5PyH8t/Lr0zhiHK7\\nb8vxllGg7z3HkGlA2YeNXsGftrOy3CximhbI6JR9zV3wl7oZRtzG41UKS45bsPlf\\n+9KoMlMCs8OL90g9B3IpANZS2svyH4QTQ+8opMh2EUlGr8z83bTr9y/yaR/pkrin\\n40O4hqz9pKfO259YuznXBTawwwKBgQC3tWLKtaK3gy4S9GPgAtrqKEydkDj3HJcG\\n8BgAIlMG+DWyQHaXV67xdeV/vSQv5bt/wT490mJ0e0X8IyE8g1+8cQtb6I1SUcus\\niz0buIHjYvZOEklCTlTOwTV1Xiqcu9nkipPI1/kaZELY5R2ySf3odrddJ38HN4wj\\nJ9yI4YOY9wKBgEiVOwcTKD1jk9plQ3mM0OMOvTH5UPLxsbbqWkYe1myxhdtnHJLU\\n1nIBVPRfT7382lTNiP0wSUmtJKx/dmRhOBKl71HQ/Ch8MEIu346ibzZwduaSPI7r\\nrg/ZE8INZuNZS6dymUwP5VTuE7bDKtW5QIMzVGIGELPY5XSeaViyfiKrAoGAB9fY\\npernTYpuNyTz9qZojkK2MqvrorE3yXYKsEbG4K9MK5YQ+hkmQOzVpYR/vQPD44pu\\ni9kllu+EZ2Vgj+LT/YmeAIiHMBKJ7Y3sR/iyovCgr2icdBZADKu3CpHAo/xpKxa3\\nhIWj/vp02CnAANKj4n7fCT0ccURv2Thkc/uc0LUCgYEA3AZPai1PZTlHkCj7q8yU\\n3CDM26Sk7KAfsm+HNUinbO9KrAHzfeoQz/yfmK041HH1TzlcjVsiHcsCwOzJfSQI\\nosdqFpwGjSBnBSgGaWUfP5Sx0n30nrBnsmRoGae1nLY16uXppIJv5Biau40ND1R5\\nUtTt+Emscc+u+K9pL/7qbzo=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-fbsvc@chatapplication-563d0.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"105318778642210417528\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40chatapplication-563d0.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}";

            Log.d("AYUB", "AccessToken  jsonString ---->: " + jsonString);
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));


            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(firebaseMessagingScope);
            googleCredentials.refresh();

            String accessToken = googleCredentials.getAccessToken().getTokenValue();
            Log.d("AYUB", "AccessToken ---->: " + accessToken);

            return accessToken;
        } catch (Exception e) {
            Log.e("AYUB", "AccessToken getAccessToken: " + e.getMessage());
            e.printStackTrace();
            return "Exception AccessToken getAccessToken ";
        }
    }
}