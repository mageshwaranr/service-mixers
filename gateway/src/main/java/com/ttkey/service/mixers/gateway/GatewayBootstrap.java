package com.ttkey.service.mixers.gateway;

import com.ttkey.service.mixers.cache.GatewayCacheRefreshTimer;
import com.ttkey.service.mixers.handlers.ApiServiceHandler;
import com.ttkey.service.mixers.handlers.AppServiceHandler;
import com.ttkey.service.mixers.handlers.FunctionServiceHandler;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Timer;

public class GatewayBootstrap {

    public static void main(final String[] args) {
        try {

            // Round robin by default
            LoadBalancingProxyClient loadBalancer = new LoadBalancingProxyClient();
            loadBalancer.setConnectionsPerThread(20);

            Properties properties = bootStrapGatewayServers(loadBalancer);

            String gatewayRoot = properties.getProperty("gatewayRoot");
            String[] info = gatewayRoot.split(":");
            Undertow reverseProxy = Undertow.builder()
                    .addHttpListener(Integer.valueOf(info[1]), info[0])
                    .setIoThreads(4)
                    .setHandler(new ProxyHandler(loadBalancer, 30000, ResponseCodeHandler.HANDLE_404))
                    .build();
            reverseProxy.start();

            Timer timer = new Timer();
            timer.schedule(new GatewayCacheRefreshTimer(), 0, 300000);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties bootStrapGatewayServers(LoadBalancingProxyClient loadBalancer) throws IOException, URISyntaxException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("C:\\DriveWorkspaces\\service-mixers\\gateway\\src\\main\\resources\\configurations.yaml"));

        String gatewayServersStr = properties.getProperty("gatewayServers");
        if (gatewayServersStr.isEmpty() || gatewayServersStr == null) {
            gatewayServersStr = properties.getProperty("gatewayRoot");
        }
        String[] gatewayServers = gatewayServersStr.split(",");

        for (int i = 0; i < gatewayServers.length; i++) {
            String gServer = gatewayServers[i];
            String[] info = gServer.split(":");
            String hostName = info[0];
            String port = info[1];
            System.out.println("HostName " + hostName + " Port " + port);
            Undertow.builder()
                    .addHttpListener(Integer.valueOf(port), hostName)
                    .setHandler(new BlockingHandler(Handlers.pathTemplate()
                            .add("/api/*", new ApiServiceHandler())
                            .add("/app/*", new AppServiceHandler())
                            .add("/function/*", new FunctionServiceHandler())
                        ))
                    .build().start();

            loadBalancer.addHost(new URI("http://" + hostName + ":" + port));
        }
        return properties;
    }

}
