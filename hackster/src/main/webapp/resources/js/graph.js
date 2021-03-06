
function generateGraph(graph){
d3.selectAll("svg > *").remove();
var svg = d3.select("svg"),
    width = +svg.attr("width"),
    height = +svg.attr("height");

var color = d3.scaleOrdinal(d3.schemeCategory20);

var simulation = d3.forceSimulation()
    .force("link", d3.forceLink().id(function(d) { return d.id; }).distance(100))
    .force("charge", d3.forceManyBody())
    .force("center", d3.forceCenter(width / 2, height / 2));

  var link = svg.append("g")
      .attr("class", "links")
    .selectAll("line")
    .data(graph.links)
    .enter().append("line")
        .attr('stroke','gray')
      .attr("stroke-width", function(d) { return Math.sqrt(d.value); });

var node = svg.selectAll(".node")
            .data(graph.nodes)
            .enter().append("g")
						.attr("class", "node")
             .call(d3.drag()
      .on("start", dragstarted)
      .on("drag", dragged)
      .on("end", dragended));

node.append("circle")
  .attr("r", 10)
  .attr("fill", function(d) { return color(d.group); });

 node.append("text")
 	.attr("dx", 6)
    .text(function(d) { return d.id; });

//var node = svg.append("g")
//      .attr("class", "nodes")
//    .selectAll("circle")
//    .data(graph.nodes)
//    .enter().append("circle")
//      .attr("r", 5)
//      .attr("fill", function(d) { return color(d.group); })
//      .call(d3.drag()
//          .on("start", dragstarted)
//          .on("drag", dragged)
//          .on("end", dragended));

  simulation
      .nodes(graph.nodes)
      .on("tick", ticked);

  simulation.force("link")
      .links(graph.links);



  function ticked() {
    link
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", lineTargetY);
//    node
//        .attr("cx", function(d) { return d.x; })
//        .attr("cy", nodeTargetY);
    node.attr("transform", function(d) {
                    return "translate(" + d.x + "," + d.y + ")";
                });


  }



function dragstarted(d) {
  if (!d3.event.active) simulation.alphaTarget(0.3).restart();
  d.fx = d.x;
  d.fy = d.y;
}

function dragged(d) {
  d.fx = d3.event.x;
  d.fy = d3.event.y;
}

function dragended(d) {
  if (!d3.event.active) simulation.alphaTarget(0);
  d.fx = null;
  d.fy = null;
}

function lineTargetY(d){
    return d.target.y;
}

function nodeTargetY(d){
    return d.y;
}
}
