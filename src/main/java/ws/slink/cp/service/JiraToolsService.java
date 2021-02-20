package ws.slink.cp.service;


import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.sal.api.user.UserProfile;

public interface JiraToolsService {

    ApplicationUser getLoggedInUser();
    Project getProjectByKey(String projectKey);
    Project getCurrentProject();

    boolean isPluginManager(UserProfile user);
    boolean isPluginManager(ApplicationUser user);

}
