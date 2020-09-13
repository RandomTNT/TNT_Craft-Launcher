// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 07.03.2012 10:51:47
// Home Page: http://members.fortunecity.com/neshkov/dj.html http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LauncherFrame.java

package net.minecraft;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

// Referenced classes of package net.minecraft:
//            LoginForm, Launcher, Util

public class LauncherFrame extends Frame
{

    public void setOffline(boolean offline)
    {
        this.offline = offline;
    }

    public boolean isOffline()
    {
        return offline;
    }

    public LauncherFrame()
    {
        super("CreeperZone");
        customParameters = new HashMap();
        offline = false;
        setMinimumSize(new Dimension(700, 400));
        setBackground(Color.BLACK);
        loginForm = new LoginForm(this);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(loginForm, "Center");
        p.setPreferredSize(new Dimension(854, 480));
        setLayout(new BorderLayout());
        add(p, "Center");
        pack();
        setLocationRelativeTo(null);
       /* try
        {
            //setIconImage(ImageIO.read(net/minecraft/LauncherFrame.getResource("favicon.png")));
        }
        catch(IOException e1)
        {
            e1.printStackTrace();
        }*/
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent arg0)
            {
                (new Thread() {

                    public void run()
                    {
                        try
                        {
                            Thread.sleep(30000L);
                        }
                        catch(InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        System.out.println("FORCING EXIT!");
                        System.exit(0);
                    }

                    //final _cls1 this$1;

                    
                    {
                      //  this$1 = _cls1.this;
            //            super();
                    }
                }
).start();
                if(launcher != null)
                {
                    launcher.stop();
                    launcher.destroy();
                }
                System.exit(0);
            }

            final LauncherFrame this$0;

            
            {
                this$0 = LauncherFrame.this;
                //super();
            }
        }
);
    }

    public void playCached(String userName)
    {
        try
        {
            if(userName == null || userName.length() <= 0)
                userName = "Player";
            launcher = new Launcher();
            launcher.customParameters.putAll(customParameters);
            launcher.customParameters.put("userName", userName);
            launcher.init();
            removeAll();
            add(launcher, "Center");
            validate();
            launcher.start();
            loginForm = null;
            setTitle("Minecraft");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            showError(e.toString());
        }
    }

    public String getFakeResult(String userName)
    {
        return (new StringBuilder(String.valueOf(Util.getFakeLatestVersion()))).append(":35b9fd01865fda9d70b157e244cf801c:").append(userName).append(":12345:").toString();
    }

    public void login(String userName)
    {
        String result;
        result = getFakeResult(userName);
        if(result == null)
        {
            showError("Can't connect to minecraft.net");
            loginForm.setNoNetwork();
            return;
        }
        if(!result.contains(":"))
        {
            if(result.trim().equals("Bad login"))
                showError("Login failed");
            else
            if(result.trim().equals("Old version"))
            {
                loginForm.setOutdated();
                showError("Outdated launcher");
            } else
            {
                showError(result);
            }
            loginForm.setNoNetwork();
            return;
        }
        try
        {
            String values[] = result.split(":");
            launcher = new Launcher();
            launcher.customParameters.putAll(customParameters);
            launcher.customParameters.put("userName", values[2].trim());
            launcher.customParameters.put("latestVersion", values[0].trim());
            launcher.customParameters.put("downloadTicket", values[1].trim());
            launcher.customParameters.put("sessionId", values[3].trim());
            launcher.init();
            removeAll();
            add(launcher, "Center");
            validate();
            launcher.start();
            loginForm.loginOk();
            loginForm = null;
            setTitle("Minecraft");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            showError(e.toString());
            loginForm.setNoNetwork();
        }
        return;
    }

    private void showError(String error)
    {
        removeAll();
        add(loginForm);
        loginForm.setError(error);
        validate();
    }

    public boolean canPlayOffline(String userName)
    {
        Launcher launcher = new Launcher();
        launcher.customParameters.putAll(customParameters);
        launcher.init(userName, null, null, null);
        return launcher.canPlayOffline();
    }

    public static void main(String args[])
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception exception) { }
        LauncherFrame launcherFrame = new LauncherFrame();
        launcherFrame.setVisible(true);
        launcherFrame.customParameters.put("stand-alone", "true");
        if(args.length >= 3)
        {
            String ip = args[2];
            String port = "25565";
            if(ip.contains(":"))
            {
                String parts[] = ip.split(":");
                ip = parts[0];
                port = parts[1];
            }
            launcherFrame.customParameters.put("server", ip);
            launcherFrame.customParameters.put("port", port);
        } else
        {
            launcherFrame.customParameters.put("server", "31.192.106.174");
            launcherFrame.customParameters.put("port", "25039");
        }
        if(args.length >= 1)
        {
            launcherFrame.loginForm.userName.setText(args[0]);
            if(args.length >= 2)
                launcherFrame.loginForm.doLogin();
        }
    }

    private int getWsGameLocalVersion()
    {
        int version;
        try
        {
            Scanner scan = new Scanner(new FileReader((new StringBuilder()).append(Util.getWorkingDirectory()).append("/wsversion").toString()));
            version = scan.nextInt();
            scan.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("WS Update: Version file not found, setting force update to true.");
            version = 0;
        }
        return version;
    }

    public boolean wsGameNeedUpdate()
    {
        if(gameVersion > getWsGameLocalVersion() && !offline)
        {
            int result = JOptionPane.showConfirmDialog(null, "\u0414\u043E\u0441\u0442\u0443\u043F\u043D\u0430 \u043D\u043E\u0432\u0430\u044F \u0432\u0435\u0440\u0441\u0438\u044F \u043A\u043B\u0438\u0435\u043D\u0442\u0430\n\u041E\u0431\u043D\u043E\u0432\u0438\u0442\u044C?", "\u041E\u0431\u043D\u043E\u0432\u043B\u0435\u043D\u0438\u0435", 0, 3);
            switch(result)
            {
            case 0: // '\0'
                return true;

            case 1: // '\001'
                return false;

            case -1: 
                return false;
            }
        }
        return false;
    }

    public double getLauncherVersion()
    {
        return 0.5D;
    }

    public static final int VERSION = 13;
    private static final long serialVersionUID = 1L;
    public Map customParameters;
    public Launcher launcher;
    public LoginForm loginForm;
    private boolean offline;
    private final double launcherVersion = 0.5D;
    public static int gameVersion = 0;

}