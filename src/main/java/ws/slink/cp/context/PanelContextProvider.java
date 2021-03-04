package ws.slink.cp.context;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import org.apache.commons.lang.StringUtils;
import ws.slink.cp.model.StyleElement;
import ws.slink.cp.model.StyledElement;
import ws.slink.cp.service.ConfigService;
import ws.slink.cp.service.CustomerPriorityService;
import ws.slink.cp.service.JiraToolsService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Scanned
public class PanelContextProvider extends AbstractJiraContextProvider {

    private final static String PARTICIPANTS_BALLOON_STYLE =
        "padding: 3px; " +
        "overflow-wrap: break-word; " +
        "background-color: lightgrey; " +
        "border-radius: 10px; " +
        "width: 90%; " +
        "margin: 0 auto; " +
        "text-align: center; " +
        "color: #8b0000; " +
        "font-weight: bolder;";

    private ConfigService configService;
    private CustomerPriorityService customerPriorityService;
    private JiraToolsService jiraToolsService;

    @Inject
    public PanelContextProvider(ConfigService configService, CustomerPriorityService customerPriorityService, JiraToolsService jiraToolsService) {
        this.configService = configService;
        this.customerPriorityService = customerPriorityService;
        this.jiraToolsService = jiraToolsService;
    }

    @Override
    public Map getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");

        Optional<StyleElement> reporterStyle =
            customerPriorityService.getStyleElement(jiraHelper.getProject().getKey(), currentIssue.getReporter().getEmailAddress());
        if (reporterStyle.isPresent()) {
            if (StringUtils.isNotBlank(reporterStyle.get().style(StyledElement.GLANCE)))
                contextMap.put("panelStyle", reporterStyle.get().style(StyledElement.GLANCE));
            else
                contextMap.put("panelStyle", "");
            if (StringUtils.isNotBlank(reporterStyle.get().text()))
                contextMap.put("panelText", reporterStyle.get().text());
            else
                contextMap.put("panelText", "");
        } else {
            getParticipantsStyle(currentIssue).ifPresent(pStyle -> {
                contextMap.put("panelText"        , pStyle.text());
                contextMap.put("panelStyle"       , pStyle.style(StyledElement.GLANCE));
                contextMap.put("participantsStyle", PARTICIPANTS_BALLOON_STYLE);
            });
        }

        contextMap.put("issueKey", currentIssue.getKey());
        // System.out.println("----> panel context: " + contextMap);
        return contextMap;
    }

    private Optional<StyleElement> getParticipantsStyle(Issue issue) {
        for (StyleElement styleElement : configService.getStyles(issue.getProjectObject().getKey())) {
            for (String userEmail : jiraToolsService.getIssueParticipants(issue).stream().map(u -> u.getEmailAddress()).collect(Collectors.toList())) {
                if (styleElement.reporters().contains(userEmail))
                    return Optional.of(styleElement);
            }
        }
        return Optional.empty();
    }

}
