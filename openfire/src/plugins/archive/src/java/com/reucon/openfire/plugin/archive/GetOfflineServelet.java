/**
 * Mara Messenger 
 * @author Gaurav
 */

package com.reucon.openfire.plugin.archive;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jivesoftware.admin.AuthCheckFilter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmpp.packet.JID;

import com.reucon.openfire.plugin.archive.model.Conversation;
import com.reucon.openfire.plugin.archive.xep0136.RetrieveRequest;

public class GetOfflineServelet extends HttpServlet {
	private static Logger Log = Logger.getLogger(GetOfflineServelet.class);
	 private static final String NAMESPACE_MANAGE = "urn:xmpp:archive:manage";
	 protected static final String NAMESPACE = "urn:xmpp:archive";
	 private  JID from;
	 private long start;
	@Override
	public void init() throws ServletException {
		super.init();

		AuthCheckFilter.addExclude("archive/userservice");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			
			 String messageid=request.getParameter("messageid");
			 String time=request.getParameter("time");
			 String jid=request.getParameter("jid");
			 String type=request.getParameter("type");
			 /**
			  * 
			  * If messageid is sent then get the time of that message othere vice time will be picked 
			  */
			 if(messageid!=null)
			 time =getPersistenceManager().getMessageTime(messageid);
			 
			 
			 Log.info("time ="+time);
			 start=Long.parseLong(time);
			 from=new JID(jid);
			 
			 
			 List<Conversation> conversations = list(jid);
			 if(type!=null && type.equals("groupchat")){
			 conversations = grouplist(jid);
			 }
			 JSONArray array=new JSONArray();
			 int j=0;
			 Log.info("conversations.size() ="+conversations.size());
		        for (int i=0;i<conversations.size();i++)
		        {
		        	JSONObject jsonObject=null;
		        	if(type==null){
		        	jsonObject=addChatElement(conversations.get(i),response,out);
		        	}else{
		        		jsonObject=addChatElement(conversations.get(i),response,out,type);	
		        	}
		        	if(jsonObject!=null && jsonObject.has("conversation")){
		        	Log.info("jsonObject ="+ jsonObject.toString());	
		        	
		        	array.put(j,jsonObject );
		        	j++;
		        	}
		        }
		        replyMessages(array.toString(), response, out);
		        

		} catch (Exception e) {
			// TODO: handle exception
			replyMessage("error", response, out);
			Log.error("error while getting message");
			Log.info("error line 69 = " + e.getMessage());
		}
	}

	private void replyMessage(String message, HttpServletResponse response,
			PrintWriter out) {
		response.setContentType("text/plain");
		out.println("{\"result\":\"" + message + "\"}");
		out.flush();
	}
	private void replyMessages(String message, HttpServletResponse response,
			PrintWriter out) {
		response.setContentType("text/plain");
		out.println("{\"result\":" + message + "}");
		out.flush();
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	  private List<Conversation> list(String from)
	    {
	        return getPersistenceManager().findConversations(null, null,
	                from, null, null);
	    }
	  
	  private List<Conversation> grouplist(String from)
	    {
	        return getPersistenceManager().findConversations(null, null,
	                null, from, null);
	    }
	  public PersistenceManager getPersistenceManager()
	    {
	        return ArchivePlugin.getInstance().getPersistenceManager();
	    }
	  
	  private JSONObject addChatElement(Conversation conversation,HttpServletResponse response,PrintWriter out)
	    {
		  	JSONObject jsonObject=new JSONObject();
	        RetrieveRequest request= new RetrieveRequest(conversation.getWithJid(), conversation.getStart());
	        Conversation conversation_= retrieve(from,request );
	        JSONArray array = null;
	        try {
	        	array=conversation_.getConversation(System.currentTimeMillis(), response, out,start);
	        
	        	jsonObject.put("conversation",array);
	        	 if(array.length()>0){
	     	        return jsonObject;
	     	        }else {
	     				return null;
	     			}
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				replyMessage("error", response, out);
				Log.error("error while getting message");
				Log.info("error line 113 = " + e.getMessage());
				return null;
			}
	       
	    }
	  private JSONObject addChatElement(Conversation conversation,HttpServletResponse response,PrintWriter out,String type)
	    {
		  	JSONObject jsonObject=new JSONObject();
		  	Log.info("with jid ="+conversation.getWithJid() );
		  	Log.info("with jid ="+conversation.getStart() );
	        RetrieveRequest request= new RetrieveRequest(conversation.getWithJid(), conversation.getStart());
	        Conversation conversation_=null;
	        if(type.equals("chat")){
	        conversation_= retrieve(from,request );
	        }else{
	        JID jid=new JID(conversation.getOwnerJid());	
	        conversation_= retrieve(jid,request );	
	        }
	        JSONArray array = null;
	        try {
	        	array=conversation_.getConversation(System.currentTimeMillis(), response, out,start,type);
	        	jsonObject.put("conversation",array);
	        	 if(array.length()>0){
	     	        return jsonObject;
	     	        }else {
	     				return null;
	     			}
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				replyMessage("error", response, out);
				Log.error("error while getting message");
				Log.info("error line 175 = " + e.getMessage());
				return null;
			}
	       
	    }
	  private Conversation retrieve(JID from, RetrieveRequest request)
	    {
		//  Log.info("from full ="+from.toFullJID());
		  Log.info("from bare ="+from.toBareJID());
		  Log.info("with jid ="+request.getWith());
		  Log.info("start ="+ request.getStart());
	        return getPersistenceManager().getConversation(from.toBareJID(), request.getWith(), request.getStart());
	    }
	  
	  
	/*  private Conversation retrieveGroup(JID from, RetrieveRequest request)
	    {
		  Log.info("retrieveGroup ");
		
	        return getPersistenceManager().getConversationForGroup(null, from.toBareJID(), null);
	    }
	  */
}
