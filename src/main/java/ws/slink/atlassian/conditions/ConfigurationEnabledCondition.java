package ws.slink.atlassian.conditions;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import ws.slink.atlassian.service.ConfigService;
import ws.slink.atlassian.tools.JiraTools;

public class ConfigurationEnabledCondition extends AbstractWebCondition {
    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        if (null == jiraHelper.getProject())
            return false;
        else
            return
                JiraTools.isPluginManager(applicationUser)
             && ConfigService.instance().getProjects().contains(jiraHelper.getProject().getKey())
            ;
    }
}