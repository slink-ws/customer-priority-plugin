package ws.slink.atlassian.service;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PluginConfigService {

    private static final String ALLOWED_GROUPS = "Users, Managers, Administrators"; // Administrators
    private static final String ALLOWED_PROJECTS = "TA,TB";

//    private static final String ALLOWED_GROUPS = "";
//    private static final String ALLOWED_PROJECTS = "";

    public List<String> configuredRoles() {
        if (StringUtils.isBlank(ALLOWED_GROUPS))
            return Collections.EMPTY_LIST;
        else
            return Arrays.stream(ALLOWED_GROUPS.split(",")).map(s -> s.trim()).collect(Collectors.toList());
    }

    public List<String> configuredProjects() {
        if (StringUtils.isBlank(ALLOWED_PROJECTS))
            return Collections.EMPTY_LIST;
        else
            return Arrays.stream(ALLOWED_PROJECTS.split(",")).map(s -> s.trim()).collect(Collectors.toList());
    }


}
