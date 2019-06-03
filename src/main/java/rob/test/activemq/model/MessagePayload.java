package rob.test.activemq.model;

import java.util.Date;

public class MessagePayload
{
    private Date sent;
    private String content;

    public Date getSent()
    {
        return sent;
    }

    public void setSent(Date sent)
    {
        this.sent = sent;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
