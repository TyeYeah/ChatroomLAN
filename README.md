# ChatroomLAN
## Why This Garbage
At first, it's the product of my OOP practice course, and the interface you can see now was according to teachers' demmands.

Now I take it out because It is the only project in these years I've made since I learned to code, which is not a completely rubbish.

Besides, it's like a little toy that me and my friends can have fun with.

But im not gonna be a professional JAVA developer, so 'Thread' or other advanced tech hasn't been properly used.
## Project Structure
```bash
|
|-server-ChatroomServer.java -- cmd mode server
|       -ServerWindow.java -- gui mode server
|       -GetHostIP.java -- util to get LAN IP 
|
|-client-ChatroomWindow.java -- gui main window
        -EnterWindow.java -- official enter window
        -SwingUtils.java -- used to apply send when press 'enter'
```
## Usage
+ jdk1.8+
- use the given jar file
```bash
# start server first
java -jar servercmd.jar 
# servergui.jar provide gui mode
# Server's IP address will show in cmd(or gui)
# Click 'Start' to start server,and click 'Stop' to stop server and exit directly

#then start the client
java -jar client.jar
# input server's IP(server should be on) and your favourate nickname,then you can enter chatroom

```
- compile and run by yourself

## Features
When you're in the main chatroom, you can see messages on the left and user list on the right

You can just type one-line message, because when you type 'Enter' the message will be sent

Users can speak to all or speak to someone only, but all of them will be seen by others.

If You want to speak to someone privately, remember to use whisper mode
