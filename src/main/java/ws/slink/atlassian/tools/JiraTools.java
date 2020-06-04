package ws.slink.atlassian.tools;

import com.atlassian.jira.bc.projectroles.ProjectRoleService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.SimpleErrorCollection;

public class JiraTools {

    public static boolean userHasRoleInProject(Project project, ApplicationUser user, ProjectRole role) {
        try {
            SimpleErrorCollection simpleErrorCollection = new SimpleErrorCollection();
            ProjectRoleService projectRoleService = ComponentAccessor.getComponent(ProjectRoleService.class);
            boolean contains = projectRoleService.getProjectRoleActors(role, project, simpleErrorCollection).contains(user);
            System.err.println(project.getKey() + " : " + user.getDisplayName() + " : " + role.getName() + " => " + contains);
            return contains;
        } catch (Exception e) {
            System.err.println("ERROR QUERYING USER ROLE: " + e.getMessage());
            return false;
        }
    }

    public static ProjectRole getProjectRoleByKey(String roleKey) {
        try {
            ProjectRole projectRole = ComponentAccessor.getComponent(ProjectRoleManager.class).getProjectRole(roleKey);
            return projectRole;
        } catch (Exception e) {
            System.err.println("ERROR CONVERTING ROLE KEY TO PROJECT ROLE: " + e.getMessage());
            return null;
        }
    }

}
