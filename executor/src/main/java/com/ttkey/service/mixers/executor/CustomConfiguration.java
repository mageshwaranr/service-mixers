package com.ttkey.service.mixers.executor;

import com.esotericsoftware.kryo.NotNull;
import io.dropwizard.Configuration;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomConfiguration extends Configuration {

  @NotNull
  @Valid
  String userDir;

  @NotNull
  @Valid
  String mixerExecutableDir;

  @NotNull
  @Valid
  String manifestSvcUrl;
}
