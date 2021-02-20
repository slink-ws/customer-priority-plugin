package ws.slink.cp.service;

import java.util.Collection;

public interface ConfigService {

    Collection<String> getAdminProjects();
    Collection<String> getAdminRoles();
    void setAdminProjects(String projects);
    void setAdminRoles(String roles);

}
