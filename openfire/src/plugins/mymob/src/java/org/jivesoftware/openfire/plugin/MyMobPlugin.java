/**
 * $Revision: 1722 $
 * $Date: 2005-07-28 15:19:16 -0700 (Thu, 28 Jul 2005) $
 *
 * Copyright (C) 2005-2008 Jive Software. All rights reserved.
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

package org.jivesoftware.openfire.plugin;

import java.io.File;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.plugin.mymob.PersistenceManager;
import org.jivesoftware.openfire.plugin.mymob.imp.JdbcPersistenceManager;



/**
 *
 * @author Jaspreet Singh
 */
public class MyMobPlugin implements Plugin{
   
	private PersistenceManager persistenceManager;
    private static MyMobPlugin mobPlugin;
    
    public MyMobPlugin() {
	mobPlugin=this;
    
    }
    
	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		// TODO Auto-generated method stub
		persistenceManager =new JdbcPersistenceManager();
	}

	@Override
	public void destroyPlugin() {
		// TODO Auto-generated method stub
		
	}

	public  PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}
   public static MyMobPlugin getInstance() {
	// TODO Auto-generated method stub
	   return mobPlugin;
}
}
