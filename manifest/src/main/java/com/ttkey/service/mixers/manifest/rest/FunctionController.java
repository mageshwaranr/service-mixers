package com.ttkey.service.mixers.manifest.rest;

import com.ttkey.service.mixers.manifest.domain.FunctionEO;
import com.ttkey.service.mixers.manifest.repository.FunctionRepository;
import com.ttkey.service.mixers.manifest.service.FunctionService;
import com.ttkey.service.mixers.manifest.service.exception.StorageException;
import com.ttkey.service.mixers.model.manifest.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.ttkey.service.mixers.manifest.domain.FunctionEO.createId;

/**
 * Created by nibunangs on 16-Aug-2017.
 */
@RestController
public class FunctionController {

    @Autowired
    private FunctionService functionService;

    @Autowired
    private FunctionRepository functionRepository;

    @GetMapping("/function/executable/{app}/{function}")
    public ResponseEntity<Resource> serveExecutable(@PathVariable String app, @PathVariable String function) {
        Resource file = functionService.loadExecutableAsResource(app, function);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + createId.apply(app, function) + "\"").body(file);
    }

    @PostMapping("/function/executable/{app}/{function}")
    public String handleExecutableUpload(@RequestParam("file") MultipartFile file, @PathVariable String app,
            @PathVariable String function) {
        functionService.storeExecutable(app, function, file);
        return "You successfully uploaded " + file.getOriginalFilename() + " for " + app + "-" + function + "!";
    }

    @PostMapping("/function/")
    public String createOrUpdateFunction(@RequestBody Function functionVO) {
        FunctionEO function = functionService.saveOrUpdate(functionVO);
        return String.format("Function '%s' successfully saved...", function.getId());
    }

    @GetMapping("/function/{app}/{function}")
    public Function getFunktion(@PathVariable String app, @PathVariable String function) {
        String funktionId = FunctionEO.createId.apply(app, function);
        FunctionEO eo = functionRepository.findOne(funktionId);
        if (eo == null)
            throw new IllegalArgumentException("No Function with name '" + function + "' in App '" + app + "'");

        return eo.toFunction();
    }

    @GetMapping("/function/")
    public List<Function> getAllFunctions() {
        Iterable<FunctionEO> allFunctions = functionRepository.findAll();
        Stream<FunctionEO> functionEOStream = StreamSupport.stream(allFunctions.spliterator(), false);

        return functionEOStream.map(FunctionEO::toFunction).collect(Collectors.toList());
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleFileNotFound(FileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
