/**
 * 
 */
package com.apoollo.commons.util.filesystem.service.model;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

import net.schmizz.sshj.xfer.InMemoryDestFile;

/**
 * @author liuyulong
 */
public class SSHClientInMemoryDestFile extends InMemoryDestFile implements Closeable {

	private ByteArrayOutputStream byteArrayOutputStream;

	/**
	 * 
	 */
	public SSHClientInMemoryDestFile() {
		byteArrayOutputStream = new ByteArrayOutputStream();

	}

	@Override
	public long getLength() {
		return byteArrayOutputStream.size();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return byteArrayOutputStream;
	}

	@Override
	public OutputStream getOutputStream(boolean append) throws IOException {
		return byteArrayOutputStream;
	}

	public byte[] toByteArray() {
		return byteArrayOutputStream.toByteArray();
	}

	@Override
	public void close() throws IOException {
		byteArrayOutputStream.close();
	}

}
