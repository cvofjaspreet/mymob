/**
 * $RCSfile$
 * $Revision: 1710 $
 * $Date: 2005-07-26 11:56:14 -0700 (Tue, 26 Jul 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.openfire.plugin.userService;

import gnu.inet.encoding.Stringprep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.openfire.SharedGroupException;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.group.GroupAlreadyExistsException;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.plugin.UserServicePlugin;
import org.jivesoftware.openfire.plugin.model.SearchResult;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;



/**
 * Servlet that addition/deletion/modification of the users info in the system.
 * Use the <b>type</b> parameter to specify the type of action. Possible values
 * are <b>add</b>,<b>delete</b> and <b>update</b>.
 * <p>
 * <p/>
 * The request <b>MUST</b> include the <b>secret</b> parameter. This parameter
 * will be used to authenticate the request. If this parameter is missing from
 * the request then an error will be logged and no action will occur.
 * 
 * @author Justin Hunt
 * 
 * 
 *         --------------------------------------------------------------------
 *         ---------------------
 * 
 * @author jaspreet
 * 
 *         TODO
 * 
 *         1. We need to change the request to json instead of xml
 * 
 *         2. Request should also contain the phone number in the device. They
 *         all are searched and the corresponding results will be created as
 *         roaster and sent back to the user. 
 *         
 *         3. Request will be of type post.
 *         
 *         4. User can register with no phone number.(It will be always the case of desktop app)
 * 
 * 
 */
public class UserServiceServlet extends HttpServlet {

    private UserServicePlugin plugin;
	private String TIME="createdTime";
	private String USER_JID="jid";
	private static final Logger Log = LoggerFactory.getLogger(UserServiceServlet.class);

    @Override
	public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        plugin = (UserServicePlugin) XMPPServer.getInstance().getPluginManager().getPlugin("userservice");
 
        // Exclude this servlet from requiring the user to login
        AuthCheckFilter.addExclude("userService/userservice");
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // Printwriter for writing out responses to browser
        PrintWriter out = response.getWriter();

        if (!plugin.getAllowedIPs().isEmpty()) {
            // Get client's IP address
            String ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null) {
                ipAddress = request.getHeader("X_FORWARDED_FOR");
                if (ipAddress == null) {
                    ipAddress = request.getHeader("X-Forward-For");
                    if (ipAddress == null) {
                        ipAddress = request.getRemoteAddr();
                    }
                }
            }
            if (!plugin.getAllowedIPs().contains(ipAddress)) {
                Log.warn("User service rejected service to IP address: " + ipAddress);
                replyError("RequestNotAuthorised",response, out);
                return;
            }
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String type = request.getParameter("type");
        String secret = request.getParameter("secret");
        String groupNames = request.getParameter("groups");
        String item_jid = request.getParameter("item_jid");
        String sub = request.getParameter("subscription");
        String serachUser = request.getParameter("serachUser");
        serachUser=URLDecoder.decode(serachUser,"UTF-8");
        //No defaults, add, delete, update only
        //type = type == null ? "image" : type;
       
       // Check that our plugin is enabled.
        if (!plugin.isEnabled()) {
            Log.warn("User service plugin is disabled: " + request.getQueryString());
           // replyError("UserServiceDisabled",rJSONObject  jsonObject=new JSONObject();esponse, out);
            return;
        }
    
        // Check this request is authorised
        if (secret == null || !secret.equals(plugin.getSecret())){
            Log.warn("An unauthorised user service request was received: " + request.getQueryString());
            replyError("RequestNotAuthorised",response, out);
            return;
         }

        // Some checking is required on the username
        if (username == null){
       replyError("IllegalArgumentException",response, out);
            return;
        }

        if ((type.equals("add_roster") || type.equals("update_roster") || type.equals("delete_roster")) &&
        	(item_jid == null || !(sub == null || sub.equals("-1") || sub.equals("0") ||
        	sub.equals("1") || sub.equals("2") || sub.equals("3")))) {
            replyError("IllegalArgumentException",response, out);
            return;
        }

        // Check the request type and process accordingly
        try {
            username = username.trim().toLowerCase();
            username = JID.escapeNode(username);
            username = Stringprep.nodeprep(username);
            if ("add".equals(type)) {
                plugin.createUser(username, password, name, email, groupNames);
               
				 replyMessage("ok".toString(),response, out);
				
          
                //imageProvider.sendInfo(request, response, presence);
            }
            else if ("delete".equals(type)) {
                plugin.deleteUser(username);
                replyMessage("ok",response,out);
                //xmlProvider.sendInfo(request, response, presence);
            }
            else if ("enable".equals(type)) {
                plugin.enableUser(username);
                replyMessage("ok",response,out);
            }
            else if ("disable".equals(type)) {
                plugin.disableUser(username);
                replyMessage("ok",response,out);
            }
            else if ("update".equals(type)) {
                plugin.updateUser(username, password,name,email, groupNames);
                replyMessage("ok",response,out);
                //xmlProvider.sendInfo(request, response, presence);
            }
            else if ("add_roster".equals(type)) {
                plugin.addRosterItem(username, item_jid, name, sub, groupNames);
                replyMessage("ok",response, out);
            }
            else if ("update_roster".equals(type)) {
                plugin.updateRosterItem(username, item_jid, name, sub, groupNames);
                replyMessage("ok",response, out);
            }
            else if ("delete_roster".equals(type)) {
                plugin.deleteRosterItem(username, item_jid);
                replyMessage("ok",response, out);
            }
            else {
                Log.warn("The userService servlet received an invalid request of type: " + type);
                // TODO Do something
            }
        }
        catch (UserAlreadyExistsException e) {
            replyError("UserAlreadyExistsException",response, out);
        }
        catch (UserNotFoundException e) {
            replyError("UserNotFoundException",response, out);
        }
        catch (IllegalArgumentException e) {
            
            replyError("IllegalArgumentException",response, out);
        }
        catch (SharedGroupException e) {
        	
        	replyError("SharedGroupException",response, out);
        }
        catch (Exception e) {
            replyError(e.toString(),response, out);
        }
    }

	
    /**
     * create roaster with subscription 3 
     * @param username 
     * 
     * @param result {@link SearchResult}
     */
    private JSONObject createRoaster(String username, SearchResult result) {
    	JSONObject jsonObject=new JSONObject();
    	Set<User> users =result.getUsers();
    	Iterator<User> iterator=users.iterator();
    	while (iterator.hasNext()) {
			User user = (User) iterator.next();
			try {
				Log.info("CREATE ROASTER ="+user.getEmail());
				Log.info("ROASTER CREATE ="+user.getUID());
				Log.info("ROASTER CREATE ="+user.getUsername());
				plugin.addRosterItem(username, user.getUsername(),user.getUsername() , "3", null);
				Log.info("ROASTER CREATED ="+user.getUID());
				String time=""+user.getCreationDate().getTime();
				String jid=user.getEmail();
				
				/**
				 * VCardManager cardManager=new VCardManager();
				 * Element element=cardManager.getVCard(user.getUsername());
				 * 
				 * TODO we need to set the field for thumb and user full image
				 * 
				 */
			jsonObject.put(TIME, time);
			jsonObject.put(USER_JID, jid);	
			return jsonObject;
			
			} catch (UserNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UserAlreadyExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SharedGroupException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
    	
		return jsonObject;
	}

	/**
	 * TODO copy the method from search plug-in for searching the number in user-name 
	 * 
	 * @param number to be searched on server
	 * @return {@link SearchResult}
	 */
	private SearchResult search(String number) {
		Log.info("Request = "+number);
		Set<User> users =plugin.performSearch(number);
		Log.info("RESULT = "+users.toString());
		SearchResult result=new SearchResult();
		
		if(users.size()>0){
		result.setUsers(users);
		result.setAvailable(true);	
		}else{
		result.setAvailable(false);
		}
		return result;
	}

	private void replyMessage(String message,HttpServletResponse response, PrintWriter out){
        response.setContentType("text/plain");        
        JSONObject  jsonObject=new JSONObject();
        jsonObject.put("result", message);
        
        out.println(jsonObject.toString());
        out.flush();
    }

    private void replyError(String error,HttpServletResponse response, PrintWriter out){
    	 response.setContentType("text/plain");        
         JSONObject  jsonObject=new JSONObject();
         jsonObject.put("result", error);
         
         out.println(jsonObject.toString());
        out.flush();
    }
  
    @Override 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	   PrintWriter out = response.getWriter();

    		InputStream inputStream=request.getInputStream();
    		InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
    		BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
    		
    		String  string="";
    		StringBuffer buffer=new StringBuffer();
    		while ((string=bufferedReader.readLine())!=null) {
    			buffer.append(string);
    		}
    		 
    		String jsonrequest=buffer.toString();
    	
    		JSONObject jsonObject=JSONObject.fromObject(jsonrequest);
    		String type=jsonObject.getString("type");
    		String secret=jsonObject.getString("secret");
    		String username=jsonObject.getString("username");
    		String password=jsonObject.getString("password");
    		String name=jsonObject.getString("name");
    		String email=jsonObject.getString("email");
    		JSONArray jsonArray=jsonObject.getJSONArray("searchUser");
    		
    		 if (secret == null || !secret.equals(plugin.getSecret())){
    	            Log.warn("An unauthorised user service request was received: " + request.getQueryString());
    	            replyError("RequestNotAuthorised",response, out);
    	            return;
    	         }
    		  if ("add".equals(type)) {
                  try {
					plugin.createUser(username, password, name, email, null);
					
					addRoaster(jsonArray,username,response,out);
					
					
				} catch (UserAlreadyExistsException e) {
					e.printStackTrace();
					try {
						/**
						 * delete user and register user with new password
						 */
						plugin.deleteUser(username);
						plugin.createUser(username, password, name, email, null);
						addRoaster(jsonArray,username,response,out);
					} catch (UserNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SharedGroupException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (UserAlreadyExistsException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (GroupAlreadyExistsException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (GroupNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					addRoaster(jsonArray,username,response,out);
				} catch (GroupAlreadyExistsException e) {
					e.printStackTrace();
				} catch (UserNotFoundException e) {
					e.printStackTrace();
				} catch (GroupNotFoundException e) {
					e.printStackTrace();
				}
    		  
    		  
  }
    		
    
    }

    private void addRoaster(JSONArray jsonArray,String username,HttpServletResponse response,PrintWriter out) {
    	JSONArray array_result=new JSONArray();
    	@SuppressWarnings("unchecked")
		Iterator<String> iterator = jsonArray.iterator();
		while (iterator.hasNext()) {
			String number = (String) iterator.next();
			SearchResult result=search(number);
			if(result.isAvailable()){
			JSONObject roaster=createRoaster(username,result);
			/**
			 * TODO  
			 * 
			 * we have to get user profile picture urls 
			 * for both thumb-nail and big image
			 * 
			 * Also we need to get user last seen status and registered time
			 */
			array_result.add(roaster);
			
			//replyMessage(array_result.toString(), response, out);
			
			}
			
		}
		JSONObject resJsonObjects=new JSONObject();
		resJsonObjects.put("roasters",array_result.toString());
		resJsonObjects.put("result","ok" );
		 replyMessage(resJsonObjects.toString(),response, out);	
	}

	@Override
	public void destroy() {
        super.destroy();
        // Release the excluded URL
        AuthCheckFilter.removeExclude("userService/userservice");
    }
    
    
    @SuppressWarnings("unused")
	private String getUserNameFromEmail(String username) {
		// TODO Auto-generated method stub
    	return username.substring(0, username.indexOf('@'));
	}

    
}
