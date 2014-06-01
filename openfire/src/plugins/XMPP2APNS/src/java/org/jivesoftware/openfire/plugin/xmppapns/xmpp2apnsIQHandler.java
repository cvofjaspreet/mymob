package org.jivesoftware.openfire.plugin.xmppapns;

import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;

public class xmpp2apnsIQHandler extends IQHandler {
	private static final Logger Log = LoggerFactory.getLogger(xmpp2apns.class);
	private IQHandlerInfo info;

	private xmpp2apnsDBHandler dbManager;

	public xmpp2apnsIQHandler() {
		super("XMPP2APNS IQ Handler");
		info = new IQHandlerInfo("query", "urn:xmpp:apns");
		dbManager = new xmpp2apnsDBHandler();
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		IQ result = IQ.createResultIQ(packet);

		JID from = packet.getFrom();
		IQ.Type type = packet.getType();

		if (type.equals(IQ.Type.get)) {

			Element responseElement = DocumentHelper.createElement(QName.get(
					"query", "urn:xmpp:apns"));
			responseElement.addElement("token").setText(
					dbManager.getDeviceToken(from));

			result.setChildElement(responseElement);

		} else if (type.equals(IQ.Type.set)) {

			Element receivedPacket = packet.getElement();

			String token = receivedPacket.element("query").elementText("token");

			if (receivedPacket.element("query").elementText("device_type") == null) {
				result.setChildElement(packet.getChildElement().createCopy());
				result.setError(PacketError.Condition.not_acceptable);
			} else {
				try {
					int device_type = Integer.parseInt(receivedPacket.element(
							"query").elementText("device_type"));
					//Log.info("device_type = " + device_type);
					switch (device_type) {

					case 1:
						if (token.length() == 64) {
							// For IOS
							if (dbManager.insertDeviceToken(from, token,
									device_type)) {
								Element responseElement = DocumentHelper
										.createElement(QName.get("query",
												"urn:xmpp:apns"));
								responseElement.addElement("token").setText(
										token);

								result.setChildElement(responseElement);

							} else {
								result.setChildElement(packet.getChildElement()
										.createCopy());
								result.setError(PacketError.Condition.internal_server_error);
							}
						} else {
							result.setChildElement(packet.getChildElement()
									.createCopy());
							result.setError(PacketError.Condition.not_acceptable);
						}

						break;
					case 2:
						// For Android
						if (token.length() == 162) {
							if (dbManager.insertDeviceToken(from, token,
									device_type)) {

								Element responseElement = DocumentHelper
										.createElement(QName.get("query",
												"urn:xmpp:apns"));
								responseElement.addElement("token").setText(
										token);

								result.setChildElement(responseElement);

							} else {
								result.setChildElement(packet.getChildElement()
										.createCopy());
								result.setError(PacketError.Condition.internal_server_error);
							}

						} else {
							result.setChildElement(packet.getChildElement()
									.createCopy());
							result.setError(PacketError.Condition.not_acceptable);
						}
						break;

					default:
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
					result.setChildElement(packet.getChildElement()
							.createCopy());
					result.setError(PacketError.Condition.not_acceptable);

				}
			}
		} else {
			result.setChildElement(packet.getChildElement().createCopy());
			result.setError(PacketError.Condition.not_acceptable);
		}

		return result;
	}

}