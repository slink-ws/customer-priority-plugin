package ws.slink.atlassian.service;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
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

    public static final String CONFIG_PREFIX = "ws.slink.customer-priority-plugin";
    private PluginSettings pluginSettings;

    private ConfigService(){}

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

    public String getList(int id) {
        return (String) pluginSettings.get(CONFIG_PREFIX + ".list" + id);
    }
    public void setList(int id, String value) {
        pluginSettings.put(CONFIG_PREFIX + ".list" + id, setString(value, "", ""));
    }
    public String getStyle(int id) {
        return (String) pluginSettings.get(CONFIG_PREFIX + ".style" + id);
    }
    public void setStyle(int id, String value) {
        pluginSettings.put(CONFIG_PREFIX + ".style" + id, value);
    }
    public String getText(int id) {
        return (String) pluginSettings.get(CONFIG_PREFIX + ".text" + id);
    }
    public void setText(int id, String value) {
        pluginSettings.put(CONFIG_PREFIX + ".text" + id, setString(value, "", ""));
    }
    public String getViewers() {
        return (String) pluginSettings.get(CONFIG_PREFIX + ".viewers");
    }
    public void setViewers(String value) {
        pluginSettings.put(CONFIG_PREFIX + ".viewers", setString(value, "", ""));
    }

    public String getColor(int id) {
        String color = (String) pluginSettings.get(CONFIG_PREFIX + ".color" + id);
        return StringUtils.isNotBlank(color) ? color : "";
    }
    public void setColor(int id, String value) {
        pluginSettings.put(CONFIG_PREFIX + ".color" + id, value);
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
