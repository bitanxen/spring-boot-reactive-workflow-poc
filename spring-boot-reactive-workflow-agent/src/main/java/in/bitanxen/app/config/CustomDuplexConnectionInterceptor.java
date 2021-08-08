package in.bitanxen.app.config;

import io.rsocket.DuplexConnection;
import io.rsocket.plugins.DuplexConnectionInterceptor;

import java.util.List;
import java.util.function.Consumer;

public class CustomDuplexConnectionInterceptor implements DuplexConnectionInterceptor {
    @Override
    public DuplexConnection apply(Type type, DuplexConnection duplexConnection) {
        System.out.println(type.toString());
        System.out.println(duplexConnection.availability());
        System.out.println(duplexConnection.remoteAddress().toString());
        return duplexConnection;
    }
}
