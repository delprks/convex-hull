
/**
 * Daniel Parks // delprks@gmail.com // 15.06.2005 // convexHull.java
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/

package convexhull;

import java.awt.*; //Point,Graphics
import java.awt.event.*; //interface
import javax.swing.*; //JPanel,JFrame
import java.lang.Math;
import javax.swing.JButton;
import java.util.*;

public class convexHull extends JPanel
        implements MouseMotionListener, MouseListener, ActionListener

{
    final static int max = 1000;
    Point[] p = new Point[max + 1]; //so that we can put the bottob point in the array as a terminal
    Point[] array = new Point[max + 1];

    double[] angleF = new double[1000];
    double[] angleS = new double[1000];
    //double [] angle=new double [20];

    int ncount = 0;
    int psx, psy, px, py, px1, py1;
    double angle0, angle1;
    boolean got_one = false;
    int index = -1;
    int myDragButton;
    boolean convex = false;

    //buttons
    JButton buttonsort = new JButton("sort points");
    JButton convexbutton = new JButton("convex hull");
    JButton closebutton = new JButton("close");

    //adding buttons to screen
    public convexHull() {
        addMouseListener(this);
        addMouseMotionListener(this);
        add(buttonsort);
        buttonsort.addActionListener(this);
        add(convexbutton);
        convexbutton.addActionListener(this);
        add(closebutton);
        closebutton.addActionListener(this);
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        String s = e.getActionCommand();

        //sort button
        if (s.equals(buttonsort.getText())) {
            System.out.println("Sort Button Pressed");
            sortPoints();
        } else

            //convex hull button
            if (s.equals(convexbutton.getText())) {
                System.out.println("Convex Button Pressed");
                convexHullSort();
                repaint();
            } else

                //close button
                if (s.equals(closebutton.getText())) {
                    System.exit(0);
                }
    }

    //sort points
    public void sortPoints() {
        MyComparator com = new MyComparator();
        Arrays.sort(p, 0, ncount, com);// , com);
        repaint();
    }

    public void sortPoints(Point[] a) {
        MyComparator com = new MyComparator();
        Arrays.sort(a, 0, ncount, com);// , com);
        //repaint();
    }

    /************************ Convex Hull ***************************
     ****************************************************************/

    int temp = ncount;

    public void convexHullSort() {
        //sort the point according to the Y coordinate
        //sortPoints();

        for (int i = 0; i < ncount; i++) {
            array[i] = p[i];
        }

        sortPoints(array);

        //the first element of "array" is the same one in the "p" array
        array[ncount] = array[0];
        int pxi;
        int pyi;
        int pxj;
        int pyj;
        boolean down = false;
        for (int i = 0; i < ncount; i++) {
            pxi = array[i].x;
            pyi = array[i].y;
            double min = 8;

            for (int j = i + 1; j < ncount + 1; j++) {
                if (i == 0 && j == ncount) break;//
                pxj = array[j].x;
                pyj = array[j].y;

                angle0 = Math.atan2(pyi - pyj, pxj - pxi);

                if (down == true) {
                    System.out.println("down true");
                    if (angle0 >= 0.0) {
                        continue;
                    }//if point is above
                }

                if (angle0 < 0.0) {
                    angle0 += 2 * Math.PI;
                }

                if (angle0 < min) {
                    min = angle0;
                    temp = j;
                }

                System.out.println("i=" + i + "  j=" + j + "  angle0=" + (int) (angle0 / (2 * Math.PI) * 360) + "  min= " + (int) (min / (2 * Math.PI) * 360));
            }

            if (min > Math.PI) {
                down = true;
            }
            //swap
            Point p0 = array[i + 1];
            array[i + 1] = array[temp];
            array[temp] = p0;
            if (temp == ncount) {
                temp = i + 1;
                System.out.println("temp=" + temp);
                break;
            }
        }

        //the last element of "array", should be the same as its first element
        //array[ncount]=array[0];

        System.out.println("temp=" + temp);

        //prints out the elements of "array"
        //for (int i=0; array[i]!=null; i++){
        for (int i = 0; i <= temp; i++) {
            System.out.println(array[i]);
        }

        //this boolean is used later to draw the convex hull
        convex = true;
    }

    private class MyComparator implements Comparator<Point> {
        public final int compare(Point a, Point b) {
            int y1 = a.y;
            int y2 = b.y;
            return y2 - y1;
        }
    }


    private class MyComparatorX implements Comparator<Point> {
        int indexStartPoint;
        int sp = 0;

        void setIndexStartPoint(int sp) {
            this.sp = sp;
        }

        public final int compare(Point a, Point b) {//what type of sort ie what is the order
            //degsp
            int y1 = a.y;
            int y2 = b.y;
            return y2 - y1;
        }
    }

    private class MyComparator2 implements Comparator<Point> {
        public final int compare(Point a1, Point b1) {

            //finding the point with the smallest Y
            int y1 = a1.y;
            int y2 = b1.y;
            return y2 - y1;

            //this was another way of doing it, but i didn't develop it.
                /*
        			float th
        			int min
        			
        			int firstN=0;
        			int secondN=1;
        			for (firstN=0; firstN<ncount; firstN++) {
       					if  (p[firstN].y<p[secondN].y) min=firstN; else min=secondN;
       					secondN++;
       				}	
       			
        			p[ncount]=p[min]; th=0.0;
   
          			for (M=0; M<ncount; M++)
      				{
        				swap(p,M,min);
        				min=ncount; v=th; th=360.0;
        				for (i=M+1; i<=ncount; i++)
        					if (theta(p[m], p[i]) > v)
        						if (theta(p[m], p[i]) < th)
        							{min=i; th=theta(p[m], p[min]);}
        				if (min==N) return M;
        			}
        		*/
        }
    }


    public void mouseClicked(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        myDragButton = button;
        //this is a bodge as drag will not give the button used
        //so I keep track myself and clear it at mouseReleased
        //I think it worked in non swing
        //if(e.isMetaDown()){ncount=0;}
        if (button == MouseEvent.BUTTON3) {
            System.out.println("button3");
            ncount = 0;
        } else if (button == MouseEvent.BUTTON1) { //add to array store
            System.out.println("button1");
            if (ncount < max) {
                p[ncount++] = e.getPoint();
            }
        } else if (button == MouseEvent.BUTTON2) { //find it in array store
            //System.out.println("button2");
            int mx = e.getX();
            int my = e.getY();
            int left_mx = mx - 10;
            int right_mx = mx + 10;
            System.out.println("button2" + e.getPoint());
            System.out.println("left_mx=" + (mx - 10) + " right_mx=" + (mx + 10));
            for (int i = 0; i < ncount; i++) {
                int px = p[i].x;
                if (px < right_mx && px > left_mx) {
                    System.out.println("foundx");
                    int top_my = my - 10;
                    int bot_my = my + 10;
                    System.out.println("top_my=" + top_my + "bot_my" + bot_my);
                    int py = p[i].y;
                    if (py < bot_my && py > top_my) {
                        System.out.println("got one" + p[i]);    //toString()
                        got_one = true;
                        index = i;
                        p[i] = e.getPoint();
                        repaint();
                        break;
                    }
                }
            }
        }
    }

    public void mouseReleased(MouseEvent event) {
        got_one = false;
        myDragButton = 0;
        index = -1;
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        int button = e.getButton();
        System.out.println("drag button no=" + button);

        //if(button==MouseEvent.BUTTON2 ){
        if (myDragButton == MouseEvent.BUTTON2) {//bodge as drag gives button as zero
            System.out.println("button2 drag");
            if (got_one == true) {
                p[index] = e.getPoint();
                repaint();
            }
        }
        //if(button==MouseEvent.BUTTON1 ){
        if (myDragButton == MouseEvent.BUTTON1) {
            if (ncount < max) {
                p[ncount++] = e.getPoint();
            }

        }

        if (true/* test for room in array*/)
               /*add point to array store*/ ;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < ncount; i++) {
            g.drawRect(p[i].x - 6, p[i].y - 8, 13, 17);
            g.drawString("p[" + i + "][" + p[i].x + "x  " + p[i].y + "y]", p[i].x, p[i].y - 10);
        }

        for (int i = 0; i < ncount; i++)
            for (int j = i; j < ncount; j++)
                g.drawLine(p[i].x, p[i].y, p[j].x, p[j].y);

        if (convex) {
            g.setColor(Color.red);
            for (int i = 0; i < temp; i++) {
                g.drawLine(array[i].x, array[i].y, array[i + 1].x, array[i + 1].y);
            }
            g.setColor(Color.black);
            convex = false;
        }
    }

    /********************* Drawing the Convex Hull ******************/
    public void paint(Graphics dC) {
        super.paint(dC);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Convex Hull v.1.3 (c) 2005");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(640, 480);
        f.getContentPane().add(new convexHull());
        f.setVisible(true);
    }
}
