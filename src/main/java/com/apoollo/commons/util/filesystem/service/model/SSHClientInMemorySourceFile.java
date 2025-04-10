/**
 * 
 */
package com.apoollo.commons.util.filesystem.service.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.schmizz.sshj.xfer.InMemorySourceFile;

/**
 * @author liuyulong
 */
public class SSHClientInMemorySourceFile extends InMemorySourceFile {

	private byte[] source;

	/**
	 * @param source
	 */
	public SSHClientInMemorySourceFile(byte[] source) {
		super();
		this.source = source;
	}

	@Override
	public String getName() {
		return "SSHClientInMemorySourceFile";
	}

	@Override
	public long getLength() {
		return source.length;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(source);
	}

}
