package com.miwan.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class liuziangDotCom {
	//port
	static public void main(String[] args) {
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(Integer.parseInt(args[0]), 1, Inet4Address.getLocalHost());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			while (true) {
				try (Socket client = listener.accept()) {
					InputStream readStream = client.getInputStream();
					OutputStream writeStream = client.getOutputStream();

					while (true) {
						//read the body length
						ByteBuffer length = ByteBuffer.wrap(new byte[4]);
						if (readStream.read(length.array()) != 4)
							continue;
						final int messageLength = length.getInt();
						if (messageLength > 2048) {
							writeStream.write("code length too long".getBytes(StandardCharsets.UTF_8));
							continue;
						}
						if (messageLength == 0) {
							writeStream.write("invalid code length".getBytes(StandardCharsets.UTF_8));
							continue;
						}

						//read body
						byte[] src = new byte[messageLength];
						int unread = messageLength;
						while (unread > 0) {
							unread -= readStream.read(src, messageLength - unread, unread);
						}
						if (unread < 0) {
							throw new Error("something goes wrong...");
						}

						//execute code
						final String srcAsString = new String(src, StandardCharsets.UTF_8);
						String execResult = String.valueOf(Interpreter.execute(srcAsString));

						//send execution result
						writeStream.write(execResult.getBytes(StandardCharsets.UTF_8));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
