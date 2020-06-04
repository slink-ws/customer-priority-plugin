package ws.slink.atlassian.servlet;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import ws.slink.atlassian.service.PluginConfigService;

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
public class RestResource {

    @ComponentImport private final UserManager userManager;
    @ComponentImport private final TransactionTemplate transactionTemplate;
    @ComponentImport private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public RestResource(UserManager userManager, PluginSettingsFactory pluginSettingsFactory,
                        TransactionTemplate transactionTemplate) {
        this.userManager = userManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.transactionTemplate = transactionTemplate;
        PluginConfigService.instance().setPluginSettings(pluginSettingsFactory.createGlobalSettings());
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class Config {
        @XmlElement private String projects;
        @XmlElement private String roles;
        public String getProjects() {
            return projects;
        }
        public Config setProjects(String projects) {
            this.projects = projects;
            return this;
        }
        public String getRoles() {
            return roles;
        }
        public Config setRoles(String roles) {
            this.roles = roles;
            return this;
        }
        public String toString() {
            return projects + " : " + roles;
        }
        public Config log(String prefix) {
            System.out.println(prefix + this);
            return this;
        }
    }

    @GET
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdminParams(@Context HttpServletRequest request) {
        UserKey userKey = userManager.getRemoteUser().getUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(transactionTemplate.execute((TransactionCallback) () -> {
            return new Config()
                .setProjects(PluginConfigService.instance().getProjects())
                .setRoles(PluginConfigService.instance().getRoles())
                .log("~~~ prepared configuration: ")
            ;
        })).build();
    }

    @PUT
    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putAdminParams(final Config config, @Context HttpServletRequest request) {
        UserKey userKey = userManager.getRemoteUser().getUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        transactionTemplate.execute((TransactionCallback) () -> {
            config.log("~~~ received configuration: ");
            PluginConfigService.instance().setProjects(config.getProjects());
            PluginConfigService.instance().setRoles(config.getRoles());
            return null;
        });
        return Response.noContent().build();
    }

    @GET
    @Path("/config")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfigParams(@Context HttpServletRequest request) {
        if (!isPluginManager())
            return Response.status(Response.Status.UNAUTHORIZED).build();
        return Response.noContent().build();
    }

    private boolean isPluginManager() {
        UserKey userKey = userManager.getRemoteUser().getUserKey();
    }

}
