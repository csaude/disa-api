package mz.org.fgh.disaapi.integ.resources.config;

import java.net.URISyntaxException;

import org.apache.catalina.Context;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    /**
     * @return A {@link TomcatContextCustomizer} sets the folder public from which
     *         static resources will be served.
     */
    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return new TomcatContextCustomizer() {

            @Override
            public void customize(Context context) {
                StandardRoot resources = new StandardRoot(context);
                try {
                    String path = getClass().getResource("/public").toURI().getPath();
                    resources.addPreResources(new DirResourceSet(resources, "/", path, "/"));
                    context.setResources(resources);
                    context.addWelcomeFile("index.html");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

        };
    }
}
