package com.ttkey.service.mixers.manifest.service;

import com.ttkey.service.mixers.manifest.domain.InputSourceEO;
import com.ttkey.service.mixers.manifest.repository.InputSourceRepository;
import com.ttkey.service.mixers.model.manifest.InputSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ttkey.service.mixers.manifest.domain.InputSourceEO.createId;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@Service
public class InputSourceServiceImpl implements InputSourceService {

    @Autowired
    private InputSourceRepository iSourceRepository;

    @Override
    public InputSourceEO save(InputSource iSourceVO) {
        String id = createId.apply(iSourceVO.getAppName(), iSourceVO.getSourceName());
        InputSourceEO iSourceEO = iSourceRepository.findOne(id);
        if (iSourceEO == null) {
            iSourceEO = new InputSourceEO();
            iSourceEO.setId(id);
        }

        iSourceEO.copy(iSourceVO);

        return iSourceRepository.save(iSourceEO);
    }
}
