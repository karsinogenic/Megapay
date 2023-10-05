package com.mega.cicilan.cicilan.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapInput {

    private String programId;
    private String mcc;
    private String currency;
    private String amount;
    private String channelCode;

}
