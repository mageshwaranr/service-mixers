package com.ttkey.service.mixers.manifest.rest;

import com.ttkey.service.mixers.manifest.domain.InputSourceEO;
import com.ttkey.service.mixers.manifest.repository.InputSourceRepository;
import com.ttkey.service.mixers.manifest.service.InputSourceService;
import com.ttkey.service.mixers.model.manifest.InputSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@RestController
@RequestMapping("/sources")
public class InputSourceController {

    @Autowired
    private InputSourceService inputSourceService;

    @Autowired
    private InputSourceRepository inputSourceRepository;

    @PostMapping("/")
    public InputSourceEO createOrUpdateInputSource(@RequestBody InputSource inputSourceVO) {
        InputSourceEO inputSource = inputSourceService.save(inputSourceVO);
        return inputSource;
    }

    @GetMapping("/{app}/{inputSource}")
    public InputSourceEO getInputSource(@PathVariable String app, @PathVariable String inputSource){
        String id = InputSourceEO.createId.apply(app, inputSource);
        return inputSourceRepository.findOne(id);
    }

}
