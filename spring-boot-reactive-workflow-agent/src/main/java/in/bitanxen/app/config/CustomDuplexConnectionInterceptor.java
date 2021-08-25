package in.bitanxen.app.config;

import io.rsocket.DuplexConnection;
import io.rsocket.plugins.DuplexConnectionInterceptor;


import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CustomDuplexConnectionInterceptor implements DuplexConnectionInterceptor {

    @Override
    public DuplexConnection apply(Type type, DuplexConnection duplexConnection) {
        System.out.println("TYPE: "+type);
        System.out.println("AVAILABLITY: "+duplexConnection.availability());
       // System.out.println("ADDRESS: "+duplexConnection.remoteAddress());

        return duplexConnection;
    }
}
