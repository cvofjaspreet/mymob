package com.reucon.openfire.plugin.archive.model;

import org.apache.log4j.Logger;
import org.jivesoftware.database.JiveID;
import org.json.JSONObject;

import com.reucon.openfire.plugin.archive.GetOfflineServelet;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

/**
 * An archived message.
 */
@JiveID(501)
public class ArchivedMessage
{
	private static Logger Log = Logger.getLogger(GetOfflineServelet.class);
    public enum Direction {
        /**
         * A message sent by the owner.
         */
        to,

        /**
         * A message received by the owner.
         */
        from
    }

    private Long id;
    private final Date time;
    private final Direction direction;
    private final String type;
    private String subject;
    private String body;
    private Conversation conversation;

    public ArchivedMessage(Date time, Direction direction, String type)
    {
        this.time = time;
        this.direction = direction;
        this.type = type;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getTime()
    {
        return time;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public String getType()
    {
        return type;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public Conversation getConversation()
    {
        return conversation;
    }

    public void setConversation(Conversation conversation)
    {
        this.conversation = conversation;
    }

    /**
     * Checks if this message contains payload that should be archived.
     *
     * @return <code>true</code> if this message is empty, <code>false</code> otherwise.
     */
    public boolean isEmpty()
    {
        return subject == null && body == null;
    }

    public JSONObject getMessage(HttpServletResponse response,PrintWriter out)
    {
       // StringBuilder sb = new StringBuilder();
       JSONObject jsonObject=new JSONObject();
       try {
		jsonObject.put("time", getTime().getTime());
		jsonObject.put("messageid", getSubject());
	    jsonObject.put("type",getType());
	    jsonObject.put("body",getBody());
	    jsonObject.put("with", getConversation().getWithJid());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		replyMessage("error", response, out);
		Log.error("error while getting message");
		Log.info("error line 128 = " + e.getMessage());
	}
       
//        sb.append("{\"time\":\""+getTime().getTime()+"\",\"messageid\":\""+getSubject()+"\",\"body\":\""+getBody()+"\",\"type\":\""+getType()+"\",\"with\":\""+getConversation().getWithJid()+"\"}");
      
        return jsonObject;
    }
    
    private void replyMessage(String message, HttpServletResponse response,
			PrintWriter out) {
		response.setContentType("text/plain");
		out.println("{\"result\":\"" + message + "\"}");
		out.flush();
	}
}
