var searchForm = document.querySelector('#searchForm');
var searchName = document.querySelector('#searchName');
var personSearch = document.querySelector('#personSearch');
var camSearch = document.querySelector('#camSearch');
var tableHeader = document.querySelector('#tableHeader');
var tableBody = document.querySelector('#tableBody');

personSearch
var loading = document.getElementById("loading");


function showLoading(){
        loading.innerHTML = "<img id=\"loading-image\" src=\"/resources/images/loading.gif\" alt=\"Loading...\" />";
        loading.style.display = "block";
}

function hideLoading(){
        loading.innerHTML = "";
        loading.style.display = "none";
}

function processVideos(){
    showLoading();
    var xhr1 = new XMLHttpRequest();
        xhr1.open("GET", "/processVideos");
        xhr1.onload = function() {
            hideLoading()
            loading.innerHTML = "<p class=\"text-center fs-4 fw-bolder\">"+xhr1.responseText+"</p>";
            loading.style.display = "block";
        }
       xhr1.send();
}

searchForm.addEventListener('submit', function(event){

    event.preventDefault();
    if(personSearch.checked){
        searchByNameHandler( searchName.value, '/findPerson/', 'CamLocation');
    }
    if(camSearch.checked){
        searchByNameHandler( searchName.value, '/findCam/','person');
    }

}, true);

function searchByNameHandler(name,uri,heading){
    var xhr = new XMLHttpRequest();
            xhr.open("GET", uri+name);
            xhr.onload = function() {
            json = JSON.parse(xhr.responseText);
            generateGraph(json.json);
            updateTableHeader(heading);
            updateTableBody(json.personList,heading);
            }
           xhr.send();
}

function updateTableHeader(heading){
    tableHeader.innerHTML="";
    tableHeader.innerHTML='<tr> <th scope="col">'+heading+'</th> <th scope="col">Start Datetime</th> <th scope="col">End Datetime</th></tr>';
}

function updateTableBody(jsonData,heading){
    tableBody.innerHTML="";
    var rows = '';
    jsonData.forEach(function(data) {
    if(heading === 'CamLocation'){
    rows = rows+ '<tr>  <td>'+data.cam+'</td> <td>'+data.startTime+'</td> <td>'+data.endTime+'</td> </tr>'
    }else{
    rows = rows+ '<tr>  <td>'+data.name+'</td> <td>'+data.startTime+'</td> <td>'+data.endTime+'</td> </tr>'
    }
    });
    tableBody.innerHTML=rows;
}

