package ws.slink.cp.condition;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import ws.slink.cp.service.ConfigService;
import ws.slink.cp.service.JiraToolsService;

import javax.inject.Inject;

@Scanned
public class ConfigurationEnabledCondition extends AbstractWebCondition {

    private ConfigService configService;
    private JiraToolsService jiraToolsService;

    @Inject
    public ConfigurationEnabledCondition(ConfigService configService, JiraToolsService jiraToolsService) {
        this.configService = configService;
        this.jiraToolsService = jiraToolsService;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        if (null == jiraHelper.getProject()) {
//            System.out.println("----> configuration enabled condition: [false]");
            return false;
        }
        else {
            boolean isPluginManager   = jiraToolsService.isPluginManager(applicationUser);
            boolean enabledForProject = configService.getAdminProjects().contains(jiraHelper.getProject().getKey());
            boolean result = isPluginManager && enabledForProject;
            return result;
        }
    }
}