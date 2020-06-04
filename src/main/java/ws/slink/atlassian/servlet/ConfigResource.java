package ws.slink.atlassian.servlet;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import ws.slink.atlassian.tools.JsonBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Scanned
@Path("/")
public class ConfigResource {

    @ComponentImport private final UserManager userManager;
    @ComponentImport private final PluginSettingsFactory pluginSettingsFactory;
    @ComponentImport private final TransactionTemplate transactionTemplate;

//    @ComponentImport private final PluginConfigService pluginConfigService;

    @Inject
    public ConfigResource(UserManager userManager, PluginSettingsFactory pluginSettingsFactory,
                          TransactionTemplate transactionTemplate) {
        this.userManager = userManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.transactionTemplate = transactionTemplate;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Config {
        @XmlElement private String projects;
        @XmlElement private String roles;
        public String getProjects() {
            return projects;
        }
        public void setProjects(String projects) {
            this.projects = projects;
        }
        public String getRoles() {
            return roles;
        }
        public void setRoles(String roles) {
            this.roles = roles;
        }
        public String toString() {
            return projects + " : " + roles;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpServletRequest request) {
        UserKey userKey = userManager.getRemoteUser().getUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(transactionTemplate.execute((TransactionCallback) () -> {
            PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
            Config config = new Config();
            config.setProjects((String)settings.get(Config.class.getName() + ".projects"));
            config.setRoles((String)settings.get(Config.class.getName() + ".roles"));
            System.out.println("~~~ prepared configuration: " + config);
            return config;
//            return new JsonBuilder()
//                .put("projects", settings.get(Config.class.getName() + ".projects"))
//                .put("roles", settings.get(Config.class.getName() + ".roles"))
//                .build();
        })).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(final Config config, @Context HttpServletRequest request) {
        UserKey userKey = userManager.getRemoteUser().getUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        transactionTemplate.execute((TransactionCallback) () -> {
            System.out.println("~~~ received configuration: " + config);
            PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
            pluginSettings.put(Config.class.getName() + ".projects", config.getProjects());
            pluginSettings.put(Config.class.getName()  +".roles", config.getRoles());
            return config;
        });
        return Response.noContent().build();
    }
}
