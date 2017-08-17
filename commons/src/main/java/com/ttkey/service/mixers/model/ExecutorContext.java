package com.ttkey.service.mixers.model;

import com.ttkey.service.mixers.model.manifest.Function;
import com.ttkey.service.mixers.model.manifest.InputSource;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by sivarajm on 8/17/2017.
 */
@Getter
@Setter
public class ExecutorContext {

  HttpRequest httpRequest;
  Function function;

  //Key is InputSource.sourceName
  Map<String, InputSource> inputSources;
}
