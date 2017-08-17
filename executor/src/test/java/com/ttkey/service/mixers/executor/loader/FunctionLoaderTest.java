package com.ttkey.service.mixers.executor.loader;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Random;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.ttkey.service.mixers.executor.util.Constants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class FunctionLoaderTest {

    @ClassRule
    public static WireMockClassRule wireMockClassRule = new WireMockClassRule(8000 + new Random().nextInt(1000));

    private FunctionLoader testClass = new FunctionLoader();

    private final String CLASS_NAME = "com.servicemixers.demo.recursion.FibonacciInVMFunction";

    @BeforeClass
    public static void setupJarLocation() throws Exception {
        byte[] output;
        try (InputStream resourceAsStream = FunctionLoaderTest.class.getClassLoader().getResourceAsStream("fibonacci-example.jar")) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = resourceAsStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();

            output = buffer.toByteArray();
        }
        stubFor(WireMock.get(WireMock.urlEqualTo("/function/executable/example/fib1")).willReturn(aResponse().withBody(output)));

        stubFor(WireMock.get(WireMock.urlEqualTo("/function/executable/example/fib2")).willReturn(aResponse().withBody(output)));
        System.setProperty(Constants.MANIFEST_SVC_URL, "http://localhost:" + wireMockClassRule.port());
        System.out.println("Configured wiremock on " + wireMockClassRule.port());
    }


    @Test
    public void findClassLoader() throws Exception {
        ClassLoader fib1Loader = testClass.findClassLoader("example", "fib1");
        assertNotNull("A class loader is created successfully for given jar", fib1Loader);

        ClassLoader fib2Loader = testClass.findClassLoader("example", "fib2");
        assertNotNull("A class loader is created successfully for given jar", fib1Loader);

        assertNotEquals(fib1Loader, fib2Loader);

        Class<?> fib1Class = Class.forName(CLASS_NAME, true, fib1Loader);
        Class<?> fib2Class = Class.forName(CLASS_NAME, true, fib2Loader);
        assertNotEquals(fib1Class, fib2Class);

        Field fib1Val = fib1Class.getField("val");
        Field fib2Val = fib2Class.getField("val");
        fib2Val.set(null, 22);
        fib1Val.set(null, 11);

        assertEquals(22, findClass("example", "fib2").getField("val").getInt(null));
        assertEquals(11, findClass("example", "fib1").getField("val").getInt(null));
    }

    private Class<?> findClass(String app, String function) throws Exception {
        ClassLoader loader = testClass.findClassLoader("example", function);
        return Class.forName(CLASS_NAME, true, loader);
    }

    @AfterClass
    public static void cleanup() {
        wireMockClassRule.shutdown();
        wireMockClassRule.shutdownServer();
    }
}