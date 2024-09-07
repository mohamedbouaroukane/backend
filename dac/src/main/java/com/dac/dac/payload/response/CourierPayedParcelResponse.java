package com.dac.dac.payload.response;

import com.dac.dac.utils.CustomDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
public class CourierPayedParcelResponse
{
    String parcelTrackingCode;
    String paymentDate;
    Date paymentDateDate;
    public CourierPayedParcelResponse(String parcelTrackingCode, Date paymentDateDate){
        this.parcelTrackingCode = parcelTrackingCode;
        this.paymentDateDate = paymentDateDate;
        paymentDate = CustomDate.dateTostring(paymentDateDate);
    }
}
