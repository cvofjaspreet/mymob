

package org.jivesoftware.openfire.plugin.mymob;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.plugin.MyMobPlugin;
import org.jivesoftware.openfire.plugin.mymob.model.ShareModel;



/**
 *
 * @author Jaspreet Singh
 */
public class MyMobServelet extends HttpServlet {

    private MyMobPlugin plugin;
    private PersistenceManager persistenceManager;

    @Override
	public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        plugin = (MyMobPlugin) XMPPServer.getInstance().getPluginManager().getPlugin("mymob");
 
        // Exclude this servlet from requiring the user to login
        AuthCheckFilter.addExclude("mymob/home");
        
       
        
        
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	PrintWriter out = response.getWriter();
    	
    	String type = request.getParameter("type");
        String image = request.getParameter("image");
        String video = request.getParameter("video");
        String thumb = request.getParameter("thumb");
        String text = request.getParameter("text");
        String jid=request.getParameter("jid");
    	JSONObject  jsonObject=new JSONObject();
    	jsonObject.put("result", "sucess");
    	
    	persistenceManager=MyMobPlugin.getInstance().getPersistenceManager();
    	ShareModel model=new ShareModel();
    	model.setDate(System.currentTimeMillis());
    	model.setImageUrl(image);
    	model.setType(type);
    	model.setVideoUrl(video);
    	model.setThumbUrl(thumb);
    	model.setText(text);
    	model.setJid(jid);
    	persistenceManager.insertShare(model);
    	replyMessage(jsonObject.toString(), response, out);
    }

    private void replyMessage(String message,HttpServletResponse response, PrintWriter out){
      //  response.setContentType("text/xml");        
        out.println( message);
        out.flush();
    }

    private void replyError(String error,HttpServletResponse response, PrintWriter out){
        response.setContentType("text/xml");        
        out.println("<error>" + error + "</error>");
        out.flush();
    }
    
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
    	// TODO Auto-generated method stub
    	super.doDelete(req, resp);
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
    	// TODO Auto-generated method stub
    	super.doPut(req, resp);
    }
    
    @Override
	public void destroy() {
        super.destroy();
        // Release the excluded URL
        AuthCheckFilter.removeExclude("mymob/home");
    }
}
