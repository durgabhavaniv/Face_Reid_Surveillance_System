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
<body onload="addVideoCards()">
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
        <p class="text-center font-monospace" style="padding: 1rem ; color: white;">Check your video inputs for furture processing</p>
      </div>
    </div>
  </div>
  <div class="container">
    <div class="row">
      <div class="col-5 card " id="videoList" style="background: rgba(0, 0, 0, 0.5); overflow-y: scroll; height:600px;">
        <!-- video tabs-->
      </div>
      <div class="col card" style="background: rgba(0, 0, 0, 0.5);" >
        <div style="margin: 1rem;">
          <!-- video -->
        <video id="video" width="700" height="540"></video>
        <button type="button" class="btn btn-outline-danger" onclick="videoPlay()">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-play-fill" viewBox="0 0 16 16">
        <path d="m11.596 8.697-6.363 3.692c-.54.313-1.233-.066-1.233-.697V4.308c0-.63.692-1.01 1.233-.696l6.363 3.692a.802.802 0 0 1 0 1.393z"></path>
      </svg>
      Play
      </button>
      <button type="button" class="btn btn-outline-danger" onclick="videoPause()">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pause-fill" viewBox="0 0 16 16">
      <path d="M5.5 3.5A1.5 1.5 0 0 1 7 5v6a1.5 1.5 0 0 1-3 0V5a1.5 1.5 0 0 1 1.5-1.5zm5 0A1.5 1.5 0 0 1 12 5v6a1.5 1.5 0 0 1-3 0V5a1.5 1.5 0 0 1 1.5-1.5z"></path>
    </svg>
    Pause
    </button>
  </div>
</div>
</div>
</div>
<div class="container">
<div class="row" style="background: rgba(0, 0, 0, 0.5); padding: 1rem; align-items: center;">
<div class="col" style="transform: translate(25%);">
  <form action="<%= request.getContextPath()%>/">
    <button type="submit" class="btn btn-secondary" style="padding: 1rem 5rem 1rem 5rem;">Back</button>
  </form>
</div>
<div class="col">
</div>
<div class="col">
</div>
<div class="col" style="transform: translate(25%);">
  <form action="<%= request.getContextPath()%>/final">
    <button type="submit" class="btn btn-secondary" style="padding: 1rem 5rem 1rem 5rem;">Process</button>
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
<script src="/resources/js/video.js" ></script>
</body>
</html>