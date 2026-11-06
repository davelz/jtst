import java.awt.Desktop;
import java.net.URI;

public class BrowserTest {
    public static void main(String[] args) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                if (args.length > 0) { 
                    URI uri = new URI(args[0]);
                    Desktop.getDesktop().browse(uri);
                } 
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

