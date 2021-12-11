package com.platform.projapp.controller;

import com.platform.projapp.dto.request.WorkspaceRequest;
import com.platform.projapp.model.Presentation;
import com.platform.projapp.repository.PresentationRepository;
import com.platform.projapp.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/presentation")
public class PresentationController {
    private final PresentationRepository presentationRepository;

    @GetMapping("/{presentationId}")
    public ResponseEntity<?> getPresentation(@PathVariable("presentationId") Long presentationId) throws IOException {
        Presentation presentation = presentationRepository.getById(presentationId);
        Byte[] pptx = presentation.getPresentation();
        byte[] pp = ArrayUtils.toPrimitive(pptx);
        String FILEPATH = "";
        File file = new File(FILEPATH);
        OutputStream os = new FileOutputStream(file);
        os.write(pp);
        return ResponseEntity.ok(os);
    }
}
