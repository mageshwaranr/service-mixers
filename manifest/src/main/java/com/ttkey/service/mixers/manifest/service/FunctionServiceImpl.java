package com.ttkey.service.mixers.manifest.service;

import com.ttkey.service.mixers.manifest.domain.FunctionEO;
import com.ttkey.service.mixers.manifest.repository.FunctionRepository;
import com.ttkey.service.mixers.manifest.service.exception.FunctionResourceNotFoundException;
import com.ttkey.service.mixers.manifest.service.exception.StorageException;
import com.ttkey.service.mixers.model.manifest.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.ttkey.service.mixers.manifest.domain.FunctionEO.createId;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private FunctionRepository functionRepository;

    @Override
    public void storeExecutable(String app, String function, MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new StorageException("Can't save the empty Executable file " + filename);
        }

        try {
            FunctionEO funktion = functionRepository.findOne(createId.apply(app, function));
            if (funktion == null)
                throw new IllegalArgumentException(String.format("Can't find the function '%s' in APP '%s'", function, app));

            funktion.setExecutable(file.getBytes());

            functionRepository.save(funktion);
            // Files.copy(file.getInputStream(), rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to storeExecutable file " + filename, e);
        }
    }

    @Override
    public Resource loadExecutableAsResource(String app, String function) {
        FunctionEO funktion = functionRepository.findOne(createId.apply(app, function));
        if (funktion == null)
            throw new FunctionResourceNotFoundException(
                    String.format("Couldn't find the resources for AppEO '%s' and Function '%s'", app, function));

        return new ByteArrayResource(funktion.getExecutable());
        //                String fileName = "catalina.log";
        //                try {
        //                    Path file = rootLocation.resolve(fileName);
        //                    Resource resource = new UrlResource(file.toUri());
        //                    if (resource.exists() || resource.isReadable()) {
        //                        return resource;
        //                    } else {
        //                        throw new FunktionResourceNotFoundException(
        //                                "Could not get or read file: " + file.toUri());
        //                    }
        //                } catch (MalformedURLException e) {
        //                    throw new FunktionResourceNotFoundException("Could not read file: " + fileName, e);
        //                }
    }

    @Override
    public FunctionEO saveOrUpdate(Function functionVO) {
        String appName = functionVO.getApp();
        String funcName = functionVO.getName();
        if (isEmpty(appName) || isEmpty(funcName))
            throw new IllegalArgumentException("AppEO and Name can't be empty!!");

        String functionId = createId.apply(appName, funcName);
        FunctionEO function = functionRepository.findOne(functionId);
        if (function == null) {
            function = new FunctionEO();
            function.setId(functionId);
        }
            function.copy(functionVO);

        return functionRepository.save(function);
    }
}
