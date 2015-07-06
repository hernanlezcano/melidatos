<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>MeliDatos</title>
        <link type="text/css" rel="stylesheet" href="resources/assets/css/bootstrap.css">
        <link type="text/css" rel="stylesheet" href="resources/assets/css/bootstrap-responsive.min.css">
        <link type="text/css" rel="stylesheet" href="resources/assets/css/style.css">
        
		<link rel="stylesheet" href="resources/jquery-ui.css">

        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
        <script src="http: //html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
        <!--[if IE 7]>
        <link type="text/css" rel="stylesheet" href="assets/Font-awesome/css/font-awesome-ie7.min.css"/>
        <![endif]-->
       
       

		 <script type="text/javascript">
		function changeToKitOne(){
				document.getElementById("span1").className="active";
				document.getElementById("span2").className="";
				document.getElementById("span3").className="";
				document.getElementById("ActualPricesData").style.display = "inline";
				document.getElementById("histogramaPrecio").style.display = "inline";
				document.getElementById("ofertadosProvincia").style.display = "none";
				document.getElementById("histogramaOferta").style.display = "none";
				document.getElementById("bajasPorDias").style.display = "none";
				document.getElementById("mercadoPago").style.display = "none";
				document.getElementById("histogramaVendidos").style.display = "none";
				document.getElementById("vendidosProvincia").style.display = "none";
				document.getElementById("ofertaDemanda").style.display = "none";
			}
		function changeNone(){
				document.getElementById("search").focus();
				document.getElementById("span1").className=" ";
				document.getElementById("span2").className=" ";
				document.getElementById("span3").className=" ";
				document.getElementById("ActualPricesData").style.display = "none";
				document.getElementById("histogramaPrecio").style.display = "none";
				document.getElementById("ofertadosProvincia").style.display = "none";
				document.getElementById("histogramaOferta").style.display = "none";
				document.getElementById("bajasPorDias").style.display = "none";
				document.getElementById("mercadoPago").style.display = "none";
				document.getElementById("histogramaVendidos").style.display = "none";
				document.getElementById("vendidosProvincia").style.display = "none";
				document.getElementById("ofertaDemanda").style.display = "none";
			}		
     	</script> 
     	
     	
     	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
		<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
     	<script type="text/javascript">
     	
     	 window.onload = function(){
         	document.onkeydown = deleteInfo;
         };
         function deleteInfo(elEvento){
         	var evento = window.event|| elEvento;
         	console.log(evento.keyCode);
         	if (evento.keyCode == 46){
         		document.getElementById("project").value = "";
         		document.getElementById("project-id").value = "";
         		
         	}
         }
     	</script>
     	
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
		<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>		
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
                                data : "word=" + document.getElementById('project').value + "&test=" +document.getElementById('project-id').value,
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
                            this.value = terms.join( ", " );
                            
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

	</head>

   <!-- <body onload="changeNone()"> --> 
   <body>
   		<div id="project-label">Seleccione el articulo:</div>
		<input id="project" class="col-md-4">
		<input id="project-id">		
		<p id="project-description"></p>
 
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
                            <img src="resources/img/MeliDatos-blanco.png" />
                            <!-- .topnav -->
                          <div class="btn-toolbar topnav">
                                <div class="btn-group">
                                
                                      <!-- Socials Buttons : http://ostr.io/code/html-social-like-share-buttons-no-javascript.html  -->
                                <a class="btn btn-default" href="http://www.facebook.com/sharer.php?u=http://www.melidatos.com.ar/" target="top"
                                onClick="window.open(this.href, this.target, 'width=100%,height=100%');"> 
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
                            </div>
                            <!-- /.topnav -->
                        </div>
                    </div>
                </div>
                <!-- /.navbar -->
            </div>
            <!-- END TOP BAR -->


            <!-- BEGIN HEADER.head -->
            <header class="head">

                <!-- ."main-bar -->
                <div class="main-bar"  style="margin-left: 40%">
                    <div class="search-bar">
                    <div class="row-fluid" >
                        <div class="span12">
                            <div class="search-bar-inner">
                                <a id="menu-toggle" href="#menu" data-toggle="collapse"
                                   class="accordion-toggle btn btn-inverse visible-phone"
                                   rel="tooltip" data-placement="bottom" data-original-title="Show/Hide Menu">
                                    <i class="icon-sort"></i>                                </a>
                                

                                <form class="main-search" action="InformationRequest" method="get">
                                
                                    <input class="input-block-level" type="text" list="suggestions" id="search" onkeyup="suggest();" name="search"
                                     placeholder="Buscar..." autocomplete="on">
                                    <datalist id="suggestions" onclick="msj();">
                                                  
                                    </datalist>
                                    <button id="searchBtn" type="submit" class="btn btn-inverse" onclick="changeToKitOne()"><i class="icon-search"></i>                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
                    <!-- /.container-fluid -->
                </div>
                <!-- /.main-bar -->
            </header>
            <!-- END HEADER.head -->
			
			<div align="center" style="vertical-align: middle">
                       <h3><i><%= request.getAttribute("data") %></i></h3>
            </div>
            
            <div align="center" style="vertical-align: middle;">
         
         <                     <img src="resources/img/wordcloud2.png" width="80%" height="100%" style="margin-bottom: 20px;
                             margin-top: 20px;margin-left: 5px;margin-right: 5px;vertical-align: middle;"></img>
           
<!-- MeliDatos -->
<!--  script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
<ins class="adsbygoogle"
     style="display:inline-block;width:970px;height:90px"
     data-ad-client="ca-pub-8876106263231512"
     data-ad-slot="7931122587"></ins>
<script>
(adsbygoogle = window.adsbygoogle || []).push({});
</script-->
                            </div>            

            <!-- #push do not remove -->
            <div id="push"></div>
            <!-- /#push -->
        </div>
        <!-- END WRAP -->

        <div class="clearfix"></div>

        <!-- BEGIN FOOTER -->
  
                                
        <div id="footer">
            <p>2014 © MeliDatos</p>
        </div>
        <!-- END FOOTER -->
  
    </body>
</html>
