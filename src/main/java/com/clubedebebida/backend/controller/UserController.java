package com.clubedebebida.backend.controller;

import com.clubedebebida.backend.dto.UserDTO;
import com.clubedebebida.backend.dto.UserLoginRequestDTO;
import com.clubedebebida.backend.dto.UserPasswordDTO;
import com.clubedebebida.backend.model.User;
import com.clubedebebida.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(
            @PageableDefault (size=10, page=0, sort="name")Pageable pageable
    )
    {
        Page<UserDTO> usersDTO = userService.findAll(pageable);

        return ResponseEntity.ok(usersDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
        UserDTO usersDTO = userService.findById(id);

        return ResponseEntity.ok(usersDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@Valid @RequestBody UserDTO userDTO){
        UserDTO savedUsuario = userService.save(userDTO);

        return new ResponseEntity<>(savedUsuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO userDTO){
        UserDTO updatedUsuario = userService.update(id, userDTO);

        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Long id){
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginRequestDTO> login(@Valid @RequestBody UserLoginRequestDTO loginRequest) {
        User user = new User(loginRequest.id(), loginRequest.email(),loginRequest.password());

        UserLoginRequestDTO userLoginRequestDTO = userService.login(user.getEmail(), user.getPassword());
        if (userLoginRequestDTO != null) {
            return ResponseEntity.ok(loginRequest);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/novasenha/{id}")
    public ResponseEntity<UserPasswordDTO> changePassword(@PathVariable("id") Long id, @RequestBody UserPasswordDTO userPasswordDTO){
        UserPasswordDTO updatedUsuario = userService.changePassword(id, userPasswordDTO);

        return ResponseEntity.ok(updatedUsuario);
    }


}
