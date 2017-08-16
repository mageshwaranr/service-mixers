package com.ttkey.service.mixers.rest;

import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.google.gson.Gson;
import com.ttkey.service.mixers.model.HttpRequest;
import com.ttkey.service.mixers.model.HttpResponse;
import com.ttkey.service.mixers.model.manifest.RequestInfo.RequestMethod;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class RestClientTest {

    @ClassRule
    public static WireMockClassRule wireMockClassRule = new WireMockClassRule(9000 + new Random().nextInt(1000));

    private Gson gson = new Gson();

    @BeforeClass
    public static void stubApis() throws Exception {
        stubFor(WireMock.get(WireMock.urlEqualTo("/get")).willReturn(okJson("[1,2,3]")));
        stubFor(WireMock.post(WireMock.urlEqualTo("/post")).willReturn(okJson("Hello Post")));
        stubFor(WireMock.put(WireMock.urlEqualTo("/put")).willReturn(okJson("{ \"method\" : \"put\" }")));
        stubFor(WireMock.patch(WireMock.urlEqualTo("/patch")).willReturn(ok()));
        stubFor(WireMock.patch(WireMock.urlEqualTo("/delete")).willReturn(badRequest()));
    }


    @Test
    public void invokeRestApisSync() throws Exception {
        Map<String,HttpRequest> requests = new HashMap<>();
        requests.put("get",newRequest(RequestMethod.GET,"/get"));
        requests.put("post",newRequest(RequestMethod.POST,"/post"));
        requests.put("put",newRequest(RequestMethod.PUT,"/put"));
        requests.put("patch",newRequest(RequestMethod.PATCH,"/patch"));
        RestClient client = new RestClient();
        Map<String, HttpResponse> responses = client.invokeRestApisSync(requests);

        String respBody = responses.get("get").getResponseBody().toString();
        assertEquals("[1,2,3]", respBody);

        respBody = responses.get("post").getResponseBody().toString();
        assertEquals("Hello Post", respBody);

        Map put = gson.fromJson(responses.get("put").getResponseBody().toString(), Map.class);
        assertFalse(put.isEmpty());
        assertEquals("put", put.get("method"));

        respBody = responses.get("patch").getResponseBody().toString();
        assertTrue(respBody.isEmpty());

        requests.put("delete",newRequest(RequestMethod.DELETE,"/delete"));
        responses = client.invokeRestApisSync(requests);
        assertEquals(404,responses.get("delete").getStatusCode());

    }

    private HttpRequest newRequest(RequestMethod method, String uri) {
        HttpRequest request = new HttpRequest();
        request.setPath(uri);
        request.setMethod(method);
        request.setBaseUri("http://localhost:"+wireMockClassRule.port());
        return request;
    }
}