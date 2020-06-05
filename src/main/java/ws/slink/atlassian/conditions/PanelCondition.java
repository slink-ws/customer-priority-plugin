package ws.slink.atlassian.conditions;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import ws.slink.atlassian.service.ConfigService;
import ws.slink.atlassian.service.CustomerLevelService;

import java.util.Arrays;

public class PanelCondition extends AbstractWebCondition {

    @ComponentImport private final PluginSettingsFactory pluginSettingsFactory;

    private final CustomerLevelService customerLevelService;

    public PanelCondition(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.customerLevelService = new CustomerLevelService();
        ConfigService.instance().setPluginSettings(pluginSettingsFactory.createGlobalSettings());
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");
        return
            (null != currentIssue)
            && Arrays.asList(ConfigService.instance().getViewers().split(" "))
                .stream()
                .anyMatch(s -> (s.contains("*")
                            && applicationUser.getEmailAddress().toLowerCase().contains(s.replaceAll("\\*", "").toLowerCase())
                            ||  applicationUser.getEmailAddress().equalsIgnoreCase(s))
            )
            && customerLevelService.getLevel(currentIssue.getReporter().getEmailAddress()) > 0
        ;
    }
}
