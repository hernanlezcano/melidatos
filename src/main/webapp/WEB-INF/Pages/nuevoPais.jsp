<!DOCTYPE html><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
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
				
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		
		<script type="text/javascript">
		function myFunction() {
		    alert("hola bebe");
		}
		</script>
		
		<script type="text/javascript">
		
			  google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
		       	console.log("Entto ACAALSDKASKDJASKL");
		    	//este no tiene drama 
		    	var data = google.visualization.arrayToDataTable([<%= request.getAttribute("avgStates") %>]);
		        var options = {
		        		title: 'Precio promedio por provincia',
		                hAxis: {title: 'Provincia', titleTextStyle: {color: 'red'}},
		                vAxis: {title: 'Promedio ($)', titleTextStyle: {color: 'red'}},
		                colors:['#0d2b26'],
		        };

		        var chart = new google.visualization.ColumnChart(document.getElementById('promedioProvincias2'));
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
			        
			      
		
			        var container = document.getElementById('map_canvas2');
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

		        var chart = new google.visualization.ColumnChart(document.getElementById('histogramaPrecio2'));
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

		        var chart = new google.visualization.PieChart(document.getElementById('ofertadosProvincia2'));
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

		        var chart = new google.visualization.ColumnChart(document.getElementById('histogramaOferta2'));
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

		        var chart = new google.visualization.ColumnChart(document.getElementById('bajasPorDias2'));
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
		        
		        var table = new google.visualization.Table(document.getElementById('mercadoPago2'));
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

		        var chart = new google.visualization.ColumnChart(document.getElementById('histogramaVendidos2'));
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

		        var chart = new google.visualization.PieChart(document.getElementById('vendidosProvincia2'));
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
		
		        var chart = new google.visualization.LineChart(document.getElementById('ofertaDemanda2'));
		        chart.draw(data, options);
		      }
	    </script>



		
		


	</head>

    <body>
    
    						<div id="ActualPricesData2" align="center">
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
                               <div id="histogramaPrecio2" style="width: 95%; height: 100%" align="center"></div><br>
                               <div id="promedioProvincias2" style="width: 95%; height: 100%" align="center"></div><br>                         
                               <!-- KIT 2 -->
                               <table>
                               <tr align="center">
                               <td width="400"><div id="ofertadosProvincia2" style="width: 400px; height: 300px"></div></td>
                               <td width="400"><div id="mercadoPago2" style="width: 100%; height: 100%"></div></td>
                               </tr>
                               </table>
                			   <div id="histogramaOferta2" style="width: 95%; height: 100%" align="center"></div><br>
                			   <div id="bajasPorDias2" style="width: 95%; height: 100%" align="center"></div><br>
                			   
                			   <!-- KIT 3 -->
                			   <div id="histogramaVendidos2" style="width: 95%; height: 100%" align="center" ></div><br>
                 			   <div id="vendidosProvincia2" style="width: 400px; height: 400px" align="center"></div><br>
                			   <div id="ofertaDemanda2" style="width: 95%; height: 100%" align="center"></div>
                			   <div id='map_canvas2'></div>
                            <!-- Fin Graficos -->
                        		<div> <input type = "button" value="hola" onclick="myFunction()"></div>
   


            <!-- #push do not remove 
            <div id="push"></div>-->
            <!-- /#push -->

        <!-- END WRAP -->

    </body>
</html>