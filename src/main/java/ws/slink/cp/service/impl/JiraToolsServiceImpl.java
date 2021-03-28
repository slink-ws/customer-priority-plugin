package ws.slink.cp.service.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.ProjectActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.sal.api.user.UserProfile;
import org.apache.commons.lang.StringUtils;
import ws.slink.cp.service.ConfigService;
import ws.slink.cp.service.CustomerPriorityService;
import ws.slink.cp.service.JiraToolsService;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Scanned
@ExportAsService
@JiraComponent
public class JiraToolsServiceImpl implements JiraToolsService {

    private final ConfigService configService;

    @Inject
    public JiraToolsServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    public boolean isPluginManager(UserProfile user) {
        return isPluginManager(user.getUsername());
    }
    public boolean isPluginManager(ApplicationUser user) {
        return isPluginManager(user.getUsername());
    }
    private boolean isPluginManager(String user) {
        Project currentProject = ComponentAccessor.getProjectManager().getProjectObj(new ProjectActionSupport().getSelectedProjectId());
        boolean canManage = (null != currentProject
                && configService.getAdminProjects().contains(currentProject.getKey())
                && userHasRoles(
                currentProject,
                configService.getAdminRoles()
                    .stream()
                    .mapToLong(v -> Long.valueOf(v))
                    .mapToObj(this::getProjectRoleById).filter(Objects::nonNull).collect(Collectors.toList()),
                ComponentAccessor.getUserManager().getUserByName(user))
        );
//        System.out.println(user + ((canManage) ? " can " : " can't ") + "manage " + currentProject.getKey());
        return canManage;
    }

    public boolean isViewer(String projectKey, ApplicationUser applicationUser) {
        return templateMatch(configService.getViewers(projectKey), applicationUser.getEmailAddress());
//        return customerPriorityService.isViewer(projectKey, applicationUser.getEmailAddress());
//        boolean result =
//            // TODO: add support for "*" to allow views for all ?
//            Arrays.asList(configService.getViewers(projectKey).trim().split(" "))
//                .stream()
//                .anyMatch(s -> ((s.contains("*")
//                    && applicationUser.getEmailAddress().toLowerCase().contains(s.replaceAll("\\*", "").toLowerCase()))
//                    || applicationUser.getEmailAddress().equalsIgnoreCase(s))
//                );
//        return result;
    }

    public boolean userHasRolesInProjects(Collection<Project> projects, Collection<ProjectRole> roles, ApplicationUser user) {
        return projects.stream().filter(Objects::nonNull).anyMatch(p -> roles.stream().filter(Objects::nonNull).anyMatch(r -> userHasRoleInProject(p, user, r)));
    }
    public boolean userHasRoles(Project project, List<ProjectRole> roles, ApplicationUser user) {
        return userHasRolesInProjects(Arrays.asList(project), roles, user);
    }
    public boolean userHasRoleInProject(Project project, ApplicationUser user, ProjectRole role) {
        ProjectRoleManager projectRoleManager = ComponentAccessor.getComponentOfType(ProjectRoleManager.class);
        boolean result = projectRoleManager.isUserInProjectRole(user, role, project);
        // System.out.println(user.getUsername() + " : " + project.getKey() + " : " + role.getName() + " => " + result);
        return result;
    }
    public ProjectRole getProjectRoleById(long roleKey) {
        try {
            ProjectRole projectRole = ComponentAccessor.getComponent(ProjectRoleManager.class).getProjectRole(roleKey);
//            System.out.println("----> project role for " + roleKey + ": " + projectRole);
            return projectRole;
        } catch (Exception e) {
//             System.err.println("ERROR CONVERTING ROLE KEY TO PROJECT ROLE: " + e.getMessage());
            return null;
        }
    }
    public List<ApplicationUser> getIssueParticipants(Issue issue) {
        List<ApplicationUser> result = new ArrayList<>();
        String fieldId = configService.getAdminParticipantsFieldId();
        if (StringUtils.isNotBlank(fieldId )) {
            CustomField customField = null;
            try {
                customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(Long.valueOf(fieldId));
            } catch (NumberFormatException e) {
                customField = ComponentAccessor.getCustomFieldManager().getCustomFieldObject(fieldId);
            }
            if (null != customField) {
                Object object = issue.getCustomFieldValue(customField);
                if (null != object && object instanceof ArrayList) {
                    result.addAll((List)object);
                }
            }
        };
        return result;
    }


    public Project getCurrentProject() {
        try {
            return ComponentAccessor.getProjectManager().getProjectObj(new ProjectActionSupport().getSelectedProjectId());
        } catch (Exception e) {
            return null;
        }
    }
    public Project getProjectByKey(String projectKey) {
        try {
            return ComponentAccessor.getProjectManager().getProjectByCurrentKey(projectKey);
        } catch (Exception e) {
            // System.err.println("ERROR CONVERTING PROJECT KEY TO PROJECT: " + e.getMessage());
            return null;
        }
    }
    public ApplicationUser getLoggedInUser() {
        JiraAuthenticationContext jiraAuthenticationContext = ComponentAccessor.getJiraAuthenticationContext();
        return jiraAuthenticationContext.getLoggedInUser();
    }

    @Override
    public boolean templateMatch(Collection<String> templates, String value) {
        for (String template : templates) {
            // exact match first
            if (value.equalsIgnoreCase(template))
                return true;
                // wildcard match next
            else if (value.toLowerCase().contains(template.replaceAll("\\*", "").toLowerCase()))
                return true;
                // any match last
            else if (template.equals("*"))
                return true;
        }
        return false;
    }

}
