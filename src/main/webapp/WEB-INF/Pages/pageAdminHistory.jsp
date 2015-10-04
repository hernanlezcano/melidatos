<!DOCTYPE html>
<!--html  xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"-->
<html>
	<head>
		<meta charset="utf-8" />
		<title>Administracion de historiales</title>
		<meta name="description" content="This is page-header (.page-header &gt; h1)" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<!--basic styles-->

		<link href="resources/css/bootstrap.min.css" rel="stylesheet" />
		<link href="resources/css/bootstrap-responsive.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="resources/themes/font-awesome/css/font-awesome.min.css" />

		<!--page specific plugin styles-->

		<link rel="stylesheet" href="resources/themes/css/prettify.css" />

		<!--fonts-->

		<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400,300" />

		<!--ace styles-->

		<link rel="stylesheet" href="resources/themes/css/w8.min.css" />
		<link rel="stylesheet" href="resources/themes/css/w8-responsive.min.css" />
		<link rel="stylesheet" href="resources/themes/css/w8-skins.min.css" />
		
		<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  		<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  		<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
		
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script type="text/javascript">
		function showCategories(){
			var obj = <%= request.getAttribute("categories") %>;
			var datos1 = "";
			var i = 0;

			for(i ; i < obj.length/4;i++){ 
				if(obj[i].id.indexOf("MLA")>-1){
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Argentina</b></span></label></li>";
				}else if(obj[i].id.indexOf("MLB")>-1){
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Brasil</b></span></label></li>";
				}else{
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Chile</b></span></label></li>";
				}
			} 
			$("#tasks1").html(datos1);
			var datos1 = "";
			for(i; i < (obj.length/4)*2;i++){ 
				if(obj[i].id.indexOf("MLA")>-1){
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Argentina</b></span></label></li>";
				}else if(obj[i].id.indexOf("MLB")>-1){
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Brasil</b></span></label></li>";
				}else{
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Chile</b></span></label></li>";
				}
			} 
			$("#tasks2").html(datos1);
			var datos1 = "";
			for(i; i < (obj.length/4)*3;i++){ 
				if(obj[i].id.indexOf("MLA")>-1){
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Argentina</b></span></label></li>";
				}else if(obj[i].id.indexOf("MLB")>-1){
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Brasil</b></span></label></li>";
				}else{
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Chile</b></span></label></li>";
				}
			} 
			$("#tasks3").html(datos1);
			var datos1 = "";
			for(i; i < (obj.length/4)*4;i++){ 
				if(obj[i].id.indexOf("MLA")>-1){
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Argentina</b></span></label></li>";
				}else if(obj[i].id.indexOf("MLB")>-1){
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Brasil</b></span></label></li>";
				}else{
					datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = " + i +" value = '" + obj[i].id +"'/><span class='lbl'>" + obj[i].name + " <br><b>Chile</b></span></label></li>";
				}
			} 
			
			$("#tasks4").html(datos1);
			var datos1 = "";
			
			datos1=datos1+"<li class='item-orange'><label class='inline'><input type='checkbox' id = "+ "todos" +" value = 'todos'/><span class='lbl'>TODOS</span></label></li>";
			$("#tasks5").html(datos1);
		}
		</script>
		<script type="text/javascript">
				function asynchronousStartRecordsAlgorithm(){
					document.getElementById("startConfirmationLabel").innerHTML="Algoritmo funcionando";
					
					var categories = "";
				
					
					if(document.getElementById("todos").checked){
						/*jsonArr.push({
				            id: document.getElementById("todos").value
				        });*/
				        
				        categories = "[{\"id\": \"todos\"}]";
				        
					}else{
						categories = "[";
						for(var i=0;i<28; i++){
							console.log(i);
						    if(document.getElementById(i).checked){
						    	
						    	categories=categories+"{\"id\": " + "\"" + document.getElementById(i).value + "\"" + " },";
						    }
						   
						}
						categories = categories.substring(0, categories.length-1);
						categories=categories+"]";
					}
					
					//alert(categories);
					
					$.ajax({  
					type : "POST",
					url : "startRecordsAlgorithm",
					data : "itemQuantity=" + document.getElementById('itemQuantity').value + "&" +
							"scheduleHour=" +document.getElementById('schedule').value + "&" +
							"frequency=" +document.getElementById('frequency').value + "&" +
							"categories=" + categories,
					success : function(response) {
						// we have the response
						
					},
					error : function(e) {
						
					}
					});

				}
				</script>
				<script type="text/javascript">
				function asynchronousStopRecordsAlgorithm(){
					document.getElementById("startConfirmationLabel").innerHTML="Algoritmo no funcionando";
					$.ajax({  
					type : "POST",
					url : "stopRecordsAlgorithm",
					
					success : function(response) {
						// we have the response
						
					},
					error : function(e) {
						
					}
					});

				}
				</script>	
	</head>

	<body onload="showCategories()">
		<div class="navbar navbar-inverse">
			<div class="navbar-inner">
				<div class="container-fluid">
					<a href="#" class="brand">
						<small>
							<i class="icon-unlock-alt"></i>
							Administracion Melidatos - Proceda con precaucion
						</small>
					</a><!--/.brand-->

					<ul class="nav ace-nav pull-right">
						
						<li class="light-blue user-profile">
							<a data-toggle="dropdown" href="#" class="user-menu dropdown-toggle">
								<img class="nav-user-photo" src="resources/themes/images/user.png" alt="Jason's Photo" />
								<span id="user_info">
									<small>Bienvenido,</small>
									administrador
								</span>

								<i class="icon-caret-down"></i>
							</a>

							<ul class="pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-closer" id="user_menu">
								<li>
									<a href="#">
										<i class="icon-cog"></i>
										Settings
									</a>
								</li>

								<li>
									<a href="#">
										<i class="icon-user"></i>
										Profile
									</a>
								</li>

								<li class="divider"></li>

								<li>
									<a href="pageAdminLogin">
										<i class="icon-off"></i>
										Logout
									</a>
								</li>
							</ul>
						</li>
					</ul><!--/.w8-nav-->
				</div><!--/.container-fluid-->
			</div><!--/.navbar-inner-->
		</div>

		<div class="container-fluid" id="main-container">
			<a id="menu-toggler" href="#">
				<span></span>
			</a>

			<div id="sidebar">
				

				<ul class="nav nav-list">
					<li class="active">
						<a href="pageAdminHistory">
							<span>Historiales</span>
						</a>
					</li>

					
			</div>

			<div id="main-content" class="clearfix">
				<div id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="icon-home"></i>
							<a href="#">Home</a>

							<span class="divider">
								<i class="icon-angle-right"></i>
							</span>
							
						</li>
						<li class="active">Historiales</li>
					</ul><!--.breadcrumb-->

				</div>
					
			
				
				<form align="center" method="post">
  					
					</br>
					<div class="span11" align="center">
					
						<div id="Quantity" class="control-group">
							<label for="itemQuantity" class="control-label">	
								Cantidad de items a buscar
							</label>
							<div class="controls">
								<select name="itemQuantity" id="itemQuantity">
									<option value="50">50 items</option>
									<option value="100">100 items</option>
									<option value="150">150 items</option>
									<option value="200">200 items</option>
								</select>
							</div>
						</div>
						
						<div class="control-group">
							<label for="shedule" class="control-label">	
								Hora de busqueda
							</label>
							<div class="controls">
								<select name="schedule" id="schedule">
									<option value="00:05">00:00 Hs.</option>
									<option value="01:00">01:00 Hs.</option>
									<option value="02:00">02:00 Hs.</option>
									<option value="03:00">03:00 Hs.</option>
									<option value="04:00">04:00 Hs.</option>
									<option value="05:00">05:00 Hs.</option>
									<option value="06:00">06:00 Hs.</option>
									<option value="07:00">07:00 Hs.</option>
									<option value="08:00">08:00 Hs.</option>
									<option value="09:00">09:00 Hs.</option>
									<option value="10:00">10:00 Hs.</option>
									<option value="11:00">11:00 Hs.</option>
									<option value="12:00">12:00 Hs.</option>
									<option value="13:06">13:06 Hs.</option>
									<option value="14:00">14:00 Hs.</option>
									<option value="15:00">15:00 Hs.</option>
									<option value="16:00">16:00 Hs.</option>
									<option value="17:00">17:00 Hs.</option>
									<option value="18:00">18:00 Hs.</option>
									<option value="19:00">19:00 Hs.</option>
									<option value="20:00">20:00 Hs.</option>
									<option value="21:00">21:00 Hs.</option>
									<option value="22:00">22:00 Hs.</option>
									<option value="23:55">23:55 Hs.</option>	
								</select>
							</div>
						</div>
						
						<div class="control-group">
							<label for="frequency" class="control-label">	
								Frecuencia de busqueda
							</label>
							<div class="controls">
								<select id="frequency" name="frequency" >
									<option value="day">Diario</option>
									<option value="semanal">Semanal</option>
									<option value="month">Mensual</option>
								</select>
							</div>
						</div>
						
							<div class="widget-body">
									<div class="widget-main padding-4">
										<div class="tab-content padding-8">
											<div id="task-tab" class="tab-pane active">
											<form id="form1"><input type="checkbox" class="item-list"/></form>
											<table>
											<tr>
											<td>
											<ul id="tasks1" class="item-list">
													
												</ul>
											</td>
											<td>
											<ul id="tasks2" class="item-list">
													
												</ul>
											</td>
											<td>
											<ul id="tasks3" class="item-list">
													
												</ul>
											</td>
											<td>
											<ul id="tasks4" class="item-list">
													
												</ul>
											</td>
											<td>
											<ul id="tasks5" class="item-list">
													
												</ul>
											</td>
											</tr>
											</table>
											
												
											</div>
										</div>
									</div>
							</div>
						
						</br>
    					<a class="btn" type="submit" onclick="asynchronousStartRecordsAlgorithm();" >
    					
        				<i class="icon-ok icon-white"></i>
        				<span><strong>Iniciar</strong></span>            
    					</a>
    					<a class="btn btn-warning" type="submit" onclick="asynchronousStopRecordsAlgorithm();" >
    					<i class="icon-ok icon-white"></i>
        				<span><strong>Parar</strong></span>            
    					</a>
    					<label id="startConfirmationLabel"></label>
    					<hr>
    					
					</div>
				</form>
			</div><!--/#main-content-->
		</div><!--/.fluid-container#main-container-->

		<!--basic scripts-->

		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script type="text/javascript">
			window.jQuery || document.write("<script src='resources/themes/js/jquery-1.9.1.min.js'>"+"<"+"/script>");
		</script>
		<script src="resources/js/bootstrap.min.js"></script>
		<script src="resources/themes/js/jquery-ui-1.10.3.custom.min.js"></script>
		<script src="resources/themes/js/jquery.ui.touch-punch.min.js"></script>
		
		<script src="resources/themes/js/jquery.slimscroll.min.js"></script>
		<script src="resources/themes/js/jquery.easy-pie-chart.min.js"></script>
		<script src="resources/themes/js/jquery.sparkline.min.js"></script>
		
		<script src="resources/themes/js/jquery.flot.min.js"></script>
		<script src="resources/themes/js/jquery.flot.pie.min.js"></script>
		<script src="resources/themes/js/jquery.flot.resize.min.js"></script>

		<!--w8 scripts-->

		<script src="resources/themes/js/w8-elements.min.js"></script>
		<script src="resources/themes/js/w8.min.js"></script>

		<!--inline scripts related to this page-->

		<script type="text/javascript">
			$(function() {
			
				$('.dialogs,.comments').slimScroll({
			        height: '300px'
			    });
				
				$('#tasks').sortable();
				$('#tasks').disableSelection();
				$('#tasks input:checkbox').removeAttr('checked').on('click', function(){
					if(this.checked) $(this).closest('li').addClass('selected');
					else $(this).closest('li').removeClass('selected');
				});
			
				var oldie = $.browser.msie && $.browser.version < 9;
				$('.easy-pie-chart.percentage').each(function(){
					var $box = $(this).closest('.infobox');
					var barColor = $(this).data('color') || (!$box.hasClass('infobox-dark') ? $box.css('color') : 'rgba(255,255,255,0.95)');
					var trackColor = barColor == 'rgba(255,255,255,0.95)' ? 'rgba(255,255,255,0.25)' : '#E2E2E2';
					var size = parseInt($(this).data('size')) || 50;
					$(this).easyPieChart({
						barColor: barColor,
						trackColor: trackColor,
						scaleColor: false,
						lineCap: 'butt',
						lineWidth: parseInt(size/10),
						animate: oldie ? false : 1000,
						size: size
					});
				})
			
				$('.sparkline').each(function(){
					var $box = $(this).closest('.infobox');
					var barColor = !$box.hasClass('infobox-dark') ? $box.css('color') : '#FFF';
					$(this).sparkline('html', {tagValuesAttribute:'data-values', type: 'bar', barColor: barColor , chartRangeMin:$(this).data('min') || 0} );
				});
			
			
			
			
			  var data = [
				{ label: "social networks",  data: 38.7, color: "#68BC31"},
				{ label: "search engines",  data: 24.5, color: "#2091CF"},
				{ label: "ad campaings",  data: 8.2, color: "#AF4E96"},
				{ label: "direct traffic",  data: 18.6, color: "#DA5430"},
				{ label: "other",  data: 10, color: "#FEE074"}
			  ];
			
			  var placeholder = $('#piechart-placeholder').css({'width':'90%' , 'min-height':'150px'});
			  $.plot(placeholder, data, {
				
				series: {
			        pie: {
			            show: true,
						tilt:0.8,
						highlight: {
							opacity: 0.25
						},
						stroke: {
							color: '#fff',
							width: 2
						},
						startAngle: 2
						
			        }
			    },
			    legend: {
			        show: true,
					position: "ne", 
				    labelBoxBorderColor: null,
					margin:[-30,15]
			    }
				,
				grid: {
					hoverable: true,
					clickable: true
				},
				tooltip: true, //activate tooltip
				tooltipOpts: {
					content: "%s : %y.1",
					shifts: {
						x: -30,
						y: -50
					}
				}
				
			 });
			
			 
			  var $tooltip = $("<div class='tooltip top in' style='display:none;'><div class='tooltip-inner'></div></div>").appendTo('body');
			  placeholder.data('tooltip', $tooltip);
			  var previousPoint = null;
			
			  placeholder.on('plothover', function (event, pos, item) {
				if(item) {
					if (previousPoint != item.seriesIndex) {
						previousPoint = item.seriesIndex;
						var tip = item.series['label'] + " : " + item.series['percent']+'%';
						$(this).data('tooltip').show().children(0).text(tip);
					}
					$(this).data('tooltip').css({top:pos.pageY + 10, left:pos.pageX + 10});
				} else {
					$(this).data('tooltip').hide();
					previousPoint = null;
				}
				
			 });
			
			
			
			
			
			
				var d1 = [];
				for (var i = 0; i < Math.PI * 2; i += 0.5) {
					d1.push([i, Math.sin(i)]);
				}
			
				var d2 = [];
				for (var i = 0; i < Math.PI * 2; i += 0.5) {
					d2.push([i, Math.cos(i)]);
				}
			
				var d3 = [];
				for (var i = 0; i < Math.PI * 2; i += 0.2) {
					d3.push([i, Math.tan(i)]);
				}
				
			
				var sales_charts = $('#sales-charts').css({'width':'100%' , 'height':'220px'});
				$.plot("#sales-charts", [
					{ label: "Domains", data: d1 },
					{ label: "Hosting", data: d2 },
					{ label: "Services", data: d3 }
				], {
					hoverable: true,
					shadowSize: 0,
					series: {
						lines: { show: true },
						points: { show: true }
					},
					xaxis: {
						tickLength: 0
					},
					yaxis: {
						ticks: 10,
						min: -2,
						max: 2,
						tickDecimals: 3
					},
					grid: {
						backgroundColor: { colors: [ "#fff", "#fff" ] },
						borderWidth: 1,
						borderColor:'#555'
					}
				});
			
			
				$('#recent-box [data-rel="tooltip"]').tooltip({plw8ment: tooltip_plw8ment});
				function tooltip_plw8ment(context, source) {
					var $source = $(source);
					var $parent = $source.closest('.tab-content')
					var off1 = $parent.offset();
					var w1 = $parent.width();
			
					var off2 = $source.offset();
					var w2 = $source.width();
			
					if( parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2) ) return 'right';
					return 'left';
				}
			})
		</script>
	</body>
</html>
