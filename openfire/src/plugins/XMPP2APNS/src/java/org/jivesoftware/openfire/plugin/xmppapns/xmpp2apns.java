package org.jivesoftware.openfire.plugin.xmppapns;

import java.io.File;

import org.apache.commons.lang.StringEscapeUtils;
import org.jivesoftware.openfire.IQRouter;
import org.jivesoftware.openfire.PresenceManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.vcard.VCardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Message.Type;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

public class xmpp2apns implements Plugin, PacketInterceptor {

	private static final Logger Log = LoggerFactory.getLogger(xmpp2apns.class);

	private InterceptorManager interceptorManager;

	private xmpp2apnsDBHandler dbManager;

	private VCardManager vcardManager;

	private UserManager userManager;

	private PresenceManager presenceManager;

	private XMPPServer server;

	private int badge;

	public xmpp2apns() {
		interceptorManager = InterceptorManager.getInstance();
		dbManager = new xmpp2apnsDBHandler();
	}

	public void initializePlugin(PluginManager pManager, File pluginDirectory) {

		interceptorManager.addInterceptor(this);
		server = XMPPServer.getInstance();
		presenceManager = server.getPresenceManager();
		vcardManager = VCardManager.getInstance();
		userManager = server.getUserManager();
		IQHandler myHandler = new xmpp2apnsIQHandler();
		IQRouter iqRouter = server.getIQRouter();
		iqRouter.addHandler(myHandler);
	}

	public void destroyPlugin() {
		presenceManager = null;
		vcardManager = null;
		userManager = null;
		server = null;
		interceptorManager.removeInterceptor(this);
	}

	public void interceptPacket(Packet packet, Session session, boolean read,
			boolean processed) throws PacketRejectedException {

		if (isValidTargetPacket(packet, read, processed)) {
			Packet original = packet;

			if (original instanceof Message) {
				Message receivedMessage = (Message) original;

				//return if there is no body
				String body = receivedMessage.getBody();
				if (body == null) return;
				
				String binMessage = null;
				if ( body.startsWith("User_Sent_Image@")) {
					binMessage = new String(" sent %s an image");
				} else if (body.startsWith("Video_thumb@")){
					binMessage = new String(" sent %s a video");					
				}
				
				JID senderJID = receivedMessage.getFrom();				
				JID targetJID = receivedMessage.getTo();
				String messageid=receivedMessage.getID();
				String type=receivedMessage.getType().toString();
				User user = getUserFromJID(targetJID);
				//return if user is online				
				if ( isUserOnline(user) ) return;				

				String deviceToken = dbManager.getDeviceToken(targetJID);
				//return if device token is null.				
				if (deviceToken == null ) return;

				int deviceType = dbManager.getDeviceType(targetJID);
				if ( deviceType != 1 &&  deviceType != 2 ) return;
				
				String senderNickName = getSenderNickName(senderJID);
				
				badge = dbManager.getBadge(targetJID);
				dbManager.updateBadge(targetJID, ++badge);
				
				String payloadString = new String();
								
				if (receivedMessage.getType() == Type.groupchat) {
					String nickName4mRsc;
					try{
						JID sendJID4mRsc = new JID(senderJID.getResource());
						nickName4mRsc = getSenderNickName(sendJID4mRsc);
					} catch(Exception e){
						//JID is not valid
						Log.error(e.getMessage());
						nickName4mRsc = senderJID.getResource();
					}
					
					payloadString = payloadString.concat(nickName4mRsc);
					
					if (binMessage != null) {
						payloadString = payloadString.concat(binMessage.replace("%s ", ""));
						payloadString = payloadString.concat(" to ");						
						payloadString = payloadString.concat(senderNickName);
					} else {					
						payloadString = payloadString.concat(" @ ");
						payloadString = payloadString.concat(senderNickName);
					}
				} else {
					payloadString = payloadString.concat(senderNickName);					
					if (binMessage != null) {
						payloadString = payloadString.concat(binMessage.replace("%s", "you"));
					}				
				}

				if ( binMessage == null ) {
					payloadString = payloadString.concat(": ");				
					payloadString = payloadString.concat(StringEscapeUtils.unescapeHtml(body));				
				}
				
				if ( deviceType == 1 ) {
					sendMessageToAPNS(deviceToken, payloadString,  badge);
				} else if ( deviceType == 2 ) {
					sendMessageToGCM(deviceToken,  payloadString,senderJID.toFullJID(),messageid,type,body);
				}
				
			} else if (original instanceof Presence) {

				JID targetJID = original.getTo();
				if (targetJID != null && isUserOnline(getUserFromJID(targetJID)) ) {
					dbManager.updateBadge(targetJID, 0);
				}				
			}
		}
	}

	private User getUserFromJID(JID targetJID) {
		User user = null;
		try {
			if ( targetJID != null ) {
				user = userManager.getUser(targetJID.getNode());
			}
		} catch (UserNotFoundException unf) {
			// Log.error("User not found : "+ unf.getMessage(), "");
		}
		return user;
	}

	private boolean isUserOnline(User user) {

		if (user == null || (user != null && presenceManager.isAvailable(user))) {
			return true;
		}
		return false;
	}

	private String getSenderNickName(JID fromJID) {

		String fromBareId = fromJID.toBareJID();
		String[] userID = fromBareId.split("@");

		String username;
		if (userID[0] == null) {
			username = new String("-");
		} else {
			username = new String(userID[0]);
		}

		String nickName = vcardManager.getVCardProperty(username, "NICKNAME");
		if (nickName == null ) {
			nickName = username;
		} else if ( nickName.trim().equalsIgnoreCase("") ) {
			nickName = username;
		}
		nickName = nickName.replaceAll("\\$#", "&#");
		nickName = StringEscapeUtils.unescapeHtml(nickName);
		return nickName;
	}
	
	
	private void sendMessageToAPNS(String deviceToken, String payloadString, int badge) {
		
		//Log.info("Sending APNS => " + payloadString + " to " + deviceToken + " badge :" + badge,"");
		pushMessage message = new pushMessage(payloadString, badge, "default", "/usr/local/openfire/directxmpp.p12", "123789", true, deviceToken);
		message.start();		
	}
	
	private void sendMessageToGCM(String deviceToken, String payloadString,String from,String messageId,String type,String body){

		//Log.info("Sending GCM =>" + payloadString + "to" + deviceToken);
		String json="{\"payload\":\""+payloadString+"\",\"from\":\""+from+"\",\"messageid\":\""+messageId+"\",\"type\":\""+type+"\",\"body\":\""+body+"\"}";
		PushAndroidMessage androidMessage = new PushAndroidMessage(deviceToken, json);
		androidMessage.start();
		
	}

	private boolean isValidTargetPacket(Packet packet, boolean read,
			boolean processed) {

		return !processed && read && (packet instanceof Message || packet instanceof Presence);
	}
}