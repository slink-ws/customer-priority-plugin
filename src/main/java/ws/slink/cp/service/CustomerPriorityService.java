package ws.slink.cp.service;

import ws.slink.cp.model.StyleElement;
import ws.slink.cp.model.StyledElement;

import java.util.Optional;

public interface CustomerPriorityService {

    Optional<StyleElement> getStyleElement(String projectKey, String customerId);
    String getStyle(String projectKey, String customerId, StyledElement type);
    String getText(String projectKey, String customerId);

}
