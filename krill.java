/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package krill;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Jain Austin
 */
import java.util.*;
public class krill 
{
    public static void main(String args[])
    {
//------Initial Parameter Setting

        int NR = 10;
        int NK = 25;
        int MI = 200;
        int C_flag = 1;

        double[] UB = new double[10];
        double[] LB = new double[10];

        for(int i=0;i<10;i++)
        {
            UB[i] = 10;
            LB[i] = -10;
        }

        int NP = LB.length;
        double Dt = 10;

        double Vf = 0.02; 
        double Dmax = 0.005; 
        double Nmax = 0.01; 
        double Sr = 0;
        double [][] F=new double [NP][NK];
        double [][] N=new double [NP][NK];
        double [][] D=new double [1][NK];


        for(int i=0;i<NP;i++)
        {
                for(int j=0;j<NK;j++)
                {
                        F[i][j] = 0;
                        N[i][j] = 0; 
                        D[0][j] = 0;
                }

        } 
        System.out.println("Length - "+NP);
         
//------Optimization
         
        double X[][]=new double[NP][NK];
        double K[][]=new double[NK][NK];
        double RR[][]=new double[NP][NK];
        double Xgb[]=new double[NR];
        double Xsum[]=new double[NR];
        double Ksum[]=new double[NR];
        
        double Sf[] = new double[NP];
        double Xf[] = new double[NP];
        double Kf[] = new double[NP];
        double Rf[] = new double[NP];
        double R[] = new double[NK];
        
        double Rgb[]=new double[NP];
        
        double alpha_b[] = new double[NP];
        double Beta_f[] =new double[NR];
        double Beta_b[] =new double[NR];
        double Rib[] =new double[NR];
        
        double smallest = K[0][0];
        double sum=0.0;
        double largetst = K[0][0];
        double kgb[]=new double[NR];
        
        double Kw_Kgb,w;
        
        int nn=0;
        double ds;
        double alphan = 0.0;
        double rnd1,rnd2,rnd3;
        int nr,z3;
        
        Cost cst=new Cost();
        Random rn = new Random();
        
        for (nr=0;nr<NR;nr++)
        {
            for (int z1=0;z1<NK;z1++)
            {
                rnd1 = rn.nextInt(NK);
                X[nr][z1] = LB[nr] +(UB[nr] - LB[nr])*rnd1;
                sum = sum + X[nr][z1];
                System.out.println(X[nr][z1]);
            }
            Xsum[nr] = sum;
            sum=0.0;
            //System.out.println("---");
            
            for (int z1=0;z1<NK;z1++)
            {
                K[nr][z1]=cst.cost_cal(X[nr][z1]);
                sum = sum + X[nr][z1];
                //System.out.print(K[nr][z1]+ " ");
            }
            Xgb[nr] =K[nr][0];
            Ksum[nr] =sum;
            sum=0.0;
            //System.out.println("---");
           
            for (int z2=0; z2<NR;z2++)
            {
                for (int z1=0;z1<NK;z1++)
                {
                     
                     if(K[z2][z1] > largetst)
                                largetst = K[z2][z1];
                     else if (K[z2][z1] < smallest)
                                kgb[z2] = K[z2][z1];
                     
                }
            }
            for (z3=0;z3<NP;z3++)
            {
                //------------Virtual Food
                
                for (int z4=0;z4<NP;z4++)
                {
                    Sf[z4] = Xsum[z4]/Ksum[z4];
                    Xf[z4] = Sf[z4]/Ksum[z4];
                }
                
                //------------Food Location 
            
                for (int z5=0;z5<NP;z5++)
                {
                    Kf[z5]=cst.cost_cal(Xf[z5]);
                }
                //System.out.println("-------");
                Kw_Kgb = largetst - kgb[nr];
                w = (0.1+0.8*(1-z3/MI));
                
                //-------------Calculation of distances
                
                for (int z6=0;z6<NK;z6++)
                {
                        Rf[z3] = Xf[z3]-X[z3][z6];
                        Rgb[z3] = Xgb[nr]-X[z3][z6];
                
                        for(int ii=0;ii<NK;ii++)
                        {
                            RR[z3][ii] = Math.sqrt(X[z3][ii]);
                            //System.out.println(RR[z3][ii]);
                            R[nr] = Math.sqrt(RR[z3][ii]);
                        }

                        //-------------Calculation of BEST KRILL effect
                        rnd2 = rn.nextDouble();
                        if(kgb[nr] < K[z3][nr])
                        {
                            alpha_b[z3] =-2*(1+rnd2*(z3/MI))*(kgb[nr] - K[z3][nr]) /Kw_Kgb/Math.sqrt(Rgb[z3]);
                        }
                        else
                        {
                            alpha_b[z3]=0;
                        } 

                        //------------Calculation of NEIGHBORS KRILL effect
                        ds = R[nr]/5;
                        for(int z7=0;z7<NK;z7++)
                        {
                           if ((R[nr]<ds) && (z7!=z6))
                           {
                                nn=nn+1;
                                if (nn<=4 && K[z3]!=K[z6])
                                {
                                    alphan = (alphan-(K[z3][nr]-K[z6][nr])) /Kw_Kgb /(R[nr]*R[nr]);
                                }
                           }
                        }
                        
                        //-----------Movement Induced
                        for(int i=0;i<NP;i++)
                        {
                                for(int j=0;j<NK;j++)
                                { 
                                        N[i][j] = w * N[i][j]* Nmax*(alpha_b[z3]*alphan);
                                }
                        } 
                        //-----------Foraging Motion
                        //-----------Calculation of FOOD attraction
                        
                        if (Kf[z3]<K[z3][nr])
                        {
                            Beta_f[z3]=(-2*(1-z3/MI)*(Kf[z3] - K[z3][nr]) /Kw_Kgb/ (Math.sqrt(Rf[z3]*Rf[z3])) * Rf[z3]);
                        }
                        else
                        {
                            Beta_f[z3]=0;
                        }
                        
                        //------------Calculation of BEST position attraction
                        
                        if (K[z3][nr] < K[z3][nr])
                        {
                            Beta_b[z3]=-(K[z3][nr] - K[z3][nr]) /Kw_Kgb;
                        }
                        else
                        {
                            Beta_b[z3]=0;
                        }
                        //-------------Foraging Motion
                        for(int i=0;i<NP;i++)
                        {
                                for(int j=0;j<NK;j++)
                                {
                                        F[i][j] = w*F[i][j]+Vf*(Beta_b[z3]*Beta_f[z3]);
                                }

                        } 
                        
                        
                        //-------------Physical Diffusion

                        rnd3 = rn.nextDouble();
                        for(int j=0;j<NK;j++)
                        {
                                D[0][j] = Dmax * (1-z3/MI)*Math.floor(rnd3+(K[z3][nr])/Kw_Kgb)*(2*rnd3*(NP));
                        }              
                }
                //-------------Update the position
                for (int z1=0;z1<NK;z1++)
                {
                    K[nr][z1]=cst.cost_cal(X[nr][z1]);
                }
            }
        }
        System.out.println("KRILL Values");
        for (int z2=0; z2<NR;z2++)
        {
            System.out.println("Min->"+kgb[z2]);
        }
        double mValue=0.0;
        int Ron_No=0;
                
        for (int i = 1; i < kgb.length; i++) 
        {
            if (kgb[i] < mValue) 
            {
                mValue = kgb[i];
                Ron_No =i;
            }
        }
        System.out.println("KRILL Value and Index");
        System.out.println("======================");
        System.out.println("Best K = "+ mValue);
        System.out.println("Index = "+ Ron_No);
        System.out.println("======================");
    }
    
}

