package com.capstone.server.controller;

import org.springframework.web.bind.annotation.*;

import com.capstone.server.code.UserErrorCode;
import com.capstone.server.dto.UserCreateRequestDto;
import com.capstone.server.dto.UserUpdateRequestDto;
import com.capstone.server.exception.UserException;
import com.capstone.server.model.UserEntity;
import com.capstone.server.response.SuccessResponse;
import com.capstone.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getUsers() {
        List<UserEntity> userEntities = userService.getAllUsers();
        return ResponseEntity.ok().body(userEntities);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Validated @RequestBody UserCreateRequestDto userCreateRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new UserException(UserErrorCode.BAD_REQUEST, errorMap);
        } else {
            return ResponseEntity.ok().body(new SuccessResponse(userService.createUser(userCreateRequestDto.toEntity())));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserNameById(@PathVariable Long userId, @Validated @RequestBody UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new UserException(UserErrorCode.BAD_REQUEST, errorMap);
        } else {
            return ResponseEntity.ok().body(userService.updateUserNameById(userId, userUpdateRequestDto));
        }
    }
}