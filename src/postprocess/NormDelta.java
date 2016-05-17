/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postprocess;

import main.MyDate;

/**
 *
 * @author mabarthe
 */

public class NormDelta
{
    public MyDate start;
    public MyDate stop;
    public double average;
    
    public double getTimeDiffMSec()
    {
        return stop.getTime() - start.getTime();
    }

    public double getTimeDiffSec()
    {
        return getTimeDiffMSec() / 1000;
    }
    
    public double getTimeDiffDay()
    {
        return getTimeDiffSec() / 60 / 60 / 24;
    }
    
    public MyDate getTimeMiddle()
    {
        return new MyDate( (start.getTime() + stop.getTime()) / 2);
    }
}    

