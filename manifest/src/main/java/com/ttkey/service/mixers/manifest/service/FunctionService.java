package com.ttkey.service.mixers.manifest.service;

import com.ttkey.service.mixers.manifest.domain.FunctionEO;
import com.ttkey.service.mixers.model.manifest.Function;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
public interface FunctionService {

    void storeExecutable(String app, String function, MultipartFile file);

    Resource loadExecutableAsResource(String app, String function);

    FunctionEO saveOrUpdate(Function functionVO);
}
