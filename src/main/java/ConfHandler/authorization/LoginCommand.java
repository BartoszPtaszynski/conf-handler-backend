package ConfHandler.authorization;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class LoginCommand {
    private String email;
    private String password;
}
