package alarm.alarmMeter;

import alarm.AlarmStatus;

public interface IAlarmMeter
{
    public String getKey();

    public AlarmStatus update(Object obj);
}
