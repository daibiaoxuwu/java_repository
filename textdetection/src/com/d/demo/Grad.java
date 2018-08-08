package com.d.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grad {//验证关于无解的情况

    public static Double dis(Double[] a, Double[] b) {
        return Math.sqrt(dis_s(a, b));
    }
    public static Double dis_s(Double[] a, Double[] b) {
        return ((a[0]-b[0])*(a[0]-b[0])+(a[1]-b[1])*(a[1]-b[1]));
    }
    public static void cal_angle(List<Double[]> corrdinatelist, Double[] answer, List<Double> anglelist)
    {
    	double firstangle=0.0;
    	int flag=-1;
		for(Double[] point:corrdinatelist)
		{
			double diffx=point[0]-answer[0];
			double diffy=point[1]-answer[1];
			double angle=Math.toDegrees(Math.atan(diffy/diffx));
			if(diffx<0) angle+=180;
			if(diffy<0 && diffx>0) angle+=360;
			//System.out.println(angle);
			if(firstangle!=0.0)
			{
				double diffangle=-angle+firstangle;
				if(diffangle<0.0) diffangle+=360.0;
				System.out.println("answerangle: "+diffangle+" diff: "+Math.abs(Math.cos(Math.PI/180*anglelist.get(flag))-Math.cos(Math.PI/180*diffangle)));
			}
			firstangle=angle;
			++flag;
			
			
		}
    }
	 public static Double[] grad_descent(List<Double> anglelist, List<Double[]> corrdinatelist, Double x, Double y) {
	        Double a = 0.0000001;
	        Double end = 0.00000001;
	        int flag = 1;
	        while (true) {
	            Double dx = 0.0;
	            Double dy = 0.0;
	            Double[] co = {x, y};
	            for (int i = 0; i < anglelist.size(); i++) {
	                Double a2 = dis_s(corrdinatelist.get(i), corrdinatelist.get(i+1));
	                Double b2 = dis_s(corrdinatelist.get(i+1), co);
	                Double c2 = dis_s(corrdinatelist.get(i), co);
	                Double d = 2 * ((b2 + c2 - a2) / (2 * Math.sqrt(b2) * Math.sqrt(c2) + 0.001) - Math.cos(3.1415926 * anglelist.get(i) / 180));
	                dx = dx + d * (-4 * (corrdinatelist.get(i)[0] + corrdinatelist.get(i+1)[0] - x * 2) * Math.sqrt(b2) * Math.sqrt(c2) - 2 * (b2 + c2 - a2) * (-1* ((Math.sqrt(c2) / (Math.sqrt(b2) * (corrdinatelist.get(i+1)[0] - x) + 0.001)) + (Math.sqrt(b2) / (Math.sqrt(c2) * (corrdinatelist.get(i)[0] - x) + 0.001)))));
	                dy = dy + d * (-4 * (corrdinatelist.get(i)[1] + corrdinatelist.get(i+1)[1] - y * 2) * Math.sqrt(b2) * Math.sqrt(c2) - 2 * (b2 + c2 - a2) * (-1 * ((Math.sqrt(c2) / (Math.sqrt(b2) * (corrdinatelist.get(i+1)[1] - y) + 0.001)) + (Math.sqrt(b2) / (Math.sqrt(c2) * (corrdinatelist.get(i)[1] - y) + 0.001)))));
	            }
	            if (Math.abs(a*dx)<end && Math.abs(a*dy)<end) {
	                break;
	            }
	            flag++;
	            x = x - a * dx;
	            y = y - a * dy;
	        }
	        Double[] ans={x, y};
	        return ans;
	    }
	 public static void main(String[] args) {
			// TODO Auto-generated method stub
			final List<Double[]> corrdinatelist=new ArrayList<>();
			Double[] location2= {-5.0,10.0};corrdinatelist.add(location2);//HUAWEI
			Double[] location7= {11.0,8.2};corrdinatelist.add(location7);//MISHA
			Double[] location5= {4.0,0.5};corrdinatelist.add(location5);//CITY
			
			final List<Double> anglelist=new ArrayList<>();
			anglelist.add(71.31687499999998);
			anglelist.add(24.47015299999999);
			
			final Double[] truth= {0.0,0.0};
			cal_angle(corrdinatelist,truth,anglelist);
			
			for(double startx=-4.0;startx<=4.0;++startx)
				for(double starty=-4.0;starty<=4.0;++starty)
				{
					
					Double[] answer=grad_descent(anglelist,corrdinatelist,startx,starty);
					System.out.println("ans:"+startx+" "+starty+" "+Arrays.toString(answer));
					cal_angle(corrdinatelist,answer,anglelist);
				}
			
			
				
		
			

		}

}
