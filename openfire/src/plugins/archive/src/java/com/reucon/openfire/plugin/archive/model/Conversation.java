package com.reucon.openfire.plugin.archive.model;

import org.apache.log4j.Logger;
import org.jivesoftware.database.JiveID;
import org.json.JSONArray;
import org.json.JSONObject;

import com.reucon.openfire.plugin.archive.GetOfflineServelet;

import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

/**
 * A conversation between two or more participants.
 */
@JiveID(502)
public class Conversation
{
	private static Logger Log = Logger.getLogger(GetOfflineServelet.class);
    private Long id;
    private final Date start;
    private Date end;
    private final String ownerJid;
    private final String ownerResource;
    private final String withJid;
    private final String withResource;
    private String subject;
    private final String thread;
    private final List<Participant> participants;
    private final List<ArchivedMessage> messages;

    public Conversation(Date start, String ownerJid, String ownerResource, String withJid, String withResource,
                        String subject, String thread)
    {
        this(start, start, ownerJid, ownerResource, withJid, withResource, subject, thread);
    }

    public Conversation(Date start, Date end, String ownerJid, String ownerResource, String withJid, String withResource,
                        String subject, String thread)
    {
        this.start = start;
        this.end = end;
        this.ownerJid = ownerJid;
        this.ownerResource = ownerResource;
        this.withJid = withJid;
        this.withResource = withResource;
        this.subject = subject;
        this.thread = thread;
        participants = new ArrayList<Participant>();
        messages = new ArrayList<ArchivedMessage>();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getStart()
    {
        return start;
    }

    public Date getEnd()
    {
        return end;
    }

    public void setEnd(Date end)
    {
        this.end = end;
    }

    public String getOwnerJid()
    {
        return ownerJid;
    }

    public String getOwnerResource()
    {
        return ownerResource;
    }

    public String getWithJid()
    {
        return withJid;
    }

    public String getWithResource()
    {
        return withResource;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getThread()
    {
        return thread;
    }

    public Collection<Participant> getParticipants()
    {
        return Collections.unmodifiableCollection(participants);
    }

    public void addParticipant(Participant participant)
    {
        synchronized (participants)
        {
            participants.add(participant);
        }
    }

    public List<ArchivedMessage> getMessages()
    {
        return messages;
    }

    public void addMessage(ArchivedMessage message)
    {
        synchronized (messages)
        {
            messages.add(message);
        }
    }

    public boolean isStale(int conversationTimeout)
    {
        Long now = System.currentTimeMillis();

        return end.getTime() + conversationTimeout * 60L * 1000L < now;
    }

    /**
     * Checks if this conversation has an active participant with the given JID.
     *
     * @param jid JID of the participant
     * @return <code>true</code> if this conversation has an active participant with the given JID,
     *         <code>false</code> otherwise.
     */
    public boolean hasParticipant(String jid)
    {
        synchronized (participants)
        {
            for (Participant p : participants)
            {
                if (p.getJid().equals(jid))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if this conversation is new and has not yet been persisted.
     *
     * @return <code>true</code> if this conversation is new and has not yet been persisted,
     *         <code>false</code> otherwise.
     */
    public boolean isNew()
    {
        return id == null;
    }
    
    @Override
    public String toString() {
    	
    	
    	
    	// TODO Auto-generated method stub
    	StringBuffer buffer=new StringBuffer();
    	buffer.append("{\"conversation\":[");
    	List<ArchivedMessage> archivedMessages=getMessages();
    	Iterator<ArchivedMessage> iterator=archivedMessages.iterator();
    	for (int i=0;iterator.hasNext();i++) {
			ArchivedMessage archivedMessage = (ArchivedMessage) iterator.next();
			
			 final long secs;

	         secs = System.currentTimeMillis()-archivedMessage.getTime().getTime();
	         if(secs<2*1000 && archivedMessage.getBody()!=null && !archivedMessage.getBody().equals("null") && archivedMessage.getDirection()==ArchivedMessage.Direction.from ){
			buffer.append(archivedMessage.toString());
			if(i!=(archivedMessages.size()-1) && i!=archivedMessages.size())
			buffer.append(",");
	         }
		}
    
    	buffer.append("]}");
    	
    	return buffer.toString();
    	
    }
    
    public JSONArray getConversation(long time,HttpServletResponse response,PrintWriter out, long start) {
    	
    	JSONArray array=new JSONArray();
    	
    	// TODO Auto-generated method stub
    	//StringBuffer buffer=new StringBuffer();
    	//buffer.append("{\"conversation\":[");
    	List<ArchivedMessage> archivedMessages=getMessages();
    	Log.info("archive messages length ="+archivedMessages.size());
    	Iterator<ArchivedMessage> iterator=archivedMessages.iterator();
    	int j=0;
    	for (int i=0;iterator.hasNext();i++) {
			
    		JSONObject jsonObject=new JSONObject();
    		ArchivedMessage archivedMessage = (ArchivedMessage) iterator.next();
    		Log.info("archive messages body ="+archivedMessages.get(i).getBody());
			 final long secs;
			 
	         secs = System.currentTimeMillis()-archivedMessage.getTime().getTime();
	         if( archivedMessage.getDirection()==ArchivedMessage.Direction.from ){
	        	 {
	        		 try {
	        			 if(archivedMessage.getTime().getTime()>start){
						jsonObject.put("message", archivedMessage.getMessage(response,out));
						array.put(j,jsonObject);
						j++;
	        			 }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						replyMessage("error", response, out);
						Log.error("error while getting message");
						Log.info("error line 242 = " + e.getMessage());
					} 
	        	 }
	        	
	         }
		}
    
    //	buffer.append("]}");
    	
    	return array;
    	
    }
    
    
public JSONArray getConversation(long time,HttpServletResponse response,PrintWriter out, long start,String type) {
    	
    	JSONArray array=new JSONArray();
    	
    	// TODO Auto-generated method stub
    	//StringBuffer buffer=new StringBuffer();
    	//buffer.append("{\"conversation\":[");
    	List<ArchivedMessage> archivedMessages=getMessages();
    	Log.info("archive messages length ="+archivedMessages.size());
    	Iterator<ArchivedMessage> iterator=archivedMessages.iterator();
    	int j=0;
    	for (int i=0;iterator.hasNext();i++) {
			
    		JSONObject jsonObject=new JSONObject();
    		ArchivedMessage archivedMessage = (ArchivedMessage) iterator.next();
    		Log.info("archive messages body ="+archivedMessages.get(i).getBody());
		//	 final long secs;
			 
	       //  secs = System.currentTimeMillis()-archivedMessage.getTime().getTime();
	         if( archivedMessage.getDirection()==ArchivedMessage.Direction.from && archivedMessage.getType().equals(type) ){
	        	 {
	        		 try {
	        			 if(archivedMessage.getTime().getTime()>start){
						jsonObject.put("message", archivedMessage.getMessage(response,out));
						array.put(j,jsonObject);
						j++;
	        			 }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						replyMessage("error", response, out);
						Log.error("error while getting message");
						Log.info("error line 288 = " + e.getMessage());
					} 
	        	 }
	        	
	         }
		}
    
    //	buffer.append("]}");
    	
    	return array;
    	
    }
    private void replyMessage(String message, HttpServletResponse response,
			PrintWriter out) {
		response.setContentType("text/plain");
		out.println("{\"result\":\"" + message + "\"}");
		out.flush();
	}
}
