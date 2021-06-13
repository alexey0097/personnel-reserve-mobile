package ru.iworking.personnel.reserve.mobile.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Resume {
    private Long id;

    private String lastName;
    private String firstName;
    private String middleName;

    private String profession;

    private String aboutMe;

    private byte[] avatar;

    public String getFullName() {
        return String.format("%s %s %s", lastName, firstName, middleName);
    }
}
