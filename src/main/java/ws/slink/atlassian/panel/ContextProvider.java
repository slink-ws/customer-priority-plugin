package ws.slink.atlassian.panel;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import ws.slink.atlassian.service.ConfigService;
import ws.slink.atlassian.service.CustomerLevelService;
import ws.slink.atlassian.tools.DefaultCSS;

import java.util.HashMap;
import java.util.Map;

public class ContextProvider extends AbstractJiraContextProvider {

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    private final CustomerLevelService customerLevelService;

    public ContextProvider(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.customerLevelService = new CustomerLevelService();
        ConfigService.instance().setPluginSettings(pluginSettingsFactory.createGlobalSettings());
    }

    @Override
    public Map getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");
        int reporterLevel = customerLevelService.getLevel(currentIssue.getReporter().getEmailAddress());
        contextMap.put("panelStyle", getPanelStyle(reporterLevel));
        contextMap.put("panelText", getPanelText(reporterLevel));
        return contextMap;
    }
    public String getPanelStyle(int reporterLevel) {
        PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        String style = ConfigService.instance().getStyle(reporterLevel);//(String)settings.get(ConfigResource.Config.class.getName() + ".style" + reporterLevel);
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
    public String getPanelText(int reporterLevel) {
        PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        String text = ConfigService.instance().getText(reporterLevel);//(String)settings.get(ConfigService.Config.class.getName() + ".text" + reporterLevel);
        if (null == text || text.isEmpty())
            return "";
        else
            return text;
    }

}
