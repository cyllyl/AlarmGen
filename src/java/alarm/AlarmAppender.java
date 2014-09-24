package alarm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import alarm.alarmMeter.IAlarmMeter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.util.FileUtil;

public class AlarmAppender<E> extends RollingFileAppender<E>
{
    private Map<String, IAlarmMeter> alarmMeters = new HashMap<String, IAlarmMeter>();

    private ConcurrentMap<String, Integer> alarmStateMap = new ConcurrentHashMap<String, Integer>();

    @Override
    public void start()
    {
        int errors = 0;
        String alarmFileName = this.getAlarmFileName();
        if (null != alarmFileName && !"".equals(alarmFileName.trim()))
        {
            addInfo(new StringBuilder("File property is set to [").append(fileName).append("]").toString());
            try
            {
                createAlarmFile(getAlarmFileName());
            }
            catch (IOException e)
            {
                errors++;
                addError(new StringBuilder("openFile(").append(fileName).append(",) call failed.").toString(), e);
            }
        }
        else
        {
            errors++;
            addError(new StringBuilder("\"File\" property not set for appender named [").append(name)
                .append("].")
                .toString());
        }

        // TODO start schedule task

        if (null == alarmMeters)
        {
            addWarn(new StringBuilder("No alarmMeters was set for the AlarmAppender named ").append(getName())
                .toString());
            addWarn(new StringBuilder("For more information, please visit ").append(CoreConstants.CODES_URL)
                .append("#rfa_no_tp")
                .toString());
            return;
        }
        HashMap<String, IAlarmMeter> alarmMeterMap = new HashMap<String, IAlarmMeter>(alarmMeters.size());
        for (IAlarmMeter alarmMeter : alarmMeters.values())
        {
            if (alarmMeter.getKey() == null)
            {
                addWarn("No key was set for alarmMeter named " + alarmMeter);
                return;
            }
            alarmMeterMap.put(alarmMeter.getKey(), alarmMeter);
        }
        alarmMeters.clear();
        alarmMeters = alarmMeterMap;
        if (errors == 0)
        {
            this.started = true;
        }
        if (super.getTriggeringPolicy() != null)
        {
            super.start();
        }

    }

    @Override
    protected void subAppend(E event)
    {
        if (!isStarted())
        {
            return;
        }
        alarmAppend(event);
    }

    protected void alarmAppend(E event)
    {
        ILoggingEvent loggingEvent = (ILoggingEvent) event;
        String businessKey = loggingEvent.getFormattedMessage();
        Object obj = loggingEvent.getArgumentArray()[0];
        if (null == obj)
        {
            return;
        }
        AlarmStatus alamState = new AlarmStatus();
        try
        {
            if (null != alarmMeters.get(businessKey))
            {
                alamState = alarmMeters.get(businessKey).update(obj);
            }

            boolean needWriteLog = false;
            synchronized (lock)
            {

                Integer statusValBefore = alarmStateMap.putIfAbsent(businessKey, alamState.getStateVal());

                if (null == statusValBefore)
                {
                    needWriteLog = true;
                }
                else if (statusValBefore != alamState.getStateVal())
                {
                    alarmStateMap.put(businessKey, alamState.getStateVal());
                    needWriteLog = true;
                }
            }

            if (needWriteLog)
            {
                if (super.getTriggeringPolicy() != null)
                {
                    String statusChange =
                        new StringBuffer(businessKey).append(" alarmstatus: ")
                            .append(alamState.getStateLevel())
                            .toString();
                    LoggingEvent events = new LoggingEvent();
                    events.setMessage(statusChange);
                    events.setTimeStamp(System.currentTimeMillis());
                    super.subAppend((E) events);
                }
            }
        }
        catch (Exception e)
        {
            this.started = false;
            addStatus(new ErrorStatus("unknown exception in appender", this, e));
        }
    }

    public void createAlarmFile(String filename)
        throws IOException
    {
        synchronized (lock)
        {
            File file = new File(fileName);
            if (FileUtil.isParentDirectoryCreationRequired(file))
            {
                boolean result = FileUtil.createMissingParentDirectories(file);
                if (!result)
                {
                    addError(new StringBuilder("Failed to create parent directories for [").append(file.getAbsolutePath())
                        .append("]")
                        .toString());
                }
                file.createNewFile();
            }
            else
            {
                if (file.isFile())
                {
                    file.createNewFile();
                }
                else
                {
                    addError(new StringBuilder("Failed to create file, as the filename [").append(filename)
                        .append("] incorrect")
                        .toString());
                }
            }

        }
    }

    public void setAlarmMeter(IAlarmMeter alarmMeter)
    {
        alarmMeters.put("alarmMeter-" + AlarmMeterFactory.getAlarmId(), alarmMeter);
    }

    public String getAlarmFileName()
    {
        return fileName;
    }

    public void setAlarmFileName(String alarmFileName)
    {
        if (alarmFileName == null)
        {
            fileName = alarmFileName;
        }
        else
        {
            fileName = alarmFileName.trim();
        }
    }

}