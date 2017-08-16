package com.ttkey.service.mixers.manifest.repository;

import com.ttkey.service.mixers.manifest.domain.AppEO;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by nibunangs on 14-Aug-2017.
 */
public interface AppRepository extends CrudRepository<AppEO, String> {
}
