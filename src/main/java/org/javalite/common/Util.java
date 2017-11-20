/*
Copyright 2009-2016 Igor Polevoy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package org.javalite.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

/**
 * @author Igor Polevoy
 * @author Eric Nielsen
 */
public final class Util {

    private Util() {
        // not instantiable
    }

    /**
     * Reads contents of resource fully into a byte array.
     *
     * @param resourceName resource name.
     * @return entire contents of resource as byte array.
     */
    public static byte[] readResourceBytes(String resourceName) {
        InputStream is = Util.class.getResourceAsStream(resourceName);
        try {
            return bytes(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    /**
     * Reads contents of resource fully into a string. Sets UTF-8 encoding
     * internally.
     *
     * @param resourceName resource name.
     * @return entire contents of resource as string.
     */
    public static String readResource(String resourceName) {
        return readResource(resourceName, "UTF-8");
    }

    /**
     * Reads contents of resource fully into a string.
     *
     * @param resourceName resource name.
     * @param charset name of supported charset
     * @return entire contents of resource as string.
     */
    public static String readResource(String resourceName, String charset) {
        InputStream is = Util.class.getResourceAsStream(resourceName);
        try {
            return read(is, charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    /**
     * Reads contents of file fully and returns as string.
     *
     * @param fileName file name.
     * @return contents of entire file.
     */
    public static String readFile(String fileName) {
        return readFile(fileName, "UTF-8");
    }

    /**
     * Reads contents of file fully and returns as string.
     *
     * @param fileName file name.
     * @param charset name of supported charset.
     * @return contents of entire file.
     */
    public static String readFile(String fileName, String charset) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            return read(in, charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(in);
        }
    }

    /**
     * Closes a resource and swallows exception if thrown during a close.
     *
     * @param autoCloseable resource to close
     */
    public static void closeQuietly(AutoCloseable autoCloseable) {
        try {
            if (autoCloseable != null) {
                autoCloseable.close();
            }
        } catch (Exception ignore) {
        }
    }

    /**
     * Reads contents of the input stream fully and returns it as String. Sets
     * UTF-8 encoding internally.
     *
     * @param in InputStream to read from.
     * @return contents of the input stream fully as String.
     * @throws IOException in case of IO error
     */
    public static String read(InputStream in) throws IOException {
        return read(in, "UTF-8");
    }

    /**
     * Reads contents of the input stream fully and returns it as String.
     *
     * @param in InputStream to read from.
     * @param charset name of supported charset to use
     * @return contents of the input stream fully as String.
     * @throws IOException in case of IO error
     */
    public static String read(InputStream in, String charset) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("input stream cannot be null");
        }
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(in, charset);
            char[] buffer = new char[1024];
            StringBuilder sb = new StringBuilder();
            int len;
            while ((len = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }
            return sb.toString();
        } finally {
            closeQuietly(reader);
        }
    }

    /**
     * Reads contents of the input stream fully and returns it as byte array.
     *
     * @param in InputStream to read from.
     * @return contents of the input stream fully as byte array
     * @throws IOException in case of IO error
     */
    public static byte[] bytes(InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("input stream cannot be null");
        }
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream(1024);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            return os.toByteArray();
        } finally {
            closeQuietly(os);
        }
    }

    /**
     * Reads file into a byte array.
     *
     * @param file file to read.
     * @return content of file.
     * @throws java.io.IOException
     */
    public static byte[] read(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        try {
            return bytes(is);
        } finally {
            closeQuietly(is);
        }
    }

    /**
     * Returns lines of text of a resource as list.
     *
     * @param resourceName name of resource
     * @return list of text lines
     * @throws java.io.IOException in case of IO error
     */
    public static List<String> getResourceLines(String resourceName) throws IOException {
        InputStreamReader isreader = null;
        BufferedReader reader = null;
        try {
            isreader = new InputStreamReader(Util.class.getResourceAsStream(resourceName));
            reader = new BufferedReader(isreader);
            List<String> lines = new ArrayList<String>();
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                lines.add(tmp);
            }
            return lines;
        } finally {
            closeQuietly(reader);
            closeQuietly(isreader);
        }
    }

    /**
     * Returns true if value is either null or it's String representation is
     * blank.
     *
     * @param value object to check.
     * @return true if value is either null or it's String representation is
     * blank, otherwise returns false.
     */
    public static boolean blank(Object value) {
        return value == null || value.toString().trim().length() == 0;
    }

    /**
     * Saves content read from input stream into a file.
     *
     * @param path path to file.
     * @param in input stream to read content from.
     */
    public static void saveTo(String path, InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("input stream cannot be null");
        }
        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(out);
        }
    }

    /**
     * Saves content of byte array to file.
     *
     * @param path path to file - can be absolute or relative to current.
     * @param content bytes to save.
     */
    public static void saveTo(String path, byte[] content) {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(content);
            saveTo(path, is);
        } finally {
            closeQuietly(is);
        }
    }

    /**
     * Converts stack trace to string.
     *
     * @param throwable - throwable to convert.
     * @return message and stack trace converted to string.
     */
    public static String getStackTraceString(Throwable throwable) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            pw.println(throwable.toString());
            throwable.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        } finally {
            closeQuietly(pw);
            closeQuietly(sw);
        }
    }

    /**
     * Will encode byte array using Base64 encoding.
     *
     * @param input bytes to encode
     * @return encoded string
     */
    public static String toBase64(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    /**
     * Will decode Base64-encoded string back into byte array.
     *
     * @param input Base64-encoded string.
     * @return byte array decoded from string.
     */
    public static byte[] fromBase64(String input) {
        return Base64.getDecoder().decode(input);
    }

    /**
     * Convenience method to create literal String arrays. Helps to replace code
     * like this:
     * <p>
     * <code>
     *         String[] t = new String[]{"one", "two"}
     * </code>
     * </p>
     * with:
     * <p>
     * <code>
     *         String[] t = arr("one", "two");
     * </code>
     * </p>
     *
     * @param params strings to create array
     * @return array of strings
     */
    public String[] arr(String... params) {
        return params;
    }

    /**
     * Reads a property file from classpath to a properties object.
     *
     * @param file full path to a property file on class path
     * @return <code>java.util.Properties</code> object initialized from the
     * file.
     * @throws IOException
     */
    public static Properties readProperties(String file) throws IOException {
        InputStream in = Util.class.getClass().getResourceAsStream(file);
        Properties props = new Properties();
        if (in != null) {
            props.load(in);
        } else {
            try (FileInputStream fin = new FileInputStream(file)) {
                props.load(fin);
            }
        }
        return props;
    }

}
