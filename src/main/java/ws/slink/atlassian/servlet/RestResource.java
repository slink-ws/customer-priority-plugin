package ws.slink.atlassian.servlet;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import ws.slink.atlassian.service.ConfigService;
import ws.slink.atlassian.service.CustomerLevelService;
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
        ConfigService.instance().setPluginSettings(pluginSettingsFactory.createGlobalSettings());
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

        @XmlElement private String text1;
        @XmlElement private String text2;
        @XmlElement private String text3;
        @XmlElement private String text4;

        @XmlElement private String color1;
        @XmlElement private String color2;
        @XmlElement private String color3;
        @XmlElement private String color4;

        @XmlElement private String viewers;

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

        public String getText1() {
            return text1;
        }
        public ConfigParams setText1(String value) {
            this.text1 = value;
            return this;
        }
        public String getText2() {
            return text2;
        }
        public ConfigParams setText2(String value) {
            this.text2 = value;
            return this;
        }
        public String getText3() {
            return text3;
        }
        public ConfigParams setText3(String value) {
            this.text3 = value;
            return this;
        }
        public String getText4() {
            return text4;
        }
        public ConfigParams setText4(String value) {
            this.text4 = value;
            return this;
        }

        public String getColor1() {
            return color1;
        }
        public ConfigParams setColor1(String value) {
            this.color1 = value;
            return this;
        }
        public String getColor2() {
            return color2;
        }
        public ConfigParams setColor2(String value) {
            this.color2 = value;
            return this;
        }
        public String getColor3() {
            return color3;
        }
        public ConfigParams setColor3(String value) {
            this.color3 = value;
            return this;
        }
        public String getColor4() {
            return color4;
        }
        public ConfigParams setColor4(String value) {
            this.color4 = value;
            return this;
        }

        public String getViewers() {
            return viewers;
        }
        public ConfigParams setViewers(String value) {
            this.viewers = value;
            return this;
        }

        public String toString() {
            return new StringBuilder()
                .append("#1 ").append(list1).append(" : ").append(style1).append(" : ").append(text1).append(" : ").append(color1).append("\n")
                .append("#2 ").append(list2).append(" : ").append(style2).append(" : ").append(text2).append(" : ").append(color2).append("\n")
                .append("#3 ").append(list3).append(" : ").append(style3).append(" : ").append(text3).append(" : ").append(color3).append("\n")
                .append("#4 ").append(list4).append(" : ").append(style4).append(" : ").append(text4).append(" : ").append(color4).append("\n")
                .append("#V ").append(viewers).append("\n")
                .toString()
            ;
        }
        public ConfigParams log(String prefix) {
            System.out.println(prefix + this);
            return this;
        }
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static final class ColorParams {
        @XmlElement private String color;
        public String getColor() {
            return color;
        }
        public ColorParams setColor(String color) {
            this.color = color;
            return this;
        }
        public String toString() {
            return "color: " + color;
        }
        public ColorParams log(String prefix) {
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
        return Response.ok(transactionTemplate.execute((TransactionCallback) () -> new AdminParams()
            .setProjects(ConfigService.instance().getProjects())
            .setRoles(ConfigService.instance().getRoles())
            .log("~~~ prepared configuration: "))).build();
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
            ConfigService.instance().setProjects(config.getProjects());
            ConfigService.instance().setRoles(config.getRoles());
            return null;
        });
        return Response.noContent().build();
    }

    @GET
    @Path("/config")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfigParams(@Context HttpServletRequest request) {
        if (!JiraTools.isPluginManager(userManager.getRemoteUser()))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        else
            return Response.ok(transactionTemplate.execute((TransactionCallback) () ->
                new ConfigParams()
                .setViewers(ConfigService.instance().getViewers())
                .setList1(ConfigService.instance().getList(1))
                    .setStyle1(ConfigService.instance().getStyle(1))
                        .setText1(ConfigService.instance().getText(1))
                            .setColor1(ConfigService.instance().getColor(1))
                .setList2(ConfigService.instance().getList(2))
                    .setStyle2(ConfigService.instance().getStyle(2))
                        .setText2(ConfigService.instance().getText(2))
                            .setColor2(ConfigService.instance().getColor(2))
                .setList3(ConfigService.instance().getList(3))
                    .setStyle3(ConfigService.instance().getStyle(3))
                        .setText3(ConfigService.instance().getText(3))
                            .setColor3(ConfigService.instance().getColor(3))
                .setList4(ConfigService.instance().getList(4))
                    .setStyle4(ConfigService.instance().getStyle(4))
                        .setText4(ConfigService.instance().getText(4))
                            .setColor4(ConfigService.instance().getColor(4))
                .log("~~~ prepared configuration: \n"))).build();
    }

    @PUT
    @Path("/config")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putConfigParams(final ConfigParams config, @Context HttpServletRequest request) {
        if (!JiraTools.isPluginManager(userManager.getRemoteUser())) {
//        UserKey userKey = userManager.getRemoteUser().getUserKey();
//        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
//        System.out.println("~~~~~~~ PUT REQUEST: " + string);
        transactionTemplate.execute((TransactionCallback) () -> {
            config.log("~~~ received configuration: \n");

            ConfigService.instance().setList(1, config.getList1());
            ConfigService.instance().setList(2, config.getList2());
            ConfigService.instance().setList(3, config.getList3());
            ConfigService.instance().setList(4, config.getList4());

            ConfigService.instance().setStyle(1, config.getStyle1());
            ConfigService.instance().setStyle(2, config.getStyle2());
            ConfigService.instance().setStyle(3, config.getStyle3());
            ConfigService.instance().setStyle(4, config.getStyle4());

            ConfigService.instance().setText(1, config.getText1());
            ConfigService.instance().setText(2, config.getText2());
            ConfigService.instance().setText(3, config.getText3());
            ConfigService.instance().setText(4, config.getText4());

            ConfigService.instance().setColor(1, config.getColor1());
            ConfigService.instance().setColor(2, config.getColor2());
            ConfigService.instance().setColor(3, config.getColor3());
            ConfigService.instance().setColor(4, config.getColor4());

            ConfigService.instance().setViewers(config.getViewers());

            return null;
        });
        return Response.noContent().build();
    }

    @GET
    @Path("/color/{issueId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIssueColor(@PathParam("issueId") String issueId, @Context HttpServletRequest request) {
        System.out.println("REST COLOR REQUEST: trying to get issue color..." + issueId + " : " + request.getQueryString());
        try {

            ApplicationUser user = JiraTools.getLoggedInUser();
            Issue issue = ComponentAccessor.getIssueManager().getIssueByCurrentKey(issueId); //((Issue) new JiraHelper().getContextParams().get("issue"));

            if (null == user || null == issue) {
                return emptyResponse(); //createColorResponse("#FFFFFF");
            }

            Project project = ComponentAccessor.getProjectManager().getProjectObj(issue.getProjectId());

            if (null == project) {
                return emptyResponse();//createColorResponse("#FFFFFF");
            } else if (JiraTools.isViewer(user) && ConfigService.instance().projectsList().contains(project.getKey())){
                String reporterEmail = issue.getReporter().getEmailAddress();
                return createColorResponse(
                    ConfigService.instance().getColor(
                        CustomerLevelService.getLevel(
                            reporterEmail
                        )
                    )
                );
            } else {
                return emptyResponse(); //createColorResponse("#FFFFFF");
            }
        } catch (Exception e) {
            System.err.println("REST COLOR REQUEST: ERROR: " + e.getClass().getName() + " : " + e.getMessage());
            return Response.serverError().build();
        }
    }

    private Response createColorResponse(String color) {
        return Response.ok(transactionTemplate.execute((TransactionCallback) () ->
            new ColorParams()
                .setColor(color
                ).log("REST COLOR REQUEST: ~~~ prepared color: \n"))
        ).build();
    }

    private Response emptyResponse() {
        return Response.noContent().build();
    }
}

//            return Response.ok(transactionTemplate.execute((TransactionCallback) () ->
//                new ColorParams()
//                    .setColor(
//                        ConfigService.instance().getColor(
//                            CustomerLevelService.getLevel(
//                                reporterEmail
//                            )
//                        )
//                    ).log("REST COLOR REQUEST: ~~~ prepared color: \n"))
//                ).build()
;
//    private boolean isPluginManager() {
//        return JiraTools.userHasRolesInProjects(
//            ConfigService.instance().projectsList().stream()
//                .map(JiraTools::getProjectByKey).filter(Objects::nonNull).collect(Collectors.toList()),
//            ConfigService.instance().rolesList().stream()
//                .map(JiraTools::getProjectRoleByKey).filter(Objects::nonNull).collect(Collectors.toList()),
//            ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()));
//    }

