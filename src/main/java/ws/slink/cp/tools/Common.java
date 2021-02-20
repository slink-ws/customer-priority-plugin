package ws.slink.cp.tools;

import com.atlassian.sal.api.auth.LoginUriProvider;
import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ws.slink.cp.json.CustomExclusionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

public class Common {

    private static class CommonSingleton {
        private static final Common INSTANCE = new Common();
    }
    public static Common instance () {
        return Common.CommonSingleton.INSTANCE;
    }

    public void redirectToLogin(LoginUriProvider loginUriProvider,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }
    private URI getUri(HttpServletRequest request) {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }

    private Gson gson = null;
    public Gson getGsonObject() {
        if (null == gson) {
            ExclusionStrategy strategy = new CustomExclusionStrategy();
            gson = new GsonBuilder()
                .addSerializationExclusionStrategy(strategy)
                .addDeserializationExclusionStrategy(strategy)
                .create()
            ;
        }
        return gson;
    }
}
