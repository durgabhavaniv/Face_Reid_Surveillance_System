var video = document.getElementById('video');
var source = document.createElement('source');
var videoList = document.querySelector('#videoList');
var template = '<div class="card"> <div class="card-body"> <div class="container"> <div class="row"> <div class="col-6"> <h5 class="card-title">~name~</h5> </div> <div class="col-3"> <button type="button" class="btn btn-dark btn-sm border" onclick="videoUpdate(\'~uri~\',\'~fileType~\')"> <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-play-fill" viewBox="0 0 16 16" > <path d="m11.596 8.697-6.363 3.692c-.54.313-1.233-.066-1.233-.697V4.308c0-.63.692-1.01 1.233-.696l6.363 3.692a.802.802 0 0 1 0 1.393z"/> </svg> </button> </div> <div class="col-3"> <input class="form-check-input border" type="checkbox" value="~videoName~" id="~name~"> <label class="form-check-label" for="~name~"> select </label> </div> </div> </div> </div></div>'

function videoUpdate(src,type){
video.pause();
source.setAttribute('src', src);
source.setAttribute('type', type);

video.appendChild(source);
video.load();
video.play();
console.log({
  src: source.getAttribute('src'),
  type: source.getAttribute('type'),
});
}

function addVideoCards(){
var xhr1 = new XMLHttpRequest();
    xhr1.open("GET", "/camData");
    xhr1.onload = function() {
        console.log(xhr1.responseText);
        var response = JSON.parse(xhr1.responseText);
        console.log(response);
        videoList.innerHTML = "";
        response.forEach(function(data) {
//            console.log(data);
           var temp = template.replace(/~name~/g, data.name);
           temp = temp.replace(/~videoName~/g, data.videoName).replace(/~uri~/g, data.uri).replace(/~fileType~/g, data.fileType);
           videoList.insertAdjacentHTML("beforeend", temp);
       });
    }
   xhr1.send();
}

function videoPlay(){
    video.play();
}

function videoPause(){
    video.pause();
}