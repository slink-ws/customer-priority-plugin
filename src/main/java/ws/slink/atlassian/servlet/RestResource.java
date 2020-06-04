package ws.slink.atlassian.servlet;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import ws.slink.atlassian.service.PluginConfigService;
import ws.slink.atlassian.tools.JiraTools;

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
import java.util.Objects;
import java.util.stream.Collectors;

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
    public static final class AdminParams {
        @XmlElement private String projects;
        @XmlElement private String roles;
        public String getProjects() {
            return projects;
        }
        public AdminParams setProjects(String projects) {
            this.projects = projects;
            return this;
        }
        public String getRoles() {
            return roles;
        }
        public AdminParams setRoles(String roles) {
            this.roles = roles;
            return this;
        }
        public String toString() {
            return projects + " : " + roles;
        }
        public AdminParams log(String prefix) {
            System.out.println(prefix + this);
            return this;
        }
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class ConfigParams {
        @XmlElement private String list1;
        @XmlElement private String list2;
        @XmlElement private String list3;
        @XmlElement private String list4;
        @XmlElement private String style1;
        @XmlElement private String style2;
        @XmlElement private String style3;
        @XmlElement private String style4;

        public String getList1() {
            return list1;
        }
        public ConfigParams setList1(String value) {
            this.list1 = value;
            return this;
        }
        public String getList2() {
            return list2;
        }
        public ConfigParams setList2(String value) {
            this.list2 = value;
            return this;
        }
        public String getList3() {
            return list3;
        }
        public ConfigParams setList3(String value) {
            this.list3 = value;
            return this;
        }
        public String getList4() {
            return list4;
        }
        public ConfigParams setList4(String value) {
            this.list4 = value;
            return this;
        }

        public String getStyle1() {
            return style1;
        }
        public ConfigParams setStyle1(String value) {
            this.style1 = value;
            return this;
        }
        public String getStyle2() {
            return style2;
        }
        public ConfigParams setStyle2(String value) {
            this.style2 = value;
            return this;
        }
        public String getStyle3() {
            return style3;
        }
        public ConfigParams setStyle3(String value) {
            this.style3 = value;
            return this;
        }
        public String getStyle4() {
            return style4;
        }
        public ConfigParams setStyle4(String value) {
            this.style4 = value;
            return this;
        }

        public String toString() {
            return new StringBuilder()
                .append("#1 ").append(list1).append(" : ").append(style1).append("\n")
                .append("#2 ").append(list2).append(" : ").append(style2).append("\n")
                .append("#3 ").append(list3).append(" : ").append(style3).append("\n")
                .append("#4 ").append(list4).append(" : ").append(style4).append("\n")
                .toString()
            ;
        }
        public ConfigParams log(String prefix) {
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
            return new AdminParams()
                .setProjects(PluginConfigService.instance().getProjects())
                .setRoles(PluginConfigService.instance().getRoles())
                .log("~~~ prepared configuration: ")
            ;
        })).build();
    }

    @PUT
    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putAdminParams(final AdminParams config, @Context HttpServletRequest request) {
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
        return Response.ok(transactionTemplate.execute((TransactionCallback) () -> {
            return new ConfigParams()
                    .setList1(PluginConfigService.instance().getList(1)).setStyle1(PluginConfigService.instance().getStyle(1))
                    .setList2(PluginConfigService.instance().getList(2)).setStyle2(PluginConfigService.instance().getStyle(2))
                    .setList3(PluginConfigService.instance().getList(3)).setStyle3(PluginConfigService.instance().getStyle(3))
                    .setList4(PluginConfigService.instance().getList(4)).setStyle4(PluginConfigService.instance().getStyle(4))
                    .log("~~~ prepared configuration: \n")
                    ;
        })).build();
//        return Response.noContent().build();
    }

    @PUT
    @Path("/config")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putConfigParams(final ConfigParams config /*String string*/, @Context HttpServletRequest request) {
        UserKey userKey = userManager.getRemoteUser().getUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
//        System.out.println("~~~~~~~ PUT REQUEST: " + string);
        transactionTemplate.execute((TransactionCallback) () -> {
            config.log("~~~ received configuration: \n");

            PluginConfigService.instance().setList(1, config.getList1());
            PluginConfigService.instance().setList(2, config.getList2());
            PluginConfigService.instance().setList(3, config.getList3());
            PluginConfigService.instance().setList(4, config.getList4());

            PluginConfigService.instance().setStyle(1, config.getStyle1());
            PluginConfigService.instance().setStyle(2, config.getStyle2());
            PluginConfigService.instance().setStyle(3, config.getStyle3());
            PluginConfigService.instance().setStyle(4, config.getStyle4());

            return null;
        });
        return Response.noContent().build();
    }

    private boolean isPluginManager() {
        return JiraTools.userHasRolesInProjects(
            PluginConfigService.instance().projectsList().stream()
                .map(JiraTools::getProjectByKey).filter(Objects::nonNull).collect(Collectors.toList()),
            PluginConfigService.instance().rolesList().stream()
                .map(JiraTools::getProjectRoleByKey).filter(Objects::nonNull).collect(Collectors.toList()),
            ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()));
    }

}




//        UserKey userKey = userManager.getRemoteUser().getUserKey();
//        System.err.println("~~~ REMOTE USERNAME: " + userManager.getRemoteUser().getUsername());
//        System.err.println("~~~ REMOTE USERKEY : " + userManager.getRemoteUser().getUserKey());
//        System.err.println("~~~ APPLIC USERNAME: " + ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()).getName());
//        System.err.println("~~~ APPLIC USERKEY : " + ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()).getKey());
//        return true;
