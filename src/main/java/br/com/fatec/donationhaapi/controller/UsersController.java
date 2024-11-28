package br.com.fatec.donationhaapi.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import br.com.fatec.donationhaapi.exception.ResponseGeneric;
import br.com.fatec.donationhaapi.dto.users.CreateUsersDto;
import br.com.fatec.donationhaapi.dto.users.DisableUsersDto;
import br.com.fatec.donationhaapi.dto.users.UpdateUsersDto;
import br.com.fatec.donationhaapi.service.UsersService;
import br.com.fatec.donationhaapi.entity.Users;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping(value = "/findUser/{usersId}")
    public ResponseEntity<Object> findUsersBy(@PathVariable UUID usersId) {
        Optional<Users> resultUserGet = usersService.findUsersById(usersId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseGeneric.response(resultUserGet));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> saveUsers(@RequestBody CreateUsersDto createUsersDto){
        Users resultUserPost = usersService.saveUsers(createUsersDto);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultUserPost));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateUsers(@RequestBody UpdateUsersDto updateUsersDto){
        Users resultUserPut = usersService.updateUsers(updateUsersDto);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultUserPut));
    }

    @PutMapping(value = "/disable")
    public ResponseEntity<Object> disableUsers(@RequestBody DisableUsersDto disableUsersDto){
        Users resultUserPut = usersService.disableUsers(disableUsersDto);
        return ResponseEntity.ok().body(ResponseGeneric.response(resultUserPut));
    }

}
