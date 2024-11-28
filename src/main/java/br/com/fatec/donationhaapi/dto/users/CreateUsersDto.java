package br.com.fatec.donationhaapi.dto.users;

public class CreateUsersDto {

    private String name;

    private String email;

    private String telephone;

    private String password;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public void setTelephone(String cellPhoneNumber) {
        this.telephone = cellPhoneNumber;
    }
    public String getTelephone() {
        return telephone;
    }
    public CreateUsersDto(){}
    
    public CreateUsersDto(String name, String email, String password, String cellPhoneNumber){
        this.name = name;
        this.email = email;
        this.password  = password;
        this.telephone = cellPhoneNumber;
    }

}
