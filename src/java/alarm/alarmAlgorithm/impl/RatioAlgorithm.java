package alarm.alarmAlgorithm.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import alarm.AlarmStatus;

public class RatioAlgorithm extends AbstractAlgorithm
{
    private BigInteger total = BigInteger.ZERO;

    private BigInteger count = BigInteger.ZERO;

    private double threshold;

    private long duration;

    private long lastUpdateTime;

    public RatioAlgorithm()
    {
        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public synchronized AlarmStatus onStatusUpdata(Object obj)
    {
        AlarmStatus businessState = (AlarmStatus) obj;
        if (System.currentTimeMillis() - lastUpdateTime > duration)
        {
            reset();
        }

        double ratio = countRatio(businessState);
        if (ratio >= getThreshold() && ratio <= 1.0)
        {
            curAlarmStatus = super.getAlarmStatus();
        }
        else
        {
            curAlarmStatus = AlarmStatus.NORMAL;
        }
        return curAlarmStatus;
    }

    public double countRatio(AlarmStatus businessState)
    {
        total = total.add(BigInteger.ONE);
        int stateVal = super.getAlarmStatus().getStateVal();
        if (businessState.getStateVal() >= stateVal)
        {
            count = count.add(BigInteger.ONE);
        }
        return new BigDecimal(count).divide(new BigDecimal(total), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void reset()
    {
        count = BigInteger.ZERO;
        total = BigInteger.ZERO;
        lastUpdateTime = System.currentTimeMillis();
        curAlarmStatus = AlarmStatus.NORMAL;
    }

    public double getThreshold()
    {
        return threshold;
    }

    public void setThreshold(double threshold)
    {
        this.threshold = threshold;
    }

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration * 1000;
    }
}
