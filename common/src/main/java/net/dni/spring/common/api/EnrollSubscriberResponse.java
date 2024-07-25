package net.dni.spring.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@CsvRecord(separator = ",", skipFirstLine = true)
public class EnrollSubscriberResponse {

    @DataField(pos = 1, trim = true, required = true)
    private Long subscriberId;

}
