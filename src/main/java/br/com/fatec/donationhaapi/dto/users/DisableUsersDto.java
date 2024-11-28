package br.com.fatec.donationhaapi.dto.users;

import java.util.UUID;

public class DisableUsersDto {


    private UUID usersId;
    private Boolean disabled;

    public UUID getUsersId() {
        return usersId;
    }

    public void setUsersId(UUID usersId) {
        this.usersId = usersId;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public DisableUsersDto(){}

    public DisableUsersDto(UUID usersId, Boolean disabled){
        this.usersId = usersId;
        this.disabled = disabled;
    }

}
