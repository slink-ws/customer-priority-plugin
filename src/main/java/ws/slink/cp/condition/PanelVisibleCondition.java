package ws.slink.cp.condition;

import com.atlassian.jira.plugin.webfragment.conditions.AbstractWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import ws.slink.cp.service.CustomerPriorityService;

@Scanned
public class PanelVisibleCondition extends AbstractWebCondition {

    private final CustomerPriorityService customerPriorityService;

    public PanelVisibleCondition(CustomerPriorityService customerPriorityService) {
        this.customerPriorityService = customerPriorityService;
    }

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        return customerPriorityService.isViewer(jiraHelper.getProject().getKey(), applicationUser.getEmailAddress());
    }

}
