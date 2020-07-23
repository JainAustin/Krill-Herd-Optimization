/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package krill;

/**
 *
 * @author Jain Austin
 */
public class Cost {
    public double cost_cal(double X)
 {
    int n = 10;
    int a = 20; 
    double b = 0.2; 
    double c = 2*3.14;
    double xx = 0.0;
    double yy = 0.0;
    double s1 = 0;
    double s2 = 0;
    double f = 0.0;
    
    s1 = s1+X*2;
    s2 = s2+Math.cos(c*X);
    xx = (double)1/n*s1;
    yy= (double)1/n*s2;
    f = (double)yy-xx;
    return(f);
    
 }
    
}
