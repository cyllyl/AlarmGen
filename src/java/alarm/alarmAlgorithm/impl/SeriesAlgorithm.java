package alarm.alarmAlgorithm.impl;

import alarm.AlarmStatus;

public class SeriesAlgorithm extends AbstractAlgorithm
{
    private float threshold;

    private long count;

    @Override
    public synchronized AlarmStatus onStatusUpdata(Object obj)
    {
        AlarmStatus businessState = (AlarmStatus) obj;
        int stateVal = super.getAlarmStatus().getStateVal();
        if (businessState.getStateVal() >= stateVal)
        {
            count++;
        }
        if (count >= threshold && businessState.getStateVal() >= stateVal)
        {
            curAlarmStatus = super.getAlarmStatus();
        }
        else if (count != 0 && businessState.getStateVal() < stateVal)
        {
            curAlarmStatus = AlarmStatus.NORMAL;
            count = 0;
        }
        else
        {
            curAlarmStatus = AlarmStatus.NORMAL;
        }
        return curAlarmStatus;
    }

    public void setThreshold(float threshold)
    {
        this.threshold = threshold;
    }
}
