<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

	<title>MeliDatos - Principal</title>
		<meta name="description" content="This is page-header (.page-header &gt; h1)" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<!--basic styles-->

		<link href="resources/css/bootstrap.min.css" rel="stylesheet" />
		<link href="resources/css/bootstrap-responsive.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="resources/themes/font-awesome/css/font-awesome.min.css" />

		<!--[if IE 7]>
		  <link rel="stylesheet" href="themes/font-awesome/css/font-awesome-ie7.min.css" />
		<![endif]-->

		<!--page specific plugin styles-->

		<link rel="stylesheet" href="resources/themes/css/prettify.css" />

		<!--fonts-->

		<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400,300" />

		<!--ace styles-->

		<link rel="stylesheet" href="resources/themes/css/w8.min.css" />
		<link rel="stylesheet" href="resources/themes/css/w8-responsive.min.css" />
		<link rel="stylesheet" href="resources/themes/css/w8-skins.min.css" />


		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
    	<script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
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
		                vAxis: {title: 'Oferta (Un)', titleTextStyle: {color: 'red'}}
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
		                 vAxis: {title: 'Bajas (Un)', titleTextStyle: {color: 'red'}}
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
		        data.addColumn('string', 'Condicion');
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
		                vAxis: {title: 'Vendidos (Un)', titleTextStyle: {color: 'red'}}
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

		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script type="text/javascript">
                function suggest(){
                    console.log(document.getElementById('search').value);

                    if(document.getElementById('search').value.replace(/ /g, '') == ''){return false;}
                    $.ajax({  
                    type : "POST",
                    url : "autosuggest",
                    data : "word=" + document.getElementById('search').value,
                    
                    success : function(response) {
                        // we have the response
                        var obj = JSON.parse(response);
                        var options = '<option value="'+ obj[0].product +'" />';
                        for(var i = 1; i < obj.length; i++)
                            options += '<option value="'+ obj[i].product +'" />';
                        document.getElementById('suggestions').innerHTML = options;
                        
                        var categoryOptions = '<option value="'+ obj[0].father +'" />';
                        for(var i = 1; i < obj.length; i++)
                            categoryOptions += '<option value="'+ obj[i].father +'" />';
                        //document.getElementById('super-category').innerHTML = categoryOptions;
                    },
                    error : function(e) {
                        
                    }
                    });

                }
        </script>
	</head>

	<body onload="kitNone()" ng-controller="PhoneListCtrl">
		
       <div style="width:950px;margin:auto">
		<div class="navbar navbar-inverse" align="center">
			<div class="navbar-inner">
				<div class="container-fluid">
				<h2 style="color:#FFFFFF">MeliDatos</h2>
					<form  action="InformationRequest" method="get">
						
						 <input class="search-query" type="text" list="suggestions" id="search" onkeyup="suggest();" placeholder="Buscar..." autocomplete="off">
                         <datalist id="suggestions">
                                                  
                         </datalist>
						 
						 <button class="btn btn-small btn-warning" type="submit">Buscar</button>
					 </form>
					 <h4 style="color: white;" id="itemSearched"></h4>
				</div><!--/.container-fluid-->
			</div><!--/.navbar-inner-->
		</div>

		<div class="container-fluid" id="main-container">
			<div id="sidebar">
				<div id="sidebar-shortcuts">				
				</div><!--#sidebar-shortcuts-->

				<ul class="nav nav-list">
						<li id="li1" onClick="changeToKitOne()" style="font-weight: bold">
							<a href="#"><span id="span1">Precio</span></a>
						</li>
						<li id="li2" onClick="changeToKitTwo()">
							<a href="#"><span id="span2">Oferta</span></a>
						</li>
                    	<li id="li3" onClick="changeToKitThree()">
							<a href="#"><span id="span3">Mercado</span></a>
						</li>
				</ul><!--/.nav-list-->
			</div>

			<div id="main-content" class="clearfix" style="width: 800px">

                <br />

                <div id="ActualPricesData" style="text-align:center;">

						<!-- Precio Maximo -->
								<div class="infobox infobox-green  " align="center">
									<div class="infobox-data"  align="center">
										<span class="infobox-data-number"><%= request.getAttribute("max") %></span>
										<div class="infobox-content">Precio Maximo</div>
									</div>
								</div>
						<!-- Promedio de precios-->
								<div class="infobox infobox-blue  " align="center">
									<div class="infobox-data"  align="center">
										<span class="infobox-data-number"><%= request.getAttribute("avg") %></span>
										<div class="infobox-content">Precio Promedio</div>
									</div>
								</div>
								
						<!-- Precio minimo -->
								<div class="infobox infobox-red  " align="center">
									<div class="infobox-data"  align="center">
										<span class="infobox-data-number"><%= request.getAttribute("min") %></span>
										<div class="infobox-content">Precio Minimo</div>
									</div>
								</div>
                </div>
                <div id="histogramaPrecio" style="width: 900px; height: 500px; position:absolte"></div>
                <div id="ofertadosProvincia" style="width: 900px; height: 500px; position:absolte"></div>
                <div id="histogramaOferta" style="width: 900px; height: 500px; position:absolte"></div>
                <div id="bajasPorDias" style="width: 900px; height: 500px; position:absolte"></div>
                <div id="mercadoPago" style="width: 900px; height: 500px; position:absolte"></div>
                <div id="histogramaVendidos" style="width: 900px; height: 500px; position:absolte"></div>
                <div id="vendidosProvincia" style="width: 900px; height: 500px; position:absolte"></div>
                <div id="ofertaDemanda" style="width: 900px; height: 500px; position:absolte"></div>
                
			</div><!--/#main-content-->
		</div><!--/.fluid-container#main-container-->
        </div>
        
        		<script type="text/javascript">
			function changeToKitOne(){
			alert("asdasd");
				document.getElementById("span1").style="font-weight: bold";
				document.getElementById("span2").style="font-weight: normal";
				document.getElementById("span3").style="font-weight: normal";				
				document.getElementById("ActualPricesData").style = "text-align:center; display:inline";
				document.getElementById("histogramaPrecio").style = "display:inline";
				document.getElementById("ofertadosProvincia").style = "display:none";
				document.getElementById("histogramaOferta").style = "display:none";
				document.getElementById("bajasPorDias").style = "display:none";
				document.getElementById("mercadoPago").style = "display:none";
				document.getElementById("histogramaVendidos").style = "display:none";
				document.getElementById("vendidosProvincia").style = "display:none";
				document.getElementById("ofertaDemanda").style = "display:none";
			}
			function changeToKitTwo(){
			alert("asdasd");
				document.getElementById("span1").style="font-weight: normal";
				document.getElementById("span2").style="font-weight: bold";
				document.getElementById("span3").style="font-weight: normal";
				document.getElementById("ActualPricesData").style = "display:none";
				document.getElementById("histogramaPrecio").style = "display:none";
				document.getElementById("ofertadosProvincia").style = "display:inline";
				document.getElementById("histogramaOferta").style = "display:inline";
				document.getElementById("bajasPorDias").style = "display:inline";
				document.getElementById("mercadoPago").style = "display:inline";
				document.getElementById("histogramaVendidos").style = "display:none";
				document.getElementById("vendidosProvincia").style = "display:none";
				document.getElementById("ofertaDemanda").style = "display:none";
			}
			function changeToKitThree(){
			alert("asdasd");
				document.getElementById("span1").style="font-weight: normal";
				document.getElementById("span2").style="font-weight: normal";
				document.getElementById("span3").style="font-weight: bold";
				document.getElementById("ActualPricesData").style = "display:none";
				document.getElementById("histogramaPrecio").style = "display:none";
				document.getElementById("ofertadosProvincia").style = "display:none";
				document.getElementById("histogramaOferta").style = "display:none";
				document.getElementById("bajasPorDias").style = "display:none";
				document.getElementById("mercadoPago").style = "display:none";
				document.getElementById("histogramaVendidos").style = "display:inline";
				document.getElementById("vendidosProvincia").style = "display:inline";
				document.getElementById("ofertaDemanda").style = "display:inline";
			}
			function kitNone(){
			alert("asdasd");
				document.getElementById("span1").style="font-weight: normal";
				document.getElementById("span2").style="font-weight: normal";
				document.getElementById("span3").style="font-weight: bold";
				document.getElementById("ActualPricesData").style = "display:none";
				document.getElementById("histogramaPrecio").style = "display:none";
				document.getElementById("ofertadosProvincia").style = "display:none";
				document.getElementById("histogramaOferta").style = "display:none";
				document.getElementById("bajasPorDias").style = "display:none";
				document.getElementById("mercadoPago").style = "display:none";
				document.getElementById("histogramaVendidos").style = "display:none";
				document.getElementById("vendidosProvincia").style = "display:none";
				document.getElementById("ofertaDemanda").style = "display:none";
			}
		</script>
	</body>
</html>
