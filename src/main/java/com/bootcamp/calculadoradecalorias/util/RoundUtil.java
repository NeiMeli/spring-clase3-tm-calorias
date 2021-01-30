package com.bootcamp.calculadoradecalorias.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundUtil {
    public static double roundOneDecimal(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        return bd.setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
