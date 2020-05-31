package socket.java;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Server {
	
	private int port;
	
	private ServerSocket server;
	private Socket client;
	protected PrintWriter out;
	protected BufferedReader in;
	
	public Server(int port) {
		this.port = port;
	}
	
	public void initServer() {
		try {
			server = new ServerSocket(this.port);
			
			System.out.println("Initialized ServerSocket on port " + port);
        } catch (UnknownHostException e) {
            System.err.println("Unknown Host.");
           // System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection.");
            e.printStackTrace();
        }
	}
	
	public boolean waitForConnections() {
		try {
			//Blocks the current thread till a client connecting to this port is found
			client = this.server.accept();
			System.out.println("Got connection on port " + this.port);
			
			//server.accept() is not blocking anymore, means somebody connected on the port.
			
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			try {
				server.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return true;
	}
	
	public Socket getClientConnection() {
		return client;
	}
	
	public String read() throws SocketException {
		String fromClient = "";
		
		try {
			fromClient = in.readLine();
			while(fromClient == null) {
				Thread.sleep(10);
				fromClient = in.readLine();
			}
			
		} catch(SocketException ex) {
			System.err.println("Probably connection reset: socket.java.Server.java (l.76)");
			waitForConnections();
			return read();
		} catch(IOException ex) {
			ex.printStackTrace();
			return "";
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fromClient;
	}
	
	public void write(String message) {
		out.println(message);
	}
	
	public void closeClientConnection() {
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeServer() {
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
