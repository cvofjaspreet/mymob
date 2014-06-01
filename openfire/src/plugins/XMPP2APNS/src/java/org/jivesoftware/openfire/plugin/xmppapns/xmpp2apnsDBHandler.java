package org.jivesoftware.openfire.plugin.xmppapns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;

public class xmpp2apnsDBHandler {

	private static final Logger Log = LoggerFactory.getLogger(xmpp2apns.class);

	private static final String LOAD_TOKEN = "SELECT devicetoken FROM ofAPNS WHERE JID=?";
	private static final String LOAD_TYPE = "SELECT devicetype FROM ofAPNS WHERE JID=?";
	private static final String INSERT_TOKEN = "INSERT INTO ofAPNS VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE devicetoken = ?";
	private static final String UPDATE_BADGE = "UPDATE ofAPNS SET badge_number=?  WHERE JID = ?";
	private static final String SELECT_BADGE = "SELECT badge_number FROM ofAPNS WHERE JID = ?";

	public boolean insertDeviceToken(JID targetJID, String token,
			int device_type) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isCompleted = false;

		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(INSERT_TOKEN);
			pstmt.setString(1, targetJID.toBareJID());
			pstmt.setString(2, token);

			pstmt.setInt(3, device_type);
			pstmt.setInt(4, 0);
			pstmt.setString(5, token);
			pstmt.executeUpdate();
			pstmt.close();

			isCompleted = true;
		} catch (SQLException sqle) {
			Log.error(sqle.getMessage(), sqle);
			isCompleted = false;
		} finally {
			DbConnectionManager.closeConnection(rs, pstmt, con);
		}
		return isCompleted;
	}

	public String getDeviceToken(JID targetJID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String returnToken = null;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_TOKEN);
			pstmt.setString(1, targetJID.toBareJID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				returnToken = rs.getString(1);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException sqle) {
			Log.error(sqle.getMessage(), sqle);
			returnToken = sqle.getMessage();
		} finally {
			DbConnectionManager.closeConnection(rs, pstmt, con);
		}
		return returnToken;
	}

	public int getDeviceType(JID targetJID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int returnType = 0;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(LOAD_TYPE);
			pstmt.setString(1, targetJID.toBareJID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				returnType = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException sqle) {
			Log.error(sqle.getMessage(), sqle);
			// returnToken = sqle.getMessage();
		} finally {
			DbConnectionManager.closeConnection(rs, pstmt, con);
		}
		return returnType;
	}

	public int updateBadge(JID targetJID, int badge) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int returnType = 0;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(UPDATE_BADGE);
			pstmt.setInt(1, badge);
			pstmt.setString(2, targetJID.toBareJID());

			returnType = pstmt.executeUpdate();

			pstmt.close();
		} catch (SQLException sqle) {
			Log.error(sqle.getMessage(), sqle);
			// returnToken = sqle.getMessage();
		} finally {
			DbConnectionManager.closeConnection(rs, pstmt, con);
		}
		return returnType;
	}

	public int getBadge(JID targetJID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int returnType = 0;
		try {
			con = DbConnectionManager.getConnection();
			pstmt = con.prepareStatement(SELECT_BADGE);

			pstmt.setString(1, targetJID.toBareJID());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				returnType = rs.getInt(1);
			}

			pstmt.close();
		} catch (SQLException sqle) {
			Log.error(sqle.getMessage(), sqle);
			// returnToken = sqle.getMessage();
		} finally {
			DbConnectionManager.closeConnection(rs, pstmt, con);
		}
		return returnType;
	}
}