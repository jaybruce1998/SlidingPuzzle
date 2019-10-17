import java.util.*;
import javax.swing.*;
import java.awt.event.*;
public class SlidingPuzzle extends KeyAdapter
{
   private int[][] puzzle;
   private int emptyR, emptyC, goodR, goodC, max, rows, mrows, cols, mcols, l;
   private String d;
   private List<Integer> a;
   private void construct(int r, int c)
   {
      find(0);
      emptyR=goodR;
      emptyC=goodC;
      max=r*c;
      l=(max-1+"").length();
      d="-";
      for(int i=0; i<c; i++)
         for(int j=0; j<=l; j++)
            d+="-";
      rows=r-1;
      mrows=rows-1;
      cols=c-1;
      mcols=cols-1;
      a=new ArrayList<>();
   }
   public SlidingPuzzle(int r, int c)
   {
      this(new int[r][c]);
      int n=0;
      for(int i=0; i<r; i++)
         for(int j=0; j<c; j++)
            puzzle[i][j]=(++n)%max;
      construct(r, c);
   }
   public SlidingPuzzle(int[][] p)
   {
      puzzle=p;
      construct(p.length, p[0].length);
   }
   private void move(int r, int c)
   {
      puzzle[emptyR][emptyC]=puzzle[r][c];
      a.add(puzzle[r][c]);
      puzzle[r][c]=0;
      emptyR=r;
      emptyC=c;
   }
   public void u()
   {
      move(emptyR+1, emptyC);
   }
   public void d()
   {
      move(emptyR-1, emptyC);
   }
   public void l()
   {
      move(emptyR, emptyC+1);
   }
   public void r()
   {
      move(emptyR, emptyC-1);
   }
   public void scramble()
   {
      for(int i=max*max; i>0; i--)
      {
         String m="";
         if(emptyR>0)
            m+='d';
         if(emptyR<rows)
            m+='u';
         if(emptyC>0)
            m+='r';
         if(emptyC<cols)
            m+='l';
         char x=m.charAt((int)(Math.random()*m.length()));
         if(x=='d')
            d();
         else if(x=='u')
            u();
         else if(x=='r')
            r();
         else
            l();
      }
   }
   private void find(int n)
   {
      for(int i=0; true; i++)
         for(int j=0; j<puzzle[0].length; j++)
            if(puzzle[i][j]==n)
            {
               goodR=i;
               goodC=j;
               return;
            }
   }
   private void place(int n, int r, int c)
   {
      int rp=r+1;
      while(emptyR<rp)
         u();
      while(emptyR>rp)
         d();
      find(n);
      if(goodR==r)
      {
         while(emptyC<goodC)
            l();
         while(emptyC>goodC)
            r();
      }
      else
      {
         if(goodC==0)
         {
            if(emptyC==0)
               l();
            else
               while(emptyC>1)
                  r();
            while(emptyR<goodR)
               u();
            r();
         }
         else
         {
            while(emptyC<goodC)
               l();
            while(emptyC>goodC)
               r();
            find(n);
            if(emptyC>=goodC)
               r();
            while(emptyR<goodR)
               u();
         }
         if(emptyR==rows)
            while(emptyC<mcols)
            {
               d();
               l();
               l();
               u();
               r();
            }
         else
            while(emptyC<mcols)
            {
               u();
               l();
               l();
               d();
               r();
            }
         d();
         l();
         u();
         while(emptyR>rp)
         {
            r();
            d();
            d();
            l();
            u();
         }
      }
      find(n);
      if(goodC>c)
      {
         int cp=c+1;
         r();
         d();
         l();
         while(emptyC>cp)
         {
            u();
            r();
            r();
            d();
            l();
         }
      }
      else if(goodC<c)
      {
         int cm=c-1;
         l();
         d();
         r();
         while(emptyC<cm)
         {
            u();
            l();
            l();
            d();
            r();
         }
      }
   }
   private void placeRow(int n, int r)
   {
      int s=n+mcols;
      for(int i=0; i<mcols; i++)
         place(n+i, r, i);
      place(n+cols, r, mcols);
      place(s, r+1, mcols);
      if(puzzle[r][cols]==s)
      {
         d();
         r();
         u();
         l();
         u();
         r();
         d();
         d();
         l();
         u();
         r();
         u();
         l();
         d();
         d();
         r();
         u();
      }
      else
      {
         if(emptyR==r)
            u();
         if(emptyC==mcols-1)
         {
            u();
            l();
         }
         if(emptyC==mcols)
         {
            l();
            d();
         }
         d();
         r();
         u();
      }
   }
   private void columnize()
   {
      r();
      u();
      l();
      d();
      l();
      u();
      r();
      r();
      d();
      l();
      u();
      l();
      d();
      r();
      r();
      u();
      l();
   }
   private void placeCol(int n, int c)
   {
      int cp=c+1;
      place(n+puzzle[0].length, mrows, c);
      if(puzzle[rows][c]==n)
         columnize();
      else
      {
         find(n);
         while(emptyC<goodC)
            l();
         if(puzzle[rows][c]==n)
         {
            d();
            columnize();
         }
         else
         {
            if(emptyR==goodR)
            {
               if(emptyR==mrows)
               {
                  u();
                  r();
               }
               else
               {
                  d();
                  r();
                  u();
               }
            }
            else if(emptyR==mrows)
               u();
            while(emptyC>cp)
            {
               r();
               d();
               l();
               u();
               r();
            }
            r();
            d();
            l();
         }
      }
   }
   public List<Integer> solve()
   {
      for(int i=0; i<mrows; i++)
         placeRow(i*puzzle[0].length+1, i);
      int n=mrows*puzzle[0].length+1;
      for(int i=0; i<mcols; i++)
         placeCol(n+i, i);
      place(n+mcols, mrows, mcols);
      if(emptyR==mrows)
         u();
      else if(emptyC==mcols)
         l();
      return finished()?a:null;
   }
   private boolean finished()
   {
      int n=1;
      for(int i=0; i<puzzle.length; i++)
         for(int j=0; j<puzzle[0].length; j++)
            if(puzzle[i][j]!=n)
               return false;
            else
               n=(n+1)%max;
      return true;
   }
   public String toString()
   {
      String r="<html><tt>";
      for(int i=0; i<puzzle.length; i++)
      {
         r+=d+"<br/>|";
         for(int j=0; j<puzzle[0].length; j++)
         {
            String s=puzzle[i][j]==0?" ":""+puzzle[i][j];
            for(int k=s.length(); k<l; k++)
               s="&nbsp;"+s;
            r+=s+"|";
         }
         r+="<br/>";
      }
      return r+d+"</tt></html>";
   }
   public void keyPressed(KeyEvent k)
   {
      int c=k.getKeyCode();
      try
      {
         if(c==KeyEvent.VK_UP)
            u();
         else if(c==KeyEvent.VK_DOWN)
            d();
         else if(c==KeyEvent.VK_LEFT)
            l();
         else if(c==KeyEvent.VK_RIGHT)
            r();
      }
      catch(Exception e){}
   }
   public static void main(String[] args)
   {
      SlidingPuzzle p=new SlidingPuzzle(17, 16);
      JFrame frame=new JFrame();
      p.scramble();
      frame.addKeyListener(p);
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JLabel j=new JLabel(p.toString());
      frame.add(j);
      frame.pack();
      while(!p.finished())
         j.setText(p.toString());
      JOptionPane.showMessageDialog(frame, "You did it.");
      System.exit(0);
   }
}