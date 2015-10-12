<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>MeliDatos</title>
        <link type="text/css" rel="stylesheet" href="resources/assets/css/bootstrap.min.css">
        <link type="text/css" rel="stylesheet" href="resources/assets/css/bootstrap-responsive.min.css">
        <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" rel="stylesheet">
        
        <link type="text/css" rel="stylesheet" href="resources/assets/css/style.css">
        <link href="resources/css/bootstrap-select.css" rel="stylesheet" />
       	<link rel="stylesheet" href="resources/fancybox/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />	
        <link rel="stylesheet" href="resources/assets/css/theme.css">
        <script src="resources/js/jquery.js"></script>
		<script src="resources/jquery-ui.min.js"></script>
		
		<script src="resources/js/bootstrap-select.js" type="text/javascript"></script>
		
		
		<script type = "text/javascript">
			
		$(document).ready(function() {
			  $('#nuevaBusqueda').on('submit', function(e){		
				 
				  $.ajax({
			            type: 	  'GET', 
			            url: 	  'InformationRequest', 
			            data: 	  "project=" + document.getElementById('project').value + "&Pais=" + $('.selectpicker').selectpicker().val() + "&project-id=" +document.getElementById('project-id').value +"&id=" + 1, 
			            dataType: 'html',
			            encode:    true
				  }).done(function(data) {			                           
			       console.log(data); 	
			       			       		       
			       $('#iFrame').contents().find("*").html(data);
			       //$("#nuevoPais").append("<iframe srcdoc= "+data+" width=\"100%\" height=\"1000\"></iframe>");
			                        });	
				  
			      e.preventDefault();			    
			      
			});
			  
		});
		</script> 
		
		<script>
			$('.selectpicker').selectpicker({
	      	style: 'btn-info',
	      	size: 4
	  		});
		</script>
		
		
		
		<script type="text/javascript">
			function borrarInputs(){
				$("#project").val("");
				
			}
		</script>
		
	
		<script type="text/javascript">
		$(function() {
	
			  $('.selectpicker').on('change', function(){
				 
				  $("#project").val("");
				  $("#project-id").val("");
			  });
			  
			});
		</script>
		
       	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		
		<script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
		       	
		    	//este no tiene drama 
		    	var data = google.visualization.arrayToDataTable([<%= request.getAttribute("avgStates") %>]);
		        var options = {
		        		title: 'Precio promedio por provincia',
		                hAxis: {title: 'Provincia', titleTextStyle: {color: 'red'}},
		                vAxis: {title: 'Promedio ($)', titleTextStyle: {color: 'red'}},
		                colors:['#0d2b26'],
		        };

		        var chart = new google.visualization.ColumnChart(document.getElementById('promedioProvincias'));
		        chart.draw(data, options);
		      }
    	</script>
		
		<script type="text/javascript">
				google.load('visualization', '1', { 'packages': ['geochart'] });
			    google.setOnLoadCallback(drawMap);
			    
			    function drawMap() {
			    	
			    	//PARA DIBUJAR MAPA - este tiene drama trae todos los estados
			    	
			    	
			        var data = google.visualization.arrayToDataTable([<%= request.getAttribute("statesOffers") %>]);
			        
			        var options = {width: 556, height: 347,
			        		colorAxis: {colors: ['#00853f', 'black', '#e31b23']},
			                backgroundColor: '#81d4fa',
			                datalessRegionColor: '#f8bbd0',
			                defaultColor: '#f5f5f5'};
			        options['region'] = "<%= request.getAttribute("idMapa") %>";
			        options['resolution'] = 'provinces';
			        
			      
		
			        var container = document.getElementById('map_canvas');
			        var geochart = new google.visualization.GeoChart(container);
			        geochart.draw(data, options);
			    };
	    </script>		
		
		
    	<script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
		    	  
		    	//ESTE NO TIENE DRAMA
		        var data = google.visualization.arrayToDataTable([<%= request.getAttribute("historyPrices") %>]);
				
		        var options = {
		        		title: 'Historial de precios',
		                hAxis: {title: 'Fecha', titleTextStyle: {color: 'red'}},
		                vAxis: {title: 'Precio ($)', titleTextStyle: {color: 'red'}}
		        };

		        var chart = new google.visualization.ColumnChart(document.getElementById('histogramaPrecio'));
		        chart.draw(data, options);
		      }
    	</script>
    	<script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
		    	  
		    	//ESTE TIENE DRAMA  
		        var data = google.visualization.arrayToDataTable([<%= request.getAttribute("statesOffers") %>]);
		        
		        var options = {
		        		title: 'Oferta por provincia',
		        		is3D: true,
		        };

		        var chart = new google.visualization.PieChart(document.getElementById('ofertadosProvincia'));
		        chart.draw(data, options);
		      }
    	</script>
    	<script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
		        var data = google.visualization.arrayToDataTable([<%= request.getAttribute("historyOffers") %>]);
		        
		        var options = {
		        		title: 'Historial de oferta',
		                hAxis: {title: 'Fecha', titleTextStyle: {color: 'red'}},
		                vAxis: {title: 'Oferta (Un)', titleTextStyle: {color: 'red'}},
		                colors:['#a52a2a'],
		        };

		        var chart = new google.visualization.ColumnChart(document.getElementById('histogramaOferta'));
		        chart.draw(data, options);
		      }
    	</script>
    	<script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
		        var data = google.visualization.arrayToDataTable([<%= request.getAttribute("stopQuantitys") %>]);
		        
		        var options = {
		        		 title: 'Cantidad de bajas en los dias proximos',
		                 hAxis: {title: 'Fecha', titleTextStyle: {color: 'red'}},
		                 vAxis: {title: 'Bajas (Un)', titleTextStyle: {color: 'red'}},
		                 colors:['#597780'],
		        };

		        var chart = new google.visualization.ColumnChart(document.getElementById('bajasPorDias'));
		        chart.draw(data, options);
		      }
    	</script>
    	<script type='text/javascript'>
		      google.load('visualization', '1', {packages:['table']});
		      google.setOnLoadCallback(drawTable);
		      function drawTable() {
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', 'Vandedores');
		        data.addColumn('number', 'Cantidad');
		        data.addRows([<%= request.getAttribute("aceptsMercadoPago") %>]);
		        
		        var table = new google.visualization.Table(document.getElementById('mercadoPago'));
		        table.draw(data, {showRowNumber: false});
		      }
    	</script>
    	<script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
		        var data = google.visualization.arrayToDataTable([<%= request.getAttribute("historySolds") %>]);
		        
		        var options = {
		        		title: 'Historial de ventas',
		                hAxis: {title: 'Fecha', titleTextStyle: {color: 'red'}},
		                vAxis: {title: 'Vendidos (Un)', titleTextStyle: {color: 'red'}},
		                colors:['#778059'],
		        };

		        var chart = new google.visualization.ColumnChart(document.getElementById('histogramaVendidos'));
		        chart.draw(data, options);
		      }
    	</script>
    	<script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
		        var data = google.visualization.arrayToDataTable([<%= request.getAttribute("statesSold") %>]);
		        
		        var options = {
		        		title: 'Vendidos por provincia',
		        		is3D: true,
		        };

		        var chart = new google.visualization.PieChart(document.getElementById('vendidosProvincia'));
		        chart.draw(data, options);
		      }
   		</script>

	    <script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
		        var data = google.visualization.arrayToDataTable([<%= request.getAttribute("offerSolds") %>]);
		        
		        var options = {
		          title: 'Comparativa entre la cantidad de ofertados y comprados',
		          pointSize: 5
		        };
		
		        var chart = new google.visualization.LineChart(document.getElementById('ofertaDemanda'));
		        chart.draw(data, options);
		      }
	    </script>


		<script type="text/javascript">
			function changeToKitOne(){
				document.getElementById("span1").className="active";
				document.getElementById("span2").className="";
				document.getElementById("span3").className="";
				document.getElementById("ActualPricesData").style.display = "inline";
				document.getElementById("promedioProvincias").style.display = "inline";
				document.getElementById("histogramaPrecio").style.display = "inline";
				document.getElementById("ofertadosProvincia").style.display = "none";
				document.getElementById("histogramaOferta").style.display = "none";
				document.getElementById("bajasPorDias").style.display = "none";
				document.getElementById("mercadoPago").style.display = "none";
				document.getElementById("histogramaVendidos").style.display = "none";
				document.getElementById("vendidosProvincia").style.display = "none";
				document.getElementById("ofertaDemanda").style.display = "none";
				document.getElementById("map_canvas").style.display = "inline";
				
			}
			function changeToKitTwo(){
				document.getElementById("span1").className="";
				document.getElementById("span2").className="active";
				document.getElementById("span3").className="";
				document.getElementById("ActualPricesData").style.display = "none";
				document.getElementById("promedioProvincias").style.display = "none";
				document.getElementById("histogramaPrecio").style.display = "none";
				document.getElementById("ofertadosProvincia").style.display = "inline";
				document.getElementById("histogramaOferta").style.display = "inline";
				document.getElementById("bajasPorDias").style.display = "inline";
				document.getElementById("mercadoPago").style.display = "inline";
				document.getElementById("histogramaVendidos").style.display = "none";
				document.getElementById("vendidosProvincia").style.display = "none";
				document.getElementById("ofertaDemanda").style.display = "none";
				document.getElementById("map_canvas").style.display = "none";
			}
			function changeToKitThree(){
				document.getElementById("span1").className="";
				document.getElementById("span2").className="";
				document.getElementById("span3").className="active";
				document.getElementById("ActualPricesData").style.display = "none";
				document.getElementById("promedioProvincias").style.display = "none";
				document.getElementById("histogramaPrecio").style.display = "none";
				document.getElementById("ofertadosProvincia").style.display = "none";
				document.getElementById("histogramaOferta").style.display = "none";
				document.getElementById("bajasPorDias").style.display = "none";
				document.getElementById("mercadoPago").style.display = "none";
				document.getElementById("histogramaVendidos").style.display = "inline";
				document.getElementById("vendidosProvincia").style.display = "inline";
				document.getElementById("ofertaDemanda").style.display = "inline";
				document.getElementById("map_canvas").style.display = "none";
				
			}
		</script>
		
		
		<script type="text/javascript">		
	
		$(document).ready(function() {
			 function split( val ) {
			      return val.split( /,\s*/ );
			    }
			    function extractLast( term ) {
			      return split( term ).pop();
			    }
                $(function() {
                        $("#project").autocomplete({                    
                        source : function(request, response) {
                        $.ajax({
                                url : "autosuggest",
                                type : "POST",
                                data : "word=" + document.getElementById('project').value + "&test=" +document.getElementById('project-id').value + "&pais=" + $('.selectpicker').selectpicker().val(),
                                dataType : "json",
                                success : function(data) {
                                    console.log (data[0].father);    
                                	response(data);                                		
                                						 }
                        	   });                        
                											},
               			 focus: function( event, ui ) {
                  		  //$( "#project" ).val( ui.item.father );
                  		  return false;
                 		 },
                        minLength: 2,
                        select: function( event, ui ) {
                        	var terms = split( this.value );
                        	console.log(this.value);
                            // remove the current input
                            terms.pop();
                            // add the selected item
                            terms.push( ui.item.father );
                            // add placeholder to get the comma-and-space at the end
                            terms.push( "" );
                            this.value = terms.join(", ");
                            
                            //$( "#project" ).val( ui.item.father);
                            $( "#project-id" ).val( ui.item.key);
                            
                            return false;
                          }                        
                        
        			}).autocomplete( "instance" )._renderItem = function( ul, item ) {
                            return $( "<li>" )                            
                            .append( "<a>" + item.father  + "</a>" )
                            .appendTo( ul );
                                };
        
        });
        });
		  </script>
		  
		 
            <!-- Socials Buttons -->
				<link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css">
				<style type="text/css">
				i.fb,       span.fb{     color: #3b5998; }
				i.tw,       span.tw{     color: #00aced; }
				i.google,   span.google{ color: #dd4b39; }
				i.linkin,   span.linkin{ color: #007bb6; }
				</style> 
		
		 <!-- Pretty photo -->


	</head>

    <body  onload="changeToKitOne()">
        <!-- BEGIN WRAP -->
        <div id="wrap">

            <!-- BEGIN TOP BAR -->
            <div id="top">
                <!-- .navbar -->
                <div class="navbar navbar-inverse navbar-static-top">
                    <div class="navbar-inner">
                        <div class="container-fluid">
                            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                            </a>
                            <a href="<%=request.getContextPath()%>"><img src="resources/img/MeliDatos-blanco.png" /></a>
                            <!-- .topnav -->
                            <div class="btn-toolbar topnav">
                                <div class="btn-group">
                                   <!-- Socials Buttons : http://ostr.io/code/html-social-like-share-buttons-no-javascript.html  -->
                                <a class="btn btn-default" href="http://www.facebook.com/sharer.php?u=http://www.melidatos.com.ar/" target="top"
                                onClick="window.open(this.href, this.target, 'width=550,height=415');"> 
                               	<i class="fa fa-thumbs-o-up fa-lg fb"></i></a>
							    <a class="btn btn-default" href="http://twitter.com/share?url=http://www.melidatos.com.ar/" target="top"
                                onClick="window.open(this.href, this.target, 'width=550,height=415');">
							   	<i class="fa fa-twitter fa-lg tw"></i></a>
							   	<a class="btn btn-default" href="https://plus.google.com/share?url=http://www.melidatos.com.ar/" target="top"
                                onClick="window.open(this.href, this.target, 'width=550,height=415');">
							   	<i class="fa fa-google-plus fa-lg google"></i></a>
                                <a class="btn btn-default" href="http://www.linkedin.com/shareArticle?url=http://www.melidatos.com.ar/" target="top"
                                onClick="window.open(this.href, this.target, 'width=550,height=415');">
                                <i class="fa fa-linkedin fa-lg linkin"></i></a>
                            </div>
                            <!-- /.topnav -->
                            <div class="nav-collapse collapse">
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.navbar -->
            </div>
            <!-- END TOP BAR -->


            <!-- BEGIN HEADER.head -->
           <header class="head">
                <div class="search-bar">
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="search-bar-inner">
                                <a id="menu-toggle" href="#menu" data-toggle="collapse"
                                   class="accordion-toggle btn btn-inverse visible-phone"
                                   rel="tooltip" data-placement="bottom" data-original-title="Show/Hide Menu">
                                    <i class="icon-sort"></i></a>
                                <form class="main-search" action="InformationRequest" method="get">                                    
                                  	<input class="input-block-level" type="text" id="project" name="project" class="col-md-4">
									<input class="input-block-level" type="text" id="project-id" name="project-id">
									<select id="comboPais" class="selectpicker" name="Pais">
			               					 <option value="MLA">Argentina</option>
			             				     <option value="MLB">Brasil</option>
			               					 <option value="MLC">Chile</option>
                		    		</select>		                                  
                                    <button id="searchBtn" type="submit" class="btn btn-inverse" onclick="changeToKitOne()"><i class="icon-search"></i></button>
                                    
                                </form>
                                
                                <form id="nuevaBusqueda" action="InformationRequest" method="get">                                  
                                	<input id="prueba" type="submit" value="Prueba">
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
                <!-- ."main-bar -->
                <div class="main-bar">
                    <div class="container-fluid">
                        <div class="row-fluid">
                            <div class="span12" align="center">
                                <h3 id="product">MeliDatos sobre: "<i><a class="iframe" target="top" href="mainpublications?var1=<%= request.getAttribute("project") %>" 
                                onClick="window.open(this.href, this.target, 'width=550,height=415');">
                                <%= request.getAttribute("project") %></a>" de <%= request.getAttribute("sampleSize") %> publicaciones</i></h3>
                            </div>
                        </div>
                        <!-- /.row-fluid -->
                    </div>
                    <!-- /.container-fluid -->
                </div>
                <!-- /.main-bar -->
            </header>
            <!-- END HEADER.head -->
            <!-- BEGIN LEFT  -->
            <div id="left">
                <!-- BEGIN MAIN NAVIGATION -->
                <ul id="menu" class="unstyled accordion collapse in">
                    <li onClick="changeToKitOne()" id="span1">
                    	<a href="#"><span>Precio</span></a>
                    </li>
                    <li onClick="changeToKitTwo()" id="span2">
                    	<a href="#"><span>Oferta</span></a>
                    </li>
					<li onClick="changeToKitThree()" id="span3">
					<a href="#"><span>Mercado</span></a>
					</li>
                </ul>
                <!-- END MAIN NAVIGATION -->

            </div>
            <!-- END LEFT -->

            <!-- BEGIN MAIN CONTENT -->
            <div id="content" align="center">
                <!-- .outer -->
                <div class="container-fluid outer">
                    <div class="row-fluid">
                        <!-- .inner -->
                        <div class="span12 inner">
                        
                            <!-- Comienzo para precios -->
                            <div id="ActualPricesData" align="center">
                                <ul class="stats_box">
                                <li>
                                    <!-- Precio Minimo -->
                                    <div>
                                      <h4>Precio Minimo</h4>
                                       <h5 class="text-error"><%= request.getAttribute("min") %></h5>
                                    </div>    
                                    </li>
                                <li>
                                    <!-- Precio Promedio -->
                                    <div>
                                      <h4>Precio Promedio</h4>
                                       <h5 class="text-warning"><%= request.getAttribute("avg") %></h5>
                                    </div>    
                                    </li>
                                    
                                    <li>
                                    <!-- Precio Maximo -->
                                    <div>
                                      <h4>Precio Maximo</h4>
                                       <h5 class="text-success"><%= request.getAttribute("max") %></h5>
                                    </div>    
                                    </li>
                                </ul>
                            </div> <!-- Fin para precios -->
                            <!-- Inicio Graficos -->
                               <!-- KIT 1 -->
                               <div id="histogramaPrecio" style="width: 95%; height: 100%" align="center"></div><br>
                               <div id="promedioProvincias" style="width: 95%; height: 100%" align="center"></div><br>                         
                               <!-- KIT 2 -->
                               <table>
                               <tr align="center">
                               <td width="400"><div id="ofertadosProvincia" style="width: 400px; height: 300px"></div></td>
                               <td width="400"><div id="mercadoPago" style="width: 100%; height: 100%"></div></td>
                               </tr>
                               </table>
                			   <div id="histogramaOferta" style="width: 95%; height: 100%" align="center"></div><br>
                			   <div id="bajasPorDias" style="width: 95%; height: 100%" align="center"></div><br>
                			   
                			   <!-- KIT 3 -->
                			   <div id="histogramaVendidos" style="width: 95%; height: 100%" align="center" ></div><br>
                 			   <div id="vendidosProvincia" style="width: 400px; height: 400px" align="center"></div><br>
                			   <div id="ofertaDemanda" style="width: 95%; height: 100%" align="center"></div>
                			   <div id='map_canvas'></div>
                            <!-- Fin Graficos -->
                            	
                            	
                            	                          
                             <div id="nuevoPais"><iframe id='iFrame' width="100%" height="1000" sandbox="allow-same-origin allow-scripts "></iframe></div> 
                            <!--<div id="nuevoPais"></div>-->
                        </div>
                        <!-- /.inner -->
                    </div>
                    <!-- /.row-fluid -->
                </div>
                <!-- /.outer -->
            </div>
            <!-- END CONTENT -->


            <!-- #push do not remove 
            <div id="push"></div>-->
            <!-- /#push -->
        </div>
       </div>
        <!-- END WRAP -->

        <div class="clearfix"></div>

        <!-- BEGIN FOOTER -->
        <div id="footer">
            <p>2015 © MeliDatos</p>
        </div>
        <!-- END FOOTER -->

    </body>
</html>
