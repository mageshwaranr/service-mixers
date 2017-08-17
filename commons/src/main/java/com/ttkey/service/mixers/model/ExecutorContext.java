package com.ttkey.service.mixers.model;

import com.ttkey.service.mixers.model.manifest.App;
import com.ttkey.service.mixers.model.manifest.Function;
import com.ttkey.service.mixers.model.manifest.InputSource;
import java.util.List;
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
  App app;
  List<InputSource> inputSources;
}
