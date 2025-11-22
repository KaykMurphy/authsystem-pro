package com.restFull.restFull.controller;

import com.restFull.restFull.dtos.UpdateResponseDTO;
import com.restFull.restFull.dtos.UpdateRequestDTO;
import com.restFull.restFull.dtos.UserResponseDTO;
import com.restFull.restFull.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users") // REFATORAÇÃO: Mudança de rota base para /users
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<EntityModel<UserResponseDTO>> getMe() {
        UserResponseDTO dto = userService.getAuthenticatedUser();

        EntityModel<UserResponseDTO> resource = EntityModel.of(dto);

        // REFATORAÇÃO: Links HATEOAS apontando para este novo controller
        resource.add(
                linkTo(methodOn(UserController.class).getMe()).withRel("me"),
                linkTo(methodOn(UserController.class).update(null)).withRel("atualizar"), // Body null apenas para gerar link
                linkTo(methodOn(UserController.class).delete()).withRel("deletar")
        );

        return ResponseEntity.ok(resource);
    }


    @PutMapping("/me")
    public ResponseEntity<UpdateResponseDTO> update(@RequestBody @Valid UpdateRequestDTO dto) {
        UpdateResponseDTO updated = userService.updateCurrentUser(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete() {
        userService.deleteCurrentUser();
        return ResponseEntity.noContent().build();
    }
}