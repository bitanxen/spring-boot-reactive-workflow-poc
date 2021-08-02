package in.bitanxen.app.util;

import in.bitanxen.app.config.provider.User;
import org.springframework.data.domain.PageRequest;

public class CommonUtil {

    public static User getUser() {
        return new User("sysbeanmember", "subscriber1");
    }

    public static PageRequest getPageRequest(Integer page, Integer size) {
        int pageVal = page != null && page >= 0 ? page : 0;
        int sizeVal = size != null && size > 0 ? size : 10;
        return PageRequest.of(pageVal, sizeVal);
    }
}
