package com.ttkey.service.mixers.executor.loader;

import static com.ttkey.service.mixers.executor.util.PropertyManager.FETCH_EXECUTABLE_PREFIX;
import static com.ttkey.service.mixers.executor.util.PropertyManager.FORWARD_SLASH;
import static com.ttkey.service.mixers.executor.util.PropertyManager.RESOURCE_NAME_SUFFIX;

import com.ttkey.service.mixers.executor.CustomConfiguration;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;

import com.ttkey.service.mixers.executor.util.PropertyManager;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.h2.util.SmallLRUCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionLoader {

    @Inject
    CustomConfiguration configuration;

    private static SmallLRUCache<String, ClassLoader> lruCache = SmallLRUCache.newInstance(50);
    private static final Logger log = LoggerFactory.getLogger(FunctionLoader.class);


    public ClassLoader findClassLoader(String app, String function){
        String key = constructKey(app, function);
        ClassLoader classLoader = lruCache.get(key);

        if(classLoader == null) {
            classLoader = lruCache.put(key, loadClassLoader(app, function));
        }
        return classLoader;
    }

    private ClassLoader loadClassLoader(String app, String function) {
        String key = constructKey(app, function);
        log.info("Cache miss for key {}", key);
        try {
            URL fileLocation = constructLibURL(app, function);
            URLClassLoader classLoader = new URLClassLoader(new URL[]{fileLocation}, this.getClass().getClassLoader());
            lruCache.put(key, classLoader);
            return classLoader;
        } catch (MalformedURLException e) {
            log.error("Unable to find executable for function " + app + function, e);
            throw new BadRequestException("Unable to load Function Executable of " + function + " due to " + e.getMessage(), e);
        }
    }

    private void downloadExecutable(String app, String function, File executable) {
        try {
            //http://localhost:8080/function/executable/NewApp/Fun1
            String uri = configuration.getManifestSvcUrl() + FETCH_EXECUTABLE_PREFIX + FORWARD_SLASH + app + FORWARD_SLASH + function;
            log.info("Fetching executable from {}",uri);
            Response response = Request.Get(uri).execute();
            Files.createDirectories(executable.getParentFile().toPath());
            response.saveContent(executable);
        } catch (IOException e) {
            log.error("Unable to download executable jar from manifest server due to "+e.getMessage(),e);
            log.info("Delete the file at {} ? {}", executable.getAbsolutePath(), executable.delete());
            throw new BadRequestException("Unable to find executable of "+function, e);
        }
    }

    private URL constructLibURL(String app, String function) throws MalformedURLException {
        String LIB_PATH = configuration.getMixerExecutableDir() == null ? configuration.getUserDir() : configuration.getMixerExecutableDir();
        File executable = new File(LIB_PATH + File.separator+ app + File.separator + function + RESOURCE_NAME_SUFFIX);
        if (!executable.exists()) {
            downloadExecutable(app, function, executable);
        }
        return executable.toURI().toURL();
    }


    private String constructKey(String app, String function) {
        return app + ":" + function ;
    }
}
