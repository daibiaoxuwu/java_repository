package com.d.demo;

import java.util.*;

public class Textdetection {

    //两点距离
    public static Double dis(Double[] a, Double[] b) {
        return Math.sqrt(dis_s(a, b));
    }

    //两点距离的平方
    public static Double dis_s(Double[] a, Double[] b) {
        return ((a[0]-b[0])*(a[0]-b[0])+(a[1]-b[1])*(a[1]-b[1]));
    }
    public static boolean lineCross(Double[] a, Double[] b, Double[] c, Double[] d) {
    	if(!(Math.min(a[0],b[0])<=Math.max(c[0],d[0]) && Math.min(c[1],d[1])<=Math.max(a[1],b[1])&&Math.min(c[0],d[0])<=Math.max(a[0],b[0]) && Math.min(a[1],b[1])<=Math.max(c[1],d[1])))//这里的确如此，这一步是判定两矩形是否相交
    	       //1.线段ab的低点低于cd的最高点（可能重合） 2.cd的最左端小于ab的最右端（可能重合）
    	       //3.cd的最低点低于ab的最高点（加上条件1，两线段在竖直方向上重合） 4.ab的最左端小于cd的最右端（加上条件2，两直线在水平方向上重合）
    	       //综上4个条件，两条线段组成的矩形是重合的
    	       /*特别要注意一个矩形含于另一个矩形之内的情况*/
    	       return false;
    	       /*
    	       跨立实验：
    	       如果两条线段相交，那么必须跨立，就是以一条线段为标准，另一条线段的两端点一定在这条线段的两段
    	       也就是说a b两点在线段cd的两端，c d两点在线段ab的两端
    	       */
    	    double u,v,w,z;//分别记录两个向量
    	    u=(c[0]-a[0])*(b[1]-a[1])-(b[0]-a[0])*(c[1]-a[1]);
    	       v=(d[0]-a[0])*(b[1]-a[1])-(b[0]-a[0])*(d[1]-a[1]);
    	       w=(a[0]-c[0])*(d[1]-c[1])-(d[0]-c[0])*(a[1]-c[1]);
    	       z=(b[0]-c[0])*(d[1]-c[1])-(d[0]-c[0])*(b[1]-c[1]);
    	       return (u*v<=0.00000001 && w*z<=0.00000001);
    }
	public static Double[] cal_corrdinate(List<Double> anglelist, List<Double[]> corrdinatelist) {
		double totangle=0.0;
		for(double angle:anglelist) totangle+=angle;
		anglelist.add(360-totangle);
		System.out.println("init");
		final int numOfPois=anglelist.size();
		//大于180°的-180
		for(int i=0;i<numOfPois;++i) {if(anglelist.get(i)>180.0) anglelist.set(i, 360-anglelist.get(i));}
		
		List<Double[][]> circles=new ArrayList<>();//存放圆心。一共有poi个元素，每个元素为[3][2],第一维代表两个圆心，第二维代表xy,[2][0]代表半径
		List<Double[][][][]> results=new ArrayList<>();//存放交点。一共有poi个元素，每个元素为[2][2][2][2],
		//第一,二维代表相交圆的形态，第三维代表两个圆的两个交点，第四维代表xy
		//求出圆心
		for(int poi=0;poi<numOfPois;++poi)
		{
			int nextPoi=(poi+1)%numOfPois;
			
			//取中点
			double midPointX=(corrdinatelist.get(poi)[0]+corrdinatelist.get(nextPoi)[0])/2;
			double midPointY=(corrdinatelist.get(poi)[1]+corrdinatelist.get(nextPoi)[1])/2;
			
			//中点指向圆心的向量（正负）
			double tangent=Math.abs(Math.tan(Math.toRadians(anglelist.get(poi))));//圆心角钝角
			double toCircleX=-(corrdinatelist.get(nextPoi)[1]-corrdinatelist.get(poi)[1])/2/tangent;
			double toCircleY=(corrdinatelist.get(nextPoi)[0]-corrdinatelist.get(poi)[0])/2/tangent;
			
			//两个对称的圆心
			Double[][] circle=new Double[3][2];
			circle[0][0]=midPointX+toCircleX;//圆心0 当a2>a1,b2>b1时，在上面
			circle[0][1]=midPointY+toCircleY;
			circle[1][0]=midPointX-toCircleX;//圆心1 当a2>a1,b2>b1时，在下面
			circle[1][1]=midPointY-toCircleY;
			Double[] midPoint= {midPointX,midPointY};
			circle[2][0]=dis(midPoint,corrdinatelist.get(poi))/Math.sin(Math.toRadians(anglelist.get(poi)));
			circles.add(circle);
			
			/*double x=corrdinatelist.get(poi)[0];
			double y=corrdinatelist.get(poi)[1];
			System.out.println("(x-a1)2+(y-b1)2=r12: "+((x-circle[0][0])*(x-circle[0][0])+(y-circle[0][1])*(y-circle[0][1])-circle[2][0]*circle[2][0]));
			System.out.println("(x-a2)2+(y-b2)2=r22: "+((x-circle[1][0])*(x-circle[1][0])+(y-circle[1][1])*(y-circle[1][1])-circle[2][0]*circle[2][0]));
			System.out.println("distance-r: "+(dis(corrdinatelist.get(poi),circle[0])-circle[2][0]));
			System.out.println("distance2-r: "+(dis(corrdinatelist.get(poi),circle[1])-circle[2][0]));
			x=corrdinatelist.get(nextPoi)[0];
			y=corrdinatelist.get(nextPoi)[1];
			System.out.println("(x2-a1)2+(y2-b1)2=r12: "+((x-circle[0][0])*(x-circle[0][0])+(y-circle[0][1])*(y-circle[0][1])-circle[2][0]*circle[2][0]));
			System.out.println("(x2-a2)2+(y2-b2)2=r22: "+((x-circle[1][0])*(x-circle[1][0])+(y-circle[1][1])*(y-circle[1][1])-circle[2][0]*circle[2][0]));
			System.out.println("distance-r: "+(dis(corrdinatelist.get(poi),circle[0])-circle[2][0]));
			System.out.println("distance2-r: "+(dis(corrdinatelist.get(poi),circle[1])-circle[2][0]));
			*/
			
		}
		
		//两两相交求出焦点
		
		for(int poi=0;poi<numOfPois;++poi)
		{
			int nextPoi=(poi+1)%numOfPois;
			
			//取圆心
			Double[][][][] result=new Double[2][2][2][2];
			for(int poinum=0;poinum<2;++poinum)
				for(int nextnum=0;nextnum<2;++nextnum)
				{
			//		System.out.println("start poi: "+poi+" poinum: "+poinum+" nextnum: "+nextnum);
					double a1=circles.get(poi)[poinum][0];
					double b1=circles.get(poi)[poinum][1];
					double r1=circles.get(poi)[2][0];
					double a2=circles.get(nextPoi)[nextnum][0];
					double b2=circles.get(nextPoi)[nextnum][1];
					double r2=circles.get(nextPoi)[2][0];
			//		System.out.println("a1: "+a1+" b1: "+b1+" r1: "+r1+" a2: "+a2+" b2: "+b2+" r2: "+r2);
					//方程(x-a1)2+(y-b1)2=r12和(x-a2)2+(y-b2)2=r22相减得到Ax+By+C=0的式子，其中A,B,C为
					double A=-2*(a1-a2);
					double B=-2*(b1-b2);
					double C=a1*a1-a2*a2+b1*b1-b2*b2-r1*r1+r2*r2;
			//		System.out.println("ABC: "+A+" "+B+" "+C);
					//代入方程(x-a1)2+(y-b1)2=r12后得到形如AAx2+BBx+CC=0的式子，其中
					double AA=1+A*A/B/B;
					double BB=-2*a1+2*A/B*(C/B+b1);
					double CC=a1*a1+Math.pow(C/B+b1,2)-r1*r1;
			//		System.out.println("AABBCC: "+AA+" "+BB+" "+CC);

					//解出delta=BB2-4AACC
					double delta=BB*BB-4*AA*CC;
					
					
							
					if(delta<0)//返回单值：Ax+By+C=0和(x-a1)/(a2-a1)=(y-b1)/(b2-b1)联立的值
					{
						double x=(-A*C-b1*A*B+a1*B*B)/(A*A+B*B);
						double y=-(A*x+C)/B;
						result[poinum][nextnum][0][0]=x;
						result[poinum][nextnum][0][1]=y;
						result[poinum][nextnum][1][0]=Double.NaN;
						results.add(result);
						System.out.println("(x-a1)/(a2-a1)=(y-b1)/(b2-b1): "+((x-a1)/(a2-a1)-(y-b1)/(b2-b1)));
						System.out.println("delta: "+delta);
						System.out.println("-BB/2/AA: "+(-BB/2/AA));
					}
					else //返回两个交点
					{
						double sqrtdelta=Math.sqrt(delta);
						
						double x=(-BB+sqrtdelta)/2/AA;
						double y=-(A*x+C)/B;
						result[poinum][nextnum][0][0]=x;
						result[poinum][nextnum][0][1]=y;
											
						x=(-BB-sqrtdelta)/2/AA;
						y=-(A*x+C)/B;
						result[poinum][nextnum][1][0]=x;
						result[poinum][nextnum][1][1]=y;
						
						
						/*System.out.println("(x-a1)*(x-a1)+(y-b1)*(y-b1)=r1*r1: "+((x-a1)*(x-a1)+(y-b1)*(y-b1)-r1*r1));
						System.out.println("(x-a2)*(x-a2)+(y-b2)*(y-b2)=r2*r2: "+((x-a2)*(x-a2)+(y-b2)*(y-b2)-r2*r2));
						System.out.println("crosspoint: "+Arrays.deepToString(result[poinum][nextnum]));
						System.out.println("sharedpoint: "+Arrays.toString(corrdinatelist.get(nextPoi)));
*/
						
						for(int check=0;check<2;++check)//两个交点中肯定有一个是nextPoi点
						{
							if(dis_s(result[poinum][nextnum][check],corrdinatelist.get(nextPoi))<1E-10)
							{
					//			System.out.println("delete for sharedpoint: "+poinum+" "+nextnum+" "+check+" "+Arrays.toString(result[poinum][nextnum][check]));
								result[poinum][nextnum][check][0]=Double.NaN;
								continue;
							}
							
						/*	Double[] circle=circles.get(poi)[poinum];
							Double[] line1=corrdinatelist.get(poi);
							Double[] line2=corrdinatelist.get(nextPoi);
							double angle=anglelist.get(poi);
							boolean ans=lineCross(circle,result[poinum][nextnum][check],line1,line2);
							if(ans==true && angle<60 || ans==false && angle>120)
							{
								System.out.println("delete for wrongdir: "+poi+poinum+nextnum+check+" "+Arrays.toString(result[poinum][nextnum][check]));
								result[poinum][nextnum][check][0]=Double.NaN;
								continue;
							}
							
							int morePoi=(nextPoi+1)%numOfPois;
							circle=circles.get(nextPoi)[nextnum];
							line1=corrdinatelist.get(nextPoi);
							line2=corrdinatelist.get(morePoi);
							angle=anglelist.get(nextPoi);
							ans=lineCross(circle,result[poinum][nextnum][check],line1,line2);
							if(ans==true && angle<60 || ans==false && angle>120)
							{
								System.out.println("delete for wrongdir: "+poi+poinum+nextnum+check+" "+Arrays.toString(result[poinum][nextnum][check]));
								result[poinum][nextnum][check][0]=Double.NaN;
								continue;
							}*/
							
							  
							/*
							//TODO:判断两边的钝角锐角 下面的会排除全部的。
							
							//判断两边的钝角锐角
							x=result[poinum][nextnum][check][0];
							y=result[poinum][nextnum][check][1];
							double x1=corrdinatelist.get(poi)[0];
							double y1=corrdinatelist.get(poi)[1];
							double x2=corrdinatelist.get(nextPoi)[0];
							double y2=corrdinatelist.get(nextPoi)[1];
							
							double updown=((y-y1)/(y2-y1)-(x-x1)/(x2-x1));//为正，则在a1b1-a2b2直线上面，否则在下面(当b2>b1,a2>a1时)
							if((updown>0 ^ poinum==0) && anglelist.get(poi)<60.0 || (updown>0 ^ poinum==1) && anglelist.get(poi)>120.0) //(updown>0 ^ poinum==1)若真，点和圆心同侧
							{
								System.out.println("delete for wrongdir: "+check+" "+Arrays.toString(result[poinum][nextnum][check]));
								result[poinum][nextnum][check][0]=Double.NaN;
								continue;
							}
							
							
							
							x1=corrdinatelist.get(nextPoi)[0];
							y1=corrdinatelist.get(nextPoi)[1];
							x2=corrdinatelist.get(morePoi)[0];
							y2=corrdinatelist.get(morePoi)[1];
							
							updown=((y-y1)/(y2-y1)-(x-x1)/(x2-x1));//为正，则在a1b1-a2b2直线上面，否则在下面(当b2>b1,a2>a1时)
							if((updown>0 ^ nextnum==0) && anglelist.get(nextPoi)<60.0 || (updown>0 ^ nextnum==1) && anglelist.get(nextPoi)>120.0) //(updown>0 ^ poinum==1)若真，点和圆心同侧
							{
								System.out.println("delete for 2wrongdir: "+check+" "+Arrays.toString(result[poinum][nextnum][check]));
								result[poinum][nextnum][check][0]=Double.NaN;
							}*/
						}
					}
				}
			results.add(result);
		}
		System.out.println("ls"+results.size());
		//这样每个圆形态得到了最多6个交点。在每个圆形态选择最好的一组
		double min=Double.POSITIVE_INFINITY;
		int resultPointer=-1;//表示6个点中的哪3个点
		int minpoinum=-1;
		int minnextnum=-1;
		for(int poinum=0;poinum<2;++poinum)
			for(int nextnum=0;nextnum<2;++nextnum)//确定圆姿态
			{
				for(int pointer=5;pointer<(1<<numOfPois);++pointer)//2^3,选择哪边的交点
				{
					double max=0;
					for(int poi=0;poi<numOfPois;++poi)//计算最大边
					{
						int nextPoi=(poi+1)%numOfPois;
						
						Double[] crossPoint=results.get(poi)[poinum][nextnum][(pointer&(1<<poi))>>poi]; 
						if(Double.isNaN(crossPoint[0])) {max=0;break;} 
					//	else System.out.println("poi:"+poi+ " "+poinum+" "+nextnum+" "+pointer+" "+Arrays.toString(crossPoint));
						Double[] nextPoint=results.get(nextPoi)[poinum][nextnum][(pointer&(1<<nextPoi))>>nextPoi];	
						if(Double.isNaN(nextPoint[0])) {max=0;break;}
					//	else System.out.println("nextpoi:"+poi+ " "+poinum+" "+nextnum+" "+pointer+" "+Arrays.toString(nextPoint));
						
						max=Math.max(max, dis_s(crossPoint,nextPoint));
						System.out.println(max);
					}
					if(max!=0 && max<min)
					{
						min=max;
						resultPointer=pointer;
						minpoinum=poinum;
						minnextnum=nextnum;
						
				//		System.out.println("res: "+min+" "+poinum+" "+nextnum+" "+resultPointer);
					}
					System.out.println("res: "+min+" "+max+" "+poinum+" "+nextnum+" "+resultPointer);
				}
			}
		Double midPointX=0.0;
		Double midPointY=0.0;
		System.out.println("res: "+min+" "+resultPointer);
		for(int poi=0;poi<numOfPois;++poi)//计算最大边
		{
			midPointX+=results.get(poi)[minpoinum][minnextnum][(resultPointer&(1<<poi))>>poi][0];
			midPointY+=results.get(poi)[minpoinum][minnextnum][(resultPointer&(1<<poi))>>poi][1];
		}
		Double[] midPoint= {midPointX/numOfPois, midPointY/numOfPois};
	    return midPoint;
	}
	
	public static void main(String[] args) {
		
//		final List<Double[]> corrdinatelist=new ArrayList<>();
//		Double[] location5= {-5.0,10.0};corrdinatelist.add(location5);//中国黄金
//		Double[] location6= {11.0,8.2};corrdinatelist.add(location6);//UNIQLO
//		//Double[] location1= {4.0,1.0};corrdinatelist.add(location1);//LOVE
//		Double[] location2= {0.0,-8.0};corrdinatelist.add(location2);//BHG
//		Double[] location3= {-8.0,-8.5};corrdinatelist.add(location3);//COMELY
//		Double[] location4= {-12.5,-5.0};corrdinatelist.add(location4);//VERO
//		final List<Double> anglelist=new ArrayList<>();
//		anglelist.add(90.1);
//		anglelist.add(43.3);
//		anglelist.add(24.8);
		
//		final List<Double[]> corrdinatelist=new ArrayList<>();
//	
//		Double[] location6= {11.0,8.2};corrdinatelist.add(location6);//UNIQLO
//		//Double[] location1= {4.0,1.0};corrdinatelist.add(location1);//LOVE
//		Double[] location2= {0.0,-8.0};corrdinatelist.add(location2);//BHG
//		Double[] location3= {-8.0,-8.5};corrdinatelist.add(location3);//COMELY
//	//	Double[] location4= {-12.5,-5.0};corrdinatelist.add(location4);//VERO
//		Double[] location5= {-5.0,10.0};corrdinatelist.add(location5);//中国黄金
//		final List<Double> anglelist=new ArrayList<>();
//		anglelist.add(136.0);
//		anglelist.add(46.0);
//		anglelist.add(88.0);
		
		final List<Double[]> corrdinatelist=new ArrayList<>();
//		Double[] location5= {-5.0,10.0};corrdinatelist.add(location5);//中国黄金
//		Double[] location6= {11.0,8.2};corrdinatelist.add(location6);//UNIQLO
		Double[] location1= {Math.random(),Math.random()};corrdinatelist.add(location1);//LOVE
		Double[] location2= {Math.random(),Math.random()};corrdinatelist.add(location2);//BHG
		Double[] location3= {Math.random(),Math.random()};corrdinatelist.add(location3);//COMELY
		Double[] location4= {Math.random(),Math.random()};corrdinatelist.add(location4);//VERO
		final List<Double> anglelist=new ArrayList<>();
		anglelist.add(Math.random()*90);
		anglelist.add(Math.random()*90);
		anglelist.add(Math.random()*90);
		
		System.out.println(Arrays.toString(cal_corrdinate(anglelist,corrdinatelist)));

	}

}
