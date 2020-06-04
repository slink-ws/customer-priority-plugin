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
import java.util.concurrent.atomic.AtomicBoolean;

public class PanelCondition extends AbstractWebCondition {

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    private final CustomerLevelService customerLevelService;

    public PanelCondition(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.customerLevelService = new CustomerLevelService();
        ConfigService.instance().setPluginSettings(pluginSettingsFactory.createGlobalSettings());
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        AtomicBoolean result = new AtomicBoolean(/*false*/ true);
//        String list = ConfigService.instance().getViewers();//(String)settings.get(ConfigResource.Config.class.getName() + ".viewers"));//.replaceAll("[*]", "@");
//        Arrays.asList(list.split(";"))
//            .stream()
//            .forEach(s -> {
//                if (s.contains("*") && applicationUser.getEmailAddress().contains(s.replaceAll("[*]", ""))
//                ||  applicationUser.getEmailAddress().equalsIgnoreCase(s)) {
//                    result.set(true);
//                }
//            });
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");
        return result.get()
//           &&  customerLevelService.getLevel(currentIssue.getReporter().getEmailAddress()) > 0
//           && !customerLevelService.isCustom(applicationUser.getEmailAddress())
        ;
    }

}
