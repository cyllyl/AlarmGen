package alarm.alarmAlgorithm.impl;

import alarm.AlarmStatus;

public class RangeAlgorithm extends AbstractAlgorithm
{
    private String thresholdMin;

    private String thresholdMax;

    private String equalPosition;

    @Override
    public synchronized AlarmStatus onStatusUpdata(Object obj)
    {
        double curValue = Double.valueOf((String) obj);
        if (isWithInThreshold(curValue))
        {
            curAlarmStatus = super.getAlarmStatus();
        }
        else
        {
            curAlarmStatus = AlarmStatus.NORMAL;
        }

        return curAlarmStatus;
    }

    private boolean isWithInThreshold(double curValue)
    {
        if ("both".equals(equalPosition))
        {
            return (this.thresholdMin == null ? true : curValue >= Double.parseDouble(this.thresholdMin))
                && (this.thresholdMax == null ? true : curValue <= Double.parseDouble(this.thresholdMax));
        }
        else if ("none".equals(equalPosition))
        {
            return (this.thresholdMin == null ? true : curValue > Double.parseDouble(this.thresholdMin))
                && (this.thresholdMax == null ? true : curValue < Double.parseDouble(this.thresholdMax));
        }
        else if ("right".equals(equalPosition))
        {
            return (this.thresholdMin == null ? true : curValue > Double.parseDouble(this.thresholdMin))
                && (this.thresholdMax == null ? true : curValue <= Double.parseDouble(this.thresholdMax));
        }
        else
        {
            return (this.thresholdMin == null ? true : curValue >= Double.parseDouble(this.thresholdMin))
                && (this.thresholdMax == null ? true : curValue < Double.parseDouble(this.thresholdMax));
        }
    }

    public String getThresholdMin()
    {
        return thresholdMin;
    }

    public void setThresholdMin(String thresholdMin)
    {
        this.thresholdMin = thresholdMin;
    }

    public String getThresholdMax()
    {
        return thresholdMax;
    }

    public void setThresholdMax(String thresholdMax)
    {
        this.thresholdMax = thresholdMax;
    }

    public String getEqualPosition()
    {
        return equalPosition;
    }

    public void setEqualPosition(String equalPosition)
    {
        this.equalPosition = equalPosition;
    }
}
