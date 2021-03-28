package ws.slink.cp.service.impl;

import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.slink.cp.model.StyleElement;
import ws.slink.cp.service.ConfigService;
import ws.slink.cp.tools.Common;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Scanned
@ExportAsService
@JiraComponent
public class ConfigServiceImpl implements ConfigService {

    private static final Logger log = LoggerFactory.getLogger(ConfigServiceImpl.class);

    public static final String CONFIG_PREFIX                  = "ws.slink.customer-priority-plugin";
    public static final String CONFIG_ADMIN_PROJECTS          = "admin.projects";
    public static final String CONFIG_ADMIN_ROLES             = "admin.roles";
    public static final String CONFIG_ADMIN_FIELD_ID          = "admin.field_id";
    public static final String CONFIG_STYLES                  = "config.styles";
    public static final String CONFIG_VIEWERS                 = "config.viewers";

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;
    private final PluginSettings pluginSettings;

    @Inject
    private ConfigServiceImpl(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.pluginSettings = pluginSettingsFactory.createSettingsForKey(CONFIG_PREFIX);
        log.trace("--- [CUSTOMER PRIORITY] [CONFIG] service instantiated");
    }

    public Collection<String> getAdminProjects() { // returns list of projectKey
        return getListParam(CONFIG_ADMIN_PROJECTS);
    }
    public void setAdminProjects(String projects) {
        setParam(CONFIG_ADMIN_PROJECTS, projects);
    }

    public Collection<String> getAdminRoles() { // returns list of roleId
        return getListParam(CONFIG_ADMIN_ROLES);
    }
    public void setAdminRoles(String roles) {
        setParam(CONFIG_ADMIN_ROLES, roles);
    }

    public String getAdminParticipantsFieldId() {
        String value = getParam(CONFIG_ADMIN_FIELD_ID);
        if (StringUtils.isBlank(value)) {
            return "";
        } else {
            return value;
        }
    }
    public void setAdminParticipantsFieldId(String value) {
        setParam(CONFIG_ADMIN_FIELD_ID, value);
    }

    public List<StyleElement> getStyles(String projectKey) {
        String stylesStr = getParam(CONFIG_STYLES, projectKey);
        if (StringUtils.isBlank(stylesStr)) {
            return Collections.emptyList();
        } else {
            List<StyleElement> result = Common.instance().getGsonObject().fromJson(stylesStr, new TypeToken<ArrayList<StyleElement>>(){}.getType());
            return result.stream().sorted(Comparator.comparing(StyleElement::id)).collect(Collectors.toList());
        }
    }
    public void setStyles(String projectKey, List<StyleElement> styles) {
        if (null == styles || styles.isEmpty()) {
            setParam(CONFIG_STYLES, projectKey,  "");
        } else {
            setParam(CONFIG_STYLES, projectKey,  Common.instance().getGsonObject().toJson(styles));
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

    public Collection<String> getViewers(String projectKey) {
        return getListParam(CONFIG_VIEWERS, projectKey);
    }
    public boolean setViewers(String projectKey, Collection<String> value) {
        setParam(CONFIG_VIEWERS, projectKey, value.stream().collect(Collectors.joining(" ")));
        return true;
    }
    public boolean addViewer(String projectKey, String viewer) {
        Collection<String> currentViewers = getViewers(projectKey);
        if (currentViewers.contains(viewer))
            return false;
        currentViewers.add(viewer);
        setViewers(projectKey, currentViewers);
        return true;
    }
    public boolean removeViewer(String projectKey, String viewer) {
        Collection<String> currentViewers = getViewers(projectKey);
        if (!currentViewers.contains(viewer)) {
            return false;
        }
        currentViewers.remove(viewer);
        setViewers(projectKey, currentViewers);
        return true;
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

    private void setParam(String paramKey, String value) {
        setParam(paramKey, null, value);
    }
    private void setParam(String paramKey, String projectKey, Object value) {
        String key = CONFIG_PREFIX + "." + paramKey;
        if (StringUtils.isNotBlank(projectKey)) {
            key += "." + projectKey;
        }
        log.info("--- [CUSTOMER PRIORITY] [CONFIG] SET KEY {} : {}", key, value);
        pluginSettings.put(key, value);
    }

    private String getParam(String paramKey) {
        return getParam(paramKey, null);
    }
    private String getParam(String paramKey, String projectKey) {
        String key = CONFIG_PREFIX + "." + paramKey;
        if (StringUtils.isNotBlank(projectKey)) {
            key += "." + projectKey;
        }
        String value = (String) pluginSettings.get(key);
        log.info("--- [CUSTOMER PRIORITY] [CONFIG] GET KEY {}.{} : empty", CONFIG_PREFIX, key);
        if (StringUtils.isBlank(value))
            return "";
        else
            return value;
    }

    private List<String> getListParam(String paramKey) {
        return getListParam(paramKey, null);
    }
    private List<String> getListParam(String paramKey, String projectKey) {
        try {
            String value = getParam(paramKey, projectKey);
            if (StringUtils.isBlank(value)) {
                log.info("--- [CUSTOMER PRIORITY] [CONFIG] GET LIST {} : null", paramKey);
                return Collections.EMPTY_LIST;
            } else {
                List<String> result = Arrays.stream(value.split(","))
                    .map(s -> s.trim())
                    .collect(Collectors.toList());
                log.info("--- [CUSTOMER PRIORITY] [CONFIG] GET LIST {} : {}", paramKey, result);
                return result;
            }
        } catch (Exception e) {
            log.info("--- [CUSTOMER PRIORITY] [CONFIG] GET LIST {} : empty", paramKey);
            return Collections.EMPTY_LIST;
        }
    }

}
