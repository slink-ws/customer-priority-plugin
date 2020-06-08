package ws.slink.atlassian.context;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import ws.slink.atlassian.service.ConfigService;
import ws.slink.atlassian.service.CustomerLevelService;
import ws.slink.atlassian.tools.DefaultCSS;

import java.util.HashMap;
import java.util.Map;

public class PanelContextProvider extends AbstractJiraContextProvider {

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    private final CustomerLevelService customerLevelService;

    public PanelContextProvider(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.customerLevelService = new CustomerLevelService();
        ConfigService.instance().setPluginSettings(pluginSettingsFactory.createGlobalSettings());
    }

    @Override
    public Map getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");
        int reporterLevel = customerLevelService.getLevel(jiraHelper.getProject().getKey(), currentIssue.getReporter().getEmailAddress());
        contextMap.put("panelStyle", getPanelStyle(jiraHelper.getProject().getKey(), reporterLevel));
        contextMap.put("panelText", getPanelText(jiraHelper.getProject().getKey(), reporterLevel));
        return contextMap;
    }
    public String getPanelStyle(String projectKey, int reporterLevel) {
        String style = ConfigService.instance().getStyle(projectKey, reporterLevel);
        if (null == style || style.isEmpty())
            switch (reporterLevel) {
                case 1:
                    return DefaultCSS.STYLE_1;
                case 2:
                    return DefaultCSS.STYLE_2;
                case 3:
                    return DefaultCSS.STYLE_3;
                case 4:
                    return DefaultCSS.STYLE_4;
                default:
                    return "";
            }
        else
            return style;
    }
    public String getPanelText(String projectKey, int reporterLevel) {
        String text = ConfigService.instance().getText(projectKey, reporterLevel);
        if (null == text || text.isEmpty())
            return "";
        else
            return text;
    }

}
