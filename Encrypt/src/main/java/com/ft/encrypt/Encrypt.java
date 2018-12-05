package com.ft.encrypt;

import com.ft.config.ParseEncConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class Encrypt {
    native byte[] encrypt(byte[] _buf);

    static {
        System.loadLibrary("libNativeEncrypt");
    }

    // 获取参数
    static Map<String, String> getArgMap(String[] args) {
        Map<String, String> map = new HashMap<>();
        String key = null, val = null;
        for (String tmp : args) {
            if (tmp.startsWith("-")) {
                if (key != null)
                    map.put(key, val);
                key = tmp;
                val = null;
            } else {
                val = tmp;
            }
        }
        if (key != null) {
            map.put(key, val);
        }

        return map;
    }

    public static void main(String[] args) throws Exception {
        if ( !ParseEncConfig.parse("enc_config.xml") ) {
            System.out.println(ParseEncConfig.errMsg);
            return;
        } else {
            StringBuilder text = new StringBuilder(128);
            text.append("src=").append(ParseEncConfig.config.src).append("\n")
                    .append("dst=").append(ParseEncConfig.config.dst).append("\n")
                    .append("loadall=").append(ParseEncConfig.config.encFile.loadall).append("\n")
                    .append("files\n");
            for (String s : ParseEncConfig.config.encFile.files) {
                text.append("    ").append(s).append("\n");
            }
            System.out.println(text.toString());
        }

        Map<String, String> map = getArgMap(args);

        String src_name = map.get("-src");
        if (src_name == null) {
            if ( (null != ParseEncConfig.config.src) && (ParseEncConfig.config.src.length() > 0) ) {
                src_name = ParseEncConfig.config.src;
            } else {
                System.out.println("usage: java Encrypt -src xxx.jar or config src node");
            }
        }

        Encrypt coder = new Encrypt();

        String dst_name = map.get("-dst");
        if (dst_name == null) {
            if ( (null != ParseEncConfig.config.dst) && (ParseEncConfig.config.dst.length() > 0) ) {
                dst_name = ParseEncConfig.config.dst;
            } else {
                dst_name = src_name.substring(0, src_name.length() - 4) + "_encrypt.jar";
            }
        } else if ( dst_name.equals(src_name) ) {
            dst_name = src_name.substring(0, src_name.length() - 4) + "_encrypt.jar";
        }

        System.out.printf("encode jar file: [%s ==> %s ]\n", src_name, dst_name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        File dst_file = new File(dst_name);
        File src_file = new File(src_name);

        FileOutputStream dst_fos = new FileOutputStream(dst_file);
        JarOutputStream dst_jar = new JarOutputStream(dst_fos);

        JarFile src_jar = new JarFile(src_file);
        for (Enumeration<JarEntry> enumeration = src_jar.entries(); enumeration.hasMoreElements();) {
            JarEntry entry = enumeration.nextElement();

            InputStream is = src_jar.getInputStream(entry);
            int len;
            while ((len = is.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, len);
            }
            byte[] bytes = baos.toByteArray();

            String name = entry.getName();
            boolean encrypt = false;
            //if(name.endsWith(".class") && name.startsWith("com/ft/decrypt")){
            if ( name.endsWith(".class") ) {
                if (ParseEncConfig.config.encFile.loadall) {
                    encrypt = true;
                } else {
                    for ( String file : ParseEncConfig.config.encFile.files ) {
                        if ( name.startsWith(file.replaceAll("\\.", "/")) ) {
                            encrypt = true;
                            break;
                        }
                    }
                }
                try {
                    if (encrypt) {
                        System.out.println("encrypt " + name.replaceAll("/", "."));
                        bytes = coder.encrypt(bytes);
                    }
                } catch (Exception e) {
                    System.out.println("encrypt error happend~");
                    e.printStackTrace();
                }
            }

            //System.out.println("input " + name);

            JarEntry ne = new JarEntry(name);
            dst_jar.putNextEntry(ne);
            dst_jar.write(bytes);
            baos.reset();
        }
        src_jar.close();

        dst_jar.close();
        dst_fos.close();
    }
}
