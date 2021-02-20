package ws.slink.cp.service;

import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//@Named("configService")
//@ExportAsService({ConfigService.class})
@Scanned
@ExportAsService
@JiraComponent
public class ConfigServiceImpl implements ConfigService {

    public static final String CONFIG_PREFIX                  = "ws.slink.customer-priority-plugin";
    public static final String CONFIG_ADMIN_PROJECTS          = "admin.projects";
    public static final String CONFIG_ADMIN_ROLES             = "admin.roles";
    public static final String CONFIG_MGMT_ROLES              = "config.mgmt.roles";
    public static final String CONFIG_VIEW_ROLES              = "config.view.roles";
//    public static final String CONFIG_LEVELS                  = "config.levels";

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;
    private final PluginSettings pluginSettings;

    @Inject
    private ConfigServiceImpl(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
        System.out.println("----> created config service");
    }

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


    public String getViewers(String projectKey) {
        return getConfigValue(projectKey, ".viewers");
    }
    public void setViewers(String projectKey, String value) {
        setConfigValue(projectKey, ".viewers", setString(value, "", ""));
    }

    private List<String> getListParam(String param) {
//        System.out.println("----> getListParam: " + param);
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
    }
    private String getParam(String param) {
//        System.out.println("----> getParam: " + param);
        String key = CONFIG_PREFIX + "." + param;
        String value = (String) pluginSettings.get(key);
        if (StringUtils.isBlank(value))
            return "";
        else
            return value;
    }
    private String getConfigValue(String projectKey, String key) {
        String result = (StringUtils.isBlank(projectKey))
            ? (String) pluginSettings.get(CONFIG_PREFIX + "." + key)
            : (String) pluginSettings.get(CONFIG_PREFIX + "." + projectKey + key);
        return StringUtils.isBlank(result) ? "" : result;
    }
    private void setConfigValue(String projectKey, String key, String value) {
        String cfgKey = (StringUtils.isBlank(projectKey))
            ? CONFIG_PREFIX + "." + key
            : CONFIG_PREFIX + "." + projectKey + key;
        pluginSettings.put(cfgKey, value);
    }
    private String setString(String value, String defaultValue, String newLineReplacement) {
        if (null == value || value.isEmpty())
            return defaultValue;
        else
            return value
                .replaceAll(" +", " ")
                .replaceAll(";" , " ")
                .replaceAll("," , " ")
                .replaceAll("\n", newLineReplacement);
    }
}
