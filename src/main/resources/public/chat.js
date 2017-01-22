/**
 * Created by Admin on 2017-01-22.
 */
var socket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/main/");
var channel = 0;
var username = "unknown";

socket.onopen = function () {
    var name = prompt("Please enter your name", "my name");
    if (name !== null) {
        username = name;
    }
    document.cookie = "username=" + username;
    initializeConnection();
};

socket.onmessage = function (msg) {
    var data = JSON.parse(msg.data);

    if (data.action == "listChannels") {
        updateChannels(data);
    }

    if (data.action == "join") {
        addUserToChannel(data)
    }

    if (data.action == "leave") {
        removeUserFromChannel(data)
    }

    if (data.action == "sendMessage") {
        id("chat").insertAdjacentHTML("afterbegin", data.userMessage);
    }
};

// EVENT LISTENERS
id("leaveChannel").addEventListener("click", function () {
    leaveChannel();
});

id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
    id("message").value = "";
});

id("addChannel").addEventListener("click", function () {
    addChannel(id("newChannelName").value)
    id("newChannelName").value = "";
})

// HELPER FUNCTIONS
function addChannel(channelName) {
    if (channelName != "") {
        var obj = new Object();
        obj.action = "newChannel";
        obj.channelName = channelName;
        socket.send(JSON.stringify(obj));
    }
}

function addUserToChannel(data) {
    console.log(data.userList);
    id("channelName").innerHTML = "Channel: " + data.channelName;
    id("chat").insertAdjacentHTML("afterbegin", "<article>" + data.username + " joined the channel." + "</article>");
    id("userList").innerHTML = "";
    data.userList.forEach(function (user) {
        id("userList").insertAdjacentHTML("afterbegin", "<li>" + user + "</li>");
    });
}

function removeUserFromChannel(data) {
    id("chat").insertAdjacentHTML("afterbegin", "<article>" + data.username + " has left the channel." + "</article>");
    id("userList").innerHTML = "";
    data.userList.forEach(function (user) {
        id("userList").insertAdjacentHTML("afterbegin", "<li>" + user + "</li>");
    });
}

function getUserName() {
    return username;
    // var name = "username=";
    // var cookie = document.cookie;
    // return cookie.substring(name.length, cookie.length);
}

function id(id) {
    return document.getElementById(id);
}

function initializeConnection() {
    id("channelsContainer").setAttribute("disabled", "false");
    id("channelsContainer").style.visibility = "visible";
    id("chatContainer").setAttribute("disabled", "true");
    id("chatContainer").style.visibility = "hidden";

    var obj = new Object();
    obj.action = "initializeUser";
    obj.username = username;
    socket.send(JSON.stringify(obj));
}

function joinChannel(channelID) {
    id("channelsContainer").setAttribute("disabled", "true");
    id("channelsContainer").style.visibility = "hidden";
    id("chatContainer").setAttribute("disabled", "false");
    id("chatContainer").style.visibility = "visible";
    id("chat").innerHTML = "";

    channel = channelID;
    var obj = new Object();
    obj.action = "join";
    obj.username = getUserName();
    obj.channelID = channelID;
    console.log(channelID);
    socket.send(JSON.stringify(obj));
}

function leaveChannel() {
    id("channelsContainer").setAttribute("disabled", "false");
    id("channelsContainer").style.visibility = "visible";
    id("chatContainer").setAttribute("disabled", "true");
    id("chatContainer").style.visibility = "hidden";

    var obj = new Object();
    obj.action = "leave";
    obj.oldChannelID = channel;
    obj.username = username;
    socket.send(JSON.stringify(obj));
}

function sendMessage(message) {
    if (message !== "") {
        var obj = new Object();
        obj.action = "sendMessage";
        obj.channelID = channel;
        obj.username = username;
        obj.userMessage = message;
        socket.send(JSON.stringify(obj))
    }
}

function updateChannels(data) {
    id("channelList").innerHTML = "";
    for (var i = 0; i < data.numberOfChannels; i++) {
        id("channelList").insertAdjacentHTML("afterbegin", "<button id='channel" + i + "'>" + data.channelNames[i] + "</button>");
    }

    var channels = id("channelList").getElementsByTagName("button");
    for (i = 0; i < channels.length; i++) {
        channels[i].addEventListener("click", function () {
            var buttonID = this.id.slice(7);
            joinChannel(buttonID);
        })
    }
}

