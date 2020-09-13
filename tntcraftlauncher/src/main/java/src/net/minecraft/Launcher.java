// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 07.03.2012 10:51:47
// Home Page: http://members.fortunecity.com/neshkov/dj.html http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Launcher.java

package net.minecraft;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

// Referenced classes of package net.minecraft:
//            LoginForm, GameUpdater

public class Launcher extends Applet
    implements Runnable, AppletStub, MouseListener
{

    public Launcher()
    {
        customParameters = new HashMap();
        gameUpdaterStarted = false;
        active = false;
        context = 0;
        hasMouseListener = false;
    }

    public boolean isActive()
    {
        if(context == 0)
        {
            context = -1;
            try
            {
                if(getAppletContext() != null)
                    context = 1;
            }
            catch(Exception exception) { }
        }
        if(context == -1)
            return active;
        else
            return super.isActive();
    }

    public void init(String userName, String latestVersion, String downloadTicket, String sessionId)
    {
        try
        {
            bgImage = ImageIO.read(LoginForm.class.getResource("dirt.png")).getScaledInstance(32, 32, 16);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        customParameters.put("username", userName);
        customParameters.put("sessionid", sessionId);
        gameUpdater = new GameUpdater(latestVersion, (new StringBuilder("minecraft.jar?user=")).append(userName).append("&ticket=").append(downloadTicket).toString());
    }

    public boolean canPlayOffline()
    {
        return gameUpdater.canPlayOffline();
    }

    public void init()
    {
        if(applet != null)
        {
            applet.init();
            return;
        } else
        {
            init(getParameter("userName"), getParameter("latestVersion"), getParameter("downloadTicket"), getParameter("sessionId"));
            return;
        }
    }

    public void start()
    {
        if(applet != null)
        {
            applet.start();
            return;
        }
        if(gameUpdaterStarted)
        {
            return;
        } else
        {
            Thread t = new Thread() {

                public void run()
                {
                    gameUpdater.run();
                    try
                    {
                        if(!gameUpdater.fatalError)
                            replace(gameUpdater.createApplet());
                    }
                    catch(ClassNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch(InstantiationException e)
                    {
                        e.printStackTrace();
                    }
                    catch(IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                }

                final Launcher this$0;

            
            {
                this$0 = Launcher.this;
                //super();
            }
            }
;
            t.setDaemon(true);
            t.start();
            t = new Thread() {

                public void run()
                {
                    while(applet == null) 
                    {
                        repaint();
                        try
                        {
                            Thread.sleep(10L);
                        }
                        catch(InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                final Launcher this$0;

            
            {
                this$0 = Launcher.this;
                //super();
            }
            }
;
            t.setDaemon(true);
            t.start();
            gameUpdaterStarted = true;
            return;
        }
    }

    public void stop()
    {
        if(applet != null)
        {
            active = false;
            applet.stop();
            return;
        } else
        {
            return;
        }
    }

    public void destroy()
    {
        if(applet != null)
        {
            applet.destroy();
            return;
        } else
        {
            return;
        }
    }

    public void replace(Applet applet)
    {
        this.applet = applet;
        applet.setStub(this);
        applet.setSize(getWidth(), getHeight());
        setLayout(new BorderLayout());
        add(applet, "Center");
        applet.init();
        active = true;
        applet.start();
        validate();
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g2)
    {
        if(applet != null)
            return;
        int w = getWidth() / 2;
        int h = getHeight() / 2;
        if(img == null || img.getWidth() != w || img.getHeight() != h)
            img = createVolatileImage(w, h);
        Graphics g = img.getGraphics();
        for(int x = 0; x <= w / 32; x++)
        {
            for(int y = 0; y <= h / 32; y++)
                g.drawImage(bgImage, x * 32, y * 32, null);

        }

        if(gameUpdater.pauseAskUpdate)
        {
            if(!hasMouseListener)
            {
                hasMouseListener = true;
                addMouseListener(this);
            }
            g.setColor(Color.LIGHT_GRAY);
            String msg = "\u0414\u043E\u0441\u0442\u0443\u043F\u043D\u044B \u043E\u0431\u043D\u043E\u0432\u043B\u0435\u043D\u0438\u044F";
            g.setFont(new Font(null, 1, 20));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 - fm.getHeight() * 2);
            g.setFont(new Font(null, 0, 12));
            fm = g.getFontMetrics();
            g.fill3DRect(w / 2 - 56 - 8, h / 2, 56, 20, true);
            g.fill3DRect(w / 2 + 8, h / 2, 56, 20, true);
            msg = "\u0412\u044B \u0445\u043E\u0442\u0438\u0442\u0435 \u043E\u0431\u043D\u043E\u0432\u0438\u0442\u044C\u0441\u044F?";
            g.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 - 8);
            g.setColor(Color.BLACK);
            msg = "\u0414\u0430!";
            g.drawString(msg, (w / 2 - 56 - 8 - fm.stringWidth(msg) / 2) + 28, h / 2 + 14);
            msg = "\u041D\u0435\u0442";
            g.drawString(msg, ((w / 2 + 8) - fm.stringWidth(msg) / 2) + 28, h / 2 + 14);
        } else
        {
            g.setColor(Color.LIGHT_GRAY);
            String msg = "\u041E\u0431\u043D\u043E\u0432\u043B\u0435\u043D\u0438\u0435 Minecraft";
            if(gameUpdater.fatalError)
                msg = "\u041D\u0435 \u0443\u0434\u0430\u043B\u043E\u0441\u044C \u0437\u0430\u043F\u0443\u0441\u0442\u0438\u0442\u044C \u043E\u0431\u043D\u043E\u0432\u043B\u0435\u043D\u0438\u0435";
            g.setFont(new Font(null, 1, 20));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 - fm.getHeight() * 2);
            g.setFont(new Font(null, 0, 12));
            fm = g.getFontMetrics();
            msg = gameUpdater.getDescriptionForState();
            if(gameUpdater.fatalError)
                msg = gameUpdater.fatalErrorDescription;
            g.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 + fm.getHeight() * 1);
            msg = gameUpdater.subtaskMessage;
            g.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 + fm.getHeight() * 2);
            if(!gameUpdater.fatalError)
            {
                g.setColor(Color.black);
                g.fillRect(64, h - 64, (w - 128) + 1, 5);
                g.setColor(new Color(32768));
                g.fillRect(64, h - 64, (gameUpdater.percentage * (w - 128)) / 100, 4);
                g.setColor(new Color(0x20a020));
                g.fillRect(65, (h - 64) + 1, (gameUpdater.percentage * (w - 128)) / 100 - 2, 1);
            }
        }
        g.dispose();
        g2.drawImage(img, 0, 0, w * 2, h * 2, null);
    }

    public void run()
    {
    }

    public String getParameter(String name)
    {
        String custom = (String)customParameters.get(name);
        if(custom != null)
            return custom;
        try
        {
            return super.getParameter(name);
        }
        catch(Exception e)
        {
            customParameters.put(name, null);
        }
        return null;
    }

    public void appletResize(int i, int j)
    {
    }

    public URL getDocumentBase()
    {
        try
        {
            return new URL("http://mineambrycraft.ru/clientupdate/game/");
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent me)
    {
        int x = me.getX() / 2;
        int y = me.getY() / 2;
        int w = getWidth() / 2;
        int h = getHeight() / 2;
        if(contains(x, y, w / 2 - 56 - 8, h / 2, 56, 20))
        {
            removeMouseListener(this);
            gameUpdater.shouldUpdate = true;
            gameUpdater.pauseAskUpdate = false;
            hasMouseListener = false;
        }
        if(contains(x, y, w / 2 + 8, h / 2, 56, 20))
        {
            removeMouseListener(this);
            gameUpdater.shouldUpdate = false;
            gameUpdater.pauseAskUpdate = false;
            hasMouseListener = false;
        }
    }

    private boolean contains(int x, int y, int xx, int yy, int w, int h)
    {
        return x >= xx && y >= yy && x < xx + w && y < yy + h;
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    private static final long serialVersionUID = 1L;
    public Map customParameters;
    private GameUpdater gameUpdater;
    private boolean gameUpdaterStarted;
    private Applet applet;
    private Image bgImage;
    private boolean active;
    private int context;
    private boolean hasMouseListener;
    private VolatileImage img;


}