package com.example.transmagdalena.user.controller;

import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.ticket.service.TicketService;
import com.example.transmagdalena.user.DTO.UserDTO;
import com.example.transmagdalena.user.Service.UserService;
import com.example.transmagdalena.user.UserRols;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.userResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping("/{id}/assignments")
    public ResponseEntity<UserDTO.userAssigmentResponse> getAssigments(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAssigments(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO.userResponse> create(@Valid @RequestBody UserDTO.userCreateRequest userDTO,
                                                        UriComponentsBuilder uribuilder) {
        var f = userService.save(userDTO);
        var uri = uribuilder.path("/api/v1/user/{id}").buildAndExpand(f.id()).toUri();
        return ResponseEntity.created(uri).body(f);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<UserDTO.userResponse>> getAll(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        var p = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(userService.getAll(p));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserDTO.userResponse> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<UserDTO.userResponse> update(@PathVariable Long id, @Valid @RequestBody UserDTO.userUpdateRequest userDTO) {
        return ResponseEntity.ok(userService.update(userDTO, id));
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<Page<TicketDTO.ticketResponse>> getTicketsById(@PathVariable Long id,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size){
        var pp =  PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(ticketService.getUserTickets(pp,id));
    }

    @GetMapping("/find/type")
    public ResponseEntity<Page<UserDTO.userResponse>> getUsersByRol(@RequestParam UserRols rol,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size){
        var pp = PageRequest.of(page, size, Sort.by("id").ascending());
        if (rol == UserRols.PASSENGER){
            return ResponseEntity.ok(userService.getPassengers(pp));
        }
        return ResponseEntity.ok(userService.getUsersByRol(rol,pp));
    }

    @GetMapping("/count/{type}")
    public ResponseEntity<Integer> countUsers(@RequestParam UserRols type) {
        return ResponseEntity.ok(userService.countUsersByRol(type));
    }


}
