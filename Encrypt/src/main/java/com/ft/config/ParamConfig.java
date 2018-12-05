package com.ft.config;

import java.util.HashSet;
import java.util.Set;

public class ParamConfig {
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
    public static class EncFile {
        public boolean loadall = false;
        public Set<String> files = new HashSet<>();
    }
    public String src;
    public String dst;
    public EncFile encFile = new EncFile();
}
