package ws.slink.cp.model;

import org.apache.commons.lang3.StringUtils;
import ws.slink.cp.tools.Common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StyleElement {

    @XmlElement private String id;
    @XmlElement private String title;
    @XmlElement private String text;
    @XmlElement private Map<String, String> style;
    @XmlElement private Set<String> reporters;

    public String id() {
        return (StringUtils.isBlank(id)) ? "" : id;
    }
    public StyleElement id(String value) {
        this.id = value; return this;
    }
    public String description() {
        return (StringUtils.isBlank(title)) ? "" : title;
    }
    public String text() {
        return (StringUtils.isBlank(text)) ? "" : text;
    }
    public Set<String> reporters() {
        return (null == reporters) ? Collections.emptySet() : reporters;
    }
    public String style(StyledElement element) {
        String elementStyle = style.get(element.title());
        return (StringUtils.isBlank(elementStyle)) ? "" : elementStyle;
    }
    public String style(String key) {
        String elementStyle = style.get(key);
        return (StringUtils.isBlank(elementStyle)) ? "" : elementStyle;
    }

    public boolean equals(Object other) {
        if (other instanceof StyleElement) {
            StyleElement o = (StyleElement) other;
            if (StringUtils.isNotBlank(this.id)) {
                return this.id.equals(o.id);
            } else {
                boolean result = true;
                result &= (StringUtils.isNotBlank(this.title)) ? this.title.equals(o.title) : false;
                result &= (StringUtils.isNotBlank(this.text)) ? this.text.equals(o.text) : false;
                return result;
            }
        }
        return false;
    }

    public String toString() {
        return Common.instance().getGsonObject().toJson(this);
//        return new StringBuilder()
//            .append("id: ").append(id).append(", ")
//            .append("desc: ").append(title).append(", ")
//            .append("text: ").append(text).append(", ")
//            .append("reps: ").append(reporters).append(", ")
//            .append("style: ").append(style)
//            .toString()
//        ;
    }
}
