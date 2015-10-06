package com.zjl.mockgps.app.Algorithm;

import android.util.Log;
import com.zjl.mockgps.app.Model.Coodinate;
import com.zjl.mockgps.app.Common.CollectionExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C0dEr on 15/8/21.
 */
public class CEAlgorithm {
    public final boolean Debug = true;

    public double dis = 0;

    public double points = 0;
    /**
     * 原始坐标点
     */
    private List<Coodinate> originalCood;
    /**
     * 常量 用于调整生成点的空隙(0.0~1.0 不包含0)
     */
    private float BasicC = 1;

    private List<Coodinate> Coodinates;

    public void setOriginalCood(List<Coodinate> cood) {
        this.originalCood = cood;
    }

    public void setBasicC(float mutiC) {
        BasicC = mutiC * BasicC;
    }

    public List<Coodinate> expand() {
        if (CollectionExtension.IsNullOrEmpty(originalCood)) {
            return null;
        }
        LinearEquationUtils util = new LinearEquationUtils();
        Coodinates = new ArrayList<Coodinate>();
        for (int i = 0; i < originalCood.size() - 2; i++) {
            double distance = PositionUtils.GetDistance(originalCood.get(i).longitude
                    , originalCood.get(i).latitude,
                    originalCood.get(i + 1).longitude,
                    originalCood.get(i + 1).latitude);
            if (distance == 0) {
                continue;
            }

            int points = (int) (distance * BasicC);
            if (Debug) {
                dis += distance;
                this.points += points;
            }
            util.MakeEquation(originalCood.get(i).longitude
                    , originalCood.get(i).latitude,
                    originalCood.get(i + 1).longitude,
                    originalCood.get(i + 1).latitude);
            double a = util.getAvargeX(points);
            if (a > 0) {
                Coodinates.add(originalCood.get(i));
                for (int j = 1; j < points; j++) {
                    switch (util.Type) {
                        case 0:
                            Coodinates.add(new Coodinate(originalCood.get(i).longitude+ j * a,
                                    util.returnY(originalCood.get(i).longitude + j * a)));
                            continue;
                        case 1:
                            Coodinates.add(new Coodinate(originalCood.get(i).longitude,
                                    originalCood.get(i).latitude + j * a));
                            continue;
                        case 2:
                            Coodinates.add(new Coodinate(originalCood.get(i).longitude + j * a,
                                    originalCood.get(i).latitude));
                            continue;
                    }
                }
            }


        }
        return Coodinates;
    }


}
