package Capstone.Capstone.service.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateVPCResponseDTO {

    @NotEmpty
    private IId IId;

    @NotEmpty
    private String IPv4_CIDR;

    @NotEmpty
    private List<SubnetInfo> SubnetInfoList;

    private List<Tag> TagList;
    private List<KeyValue> KeyValueList;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IId {
        private String NameId;

        private String SystemId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubnetInfo {
        private IId IId;

        private String Zone;

        private String IPv4_CIDR;

        private List<Tag> TagList;
        private List<KeyValue> KeyValueList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        private String Key;
        private String Value;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyValue {
        private String Key;

        private String Value;
    }
}
