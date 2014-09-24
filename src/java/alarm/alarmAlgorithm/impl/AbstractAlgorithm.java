package alarm.alarmAlgorithm.impl;

import alarm.AlarmStatus;
import alarm.alarmAlgorithm.IAlarmAlgorithm;

public abstract class AbstractAlgorithm implements IAlarmAlgorithm
{
    protected String alarmLevel;

    protected AlarmStatus curAlarmStatus;

    public abstract AlarmStatus onStatusUpdata(Object obj);

    public String getAlarmLevel()
    {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel)
    {
        this.alarmLevel = alarmLevel;
    }

    public AlarmStatus getAlarmStatus()
    {
        return AlarmStatus.toAlarmStatus(alarmLevel);
    }
}
