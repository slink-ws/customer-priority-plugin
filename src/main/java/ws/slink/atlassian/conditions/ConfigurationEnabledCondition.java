package ws.slink.atlassian.conditions;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import ws.slink.atlassian.service.PluginConfigService;
import ws.slink.atlassian.tools.JiraTools;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigurationEnabledCondition extends AbstractWebCondition {

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        AtomicBoolean allowed = new AtomicBoolean(false);
        List<String> configuredRoles    = new PluginConfigService().configuredRoles();
        List<String> configuredProjects = new PluginConfigService().configuredProjects();
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



//JiraAuthenticationContext jiraAuthenticationContext = ComponentAccessor.getJiraAuthenticationContext();
//        com.atlassian.jira.user.ApplicationUser user = jiraAuthenticationContext.getUser();
//        ProjectRoleService prs = ComponentAccessor.getComponent(ProjectRoleService.class);
//        prs.get


//        ProjectRole projectRole = ComponentAccessor.getComponent(ProjectRoleManager.class).getProjectRole("Administrators");
//        ArrayList<String> actors = new ArrayList<String>(Arrays.asList("replace by username"));
//        ApplicationUser adminUser = ComponentAccessor.getUserManager().getUserByName("replace by admin username");
//        SimpleErrorCollection simpleErrorCollection = new SimpleErrorCollection();

//        ProjectRoleService projectRoleService = ComponentAccessor.getComponent(ProjectRoleService.class);
//        projectRoleService.getProjectRoleActors().addActorsToProjectRole(adminUser, (Collection<String>)actors, projectRole, project, "atlassian-user-role-actor", simpleErrorCollection);
//        if (simpleErrorCollection.hasAnyErrors()){
//            for (String msg: simpleErrorCollection.getErrorMessages()){
//                log.info("add user to project error msg: " + msg);
//            }
//        }

//        System.err.println("~~~~~~ CONFIGURATION ENABLED CONDITION CHECK ~~~~~~");
//        System.err.println("~~~~~~ PROJECT: " + projectId);
//        System.err.println("~~~~~~ USER   : " + userKey);
////        System.err.println("~~~~~~ ROLES  : " + jiraHelper.);
//
//        System.err.println("~~~~~~ USER   : " + userKey);
//        System.err.println("~~~~~~ COMPNTS: ");
//        jiraHelper.getProject().getProjectComponents().stream().forEach(c -> System.err.println(c.getId() + " " + c.getName() + " " + c.getDescription()));
//
//        System.err.println("~~~~~~ COMPNTS: ");
//        jiraHelper.getProject().getComponents().stream().forEach(c -> System.err.println(c.getId() + " " + c.getName() + " " + c.getDescription()));
//
//        System.err.println("~~~~~~ PARAMS: ");
//        jiraHelper.getContextParams().entrySet().stream().forEach(e -> System.err.println(e.getKey() + " -> " + e.getValue()));
//
//        return true;
