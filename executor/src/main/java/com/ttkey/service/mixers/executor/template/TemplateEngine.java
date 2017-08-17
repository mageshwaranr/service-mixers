package com.ttkey.service.mixers.executor.template;

import java.util.WeakHashMap;
import java.util.stream.Stream;

import com.ttkey.service.mixers.executor.model.FunctionContext;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TemplateEngine {
    private Logger log = LoggerFactory.getLogger(getClass());

    private static final WeakHashMap<String, CompiledTemplate> cache = new WeakHashMap<>();

    /**
     *
     * @param input an array of JSON template strings like [ {"1" : "One" } , [1,2,3] ]
     * @param ctxt Function Context which has all the values necessary to enrich json template
     * @return an array containing filled
     */
    public Object[] replaceTemplates(String[] input, FunctionContext ctxt) {
        return Stream.of(input)
                .map(argument -> replaceTemplate(argument,ctxt))
                .toArray();
    }

    public Object replaceTemplate(String input, FunctionContext ctxt){
        try {
            CompiledTemplate template = cache.computeIfAbsent(input, s -> TemplateCompiler.compileTemplate(input));
            return TemplateRuntime.execute(template,ctxt);
        } catch (Exception e) {
            log.warn("Unable to resolve template {} using context {}",input,ctxt);
            log.error("Unable to resolve template due to {}",e.getMessage());
            throw e;
        }
    }

}
