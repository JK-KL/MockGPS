package com.zjl.mockgps.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by C0dEr on 15/8/21.
 */
public class CEAlgorithm {
    private List<Coodinate> originalCood;//原始坐标点
    private int distance = 0;//该段路程距离
    private int avargePoint = 0;//平均两个坐标之间插入做标数

    public void setDistance(int distance) {
        this.distance = distance;
        if (originalCood != null && originalCood.size() > 0) {
            avargePoint = distance / originalCood.size();
        }
    }

    public void setOriginalCood(List<Coodinate> cood) {
        this.originalCood = cood;
        if (distance != 0 && originalCood.size() > 0) {
            avargePoint = distance / originalCood.size();
        }
    }

    public List<Coodinate> expand(ExpansionType type) {
        List<Coodinate> expandedCood = new ArrayList<Coodinate>();
        if (originalCood == null || originalCood.size() == 0) {
            throw new IllegalArgumentException("The Orignal Points Must Be Initialized");
        }
        switch (type) {
            case TIGHT:
                break;
            case LOOSE:
                break;
            case STANDARD:
                break;
        }
        return expandedCood;

    }

    //设置
    private enum ExpansionType {
        TIGHT, LOOSE, STANDARD
    }

    private class Slope {
        public BigDecimal K;
        public BigDecimal C;

        Slope() {

        }

        Slope(double y1, double x1, double y2, double x2) {
            getKC(y1, x1, y2, x2);
        }

        public void getKC(double y1, double x1, double y2, double x2) {
            BigDecimal Y1 = new BigDecimal(y1);
            BigDecimal X1 = new BigDecimal(x1);
            BigDecimal Y2 = new BigDecimal(y2);
            BigDecimal X2 = new BigDecimal(x2);
            this.K = Y2.subtract(Y1).divide(X2.subtract(X1), 8, RoundingMode.HALF_UP);
            this.C = Y1.subtract(X1.multiply(K));
        }

        public BigDecimal returnX(BigDecimal Y) {
            return (Y.subtract(C)).divide(K, 8, RoundingMode.HALF_UP);
        }

        public BigDecimal returnY(BigDecimal X) {
            return X.multiply(K).add(C);
        }
    }
}
