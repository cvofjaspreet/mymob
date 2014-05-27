package org.jivesoftware.openfire.plugin.mymob.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.plugin.mymob.PersistenceManager;
import org.jivesoftware.openfire.plugin.mymob.model.ShareModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * Manages database persistence.
 */
public class JdbcPersistenceManager implements PersistenceManager {
	public static final int DEFAULT_MAX = 1000;
	private static final Logger Log = LoggerFactory.getLogger(JdbcPersistenceManager.class);
	public static final String INSERT_SHARE ="INSERT INTO ofShare(shareDate,text,imageUrl,videoUrl,thumbUrl,jid,type) VALUES(?, ?,?,?,?,?,?)";



	@Override
	public void insertShare(ShareModel model) {
		// TODO Auto-generated method stub

		Log.info("insertShare");
		 Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	                
	        try {
	            con = DbConnectionManager.getConnection();
	            pstmt = con.prepareStatement(INSERT_SHARE);
	           
	            pstmt.setLong(1, model.getDate());
	            pstmt.setString(2, model.getText());
	            pstmt.setString(3, model.getImageUrl());
	            pstmt.setString(4, model.getVideoUrl());
	            pstmt.setString(5, model.getThumbUrl());
	            pstmt.setString(6, model.getJid());
	            pstmt.setString(7, model.getType());
	            
	            pstmt.executeUpdate();
	           
	        }
	        catch (SQLException sqle) {
	        	Log.info("error ="+sqle);
	        }
	        finally {
	            DbConnectionManager.closeConnection(rs, pstmt, con);
	        }
		
	
	}

	
}
