package messageQueue;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.junit.Test;

import java.net.URISyntaxException;

public class SocketTest {


//    const socketIo = io(server);
//
//// Allow CORS
//app.use(cors());
//
//// Render a API index page
//app.get('/', (req, res) => {
//        res.sendFile(path.resolve('public/index.html'));
//    });
//
//// Start listening
//server.listen(process.env.PORT || config.port);
//console.log(`Started on port ${config.port}`);
//
//// Setup socket.io
//socketIo.on('connection', socket => {
//  const username = socket.handshake.query.username;
//        console.log(`${username} connected`);
//
//        socket.on('client:message', data => {
//                console.log(`${data.username}: ${data.message}`);
//
//        // message received from client, now broadcast it to everyone else
//        socket.broadcast.emit('server:message', data);
//  });
//
//        socket.on('disconnect', () => {
//                console.log(`${username} disconnected`);
//  });
//    });

    @Test
    public void test1() throws URISyntaxException {
        Socket socketIo = IO.socket("http://localhost");
        socketIo.connect();












    }







}
