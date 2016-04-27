package com.laci;

public class IdleThread extends Thread
{

    int remaining;
    int defaultRemainingTime;
    private static IdleThread instance = null;

    public IdleThread()
    {
        defaultRemainingTime = 2 * 60 * 60; // 2 hors
        remaining = defaultRemainingTime;
    }

    public void run()
    {
        do
        {
            if(remaining == 0)
            {
                Commander commander = Commander.getInstance();
                commander.exec("mpc stop");
                remaining--;
            } else
            if(remaining > 0)
                remaining--;
            try
            {
                sleep(1000L);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        } while(true);
    }

    public void resetRmeainingTime()
    {
        remaining = defaultRemainingTime;
    }

    public static IdleThread getInsatnce()
    {
        if(instance == null && instance == null)
        {
            instance = new IdleThread();
            instance.start();
        }
        return instance;
    }

}