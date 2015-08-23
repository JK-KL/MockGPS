package com.zjl.mockgps.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C0dEr on 15/8/21.
 */
public class CEAlgorithm {
    private List<Coodinate> originalCood;//原始坐标点


    private Formula mFormula = Formula.EUCLIDEAN;

    private enum Formula {
        EUCLIDEAN
    }

    private class Euclidean {
        public double K;//斜率
        public double C;//常数
        private double virtualDistance = 0;//该段路程的计算距离
        private double actualDistance = 0;//该段路程的实际距离
        private double speed = 1;//走完该路程的平均速度,单位m/s
        private double avargeDistance = 0;//平均距离
        private int avargePoints = 0;
        private double distancePerPoint = 0;

        private void init() {
            if (originalCood.size() != 0 && originalCood != null) {
                double sum = 0;
                Euclidean euclidean = new Euclidean();
                for (int i = 0; i < originalCood.size() - 2; i++) {
                    sum += euclidean.setVirtualDistance(originalCood.get(i).longitude, originalCood.get(i).latitude,
                            originalCood.get(i + 1).longitude, originalCood.get(i + 1).latitude);
                }
                this.virtualDistance = sum;
                this.avargeDistance = sum / originalCood.size();
                this.avargePoints = (int) actualDistance / (originalCood.size() - 1);
                this.distancePerPoint = avargeDistance / avargePoints;
            }
        }

        Euclidean() {
            init();
        }

        Euclidean(double y1, double x1, double y2, double x2) {
            setKC(y1, x1, y2, x2);
            init();
        }

        private double setVirtualDistance(double y1, double x1, double y2, double x2) {
            this.virtualDistance = Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
            return virtualDistance;
        }

        public void setKC(double y1, double x1, double y2, double x2) {
            this.K = (y2 - y1) / (x2 - x1);
            this.C = y1 - (x1 * K);
        }

        public double returnX(double l, double y) {
            return (y - C) / K;
        }

        public double returnY(double L, double x) {
            return x * K + C;
        }

        public List<Coodinate> euclideanExpand() {
            List<Coodinate> expandedCood = new ArrayList<Coodinate>();
            for (int i = 0; i < originalCood.size() - 2; i++) {

            }
            return null;
        }
    }

    public void setOriginalCood(List<Coodinate> cood) {
        this.originalCood = cood;
    }


    public List<Coodinate> expand() {

        if (originalCood == null || originalCood.size() == 0) {
            throw new IllegalArgumentException("The Orignal Points Must Be Initialized");
        }
        switch (mFormula) {
            case EUCLIDEAN:
                break;

        }
        return null;
    }


}
