package br.com.fatec.donationhaapi.dto.users;

import java.util.UUID;

public class UpdateUsersDto {

    private UUID usersId;
    
    private String name;

    private String email;

    private String password;

    public UUID getUsersId() {
        return usersId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UpdateUsersDto(){}

    public UpdateUsersDto(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
