package uz.pdp.appjparelationships.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    private String firstName;

    private String lastName;

    private String city;

    private String district;

    private String street;

    private Integer group_id;

    private List<Integer> subject_id;

}
