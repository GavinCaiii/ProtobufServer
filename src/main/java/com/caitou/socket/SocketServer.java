package com.caitou.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The socket server, this server could listen the client and get a connection then pass the connection to the
 * service to handle the business.
 * @author GuangzhaoCai 
 * @since 2016-06-24
 */
public class SocketServer {
    public static final int PORT = 8888;
    private ServerSocket server = null;
    private Socket socket = null;
    private BlockingQueue<Runnable> blockingQueue = null;
    private ThreadPoolExecutor executor = null;
    private ListenThread listenThread = null;
    private TransferService.DataReceived mListener;

    private static SocketServer mInstance = null;

    public static SocketServer getInstance(){
        if (mInstance == null)
            mInstance = new SocketServer();
        return mInstance;
    }

    private SocketServer(){
        try {
            server = new ServerSocket(PORT);
            blockingQueue = new ArrayBlockingQueue<>(10);
            executor = new ThreadPoolExecutor(1, 5, 60, TimeUnit.SECONDS, blockingQueue,
                    new ThreadPoolExecutor.DiscardPolicy());
            System.out.println("========================== begin ==========================");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start to listen on the socket, when get a connection run a Service to serve for the client.
     */
    public void startToListen(TransferService.DataReceived listener){
        if (listenThread == null || listenThread.getState() != Thread.State.NEW)
            listenThread = new ListenThread();
        listenThread.isRunning = true;
        this.mListener = listener;
        listenThread.start();
    }

    public void sendDataToClient(byte[] data){
        try {
            if (socket == null)
                return;
            OutputStream os = socket.getOutputStream();
            os.write(data, 0, data.length);
            os.flush();
//            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop the listen if the ListenThread is running.
     */
    public void stopListen(){
        if (listenThread == null)
            return;
        if (listenThread.isRunning){
            listenThread.isRunning = false;
            listenThread.interrupt();
            listenThread = null;
        }else {
            listenThread.interrupt();
            listenThread = null;
        }
    }

    //客户端连接线程
    private class ListenThread extends Thread {
        boolean isRunning = false;
        int connectCount = 0;

        @Override
        public void run() {
            while (isRunning){
                try {
                    socket = server.accept();
                    System.out.println("============= SocketServer : connect success =============");
                    executor.execute(new TransferService(socket, mListener));
                    connectCount ++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
