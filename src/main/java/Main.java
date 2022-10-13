import br.com.luque.simplemvc.server.embedded.EmbeddedServer;
import br.com.luque.simplemvc.server.embedded.tomcat.EmbeddedTomcat;

public class Main {
    public static void main(String[] args) {

        EmbeddedServer embeddedServer = new EmbeddedTomcat();
        embeddedServer.start("src/main/webapp/", 8080);

    }
}