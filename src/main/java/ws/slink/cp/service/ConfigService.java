package ws.slink.cp.service;

import java.util.Collection;

public interface ConfigService {

    Collection<String> getAdminProjects();
    void setAdminProjects(String projects);

    Collection<String> getAdminRoles();
    void setAdminRoles(String roles);

    Collection<String> getConfigMgmtRoles(String projectKey);
    void setConfigMgmtRoles(String projectKey, String roles);

    Collection<String> getConfigViewRoles(String projectKey);
    void setConfigViewRoles(String projectKey, String roles);

    String getViewers(String projectKey);
    void setViewers(String projectKey, String value);

}
