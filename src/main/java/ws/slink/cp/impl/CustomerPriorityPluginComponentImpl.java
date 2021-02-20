package ws.slink.cp.impl;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import ws.slink.cp.api.CustomerPriorityPluginComponent;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService ({CustomerPriorityPluginComponent.class})
@Named ("myPluginComponent")
public class CustomerPriorityPluginComponentImpl implements CustomerPriorityPluginComponent
{
        @ComponentImport
        private final ApplicationProperties applicationProperties;

        @Inject
        public CustomerPriorityPluginComponentImpl(final ApplicationProperties applicationProperties)
    {
        this.applicationProperties = applicationProperties;
    }

    public String getName()
    {
        if(null != applicationProperties)
        {
            return "myComponent:" + applicationProperties.getDisplayName();
        }
        
        return "myComponent";
    }
}