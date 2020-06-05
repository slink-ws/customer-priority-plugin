package ws.slink.atlassian.servlet;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import ws.slink.atlassian.service.ConfigService;
import ws.slink.atlassian.tools.JiraTools;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Scanned
public class ConfigServlet extends HttpServlet {

    @ComponentImport private final UserManager userManager;
    @ComponentImport private final TemplateRenderer renderer;
    @ComponentImport private final LoginUriProvider loginUriProvider;
    @ComponentImport private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public ConfigServlet(UserManager userManager, LoginUriProvider loginUriProvider, TemplateRenderer renderer, PluginSettingsFactory pluginSettingsFactory) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.renderer = renderer;
        this.pluginSettingsFactory = pluginSettingsFactory;
        ConfigService.instance().setPluginSettings(pluginSettingsFactory.createGlobalSettings());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!isPluginManager()) {
            response.setContentType("text/html;charset=utf-8");
            renderer.render("templates/unauthorized.vm", response.getWriter());
        } else {
            response.setContentType("text/html;charset=utf-8");
            renderer.render("templates/config.vm", response.getWriter());
        }
    }

    private boolean isPluginManager() {
        return JiraTools.userHasRolesInProjects(
            ConfigService.instance().projectsList().stream()
                .map(JiraTools::getProjectByKey).filter(Objects::nonNull).collect(Collectors.toList()),
            ConfigService.instance().rolesList().stream()
                .map(JiraTools::getProjectRoleByKey).filter(Objects::nonNull).collect(Collectors.toList()),
            ComponentAccessor.getUserManager().getUserByName(userManager.getRemoteUser().getUsername()));
    }

}