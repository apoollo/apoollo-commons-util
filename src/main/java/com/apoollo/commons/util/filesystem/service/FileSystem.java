/**
 * 
 */
package com.apoollo.commons.util.filesystem.service;

import java.util.concurrent.TimeUnit;

import com.apoollo.commons.util.filesystem.service.model.CommandResult;

/**
 * @author liuyulong
 */
public interface FileSystem {

	public byte[] getFile(String path);
	
	public void mkdirs(String path);

	public void putFile(String path, byte[] bytes);

	public void deleteFile(String path);

	public boolean isExists(String path);

	public CommandResult executeCommand(String command, TimeUnit timeUnit, Long timeout);

}
