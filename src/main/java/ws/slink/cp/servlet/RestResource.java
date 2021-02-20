package ws.slink.cp.servlet;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import ws.slink.cp.service.ConfigService;
import ws.slink.cp.tools.Common;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Scanned
@Path("/")
public class RestResource {

    @ComponentImport private final UserManager userManager;
    @ComponentImport private final TransactionTemplate transactionTemplate;

    @Inject
    public RestResource(
        UserManager userManager,
        TransactionTemplate transactionTemplate) {
        this.userManager = userManager;
        this.transactionTemplate = transactionTemplate;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    private static final class AdminParams {
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
            return Common.instance().getGsonObject().toJson(this);
        }
        public AdminParams log(String prefix) {
            System.out.println(prefix + this);
            return this;
        }
    }

    @PUT
    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putAdminParams(final AdminParams config, @Context HttpServletRequest request) {
        UserKey userKey = userManager.getRemoteUser().getUserKey();
        if (userKey == null || !userManager.isSystemAdmin(userKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        AtomicBoolean saveResult = new AtomicBoolean(true);
        AtomicReference<String> message = new AtomicReference<>("");

        transactionTemplate.execute(/*(TransactionCallback)*/ () -> {
            config.log("----> received configuration: " + config);
            ConfigService.instance().setAdminProjects(config.getProjects());
            ConfigService.instance().setAdminRoles(config.getRoles());
            saveResult.set(true);
            return null;
        });
        if (saveResult.get()) {
            return Response.noContent().build();
        } else {
            return Response.status(400).entity(message.get()).build();
        }
    }

}


//    @XmlRootElement
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static final class ConfigParams {
//        @XmlElement private String project;
//        @XmlElement private String apikey;
//        @XmlElement private String mgmt_roles;
//        @XmlElement private String view_roles;
//        public String getProject() {
//            return project;
//        }
//        public ConfigParams setProject(String value) {
//            this.project = value;
//            return this;
//        }
//        public String getApikey() {
//            return apikey;
//        }
//        public ConfigParams setApikey(String value) {
//            this.apikey = value;
//            return this;
//        }
//        public String getMgmtRoles() {
//            return mgmt_roles;
//        }
//        public ConfigParams getMgmtRoles(String value) {
//            this.mgmt_roles = value;
//            return this;
//        }
//        public String getViewRoles() {
//            return view_roles;
//        }
//        public ConfigParams getViewRoles(String value) {
//            this.view_roles = value;
//            return this;
//        }
//        public String toString() {
//            return project + " : " + apikey + " : " + mgmt_roles + " : " + view_roles;
//        }
//        public ConfigParams log(String prefix) {
//            System.out.println(prefix + this);
//            return this;
//        }
//    }
//
//    @XmlRootElement
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static final class IncidentUpdateParams {
//
//        @XmlElement private String project;
//        @XmlElement private String issue;
//        @XmlElement private String page;
//        @XmlElement private String incident;
//        @XmlElement private String status;
//        @XmlElement private String impact;
//        @XmlElement private String message;
//        @XmlElement private boolean publishComment;
//        @XmlElement private Map<String, List<String>> components;
//
//        public String getProject() {
//            return project;
//        }
//        public IncidentUpdateParams setProject(String value) {
//            this.project = value;
//            return this;
//        }
//        public String getIssue() {
//            return issue;
//        }
//        public IncidentUpdateParams setIssue(String value) {
//            this.issue = value;
//            return this;
//        }
//        public String getPage() {
//            return page;
//        }
//        public IncidentUpdateParams setPage(String value) {
//            this.page = value;
//            return this;
//        }
//        public String getIncident() {
//            return incident;
//        }
//        public IncidentUpdateParams setIncident(String value) {
//            this.incident = value;
//            return this;
//        }
//        public String getStatus() {
//            return status;
//        }
//        public IncidentUpdateParams setStatus(String value) {
//            this.status = value;
//            return this;
//        }
//        public String getImpact() {
//            return impact;
//        }
//        public IncidentUpdateParams setImpact(String value) {
//            this.impact = value;
//            return this;
//        }
//        public String getMessage() {
//            return message;
//        }
//        public IncidentUpdateParams setMessage(String value) {
//            this.message = value;
//            return this;
//        }
//        public Map<String, List<String>> getComponents() {
//            return components;
//        }
//        public IncidentUpdateParams setComponents(Map<String, List<String>> value) {
//            this.components = value;
//            return this;
//        }
//
//        public String toString() {
//            return project + " " + issue + " " + page + " " + incident + " " + status + " " + impact + " " + message + " " + components;
//        }
//        public IncidentUpdateParams log(String prefix) {
//            System.out.println(prefix + this);
//            return this;
//        }
//    }






//    @PUT
//    @Path("/config")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response putConfigParams(final ConfigParams config, @Context HttpServletRequest request) {
//        if (!JiraTools.instance().isPluginManager(userManager.getRemoteUser())) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//        transactionTemplate.execute((TransactionCallback) () -> {
////            config.log("~~~ received plugin config: ");
//            ConfigService.instance().setConfigMgmtRoles(config.getProject(), config.getMgmtRoles());
//            ConfigService.instance().setConfigViewRoles(config.getProject(), config.getViewRoles());
//            ConfigService.instance().setConfigApiKey   (config.getProject(), config.getApikey());
//            StatuspageService.instance().clear();
//            ConfigService.instance().getAdminProjects().stream().forEach(p ->
//                StatuspageService.instance().init(p, ConfigService.instance().getConfigApiKey(p))
//            );
//            return null;
//        });
//        return Response.noContent().build();
//    }
//
//    @GET
//    @Path("/api/access")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response checkAccess(
//            @Context HttpServletRequest request) {
////        ComponentAccessor.getJiraAuthenticationContext().getUser();
////        System.out.println("-----> " + ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser());
//        return Response.ok("{\"status\": \"ok\", \"user\": \"" + ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser() + "\"}").build();
////        request.getRemoteUser()
////        return Response.ok(JiraTools.instance().getGsonObject().toJson(
////                Arrays.stream(ComponentStatus.values())
////                        .sorted(Comparator.comparing(ComponentStatus::id))
////                        .filter(c -> StringUtils.isNotBlank(c.value()))
////                        .map(AffectedComponentStatus::of)
////                        .collect(Collectors.toList())
////        )).build();
//    }
//
//    @GET
//    @Path("/api/pages")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response pages(
//        @QueryParam("issueKey") String issueKey,
//        @Context HttpServletRequest request) {
//
//        Optional<Project> project = JiraTools.instance().getProjectForIssue(issueKey);
//        if (!project.isPresent())
//            return Response.noContent().build();
//
//        if (!JiraTools.instance().isIncidentManager(
//                project.get().getKey(),
//                ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()))) {
//            System.out.println("--- UNAUTHORIZED");
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//
//        Optional<StatusPage> statusPage = StatuspageService.instance().get(project.get().getKey());
//        if (!statusPage.isPresent())
//            return Response.noContent().build();
//
//        return Response.ok(JiraTools.instance().getGsonObject().toJson(statusPage.get().pages())).build();
//    }
//
//    @GET
//    @Path("/api/components")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response components(
//        @QueryParam("issueKey") String issueKey,
//        @QueryParam("pageId") String pageId,
//        @Context HttpServletRequest request) {
//
//        if (StringUtils.isBlank(pageId))
//            return Response.status(HttpStatus.SC_NOT_FOUND).build();
//
//        Optional<Project> project = JiraTools.instance().getProjectForIssue(issueKey);
//        if (!project.isPresent())
//            return Response.status(HttpStatus.SC_NOT_FOUND).build();
//
//        if (!JiraTools.instance().isIncidentManager(
//                project.get().getKey(),
//                ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()))) {
//            System.out.println("--- UNAUTHORIZED");
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//
//        Optional<StatusPage> statusPage = StatuspageService.instance().get(project.get().getKey());
//        if (!statusPage.isPresent())
//            return Response.status(HttpStatus.SC_NOT_FOUND).build();
//
//        return Response.ok(JiraTools.instance().getGsonObject().toJson(statusPage.get().components(pageId))).build();
//    }
//
//    @GET
//    @Path("/api/groups")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response groups(
//        @QueryParam("issueKey") String issueKey,
//        @QueryParam("pageId") String pageId,
//        @Context HttpServletRequest request) {
//
//        if (StringUtils.isBlank(pageId))
//            return Response.noContent().build();
//
//        Optional<Project> project = JiraTools.instance().getProjectForIssue(issueKey);
//        if (!project.isPresent())
//            return Response.noContent().build();
//
//        if (!JiraTools.instance().isIncidentManager(
//                project.get().getKey(),
//                ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()))) {
//            System.out.println("--- UNAUTHORIZED");
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//
//        Optional<StatusPage> statusPage = StatuspageService.instance().get(project.get().getKey());
//        if (!statusPage.isPresent())
//            return Response.noContent().build();
//
//        return Response.ok(
//            JiraTools.instance().getGsonObject().toJson(
//                statusPage
//                    .get()
//                    .groups(pageId)
//                    .stream()
//                    .sorted(Comparator.comparing(Group::name))
//                    .collect(Collectors.toList())
//            )
//        ).build();
//    }
//
//    @GET
//    @Path("/api/groupComponents")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response groupComponents(
//        @QueryParam("issueKey") String issueKey,
//        @QueryParam("pageId") String pageId,
//        @QueryParam("groupId") String groupId,
//        @Context HttpServletRequest request) {
//
//        if (StringUtils.isBlank(pageId))
//            return Response.noContent().build();
//
//        if (StringUtils.isBlank(groupId))
//            return Response.noContent().build();
//
//        Optional<Project> project = JiraTools.instance().getProjectForIssue(issueKey);
//        if (!project.isPresent())
//            return Response.noContent().build();
//
//        if (!JiraTools.instance().isIncidentManager(
//                project.get().getKey(),
//                ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()))) {
//            System.out.println("--- UNAUTHORIZED");
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//
//        Optional<StatusPage> statusPage = StatuspageService.instance().get(project.get().getKey());
//        if (!statusPage.isPresent())
//            return Response.noContent().build();
//
//        return Response.ok(JiraTools.instance().getGsonObject().toJson(statusPage.get().groupComponents(pageId, groupId))).build();
//    }
//
//    @GET
//    @Path("/api/nonGroupComponents")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response nonGroupComponents(
//        @QueryParam("issueKey") String issueKey,
//        @QueryParam("pageId") String pageId,
//        @Context HttpServletRequest request) {
//
//        if (StringUtils.isBlank(pageId))
//            return Response.noContent().build();
//
//        Optional<Project> project = JiraTools.instance().getProjectForIssue(issueKey);
//        if (!project.isPresent())
//            return Response.noContent().build();
//
//        if (!JiraTools.instance().isIncidentManager(
//                project.get().getKey(),
//                ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()))) {
//            System.out.println("--- UNAUTHORIZED");
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//
//        Optional<StatusPage> statusPage = StatuspageService.instance().get(project.get().getKey());
//        if (!statusPage.isPresent())
//            return Response.noContent().build();
//
//        return Response.ok(JiraTools.instance().getGsonObject().toJson(
//            statusPage
//                .get()
//                .nonGroupComponents(pageId)
//                .stream()
//                .filter(c -> !c.group())
//                .collect(Collectors.toList())
//            )
//        ).build();
//    }
//
//    @GET
//    @Path("/api/incidents")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response incidents(
//        @QueryParam("issueKey") String issueKey,
//        @QueryParam("pageId") String pageId,
//        @DefaultValue("false") @QueryParam("activeOnly") boolean activeOnly,
//        @Context HttpServletRequest request) {
//
//        if (StringUtils.isBlank(pageId))
//            return Response.noContent().build();
//
//        Optional<Project> project = JiraTools.instance().getProjectForIssue(issueKey);
//        if (!project.isPresent())
//            return Response.noContent().build();
//
//        if (!JiraTools.instance().isIncidentManager(
//                project.get().getKey(),
//                ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()))) {
//            System.out.println("--- UNAUTHORIZED");
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }
//
//        Optional<StatusPage> statusPage = StatuspageService.instance().get(project.get().getKey());
//        if (!statusPage.isPresent())
//            return Response.noContent().build();
//
//        if (activeOnly) {
//            return Response.ok(JiraTools.instance().getGsonObject().toJson(
//                statusPage.get().incidents(pageId)
//                    .stream()
//                    .filter(
//                        i -> i.status() != IncidentStatus.RESOLVED
//                          && i.status() != IncidentStatus.COMPLETED
//                    ).collect(Collectors.toList())
//            )).build();
//        } else {
//            return Response.ok(JiraTools.instance().getGsonObject().toJson(statusPage.get().incidents(pageId))).build();
//        }
//    }
//
//    @GET
//    @Path("/api/incident/{issueKey}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response incident(
//            @PathParam("issueKey") String issueKey,
//            @Context HttpServletRequest request) {
//
//        Optional<Issue> issue = JiraTools.instance().getIssueByKey(issueKey);
//        if (!issue.isPresent())
//            return Response.status(HttpStatus.SC_NOT_FOUND)/*.noContent()*/.build();
//
//        Optional<IssueIncident> issueIncident = JiraTools.instance().issueIncident(issue.get());
//        if (!issueIncident.isPresent())
//            return Response.status(HttpStatus.SC_NOT_FOUND)/*.noContent()*/.build();
//
//        if (!JiraTools.instance().isIncidentViewer(
//                issueIncident.get().projectKey(),
//                ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()))
//            return Response.status(HttpStatus.SC_FORBIDDEN).build();
//
//        Optional<Incident> incident = StatuspageService.instance().getIncident(issueIncident.get(), true);
//        if (!incident.isPresent())
//            return Response.status(HttpStatus.SC_NOT_FOUND)/*noContent()*/.build();
//
//        return Response
//            .ok(JiraTools.instance().getGsonObject().toJson(incident.get()))
//            .build()
//        ;
//    }
//
//
//    @PUT
//    @Path("/api/incident")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response updateIncident(final IncidentUpdateParams incidentUpdateParams, @Context HttpServletRequest request) {
////        System.out.println("----> SAVING INCIDENT: " + incidentUpdateParams);
//
//        AtomicInteger resultCode              = new AtomicInteger(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
//        AtomicBoolean resultStatus            = new AtomicBoolean(false);
//        AtomicReference<String> resultMessage = new AtomicReference<>("");
//
//        String messageEscaped =
//            (StringUtils.isNotBlank(incidentUpdateParams.getMessage()))
//            ? org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(incidentUpdateParams.getMessage())
//            : Common.getDefaultStatusMessage(IncidentStatus.of(incidentUpdateParams.getStatus()));
//
//        try {
//            StatuspageService.instance().updateIncident(incidentUpdateParams, messageEscaped);
//            resultStatus.set(true);
//            resultCode.set(Response.Status.OK.getStatusCode());
//        } catch (IncidentNotFound e) {
//            resultCode.set(Response.Status.NOT_FOUND.getStatusCode());
//            resultMessage.set(e.getMessage());
//        } catch (StatusPageObjectNotFound e) {
//            resultCode.set(Response.Status.NOT_FOUND.getStatusCode());
//            resultMessage.set(e.getMessage());
//        } catch (ServiceCallException e) {
//            resultMessage.set("could not update incident<br>please, try again in several seconds");
////            resultMessage.set("StatusPage service error:<br>"+ e.getMessage());
//        } catch (StatusPageException e) {
//            resultMessage.set("StatusPage API error:<br>" + e.getMessage());
//        } catch (Exception e) {
//            resultMessage.set(e.getMessage());
//        }
//
////        System.out.println("-- rest: " + resultCode.get() + " " + resultStatus.get() + " " + resultMessage.get());
//
//        if (resultStatus.get()) {
//            if (incidentUpdateParams.publishComment && StringUtils.isNotBlank(messageEscaped)) {
//                try {
//                    CommentManager commentManager = ComponentAccessor.getCommentManager();
//                    commentManager.create(
//                        ComponentAccessor.getIssueManager().getIssueByKeyIgnoreCase(incidentUpdateParams.issue),
//                        ComponentAccessor.getUserManager().getUserByName(request.getRemoteUser()),
//                        messageEscaped,
//                        true
//                    );
//                } catch (Exception e) {
//                    System.err.println("could not publish message comment to Jira: " + e.getClass().getSimpleName() + " : " + e.getMessage());
//                }
//            }
//            return Response.ok().build();
//        } else {
//            return Response.status(resultCode.get()).entity(resultMessage.get()).build();
//        }
//    }
//
//    @GET
//    @Path("/api/impacts")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response impacts(@Context HttpServletRequest request) {
//        return Response.ok(JiraTools.instance().getGsonObject().toJson(
//            Arrays.stream(IncidentSeverity.values())
//                .sorted(Comparator.comparing(IncidentSeverity::id))
//                .map(IncidentSeverity::value)
//                .collect(Collectors.toList())
//        )).build();
//    }
//    @GET
//    @Path("/api/component/statuses")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response componentsStatuses(@Context HttpServletRequest request) {
//        return Response.ok(JiraTools.instance().getGsonObject().toJson(
//            Arrays.stream(ComponentStatus.values())
//                .sorted(Comparator.comparing(ComponentStatus::id))
//                .filter(c -> StringUtils.isNotBlank(c.value()))
//                .map(AffectedComponentStatus::of)
//                .collect(Collectors.toList())
//        )).build();
//    }
//
//    private List<Project> getConfiguredProjects(String sourceStr) {
//        return Arrays.asList(sourceStr.split(","))
//            .stream()
//            .map(String::trim)
//            .distinct()
//            .map(JiraTools.instance()::getProjectByKey)
//            .filter(Objects::nonNull)
//            .collect(Collectors.toList())
//        ;
//    }
