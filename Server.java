import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading=new JLabel("server Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

   

    public Server(){
        try{
        
        server=new ServerSocket(7779);//to identify port;
         System.out.println("server is ready to make connection");
          System.out.println("waiting for connection .....");
          socket=server.accept();
          br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
          out=new PrintWriter(socket.getOutputStream());
           createGUI();
          handleEvents();
          startReading();
         // startWriting();


        }catch(Exception e){
            e.printStackTrace();
        }

    }
     public void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode()==10){
                    String cts=messageInput.getText();
                    messageArea.append("Me :"+cts+"\n");
                    out.println(cts);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();


                }

            }
            public void keyPressed(KeyEvent e){
                
            }
            public void keyTyped(KeyEvent e){
                
            }



        });
    }
     public void createGUI(){
        this.setTitle("server Messenge");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageArea.setEditable(false);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jcp=new JScrollPane(messageArea);
        this.add(jcp,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


         this.setVisible(true);




    }
   
    public void startReading(){
        Runnable r1=()->{
            System.out.println("reader started");
            try{
            while(true){
               
                String msg=br.readLine();
                if(msg.equals("lets talk later")){
                    System.out.println("chat over by client");
                     JOptionPane.showMessageDialog(this,"chat over by client");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
            
            messageArea.append("Client:"+ msg+"\n");
            }
            
            }catch(Exception e){
                System.out.println("connection over");
               
                }
        };
        new Thread(r1).start();

    }
     public void startWriting(){
        Runnable r2=()->{
             System.out.println("writer started");
             try{
            while(!socket.isClosed()){
                
                 BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                 String content=br1.readLine();
                 out.println(content);
                 out.flush();
                 if(content.equals("lets talk later")){
                    socket.close();
                    break;
                 }

                }
            }catch(Exception e){
                System.out.println("connection over");
                }
            };
            new Thread(r2).start();


        }
        
    
    public static void main(String a[]){
        System.out.println("server is going to start .......");
        new Server();
    }
}