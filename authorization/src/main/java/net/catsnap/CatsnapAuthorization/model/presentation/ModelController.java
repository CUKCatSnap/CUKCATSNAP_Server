package net.catsnap.CatsnapAuthorization.model.presentation;

import net.catsnap.CatsnapAuthorization.model.application.ModelService;
import net.catsnap.CatsnapAuthorization.model.dto.request.ModelSignUpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorization/model")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody ModelSignUpRequest request) {
        modelService.signUp(request);
        return ResponseEntity.ok().build();
    }
}

