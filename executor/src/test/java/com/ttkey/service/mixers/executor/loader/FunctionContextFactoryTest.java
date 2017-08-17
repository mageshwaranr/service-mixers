package com.ttkey.service.mixers.executor.loader;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ttkey.service.mixers.executor.ExecutorModule;
import com.ttkey.service.mixers.executor.model.FunctionContext;
import com.ttkey.service.mixers.model.HttpRequest;
import com.ttkey.service.mixers.model.manifest.App;
import com.ttkey.service.mixers.model.manifest.Function;
import com.ttkey.service.mixers.model.manifest.Function.AliasAndIS;
import com.ttkey.service.mixers.model.manifest.InputSource;
import com.ttkey.service.mixers.model.manifest.RequestInfo;
import com.ttkey.service.mixers.model.manifest.RequestInfo.RequestMethod;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class FunctionContextFactoryTest {

    @ClassRule
    public static WireMockClassRule wireMockClassRule = new WireMockClassRule(9090 + new Random().nextInt(1000));

    private FunctionContextFactory testClass;
    private String baseUri;
    private static Gson gson = new Gson();
    private static List<String> regions = asList("us-east", "us-west", "eu-north", "eu-south");
    private static List<String> usWestAzs = asList("us-west-1","us-west-2","us-west-3");

    @BeforeClass
    public static void setupApis() throws Exception {
        stubFor(WireMock.get(WireMock.urlEqualTo("/aws/regions/all")).willReturn(okJson(gson.toJson(regions))));
        String region = gson.toJson(singletonMap("region","us-west"));
        stubFor(WireMock.post(WireMock.urlEqualTo("/aws/az/findBy")).withRequestBody(equalToJson(region)).willReturn(okJson(gson.toJson(usWestAzs))));
    }

    @Before
    public void init() {
        Injector injector = Guice.createInjector(new ExecutorModule());
        testClass = injector.getInstance(FunctionContextFactory.class);
        baseUri = "http://localhost:" + wireMockClassRule.port();
    }


    @Test
    public void newFunctionContext() throws Exception {
        App app = newApp();
        Function function = newFunction(app);
        function.setInputSources(asList(new AliasAndIS("azs", "azs"), new AliasAndIS("regions", "regions")));
        List<InputSource> inputSources = asList(listRegions(), listAz());
        HttpRequest receivedReq = new HttpRequest();
        Map<String, Object> body = new HashMap<>();
        receivedReq.setBody(body);
        body.put("region", "us-west");
        receivedReq.setHeaders(Collections.singletonMap("key1", "headerVal1"));
        FunctionContext functionContext = testClass.newFunctionContext(app, function, inputSources, receivedReq);
        System.err.println("functionContext is "+functionContext);
        Object[] args = functionContext.getArgs();
        System.err.println("Arguments are "+ Arrays.toString(args));
        assertEquals(3, args.length);
        assertEquals(gson.toJson(regions),args[0]);
        assertEquals(gson.toJson(usWestAzs),args[1]);
        Map<String,Object> payload = gson.fromJson(args[2].toString(), new TypeToken<Map<String, Object>>() {
        }.getType());

        assertEquals(2,payload.size());
        assertEquals("headerVal1", payload.get("apiKey").toString());
    }

    private App newApp() {
        App app = new App();
        app.setName("ServiceMixers");
        app.setConfigs(Collections.singletonMap("baseUri", baseUri));
        return app;
    }

    private InputSource listRegions() {
        InputSource is = new InputSource();
        is.setSourceName("regions");
        RequestInfo info = new RequestInfo();
        info.setHttpVerb(RequestMethod.GET);
        info.setUri("@{appConfigs['baseUri']}/aws/regions/all");
        is.setRequest(info);
        return is;
    }

    private InputSource listAz() {
        InputSource is = new InputSource();
        is.setSourceName("azs");
        RequestInfo info = new RequestInfo();
        info.setHttpVerb(RequestMethod.POST);
        info.setUri("@{appConfigs['baseUri']}/aws/az/findBy");
        info.setBody("{ \"region\" : \"@{request.body['region']}\" }");
        is.setRequest(info);
        return is;
    }

    private Function newFunction(App app) {
        Function function = new Function();
        function.setApp(app.getName());
        function.setName("FindEc2Price");
        String[] args = {"@{sources['regions'].body}", "@{sources['azs'].body}", "{\"instances\" : [ \"m3.xlarge\",\"m3.3xlarge\" ] , \"apiKey\" : \"@{request.headers['key1']}\" }"};
        function.setArgs(args);
        return function;
    }


    @AfterClass
    public static void cleanup() throws Exception {
        wireMockClassRule.shutdownServer();
        wireMockClassRule.stop();
    }
}