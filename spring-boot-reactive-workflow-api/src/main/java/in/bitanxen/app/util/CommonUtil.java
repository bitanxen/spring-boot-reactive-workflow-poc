package in.bitanxen.app.util;

import in.bitanxen.app.config.provider.User;

public class CommonUtil {

    public static User getUser() {
        return new User("sysbeanmember", "subscriber1");
    }
}
