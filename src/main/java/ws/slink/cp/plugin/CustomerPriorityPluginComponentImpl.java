package ws.slink.cp.plugin;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService ({CustomerPriorityPluginComponent.class})
@Named ("customer-priority-plugin")
public class CustomerPriorityPluginComponentImpl implements CustomerPriorityPluginComponent {

    private static final String PLUGIN_KEY = "customer-priority-plugin";

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public CustomerPriorityPluginComponentImpl(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getName() {
        if(null != applicationProperties)
            return PLUGIN_KEY + ": " + applicationProperties.getDisplayName();
        return PLUGIN_KEY;
    }
}