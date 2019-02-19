package com.rest.utility;

public class CommonUtils {

        public static String getRandomString(int length) {
            StringBuilder buildString = new StringBuilder();
            String characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

            for (int i = 0; i < length; i++) {
                int index = (int) (Math.random() * characterSet.length());
                buildString.append(characterSet.charAt(index));
            }

            return buildString.toString();
        }
}
