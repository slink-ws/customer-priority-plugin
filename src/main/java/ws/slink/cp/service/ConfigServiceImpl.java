package ws.slink.cp.service;

import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import ws.slink.cp.model.StyleElement;
import ws.slink.cp.tools.Common;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
//    public static final String CONFIG_MGMT_ROLES              = "config.roles.view";
//    public static final String CONFIG_VIEW_ROLES              = "config.view.roles";
    public static final String CONFIG_STYLES                  = "config.styles";

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
            : (String) pluginSettings.get(CONFIG_PREFIX + "." + projectKey + "." + key);
        return StringUtils.isBlank(result) ? "" : result;
    }
    private void setConfigValue(String projectKey, String key, String value) {
        String cfgKey = (StringUtils.isBlank(projectKey))
            ? CONFIG_PREFIX + "." + key
            : CONFIG_PREFIX + "." + projectKey + "." + key;
//        System.out.println("----> set config key " + cfgKey + " to " + value);
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

    public List<StyleElement> getStyles(String projectKey) {
        String stylesStr = getConfigValue(projectKey, CONFIG_STYLES);
        if (StringUtils.isBlank(stylesStr)) {
            return Collections.emptyList();
        } else {
            List<StyleElement> result = Common.instance().getGsonObject().fromJson(stylesStr, new TypeToken<ArrayList<StyleElement>>(){}.getType());
            return result;
        }
    }
    public Optional<StyleElement> getStyle(String projectKey, String styleId) {
        return getStyles(projectKey)
            .stream()
            .filter(s -> StringUtils.isNotBlank(s.id()) && s.id().equals(styleId))
            .findFirst()
        ;
    }
    public boolean addStyle(String projectKey, StyleElement style) {
        List<StyleElement> existingStyles = new ArrayList<>(getStyles(projectKey));
        if (StringUtils.isNotBlank(style.id())) {
            if (!existingStyles.isEmpty()) {
                if(existingStyles.stream().filter(s -> StringUtils.isNotBlank(s.id()) && s.id().equals(style.id())).count() > 0) {
                    return false;
                }
            }
        } else {
            style.id(RandomStringUtils.random(10, true, true));
        }
        existingStyles.add(style);
        setStyles(projectKey, existingStyles);
        return true;
    }
    public boolean removeStyle(String projectKey, String styleId) {
        List<StyleElement> styles = getStyles(projectKey);
        Optional<StyleElement> styleToRemove = styles.stream().filter(s -> StringUtils.isNotBlank(s.id()) && s.id().equals(styleId)).findAny();
        if (!styleToRemove.isPresent())
            return false;
        styles.remove(styleToRemove.get());
        setStyles(projectKey, styles);
        return true;
    }
    public void setStyles(String projectKey, List<StyleElement> styles) {
        if (null == styles || styles.isEmpty()) {
            setConfigValue(projectKey, CONFIG_STYLES, null);
        } else {
            String stylesStr = Common.instance().getGsonObject().toJson(styles);
            setConfigValue(projectKey, CONFIG_STYLES, stylesStr);
        }
    }
    public boolean updateStyle(String projectKey, StyleElement style) {
        if (null != style) {
            Optional<StyleElement> found = getStyle(projectKey, style.id());
            if (found.isPresent())
                if (removeStyle(projectKey, style.id()))
                    if (addStyle(projectKey, style))
                        return true;
        }
        return false;
    }
    public boolean addReporter(String projectKey, String styleId, String reporter) {
        AtomicBoolean result = new AtomicBoolean(false);
        getStyle(projectKey, styleId).ifPresent(found -> {
            found.reporters().add(reporter);
            result.set(updateStyle(projectKey, found));
        });
        return result.get();
    }
    public boolean removeReporter(String projectKey, String styleId, String reporter) {
        AtomicBoolean result = new AtomicBoolean(false);
        getStyle(projectKey, styleId).ifPresent(found -> {
            found.reporters().remove(reporter);
            result.set(updateStyle(projectKey, found));
        });
        return result.get();
    }

}

//    public Collection<String> getConfigMgmtRoles(String projectKey) {
//        return getListParam(CONFIG_MGMT_ROLES + "." + projectKey);
//    }
//    public void setConfigMgmtRoles(String projectKey, String roles) {
//        pluginSettings.put(CONFIG_PREFIX + "." + CONFIG_MGMT_ROLES + "." + projectKey, roles);
//    }
//    public Collection<String> getConfigViewRoles(String projectKey) {
//        return getListParam(CONFIG_VIEW_ROLES + "." + projectKey);
//    }
//    public void setConfigViewRoles(String projectKey, String roles) {
//        pluginSettings.put(CONFIG_PREFIX + "." + CONFIG_VIEW_ROLES + "." + projectKey, roles);
//    }
