package ws.slink.atlassian.conditions;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import ws.slink.atlassian.service.ConfigService;
import ws.slink.atlassian.tools.JiraTools;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigurationEnabledCondition extends AbstractWebCondition {

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        AtomicBoolean allowed = new AtomicBoolean(false);
        List<String> configuredRoles    = ConfigService.instance().rolesList();
        List<String> configuredProjects = ConfigService.instance().projectsList();
        if (configuredRoles.isEmpty()) {
            if (configuredProjects.isEmpty()) {
                System.out.println("allow for all roles and projects");
                return true;
            } else {
                boolean allow = configuredProjects.contains(jiraHelper.getProject().getKey());
                if (allow)
                    System.out.println("allow for all roles in '" + jiraHelper.getProject().getKey() + "'");
                else
                    System.out.println("deny for all roles in '" + jiraHelper.getProject().getKey() + "'");
                return allow;
            }
        } else if (configuredProjects.isEmpty()){
            configuredRoles
                .stream()
                .map(JiraTools::getProjectRoleByKey)
                .filter(Objects::nonNull)
                .forEach(role -> {
                    if (!allowed.get())
                        allowed.set(JiraTools.userHasRoleInProject(jiraHelper.getProject(), applicationUser, role));
                    }
                )
            ;
            if (allowed.get())
                System.out.println("allow for configured roles in all projects");
            return allowed.get();
        } else {
            if (configuredProjects.contains(jiraHelper.getProject().getKey())) {
                System.out.println("allow for configured roles / projects");
                configuredRoles
                    .stream()
                    .map(JiraTools::getProjectRoleByKey)
                    .filter(Objects::nonNull)
                    .forEach(role -> allowed.set(allowed.get() || JiraTools.userHasRoleInProject(jiraHelper.getProject(), applicationUser, role)))
                ;
                return allowed.get();
            } else {
                System.out.println("deny for all in '" + jiraHelper.getProject().getKey() + "'");
                return false;
            }
        }
    }
}
