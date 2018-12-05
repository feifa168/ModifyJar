package com.ft.config;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;
import java.util.Map;

// <encrypt>
//     <src>IdsConfig.jar</src>
//     <dst>IdsConfigEnc.jar</dst>
//     <files loadall="true">
//         <file type="package">com.ids.config</file>
//         <file type="package">com.ids.copy</file>
//         <file type="package">com.ids.param</file>
//         <file type="package">com.ids.shell</file>
//         <file type="package">com.ids.syslog</file>
//         <file type="package">com.ids</file>
//     </files>
// </encrypt>
public class ParseEncConfig {
    public static String errMsg = null;
    public static ParamConfig config = new ParamConfig();

    public static boolean parse(String xml) {
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(new File(xml));
            Node ndSrc = doc.selectSingleNode("/encrypt/src");
            Node ndDst = doc.selectSingleNode("/encrypt/dst");
            Node ndFiles = doc.selectSingleNode("/encrypt/files");
            List<Node> ndLstFiles;

            config.src = parseField(ndSrc, "");
            config.dst = parseField(ndDst, "");

            // 由于class文件加载时会加载所有的字节码文件，所以强制要求xml中必须明确加密哪些字节码文件，这个loadall就没用了
//            config.encFile.loadall = Boolean.valueOf(parseNodeAttribute(ndFiles, "loadall", "false")).booleanValue();
//            if (config.encFile.loadall) {
//                return true;
//            }
//            if ( (null != ndFiles) && (false == config.encFile.loadall) ) {
            if ( null != ndFiles ) {
                ndLstFiles = ndFiles.selectNodes("file[@type=\"package\"]");
                if ( parsePackages(ndLstFiles) ) {
                    return true;
                }
            }
        } catch (DocumentException e) {
            //e.printStackTrace();
            errMsg = e.getMessage();
        }
        return false;
    }

    private static String parseField(Node nd, String defaultValue) {
        if (nd != null) {
            String s = nd.getText();
            if (!s.equals("")) {
                return s;
            }
        }
        return defaultValue;
    }
    private static String parseNodeAttribute(Node nd, String attrName, String defaultValue) {
        if (nd != null) {
            Attribute attr = ((Element)nd).attribute(attrName);
            if (attr != null) {
                String s = attr.getText();
                if (!s.equals("")) {
                    return s;
                }
            }
        }
        return defaultValue;
    }

    private static boolean parsePackages(List<Node> items) {
        if (items != null) {
            if (items.size() == 0) {
                errMsg = "three is no package config";
                return false;
            }

            for (Node item : items) {
                if (item != null) {
                    config.encFile.files.add(item.getText());
                }
            }
            if (!config.encFile.files.isEmpty()) {
                return true;
            }
        }

        errMsg = "three is no valid package config";
        return false;
    }
}
