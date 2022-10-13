package br.com.luque.simplemvc.server.embedded.tomcat;

import br.com.luque.simplemvc.server.embedded.EmbeddedServer;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;

public class EmbeddedTomcat implements EmbeddedServer {

    private static final Logger logger = Logger.getLogger(EmbeddedTomcat.class.getName());

    public void start(String webappDirLocation, int port) {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);

        logger.info("Configuring webapp with base dir: " + new File("./" + webappDirLocation).getAbsolutePath() + "...");

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
        tomcat.enableNaming();
        tomcat.getConnector();

        try {
            logger.info("Starting Tomcat...");
            tomcat.start();
        } catch (LifecycleException e) {
            logger.log(Level.SEVERE, "An error happened while starting the embedded Tomcat", e);
        }
        tomcat.getServer().await();

    }

}
