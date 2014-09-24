package alarm;

import java.util.concurrent.atomic.AtomicInteger;

public class AlarmMeterFactory
{
    private static AtomicInteger alarmId = new AtomicInteger();

    public static int getAlarmId()
    {
        return alarmId.getAndIncrement();
    }
}
