package ws.slink.cp.service.impl;

import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import ws.slink.cp.model.StyleElement;
import ws.slink.cp.model.StyledElement;
import ws.slink.cp.service.ConfigService;
import ws.slink.cp.service.CustomerPriorityService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;

@Scanned
@ExportAsService
@JiraComponent
public class CustomerPriorityServiceImpl implements CustomerPriorityService {

    private ConfigService configService;

    @Inject
    public CustomerPriorityServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public boolean isViewer(String projectKey, String userEmail) {
        return templateMatch(configService.getViewers(projectKey), userEmail);
    }

    @Override
    public Optional<StyleElement> getStyleElement(String projectKey, String customerId) {
        for (StyleElement styleElement : configService.getStyles(projectKey)) {
            if (templateMatch(styleElement.reporters(), customerId))
                return Optional.of(styleElement);
        }
        return Optional.empty();
    }

    @Override
    public String getStyle(String projectKey, String customerId, StyledElement type) {
        Optional<StyleElement> styleElement = getStyleElement(projectKey, customerId);
        if (styleElement.isPresent())
            return styleElement.get().style(type);
        return "";
    }

    @Override
    public String getText(String projectKey, String customerId) {
        Optional<StyleElement> styleElement = getStyleElement(projectKey, customerId);
        if (styleElement.isPresent())
            return styleElement.get().text();
        return "";
    }

    private boolean templateMatch(Collection<String> templates, String value) {
        for (String template : templates) {
            // exact match first
            if (value.equalsIgnoreCase(template))
                return true;
            // wildcard match next
            else if (value.toLowerCase().contains(template.replaceAll("\\*", "").toLowerCase()))
                return true;
            // any match last
            else if (template.equals("*"))
                return true;
        }
        return false;
    }

}
