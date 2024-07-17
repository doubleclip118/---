package Capstone.Capstone.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AWSInfoRequest {
    @NotNull
    private Long userId;
    @NotEmpty
    private String DriverName;
    @NotEmpty
    private String ProviderName;
    @NotEmpty
    private String DriverLibFileName;
    @NotEmpty
    private String CredentialName;
    @NotEmpty
    private String CredentialAccessKey;
    @NotEmpty
    private String CredentialAccessKeyVal;
    @NotEmpty
    private String CredentialSecretKey;
    @NotEmpty
    private String CredentialSecretKeyVal;
    @NotEmpty
    private String RegionName;
    @NotEmpty
    private String RegionKey;
    @NotEmpty
    private String RegionValue;
    @NotEmpty
    private String ZoneKey;
    @NotEmpty
    private String ZoneValue;
}
