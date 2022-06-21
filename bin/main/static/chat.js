var stompClient = null;

$(document).ready(function() {

    $('#connectButton').click(function() {
        connect($('#username').val());
        displayMessage("Boobe swag", "you fat nigger", "10:21:00");
    });

    $('#sendGlobalMessage').click(function() {
        sendGlobalMessage();
        $("#globalMessage").val("");
    });

    $('#sendPrivateMessage').click(function() {
        sendPrivateMessage();
        $("#privateMessage").val("");
        $("#messageTo").val("");
    });
});

function connect(username) {
    var socket =  new SockJS('/connect-to-cool-chat');
    var stompClient = Stomp.over(socket);

    stompClient.connect({username1: username}, function(frame) {
        stompClient.subscribe('/topic/global-messages', function(message) {
            messageBody = JSON.parse(message.body);
            displayMessage(messageBody.from, messageBody.content, messageBody.time);
        });
        stompClient.subscribe('/user/topic/private-messages', function(message) {
            messageBody = JSON.parse(message.body);
            displayMessage(messageBody.from, messageBody.content, messageBody.time);
        });
    });
}

function displayMessage(from, content, time) {
    var message = document.createElement("div");
    var msgHeader = document.createElement("div");
    var msgBody = document.createElement("div");
    var msgTime = document.createElement("div");
    var msgFrom = document.createElement("div");

    message.className = "message is-dark is-flex mt-1";
    msgHeader.className = "message-header is-flex is-flex-direction-column";
    msgBody.className = "message-body";
    msgTime.className = "time";
    msgFrom.className = "from";

    msgBody.innerHTML = content;
    msgTime.innerHTML = time;
    msgFrom.innerHTML = from;
    
    msgHeader.append(msgTime);
    msgHeader.append(msgFrom);

    message.append(msgHeader);
    message.append(msgBody);

    $('#chat-history').append(message);    
}