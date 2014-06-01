package org.jivesoftware.openfire.plugin;

import java.io.File;

import org.apache.log4j.Logger;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;

public class Fileupload implements Plugin {

	private static Logger Log = Logger.getLogger(Fileupload.class);
	public static String VIDEO_DIRECTORY = "/opt/openfire/resources/videos";
	public static String AUDIO_DIRECTORY = "/opt/openfire/resources/audio";
	public static String IMAGE_DIRECTORY = "/opt/openfire/resources/image";
	public static String THUMB_DIRECTORY = "/opt/openfire/resources/thumb";

	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		try {
			File video = new File(VIDEO_DIRECTORY);
			File audio = new File(AUDIO_DIRECTORY);
			File image = new File(IMAGE_DIRECTORY);
			File thumb = new File(THUMB_DIRECTORY);
			
			if (!video.exists())
				video.mkdir();
			
			if (!audio.exists())
				audio.mkdir();

			if (!image.exists())
				image.mkdir();

			if (!thumb.exists())
				thumb.mkdir();
		
		} catch (Exception e) {
			// TODO: handle exception
			Log.info("error while creating ../resources/ "
					+ e.getMessage());
		}
	}

	public void destroyPlugin() {
	}

}
