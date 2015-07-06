<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>MeliDatos</title>
        <link type="text/css" rel="stylesheet" href="resources/assets/css/bootstrap.min.css">
        <link type="text/css" rel="stylesheet" href="resources/assets/css/bootstrap-responsive.min.css">
        <link type="text/css" rel="stylesheet" href="resources/assets/css/style.css">
        

        <link rel="stylesheet" href="resources/assets/css/theme.css">
        

        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
        <script src="http: //html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
        <!--[if IE 7]>
        <link type="text/css" rel="stylesheet" href="assets/Font-awesome/css/font-awesome-ie7.min.css"/>
        <![endif]-->
       
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		
		<script type="text/javascript">
		      google.load("visualization", "1", {packages:["corechart"]});
		      google.setOnLoadCallback(drawChart);
		      function drawChart() {
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
                        var options = '<option value="'+ obj[0].product + '" />';
                        for(var i = 1; i < obj.length; i++)
                            options += '<option value="'+ obj[i].product + '" />';
                        document.getElementById('suggestions').innerHTML = options;
                        
                        /* var categoryOptions = '<option value="'+ obj[0].father +'" />';
                        for(var i = 1; i < obj.length; i++)
                            categoryOptions += '<option value="'+ obj[i].father +'" />';
                        document.getElementById('super-category').innerHTML = categoryOptions; */
                    },
                    error : function(e) {
                        
                    }
                    });

                }
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
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script type="text/javascript" src="resources/fancybox/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript" src="resources/fancybox/fancybox/jquery.easing-1.3.pack.js"></script>
<script type="text/javascript" src="resources/fancybox/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
<link rel="stylesheet" href="resources/fancybox/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script>
$('.showDetail').fancybox({
    type: "ajax"
});
</script>
	</head>

    <body onload="changeToKitOne()">
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
                                    <input class="input-block-level" type="text" list="suggestions" id="search" onkeyup="suggest();" name="search" placeholder="Buscar..." autocomplete="off">
                                    <datalist id="suggestions">
                                                  
                                    </datalist>
                                    <button id="searchBtn" type="submit" class="btn btn-inverse" onclick="changeToKitOne()"><i class="icon-search"></i>                                    </button>
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
                                <h3 id="product">MeliDatos sobre: "<i><a class="iframe" target="top" href="mainpublications?var1=<%= request.getAttribute("search") %>" 
                                onClick="window.open(this.href, this.target, 'width=550,height=415');">
                                <%= request.getAttribute("search") %></a>" de <%= request.getAttribute("sampleSize") %> publicaciones</i></h3>
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
                               <table cellpadding="20px">
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
                            <!-- Fin Graficos -->
                            
                        </div>
                        <!-- /.inner -->
                    </div>
                    <!-- /.row-fluid -->
                </div>
                <!-- /.outer -->
            </div>
            <!-- END CONTENT -->


            <!-- #push do not remove -->
            <div id="push"></div>
            <!-- /#push -->
        </div>
        <!-- END WRAP -->

        <div class="clearfix"></div>

        <!-- BEGIN FOOTER -->
        <div id="footer">
            <p>2013 © MeliDatos</p>
        </div>
        <!-- END FOOTER -->

    </body>
</html>
