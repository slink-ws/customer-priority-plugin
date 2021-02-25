package ws.slink.cp.service.impl;

import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import ws.slink.cp.model.StyleElement;
import ws.slink.cp.model.StyledElement;
import ws.slink.cp.service.ConfigService;
import ws.slink.cp.service.CustomerPriorityService;

import javax.inject.Inject;
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
    public Optional<StyleElement> getStyleElement(String projectKey, String customerId) {
        for (StyleElement styleElement : configService.getStyles(projectKey)) {
            for (String reporter : styleElement.reporters()) {
                // exact match first
                if (customerId.toLowerCase().equalsIgnoreCase(reporter))
                    return Optional.of(styleElement);
                // wildcard match next
                else if (customerId.toLowerCase().contains(reporter.replaceAll("\\*", "").toLowerCase()))
                    return Optional.of(styleElement);
                // any match last
                else if (reporter.equals("*"))
                    return Optional.of(styleElement);
            }
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

}
