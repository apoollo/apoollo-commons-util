/**
 * 
 */
package com.apoollo.commons.util.filesystem.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.filesystem.service.FileSystem;
import com.apoollo.commons.util.filesystem.service.model.CommandResult;
import com.apoollo.commons.util.filesystem.service.model.RemoteLoginProperties;
import com.apoollo.commons.util.filesystem.service.model.SSHClientInMemoryDestFile;
import com.apoollo.commons.util.filesystem.service.model.SSHClientInMemorySourceFile;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.sftp.FileAttributes;
import net.schmizz.sshj.sftp.FileMode;
import net.schmizz.sshj.sftp.Response;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.sftp.SFTPException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

/**
 * @author liuyulong
 */
public class SSHClientRemoteFileSystem implements FileSystem {

	private RemoteLoginProperties remoteLogin;

	/**
	 * @param remoteLogin
	 */
	public SSHClientRemoteFileSystem(RemoteLoginProperties remoteLogin) {
		super();
		this.remoteLogin = remoteLogin;
	}

	public SSHClient connect() throws IOException {
		SSHClient sshClient = new SSHClient();
		sshClient.setTimeout(10000);
		sshClient.loadKnownHosts();
		sshClient.addHostKeyVerifier(new PromiscuousVerifier());
		sshClient.connect(remoteLogin.getHost(), remoteLogin.getPort());
		sshClient.authPassword(remoteLogin.getUsername(), remoteLogin.getPassword());
		return sshClient;
	}

	public <I, O> O executeSSHClientTemplate(Function<SSHClient, O> function) {
		try (SSHClient sshClient = connect()) {
			return function.apply(sshClient);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <I, O> O executeSFTPClientTemplate(SSHClient sshClient, Function<SFTPClient, O> function) {
		try (SFTPClient sftpClient = sshClient.newSFTPClient()) {
			return function.apply(sftpClient);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <I, O> O executeSFTPClientTemplate(Function<SFTPClient, O> function) {
		return executeSSHClientTemplate((sshClient) -> {
			return executeSFTPClientTemplate(sshClient, function);
		});
	}

	public <I, O> O executeSessionTemplate(SSHClient sshClient, Function<Session, O> function) {
		try (Session session = sshClient.startSession();) {
			return function.apply(session);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <I, O> O executeSessionTemplate(Function<Session, O> function) {
		return executeSSHClientTemplate((sshClient) -> {
			return executeSessionTemplate(sshClient, function);
		});
	}

	public String inputStreamAsString(InputStream inputStream) {
		String result = null;
		if (null != inputStream) {
			byte[] bytes;
			try {
				bytes = IOUtils.readFully(inputStream).toByteArray();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
			if (null != bytes && bytes.length > 0) {
				result = new String(bytes, StandardCharsets.UTF_8);
			}
		}
		return result;
	}

	public CommandResult executeCommandTemplate(Session session, String command, TimeUnit timeUnit, Long timeout) {
		Command cmd = null;
		try {
			cmd = session.exec(command); //
			if (null != timeUnit && null != timeout) {
				cmd.join(timeout, timeUnit);
			} else {
				cmd.join();
			}
			return new CommandResult(inputStreamAsString(cmd.getInputStream()),
					inputStreamAsString(cmd.getErrorStream()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(cmd);
		}
	}

	public CommandResult executeCommandTemplate(SSHClient sshClient, String command, TimeUnit timeUnit, Long timeout) {
		return executeSessionTemplate(sshClient, (session) -> {
			return executeCommandTemplate(session, command, timeUnit, timeout);
		});
	}

	public CommandResult executeCommandTemplate(String command, TimeUnit timeUnit, Long timeout) {
		return executeSSHClientTemplate((sshClient) -> {
			return executeCommandTemplate(sshClient, command, timeUnit, timeout);
		});

	}

	public FileAttributes stat(SFTPClient sftpClient, String path) {
		FileAttributes fileAttributes;
		try {
			fileAttributes = sftpClient.stat(path);
		} catch (SFTPException e) {
			if (e.getStatusCode() == Response.StatusCode.NO_SUCH_FILE
					|| e.getStatusCode() == Response.StatusCode.NO_SUCH_PATH) {
				return null;
			} else {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return fileAttributes;
	}

	public boolean sftpClientIsExists(SFTPClient sftpClient, String path) {
		FileAttributes fileAttributes = stat(sftpClient, path);
		return null != fileAttributes;
	}

	public byte[] sftpClientGetFile(SFTPClient sftpClient, String path) {
		try (SSHClientInMemoryDestFile clientInMemoryDestFile = new SSHClientInMemoryDestFile()) {
			if (sftpClientIsExists(sftpClient, path)) {
				sftpClient.get(path, clientInMemoryDestFile);
				return clientInMemoryDestFile.toByteArray();
			} else {
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void sftpClientPutFile(SFTPClient sftpClient, String path, byte[] bytes) {
		try {
			String splitor = "/";
			if (path.contains(splitor)) {
				String parentPath = StringUtils.substringBeforeLast(path, splitor);
				if (StringUtils.isBlank(parentPath)) {
					throw new RuntimeException("parentPath must not be null");
				}
				if (!sftpClientIsExists(sftpClient, parentPath)) {
					sftpClient.mkdirs(parentPath);
				}
				sftpClient.put(new SSHClientInMemorySourceFile(bytes), path);
			} else {
				throw new RuntimeException("path must contains '/'");
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void sftpClientDelete(SSHClient sshClient, SFTPClient sftpClient, String path) {
		FileAttributes fileAttributes = stat(sftpClient, path);
		if (null != fileAttributes) {
			try {
				if (fileAttributes.getType() == FileMode.Type.DIRECTORY) {
					executeCommandTemplate(sshClient, "rm -rf " + path, TimeUnit.SECONDS, 10L);
				} else {
					sftpClient.rm(path);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void sftpDelete(String path) {
		executeSSHClientTemplate((sshClient) -> {

			executeSFTPClientTemplate(sshClient, sftpClient -> {

				sftpClientDelete(sshClient, sftpClient, path);

				return null;
			});
			return null;
		});
	}

	public void commandDelete(String path) {
		executeCommandTemplate("rm -rf " + path, TimeUnit.SECONDS, 10L);
	}

	@Override
	public byte[] getFile(String path) {
		return executeSFTPClientTemplate((sftpClient) -> {
			return sftpClientGetFile(sftpClient, path);
		});
	}

	public void mkdirs(String path) {
		executeSFTPClientTemplate((sftpClient) -> {
			if (!sftpClientIsExists(sftpClient, path)) {
				try {
					sftpClient.mkdirs(path);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return null;
		});
	}

	@Override
	public void putFile(String path, byte[] bytes) {
		executeSFTPClientTemplate((sftpClient) -> {
			sftpClientPutFile(sftpClient, path, bytes);
			return null;
		});
	}

	@Override
	public void deleteFile(String path) {
		commandDelete(path);
		// sftpDelete(path);
	}

	@Override
	public boolean isExists(String path) {
		return executeSFTPClientTemplate((sftpClient) -> {
			return sftpClientIsExists(sftpClient, path);
		});
	}

	@Override
	public CommandResult executeCommand(String command, TimeUnit timeUnit, Long timeout) {
		return executeCommandTemplate(command, timeUnit, timeout);
	}

}
