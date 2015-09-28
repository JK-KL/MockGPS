package com.zjl.mockgps.app.Algorithm;

/**
 * Created by C0dEr on 15/9/26.
 * 计算两间距离
 * X坐标为longitude,Y坐标为latitude
 */
public class LinearEquationUtils {
    private double K; //斜率
    private double C;//常数
    private double A;//斜率角
    public double x;//∆x

    public int Type ;//判断直线类型0普通直线,1垂直x轴直线,2垂直y轴直线,3同一点
    public double Answer; //计算结果

    private double Y2;
    private double Y1;
    private double X2;
    private double X1;

    public LinearEquationUtils() {

    }

    public LinearEquationUtils(double x1, double y1, double x2, double y2) {
        MakeEquation(x1, y1, x2, y2);
        this.Y1 = y1;
        this.Y2 = y2;
        this.X1 = x1;
        this.X2 = x2;
    }

    /**
     * 计算方程斜率以及常数
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void MakeEquation(double x1, double y1, double x2, double y2) {
        if (x1 != x2 && y1 != y2) {
            this.K = (y2 - y1) / (x2 - x1);
            this.C = y1 - K * x1;
            this.A = Math.atan(this.K);
            this.Type = 0;
        } else if (x1 == x2 && y1 != y2) {
            this.K = 0;
            this.C = x1;
            this.Type = 1;
            this.A = Math.toRadians(90);
        } else if (x1 != x2 && y1 == y2) {
            this.K = 0;
            this.C = y1;
            this.Type = 2;
            this.A = Math.toRadians(0);
        } else {
            this.K = 0;
            this.C = 0;
            this.Type = 3;
            this.A = Math.toRadians(0);
        }
        this.Y1 = y1;
        this.Y2 = y2;
        this.X1 = x1;
        this.X2 = x2;
    }

    /**
     * 返回X值
     *
     * @param y
     * @return
     */
    public double returnX(double y) {
        switch (this.Type) {
            case 0:
                Answer = (y - C) / K;
                break;
            case 1:
                Answer = C;
                break;
            case 2:
                throw new IllegalArgumentException("Not In this Line");
            case 3:
                Answer = X1;
        }
        return Answer;
    }

    /**
     * 返回Y值
     *
     * @param x
     * @return
     */
    public double returnY(double x) {
        switch (this.Type) {
            case 0:
                Answer = K * x + C;
                break;
            case 1:
                throw new IllegalArgumentException("Not In this Line");
            case 2:
                Answer = C;
            case 3:
                Answer = Y1;
        }
        return Answer;
    }

    /**
     * 计算两点间距离
     *
     * @return
     */
    private double returnLengthBetweenPoints() {
        Answer = Math.sqrt(Math.pow((Y2 - Y1), 2) + Math.pow((X2 - X1), 2));
        return Answer;
    }

    /**
     * 计算平均每段△X值
     *
     * @param points
     * @return
     */
    public double getAvargeX(int points) {
        double length = returnLengthBetweenPoints();
        double avargeLength = length / points;
        if ((int) Math.toDegrees(this.A) != 90) {
            x = avargeLength * Math.cos(this.A);
        } else {
            x = avargeLength;
        }
        return x;
    }

}
