package com.ttkey.service.mixers.gateway;

import com.ttkey.service.mixers.handlers.ServiceHandler;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class GatewayBootstrap {

    public static void main(final String[] args) {
        try {

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

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static Properties bootStrapGatewayServers(LoadBalancingProxyClient loadBalancer) throws IOException, URISyntaxException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("C:\\DriveWorkspaces\\service-mixers\\gateway\\src\\main\\resources\\configurations.yaml"));

        String gatewayServersStr = properties.getProperty("gatewayServers");
        String[] gatewayServers = gatewayServersStr.split(",");

        for (int i = 0; i < gatewayServers.length; i++) {
            String gServer = gatewayServers[i];
            String[] info = gServer.split(":");
            String hostName = info[0];
            String port = info[1];
            System.out.println("HostName " + hostName + " Port " + port);
            Undertow.builder()
                    .addHttpListener(Integer.valueOf(port), hostName)
                    .setHandler(Handlers.pathTemplate()
                            .add("/api/", new ServiceHandler()))
                    .build().start();

            loadBalancer.addHost(new URI("http://" + hostName + ":" + port));
        }
        return properties;
    }

}
