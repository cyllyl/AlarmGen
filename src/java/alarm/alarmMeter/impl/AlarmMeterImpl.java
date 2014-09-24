package alarm.alarmMeter.impl;

import java.util.ArrayList;
import java.util.List;

import alarm.AlarmStatus;
import alarm.alarmAlgorithm.IAlarmAlgorithm;
import alarm.alarmMeter.IAlarmMeter;

public class AlarmMeterImpl implements IAlarmMeter
{
    private String key;

    private List<IAlarmAlgorithm> alarmAlgorithmList;

    public AlarmMeterImpl()
    {
        alarmAlgorithmList = new ArrayList<IAlarmAlgorithm>();
    }

    @Override
    public AlarmStatus update(Object obj)
    {
        List<AlarmStatus> alarmStates = new ArrayList<AlarmStatus>();

        for (IAlarmAlgorithm alarmAlgorithm : alarmAlgorithmList)
        {
            alarmStates.add(alarmAlgorithm.onStatusUpdata(obj));
        }
        int max = 0;
        AlarmStatus alarmStateReturn = new AlarmStatus();
        for (AlarmStatus alarmState : alarmStates)
        {
            if (alarmState.getStateVal() >= max)
            {
                max = alarmState.getStateVal();
                alarmStateReturn = alarmState;
            }
        }
        return alarmStateReturn;
    }

    public void setAlarmAlgorithm(IAlarmAlgorithm alarmAlgorithm)
    {
        alarmAlgorithmList.add(alarmAlgorithm);
    }

    @Override
    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}