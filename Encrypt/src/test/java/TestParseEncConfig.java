import com.ft.config.ParseEncConfig;
import org.junit.Test;

public class TestParseEncConfig {
    @Test
    public void testConfig() {
        if (ParseEncConfig.parse("enc_config.xml")) {
            StringBuilder text = new StringBuilder(128);
            text.append("src=").append(ParseEncConfig.config.src).append("\n")
                    .append("dst=").append(ParseEncConfig.config.dst).append("\n")
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
