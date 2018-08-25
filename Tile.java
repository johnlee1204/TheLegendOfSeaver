import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
public class Tile implements Serializable
{
    private Image tile;
    private BufferedImage resize;
    String type;
    int rotation=0;
    boolean png,big=false;
    static int zoom = CreatorDriver.getFrame().getZoom();
    static int ext=CreatorDriver.getFrame().extra();
    int pZoom = CreatorDriver.getFrame().getZoom();
    public Tile(String s,boolean test)
    {
        type=s;
        png=test;
        try
        {
            int zoom = CreatorDriver.getFrame().getZoom();
            if(test)
            {
                tile = ImageIO.read(new File("images/tiles/"+s));
            }
            else
            {
                tile = ImageIO.read(new File("images/tiles/"+s+".png"));
            }
            resize = toBufferedImage(tile.getScaledInstance(16*zoom,16*zoom,0));
        }
        catch(Exception e)
        {
            //e.printStackTrace();
            //System.out.println("The Trouble Maker:+\t"+s);
        }
    }

    public Tile(String s,int r,boolean test)
    {
        type=s;
        png=test;
        rotation=r;
        try
        {
            if(test)
            {
                tile = ImageIO.read(new File("images/tiles/"+s));
            }
            else
            {
                tile = ImageIO.read(new File("images/tiles/"+s+".png"));
            }
            resize = toBufferedImage(tile.getScaledInstance(16*zoom,16*zoom,0));
        }
        catch(Exception e)
        {
            //e.printStackTrace();
            //System.out.println("The Trouble Maker:+\t"+s);
        }
    }

    public void draw(Graphics2D g,int x,int y,boolean h)
    {
        //AffineTransform old = g.getTransform();
        //AffineTransform trans = new AffineTransform();
        //trans.setTransform(trans);
        //trans.rotate((Math.toRadians(rotation*90)));
        //g.setTransform(trans);// Rotation information
        double rotationRequired = Math.toRadians(90*rotation);
        try
        {
            ext = CreatorDriver.getFrame().extra();
            if(pZoom!=zoom)
            {
                pZoom=zoom;
                resize = toBufferedImage(tile.getScaledInstance(16*zoom,16*zoom,0));
            }
            double locationY;
            double locationX;
            if(!big)
            {
                locationX = 8*CreatorDriver.getFrame().getZoom();
                locationY = 8*CreatorDriver.getFrame().getZoom();
            }
            else
            {
                locationX = resize.getWidth()/2;
                locationY = resize.getWidth()/2;
            }
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            if(h)
            {
                //g.drawImage(resize,x+ext,y,null);
                g.drawImage(op.filter(resize, null), x+ext, y, null);
            }
            else
            {
                //g.drawImage(resize,x+zoom,y,null);
                g.drawImage(op.filter(resize, null), x+zoom, y, null);
            }
        }
        catch(Exception e)
        {
        }
    }

    public void resize()
    {
        resize= (BufferedImage)((Image)tile).getScaledInstance(16*CreatorDriver.getFrame().getZoom(),16*CreatorDriver.getFrame().getZoom(),0);
    }

    public BufferedImage getImage()
    {
        return toBufferedImage(tile);
    }

    public void setRotation(int r)
    {
        rotation=r;
    }

    public int getRotation()
    {
        return rotation;
    }
    
    public String getType()
    {
        return type;
    }

    public String toString()
    {
        if(!png)
        {
            return type+".png";
        }
        else
        {
            return type;
        }
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    public void setBig(int space)
    {
        big=true;
        int zoi=zoom*5;
        resize = toBufferedImage(tile.getScaledInstance(16*zoi,16*zoi,0));
    }

    public static void updateZoom()
    {
        zoom = CreatorDriver.getFrame().getZoom();
    }
    
    public boolean equals(Tile hello)
    {
        return type.equals(hello.getType());
    }
}
