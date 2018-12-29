import com.ft.config.ParseEncConfig;
import org.junit.Test;
import java.util.Map;

public class TestParseEncConfig {
    @Test
    public void testOsName() {

        String osname = System.getProperty("os.name");
        if (osname == null) {
            osname = "Windows";
        }
        osname = osname.toLowerCase();
        System.out.println(osname);

        for ( Map.Entry<Object, Object> kv : System.getProperties().entrySet() ) {
            System.out.println("name " + kv.getKey().toString() + "\t ---- \t value " + kv.getValue().toString() + "!!!!");
        }
//        for (Hashtable.Entry<String, String> kv : ps) {
//
//        }
    }

    @Test
    public void testConfig() {
        if (ParseEncConfig.parse("enc_config.xml")) {
            StringBuilder text = new StringBuilder(128);
            text.append("src=").append(ParseEncConfig.config.src).append("\n")
                    .append("dst=").append(ParseEncConfig.config.dst).append("\n")
                    .append("decxml=").append(ParseEncConfig.config.decxml).append("\n")
                    .append("loadall=").append(ParseEncConfig.config.encFile.loadall).append("\n")
                    .append("files\n");
            for (String s : ParseEncConfig.config.encFile.files) {
                text.append("    ").append(s).append("\n");
            }
            System.out.println(text.toString());
        } else {
            System.out.println(ParseEncConfig.errMsg);
        }
    }
}
