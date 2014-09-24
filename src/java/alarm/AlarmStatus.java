package alarm;

public class AlarmStatus
{
    /**
     * normal status
     */
    public static final AlarmStatus NORMAL = new AlarmStatus(0, "NORMAL");

    /**
     * no normal but not serious status
     */
    public static final AlarmStatus MINOR = new AlarmStatus(1, "MINOR");

    /**
     * serious status
     */
    public static final AlarmStatus MAJOR = new AlarmStatus(2, "MAJOR");

    /**
     * critical status
     */
    public static final AlarmStatus CRITICAL = new AlarmStatus(3, "CRITICAL");

    private int stateVal;

    private String stateLevel;

    public AlarmStatus()
    {
    }

    public AlarmStatus(final int stateVal, final String stateLevel)
    {
        this.stateVal = stateVal;
        this.stateLevel = stateLevel;
    }

    public static AlarmStatus toAlarmStatus(String level)
    {
        if ("NORMAL".equalsIgnoreCase(level))
        {
            return AlarmStatus.NORMAL;
        }
        else if ("MINOR".equalsIgnoreCase(level))
        {
            return AlarmStatus.MINOR;
        }
        else if ("MAJOR".equalsIgnoreCase(level))
        {
            return AlarmStatus.MAJOR;
        }
        else if ("CRITICAL".equalsIgnoreCase(level))
        {
            return AlarmStatus.CRITICAL;
        }
        return NORMAL;
    }

    public int getStateVal()
    {
        return stateVal;
    }

    public void setStateVal(int stateVal)
    {
        this.stateVal = stateVal;
    }

    public String getStateLevel()
    {
        return stateLevel;
    }

    public void setStateLevel(String stateLevel)
    {
        this.stateLevel = stateLevel;
    }
}