<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="stag" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
  <title>Honey Go</title>
</head>
<body onload="pageLoadHandler()">
  <!-- navbar -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">
        <img src="/resources/images/logo.jpg" alt="" width="30" height="24" class="d-inline-block align-text-top">
        Honey Go
      </a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
        <div class="navbar-nav">
          <a class="nav-link active" aria-current="page" href="#">Home</a>
        </div>
      </div>
    </div>
  </nav>
  <div class="container card" style="background: rgba(0, 0, 0, 0.5);">
    <div class="row">
      <div class="col">
        <p class="text-center font-monospace fw-bolder fs-4" style="padding: 5rem 1rem 1rem 1rem; color: white;">Face-reIdentification Surveillance System</p>
        <p class="text-center font-monospace" style="padding: 1rem ; color: white;">Add your camera videos one by one with its respective connections</p>
      </div>
    </div>
  </div>
  <div class="container">
    <div class="row">
      <div class="col-5 card" style="background: rgba(0, 0, 0, 0.5);">
        <form id="createCamForm" name="createCamForm">
          <div style="margin: 1rem auto;">
            <div class="row card" style="margin: 1rem 1rem 3rem 1rem; background: rgba(256, 256, 256, 0.7); padding: 2rem;">
              <div class="col-md">
                <label class="form-label">Camera Location</label>
                <input type="text" class="form-control" id="camLocation" name="camLocation">
              </div>
              <div class="col-md">
                <label class="form-label">Date-Time</label>
                <input type="text" class="form-control" id="datetime" placeholder="yyyy-MM-dd HH:mm:ss" name="datetime">
              </div>
              <div class="col-md">
                <label class="form-label">Upload Video</label>
                <input type="file" class="form-control" id="video" name="video">
              </div>
              <div class="col-md">
                <label class="form-label">Choose Connections/Path</label>
                <div class="card" id="connections" style="padding: 1rem;"></div>
              </div>
              <div class="d-md-block">
                <button type="submit" class="btn btn-outline-primary" style="margin: 1rem;">Add</button>
                <div id="createError"></div>
                <div id="createSuccess"></div>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="col card" style="background: rgba(0, 0, 0, 0.5);" >
        <div style="margin: 1rem;">
        <div id="loading" style="z-index: 99; margin-left: auto; margin-right: auto; width: 25%; position: absolute"></div>
        <svg width="700" height="560" style="background: rgb(256, 256, 256);"></svg>
      </div>
    </div>
  </div>
</div>
<div class="container">
  <div class="row" style="background: rgba(0, 0, 0, 0.5); padding: 1rem; align-items: center;">
    <div class="col-4">
       <button id="nextPage" type="button" class="btn btn-secondary" style="padding: 1rem 5rem 1rem 5rem;" onclick="deleteAllHandler()">Delete All</button>
    </div>
    <div class="col-4" >

    </div>
    <div class="col-4">
    <form action="<%= request.getContextPath()%>/video">
            <button id="nextPage" type="submit" class="btn btn-secondary" style="padding: 1rem 5rem 1rem 5rem;">Next</button>
          </form>
    </div>
  </div>
</div>
<!-- Optional JavaScript; choose one of the two! -->
<!-- Option 1: Bootstrap Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
<!--<script src="/resources/css/index.css"></script>-->
<!-- Option 2: Separate Popper and Bootstrap JS -->
<!--
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<style>
body  {
background-image: url('/resources/images/bg.jpg');
background-size: 100%;
background-repeat: no-repeat;
background-color: #cccccc;
}
</style>
<script src="/resources/js/d3.v4.min.js" ></script>
<script src="/resources/js/home.js" ></script>
<script src="/resources/js/graph.js" ></script>
</body>
</html>