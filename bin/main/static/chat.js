var stompClient = null;

$(document).ready(function() {

    $('#connectButton').click(function() {
        connect($('#username').val());
    });

    $('#sendGlobalMessage').click(function() {
        sendGlobalMessage();
        $('#globalMessage').val('');
    });

    $('#sendPrivateMessage').click(function() {
        sendPrivateMessage();
        $('#privateMessage').val('');
        $('#messageTo').val('');
    })
});

function connect(username) {
    var socket =  new SockJS('/connect-to-cool-chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({username: username,}, function() {
        stompClient.subscribe('/topic/global-messages', function(message) {
            messageBody = JSON.parse(message.body);
            displayMessage(messageBody.from, messageBody.content, messageBody.time, false);
        });
        stompClient.subscribe('/user/queue/private-messages', function(message) {
            messageBody = JSON.parse(message.body);
            displayMessage(messageBody.from, messageBody.content, messageBody.time, true);
        });
    });

    $('#username').prop('disabled', true)
    $('#connectButton').prop('disabled', true)

    $('#globalMessage').prop('disabled', false)
    $('#sendGlobalMessage').prop('disabled', false)
    $('#privateMessage').prop('disabled', false)
    $('#messageTo').prop('disabled', false)
    $('#sendPrivateMessage').prop('disabled', false)
}

function displayMessage(from, content, time, isPrivate) {
    var message = document.createElement('div');
    var msgHeader = document.createElement('div');
    var msgBody = document.createElement('div');
    var msgTime = document.createElement('div');
    var msgFrom = document.createElement('div');

    message.className = 'message is-dark is-flex mt-1';
    if (isPrivate) {
        msgHeader.className = 'message-header has-background-primary is-flex is-flex-direction-column';
    } else {
        msgHeader.className = 'message-header is-flex is-flex-direction-column';
    }
    msgBody.className = 'message-body';
    msgTime.className = 'time';
    msgFrom.className = 'from';

    msgBody.innerHTML = content;
    msgTime.innerHTML = time;
    msgFrom.innerHTML = from;
    
    msgHeader.append(msgTime);
    msgHeader.append(msgFrom);

    message.append(msgHeader);
    message.append(msgBody);

    $('#chat-history').append(message);    
}

function sendGlobalMessage() {
    message = JSON.stringify({'content': $('#globalMessage').val()});
    stompClient.send('/chat/global-message', {}, message);
}

function sendPrivateMessage() {
    message = JSON.stringify({'content': $('#privateMessage').val(), 'to': $('#messageTo').val()});
    stompClient.send('/chat/private-message', {}, message);
}