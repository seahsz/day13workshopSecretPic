package day13workshop.secretPic.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*  i am creating an object to store the keyed in username and password
    because i want to be able to validate it
*/

public class User {

    @NotBlank(message="Username cannot be empty")
    @Size(min=2, max=32, message="Username has to be between 2 to 30 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    
}
