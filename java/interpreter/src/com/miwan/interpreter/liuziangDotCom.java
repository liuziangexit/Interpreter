package com.miwan.interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class liuziangDotCom {
	//port
	static public void main(String[] args) {
		ServerSocket listener = null;
		try {
			listener = new ServerSocket(Integer.parseInt(args[0]), 1, Inet4Address.getByName("localhost"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			System.out.println("go");
			while (true) {
				try (Socket client = listener.accept()) {
					System.out.println("connected");
					InputStream readStream = client.getInputStream();
					OutputStream writeStream = client.getOutputStream();

					Consumer<String> send = s -> {
						byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
						try {
							writeStream.write(i32s(bytes.length));
							writeStream.write(bytes);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					};

					while (true) {
						//read the body length
						byte[] length = new byte[4];
						if (readStream.read(length) != 4)
							break;
						final int messageLength = i32d(length);
						if (messageLength > 2048) {
							send.accept("code length too long");
							break;
						}
						if (messageLength == 0) {
							send.accept("invalid code length");
							break;
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
						boolean hasEx = false;
						Object executeResult = null;
						try {
							executeResult = Interpreter.execute(srcAsString);
						} catch (Exception ex) {
							send.accept(ex.toString());
							hasEx = true;
						}
						if (!hasEx) {
							//send execution result
							send.accept(executeResult.toString());
						}
						System.out.println("request ok");
					}
					System.out.println("disconnected");
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

	// serialization 32 bit integer number to bytes
	static private byte[] i32s(int i) {
		byte[] result = new byte[4];
		for (int f = 0; f < 4; f++)
			result[f] = (byte) (i >>> ((3 - f) * 8));
		return result;
	}

	// deserialization 32 bit integer number from bytes
	static private int i32d(byte[] i) {
		int result = 0;
		for (int f = 0; f < 4; f++)
			result |= ((0xFF & (int) i[f]) << ((3 - f) * 8));
		return result;
	}
}
