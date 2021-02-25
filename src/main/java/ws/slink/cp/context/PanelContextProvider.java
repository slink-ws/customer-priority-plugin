package ws.slink.cp.context;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import org.apache.commons.lang.StringUtils;
import ws.slink.cp.model.StyledElement;
import ws.slink.cp.service.CustomerPriorityService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Scanned
public class PanelContextProvider extends AbstractJiraContextProvider {

    private CustomerPriorityService customerPriorityService;

    @Inject
    public PanelContextProvider(CustomerPriorityService customerPriorityService) {
        this.customerPriorityService = customerPriorityService;
    }

    @Override
    public Map getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");
        String style = customerPriorityService.getStyle(jiraHelper.getProject().getKey(), currentIssue.getReporter().getEmailAddress(), StyledElement.GLANCE);
        if (StringUtils.isNotBlank(style))
            contextMap.put("panelStyle", style);
        String text = customerPriorityService.getText(jiraHelper.getProject().getKey(), currentIssue.getReporter().getEmailAddress());
        if (StringUtils.isNotBlank(text))
            contextMap.put("panelText", text);
        contextMap.put("issueKey", currentIssue.getKey());
        // System.out.println("----> panel context: " + contextMap);
        return contextMap;
    }


}
