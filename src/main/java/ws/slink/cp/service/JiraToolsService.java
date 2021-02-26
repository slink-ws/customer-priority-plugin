package ws.slink.cp.service;


import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.sal.api.user.UserProfile;

import java.util.Collection;
import java.util.List;

public interface JiraToolsService {

    ApplicationUser getLoggedInUser();
    Project getProjectByKey(String projectKey);
    Project getCurrentProject();

    List<ApplicationUser> getIssueParticipants(Issue issue);

    boolean isPluginManager(UserProfile user);
    boolean isPluginManager(ApplicationUser user);
    boolean isViewer(String projectKey, ApplicationUser applicationUser);

    boolean templateMatch(Collection<String> templates, String value);
}
