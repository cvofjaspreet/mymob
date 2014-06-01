package org.jivesoftware.openfire.plugin.fileupload;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.plugin.Fileupload;



/**

 *
 * @author Jaspreet
 */
public class FileUploadServlet extends HttpServlet {

    private Fileupload plugin;
    private static Logger Log = Logger.getLogger(Fileupload.class);
    protected long amountWritten = -1;
    private static final int BUFFER_SIZE = 8192;
    @Override
	public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        plugin = (Fileupload) XMPPServer.getInstance().getPluginManager().getPlugin("fileupload");
 Log.info("File upload  plugin" +plugin.getClass());
        // Exclude this servlet from requiring the user to login
        AuthCheckFilter.addExclude("fileupload/userservice");
        Log.info("Fileupload  plugin   AuthCheckFilter.addExclude" );
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
    	PrintWriter out = response.getWriter();
    	try{
    	Log.info("response HttpServletRequest ok");
    	
    	
    	 String name=request.getParameter("name");
    	 String type=request.getParameter("type");
    	 Log.info("type = "+type);
    	 Log.info("name = "+name);
    	 if (type.equals("download")) {
    		 Log.info("line 68");
    		 OutputStream outputStream=response.getOutputStream();
    		FileInputStream fileInputStream=new FileInputStream(Fileupload.VIDEO_DIRECTORY+"/"+name);
    		 Log.info("line 72 ");
    		sendFile(response, outputStream, fileInputStream);
    		 Log.info("line 73");
    		 return;
    	 } 
    	 
    	}catch (Exception e) {
			// TODO: handle exception
    		replyMessage("error", response, out);
	    	Log.error("error while downloading file");
    	Log.info("error = "+e.getMessage());
		}
    	}
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    		
  
    	 PrintWriter out = response.getWriter();
    		 try {
    			 Log.info("line 93");
    			 
    		        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
    		        Log.info("line 94");
    		        for (FileItem item : items) {
    		        	Log.info("line 96");
    		            if (item.isFormField()) {
    		            	Log.info("line 98");
    		                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
    		                String fieldname = item.getFieldName();
    		                String fieldvalue = item.getString();
    		                
    		                Log.info("fieldname = "+fieldname+"  fieldvalue = "+fieldvalue);
    		                
    		                // ... (do your job here)
    		            } else {
    		                // Process form file field (input type="file").
    		            	Log.info("line 105");
    		                String fieldname = item.getFieldName();
    		                String filename = FilenameUtils.getName(item.getName());
    		                Log.info("line 108");
    		                InputStream filecontent = item.getInputStream();
    		                Log.info("line 110");
    		               // InputStream inputStream=(InputStream) request.getAttribute("uploaded");
    		        		//InputStream inputStream=request.getInputStream();
    		        		File file = new File(Fileupload.VIDEO_DIRECTORY+"/"+filename);
    		        		if(!file.exists()){
    		        			file.createNewFile();
    		        		}
//    		            	File file = new File(filename);
    		        		Log.info("line 114");
    		        		FileOutputStream fop = new FileOutputStream(file);
    		        		Log.info("line 116");
    		        		uploadFile(response, fop, filecontent);
    		        		Log.info("line 118");
//    		        		replyMessage("error", response, out);
    		        		replyMessage("https://xmpp.mara.com:9091/plugins/fileupload/userservice?type=download&name="+filename, response, out);
    		            }
    		        }
    		    } catch (Exception e) {
    		    	replyMessage("error", response, out);
    		    	Log.error("error while uploading file");
    		        throw new ServletException("Cannot parse multipart request.", e);
    		    }
    		
    	/*	InputStream inputStream=(InputStream) request.getAttribute("uploaded");
    		//InputStream inputStream=request.getInputStream();
    		File file = new File(name);
    		FileOutputStream fop = new FileOutputStream(file);
    		uploadFile(response, fop, inputStream);*/
    		
    	// }
    	
        	
    }
    private void replyMessage(String message,HttpServletResponse response, PrintWriter out){
        response.setContentType("text/xml");        
        out.println("{\"result\":\"" + message + "\"}");
        out.flush();
    }
    
    private void uploadFile(HttpServletResponse response, OutputStream out,InputStream inputStream){
    	

		final byte[] b = new byte[BUFFER_SIZE];
		int count = 0;
		amountWritten = 0;

        do {
			// write to the output stream
			try {
				out.write(b, 0, count);
			} catch (IOException e) {
			}

			amountWritten += count;

			// read more bytes from the input stream
			try {
				count = inputStream.read(b);
			} catch (IOException e) {
				Log.info("error = "+e.getMessage());
			}
		} while (count != -1 );
        try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.info("error = "+e.getMessage());
		}
		// the connection was likely terminated abrubtly if these are not equal
		
	
    	
    	
    }
    private void sendFile(HttpServletResponse response, OutputStream out,FileInputStream inputStream){
    	
    	try{
    	response.setContentType("video/mp4");      
    	
    	
    	final byte[] b = new byte[BUFFER_SIZE];
		int count = 0;
		amountWritten = 0;

        do {
			// write to the output stream
			try {
				out.write(b, 0, count);
			} catch (IOException e) {
				//throw new XMPPException("error writing to output stream", e);
			}

			amountWritten += count;

			// read more bytes from the input stream
			try {
				count = inputStream.read(b);
			} catch (IOException e) {
				//throw new XMPPException("error reading from input stream", e);
			}
		} while (count != -1 );
    	
    	}catch (Exception e) {
			// TODO: handle exception
    		Log.info("error = "+e.getMessage());
		}
    	try {
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       // out.flush();
    }
    private void replyError(String error,HttpServletResponse response, PrintWriter out){
        response.setContentType("text/xml");        
        out.println("<error>" + error + "</error>");
        out.flush();
    }
    
   

    @Override
	public void destroy() {
        super.destroy();
        // Release the excluded URL
     //   AuthCheckFilter.removeExclude("userService/userservice");
    }
}
