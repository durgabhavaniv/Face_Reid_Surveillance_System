'use strict';
var createCamForm = document.querySelector('#createCamForm');
var camLocation = document.querySelector('#camLocation');
var datetime = document.querySelector('#datetime');
var video = document.querySelector('#video');
var createError = document.querySelector('#createError');
var connections = document.querySelector('#connections');
var elms = connections.getElementsByClassName("connection");
var template ='<div class="form-check connection"><input class="form-check-input" type="checkbox" value="~id~" ><label class="form-check-label" >~id~</label></div>';
var loading = document.getElementById("loading");


function showLoading(){
        loading.innerHTML = "<img id=\"loading-image\" src=\"/resources/images/loading.gif\" alt=\"Loading...\" />";
        loading.style.display = "block";
}

function hideLoading(){
        loading.innerHTML = "";
        loading.style.display = "none";
}
createCamForm.addEventListener('submit', function(event){
    var files = video.files;
    if(files.length === 0) {
        createError.innerHTML = "Please select a file";
        createError.style.display = "block";
    }
    createCamHandler(files[0], camLocation.value, datetime.value);
    event.preventDefault();

}, true);

function getConnections(){
    var connect = [];
    for (var i=0; i<elms.length; i++) {
        var input = elms[i].querySelector('input[type="checkbox"]');
        if(input.checked){
        connect.push(input.value);
        }
    }
    return connect;
}

function addConnection(){
    showLoading();
    var xhr1 = new XMLHttpRequest();
    xhr1.open("GET", "/cams");
    xhr1.onload = function() {
        hideLoading()
        console.log(xhr1.responseText);
        var data = xhr1.responseText.split(",");;
        connections.innerHTML = "";
        data.forEach(function(item) {
           connections.insertAdjacentHTML("beforeend", template.replace(/~id~/g, item));
       });
    }
   xhr1.send();
}


function createCamHandler(file, camLocationValue, datetimeValue){
    var formData = new FormData();
    var newConnections = getConnections()
    formData.append("video", file);
    formData.append("camLocation", camLocationValue);
    formData.append("datetime", datetimeValue);
    formData.append("connections", newConnections);
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/create");
    xhr.onload = function() {
            console.log(xhr.responseText);
            addConnection();
            getGraphJson()
    }
    xhr.send(formData);

}

function getGraphJson()
{
    var json;
    var xhr = new XMLHttpRequest();
            xhr.open("GET", "/camsGraph");
            xhr.onload = function() {
                json = JSON.parse(xhr.responseText);
                generateGraph(json);
            }
            xhr.send();
}

function pageLoadHandler(){
    addConnection();
    getGraphJson()
}

function deleteAllHandler(){
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/deleteAll");
    xhr.onload = function() {
        pageLoadHandler();
    }
    xhr.send();
}