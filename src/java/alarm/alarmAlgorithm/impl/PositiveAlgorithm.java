package alarm.alarmAlgorithm.impl;

import alarm.AlarmStatus;

public class PositiveAlgorithm extends AbstractAlgorithm
{
    @Override
    public synchronized AlarmStatus onStatusUpdata(Object obj)
    {
        AlarmStatus businessState = (AlarmStatus) obj;
        int stateVal = super.getAlarmStatus().getStateVal();
        if (businessState.getStateVal() >= stateVal)
        {
            curAlarmStatus = super.getAlarmStatus();
        }
        else
        {
            curAlarmStatus = AlarmStatus.NORMAL;
        }
        return curAlarmStatus;
    }
}
