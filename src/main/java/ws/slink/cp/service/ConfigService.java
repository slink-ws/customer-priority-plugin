package ws.slink.cp.service;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigService {

    private static class PluginConfigServiceSingleton {
        private static final ConfigService INSTANCE = new ConfigService();
    }
    public static ConfigService instance () {
        return PluginConfigServiceSingleton.INSTANCE;
    }

    public static final String CONFIG_PREFIX                  = "ws.slink.customer-priority-plugin";
    public static final String CONFIG_ADMIN_PROJECTS          = "admin.projects";
    public static final String CONFIG_ADMIN_ROLES             = "admin.roles";
    public static final String CONFIG_MGMT_ROLES              = "config.mgmt.roles";
    public static final String CONFIG_VIEW_ROLES              = "config.view.roles";
    public static final String CONFIG_LEVELS                  = "config.levels";

    private PluginSettings pluginSettings;

    private ConfigService() {
        System.out.println("----> created config service");
    }

//    public void setPluginSettings(PluginSettings pluginSettings) {
//        if (null == this.pluginSettings) {
//            System.out.println("----> initialized plugin settings");
//            this.pluginSettings = pluginSettings;
//        }
//    }

    public Collection<String> getAdminProjects() { // returns list of projectKey
        return getListParam(CONFIG_ADMIN_PROJECTS);
    }
    public Collection<String> getAdminRoles() { // returns list of roleId
        return getListParam(CONFIG_ADMIN_ROLES);
    }
    public void setAdminProjects(String projects) {
        pluginSettings.put(CONFIG_PREFIX + "." + CONFIG_ADMIN_PROJECTS, projects);
    }
    public void setAdminRoles(String roles) {
        pluginSettings.put(CONFIG_PREFIX + "." + CONFIG_ADMIN_ROLES, roles);
    }

    public Collection<String> getConfigMgmtRoles(String projectKey) {
        return getListParam(CONFIG_MGMT_ROLES + "." + projectKey);
    }
    public void setConfigMgmtRoles(String projectKey, String roles) {
        pluginSettings.put(CONFIG_PREFIX + "." + CONFIG_MGMT_ROLES + "." + projectKey, roles);
    }
    public Collection<String> getConfigViewRoles(String projectKey) {
        return getListParam(CONFIG_VIEW_ROLES + "." + projectKey);
    }
    public void setConfigViewRoles(String projectKey, String roles) {
        pluginSettings.put(CONFIG_PREFIX + "." + CONFIG_VIEW_ROLES + "." + projectKey, roles);
    }


    private List<String> getListParam(String param) {
        System.out.println("----> getListParam: " + param);
        return Collections.emptyList();
        /*
        try {
            Object value = pluginSettings.get(CONFIG_PREFIX + "." + param);
            if (null == value || StringUtils.isBlank(value.toString())) {
                return Collections.EMPTY_LIST;
            } else {
                return Arrays.stream(value.toString().split(",")).map(s -> s.trim()).collect(Collectors.toList());
            }
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
        */
    }
    private String getParam(String param) {
        System.out.println("----> getParam: " + param);
        return "";
        /*
        String key = CONFIG_PREFIX + "." + param;
        String value = (String) pluginSettings.get(key);
//        System.out.println("--- GET " + key + " : " + value);
        if (StringUtils.isBlank(value))
            return "";
        else
            return value;
        */
    }

}
