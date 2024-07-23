package net.dni.spring.common.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@NoArgsConstructor
@Data
@CsvRecord(separator = ",", skipFirstLine = true)
public class EnrollSubscriberRequest {

    @DataField(pos = 1, trim = true, required = true)
    private String firstName;

    @DataField(pos = 2, trim = true, required = true)
    private String lastName;

    @DataField(pos = 3, trim = true, required = true)
    private String email;

}
