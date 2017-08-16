package com.ttkey.service.mixers.executor.template;

import java.util.stream.Stream;

import com.ttkey.service.mixers.executor.model.FunctionContext;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;


public class TemplateEngine {

    /**
     *
     * @param input an array of JSON template strings like [ {"1" : "One" } , [1,2,3] ]
     * @param ctxt Function Context which has all the values necessary to enrich json template
     * @return an array containing filled
     */
    public Object[] replaceTemplates(String[] input, FunctionContext ctxt) {
        return Stream.of(input)
                .map(argument -> {
                    CompiledTemplate compiledTemplate = TemplateCompiler.compileTemplate(argument);
                    return TemplateRuntime.execute(compiledTemplate,ctxt);
                } )
                .toArray();
    }

}
