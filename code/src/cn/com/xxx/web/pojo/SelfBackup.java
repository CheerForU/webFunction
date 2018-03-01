package cn.com.xxx.web.pojo;

public class SelfBackup
{
    private int enable;
    private String month;
    private String day;
    private String week;
    private String hour;
    private String minute;
    private boolean mysql;
    private boolean mds_mongo;
    private boolean index_mongo;
    private boolean dedup_mongo;

    public SelfBackup()
    {

    }

    public SelfBackup(final int enable, final String month, final String day,
            final String week, final String hour, final String minute,
            final boolean mysql, final boolean mds_mongo, final boolean index_mongo,
            final boolean dedup_mongo)
    {
        super();
        this.enable = enable;
        this.month = month;
        this.day = day;
        this.week = week;
        this.hour = hour;
        this.minute = minute;
        this.mysql = mysql;
        this.mds_mongo = mds_mongo;
        this.index_mongo = index_mongo;
        this.dedup_mongo = dedup_mongo;
    }

    public int getEnable()
    {
        return enable;
    }

    public void setEnable(final int enable)
    {
        this.enable = enable;
    }

    public String getMonth()
    {
        return month;
    }

    public void setMonth(final String month)
    {
        this.month = month;
    }

    public String getDay()
    {
        return day;
    }

    public void setDay(final String day)
    {
        this.day = day;
    }

    public String getWeek()
    {
        return week;
    }

    public void setWeek(final String week)
    {
        this.week = week;
    }

    public String getHour()
    {
        return hour;
    }

    public void setHour(final String hour)
    {
        this.hour = hour;
    }

    public String getMinute()
    {
        return minute;
    }

    public void setMinute(final String minute)
    {
        this.minute = minute;
    }

    public boolean isMysql()
    {
        return mysql;
    }

    public void setMysql(final boolean mysql)
    {
        this.mysql = mysql;
    }

    public boolean isMds_mongo()
    {
        return mds_mongo;
    }

    public void setMds_mongo(final boolean mds_mongo)
    {
        this.mds_mongo = mds_mongo;
    }

    public boolean isIndex_mongo()
    {
        return index_mongo;
    }

    public void setIndex_mongo(final boolean index_mongo)
    {
        this.index_mongo = index_mongo;
    }

    public boolean isDedup_mongo()
    {
        return dedup_mongo;
    }

    public void setDedup_mongo(final boolean dedup_mongo)
    {
        this.dedup_mongo = dedup_mongo;
    }

    @Override
    public String toString()
    {
        return "SelfBackup [enable=" + enable + ", month=" + month + ", day=" + day
                + ", week=" + week + ", hour=" + hour + ", minute=" + minute + ", mysql="
                + mysql + ", mds_mongo=" + mds_mongo + ", index_mongo=" + index_mongo
                + ", dedup_mongo=" + dedup_mongo + "]";
    }

}
