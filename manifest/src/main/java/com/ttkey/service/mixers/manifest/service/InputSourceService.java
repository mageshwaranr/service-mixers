package com.ttkey.service.mixers.manifest.service;

import com.ttkey.service.mixers.manifest.domain.InputSourceEO;
import com.ttkey.service.mixers.model.manifest.InputSource;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
public interface InputSourceService {
    InputSourceEO save(InputSource inputSourcevo);
}
