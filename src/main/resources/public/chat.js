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

    if (data.action = "listChannels") {
        updateChannels(data);
    }
};

// HELPER FUNCTIONS

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

}

function updateChannels(data) {
    id("channelList").innerHTML = "";
    for (var i = 0; i < data.numberOfChannels; i++) {
        id("channelList").insertAdjacentHTML("afterbegin",
        "<button id='channel'" + i + ">" + data.channelNames[i] + "</button>");
    }

    var channels = id("channelList").getElementsByTagName("button");
    for (i = 0; i < channels.length; i++) {
        channels[i].addEventListener("click", function () {
            var buttonID = this.id.slice(7);
            joinChannel(buttonID);
        })
    }
}