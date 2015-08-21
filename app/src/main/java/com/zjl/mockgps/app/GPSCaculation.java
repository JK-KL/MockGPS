package com.zjl.mockgps.app;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by C0dEr on 15/8/10.
 */
public class GPSCaculation {
    public BigDecimal K;
    public BigDecimal C;

    public void cacu(double y1, double x1, double y2, double x2) {
        BigDecimal Y1 = new BigDecimal(y1);
        BigDecimal X1 = new BigDecimal(x1);
        BigDecimal Y2 = new BigDecimal(y2);
        BigDecimal X2 = new BigDecimal(x2);
        this.K = Y2.subtract(Y1).divide(X2.subtract(X1),8, RoundingMode.HALF_UP);
        this.C = Y1.subtract(X1.multiply(K));

    }

    public BigDecimal returnX(BigDecimal Y) {
        return (Y.subtract(C)).divide(K, 8, RoundingMode.HALF_UP);
    }

    public BigDecimal returnY(BigDecimal X) {
        return X.multiply(K).add(C);
    }

}
