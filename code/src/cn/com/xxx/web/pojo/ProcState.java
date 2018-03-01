package cn.com.xxx.web.pojo;

public class ProcState
{
    private String name;
    private String state;

    public ProcState()
    {

    }

    public ProcState(final String name, final String state)
    {
        this.name = name;
        this.state = state;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getState()
    {
        return state;
    }

    public void setState(final String state)
    {
        this.state = state;
    }

    @Override
    public String toString()
    {
        return "ProcState [name=" + name + ", state=" + state + "]";
    }

}
