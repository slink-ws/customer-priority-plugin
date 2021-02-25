package ws.slink.cp.service;

import ws.slink.cp.model.StyleElement;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ConfigService {

    Collection<String> getAdminProjects();

    void setAdminProjects(String projects);

    Collection<String> getAdminRoles();

    void setAdminRoles(String roles);

    Collection<String> getViewers(String projectKey);
    boolean setViewers(String projectKey, Collection<String> value);
    boolean addViewer(String projectKey, String viewer);
    boolean removeViewer(String projectKey, String viewer);

    List<StyleElement> getStyles(String projectKey);
    Optional<StyleElement> getStyle(String projectKey, String styleId);
    boolean removeStyle(String projectKey, String styleId);
    boolean addStyle(String projectKey, StyleElement style);
    void setStyles(String projectKey, List<StyleElement> styles);
    boolean updateStyle(String projectKey, StyleElement style);

    boolean addReporter(String projectKey, String styleId, String reporter);
    boolean removeReporter(String projectKey, String styleId, String reporter);
}

//    Collection<String> getConfigMgmtRoles(String projectKey);
//    void setConfigMgmtRoles(String projectKey, String roles);

//    Collection<String> getConfigViewRoles(String projectKey);
//    void setConfigViewRoles(String projectKey, String roles);
