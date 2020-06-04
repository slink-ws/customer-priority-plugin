package ws.slink.atlassian.service;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PluginConfigService {

    private static class PluginConfigServiceSingleton {
        private static final PluginConfigService INSTANCE = new PluginConfigService();
    }
    public static PluginConfigService instance () {
        return PluginConfigServiceSingleton.INSTANCE;
    }

    public static final String CONFIG_PREFIX = "ws.slink.customer-priority-plugin";
    private PluginSettings pluginSettings;

    private PluginConfigService(){}

    public void setPluginSettings(PluginSettings pluginSettings) {
        if (null == this.pluginSettings)
            this.pluginSettings = pluginSettings;
    }

    public void setRoles(String roles) {
        pluginSettings.put(CONFIG_PREFIX + ".roles", roles);
    }
    public void setProjects(String projects) {
        pluginSettings.put(CONFIG_PREFIX + ".projects", projects);
    }
    public String getRoles() {
        return (String) pluginSettings.get(CONFIG_PREFIX + ".roles");
    }
    public String getProjects() {
        return (String) pluginSettings.get(CONFIG_PREFIX + ".projects");
    }
    public List<String> rolesList() {
        return getListParam("roles");
    }
    public List<String> projectsList() {
        return getListParam("projects");
    }
    private List<String> getListParam(String param) {
        String value = (String) pluginSettings.get(CONFIG_PREFIX + "." + param);
        if (StringUtils.isBlank(value))
            return Collections.EMPTY_LIST;
        else
            return Arrays.stream(value.split(",")).map(s -> s.trim()).collect(Collectors.toList());
    }

}
