package com.reucon.openfire.plugin.archive.xep0136;

import com.reucon.openfire.plugin.archive.model.Conversation;
import com.reucon.openfire.plugin.archive.util.XmppDateUtil;
import com.reucon.openfire.plugin.archive.xep0059.XmppResultSet;
import org.dom4j.Element;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.disco.ServerFeaturesProvider;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Message Archiving List Handler.
 */
public class IQListHandler extends AbstractIQHandler implements ServerFeaturesProvider
{
    private static final String NAMESPACE_MANAGE = "urn:xmpp:archive:manage";
    private  JID from;
    public IQListHandler()
    {
        super("Message Archiving List Handler", "list");
    }

    public IQ handleIQ(IQ packet) throws UnauthorizedException
    {
        IQ reply = IQ.createResultIQ(packet);
        ListRequest listRequest = new ListRequest(packet.getChildElement());
        from = packet.getFrom();

        Element listElement = reply.setChildElement("list", NAMESPACE);
        List<Conversation> conversations = list(from, listRequest);
        XmppResultSet resultSet = listRequest.getResultSet();

        for (Conversation conversation : conversations)
        {
            addChatElement(listElement, conversation);
        }

        if (resultSet != null)
        {
            listElement.add(resultSet.createResultElement());
        }

        return reply;
    }

    private List<Conversation> list(JID from, ListRequest request)
    {
        return getPersistenceManager().findConversations(request.getStart(), request.getEnd(),
                from.toBareJID(), request.getWith(), request.getResultSet());
    }

    private Element addChatElement(Element listElement, Conversation conversation)
    {
        Element chatElement = listElement.addElement("chat");

        chatElement.addAttribute("with", conversation.getWithJid());
        DateFormat dateFormatWithoutMillis =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormatWithoutMillis.setTimeZone(TimeZone.getTimeZone("UTC"));
        chatElement.addAttribute("start", dateFormatWithoutMillis.format(conversation.getStart()));
        RetrieveRequest request= new RetrieveRequest(conversation.getWithJid(), conversation.getStart());
        Conversation conversation_= retrieve(from,request );
       
        chatElement.addAttribute("conversation", conversation_.toString());
        return chatElement;
    }

    public Iterator<String> getFeatures()
    {
        ArrayList<String> features = new ArrayList<String>();
        features.add(NAMESPACE_MANAGE);
        return features.iterator();
    }
    private Conversation retrieve(JID from, RetrieveRequest request)
    {
        return getPersistenceManager().getConversation(from.toBareJID(), request.getWith(), request.getStart());
    }
}
