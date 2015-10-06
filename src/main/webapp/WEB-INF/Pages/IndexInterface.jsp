<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
	
    <title>MeliDatos | Estadísticas de MercadoLibre</title>
	<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" rel="stylesheet">
    <!-- Bootstrap Core CSS -->
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">
	<link href="resources/css/bootstrap-select.css" rel="stylesheet" />
    <!-- Custom CSS -->
    <link href="resources/css/logo-nav.css" rel="stylesheet">
	<script src="resources/js/jquery.js"></script>
	<script src="resources/jquery-ui.min.js"></script>
	

	<script src="resources/js/bootstrap-select.js" type="text/javascript"></script>
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

    
</head>

<body>

    <!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
            	
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                
                <a class="navbar-brand" href="#">
                    <img class="img-responsive" src="resources/css/logo.png" alt="">
                </a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="pageAdminHistory"><span class="glyphicon glyphicon-user"></span> Administrador</a>
                    </li>
                    <li>
                        <a href="#"><span class="glyphicon glyphicon-info-sign"></span> Ayuda</a>
                    </li>
                    <li>
                        <a href="contactos.jsp"><span class="glyphicon glyphicon-briefcase"></span> Contacto</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

    <!-- Page Content -->
    <div class="container">
        
        <!-- example 2 -->
        <div class="mdsearch">
           <div class="mdsearch-inner text-center">
              <form class="search has-button" action="InformationRequest" method="get">
              <!--<form class="main-search" action="InformationRequest" method="get"> -->     	
                 		<h3 class="no-margin-top h1">¿Qu&eacute est&aacutes buscando?</h3>
                 		<div class="form-group">
                    	<input type="text" placeholder="Ej: iPod" class="form-control form-control-lg" id="project"  name="project">
                    		<select id="comboPais" class="selectpicker" name="Pais">
               					 <option value="MLA">Argentina</option>
             				     <option value="MLB">Brasil</option>
               					 <option value="MLC">Chile</option>
                		    </select>
                    	<input type="text" id="project-id" name="project-id" class="form-control form-control-lg" >
                    	<button id="searchBtn" type="submit" class="btn btn-lg btn-warning" >Buscar</button>
                    	</div>
              </form>     
                 <!-- /form-group -->
              
              <!-- /.max-width on this form -->
           </div>
           <!-- /.featurette-inner -->

        </div>
    
    </div>
    <!-- /.container -->
    
    <!-- Footer -->
    <footer class="footer">MeliDatos &#169 2015 &#8212 <a href="#" title="E-mail" data-toggle="popover" data-placement="top" data-content="lezcanotasso@gmail.com">Hern&aacuten Lezcano</a> |  <a href="http://www.ucc.edu.ar" target=_blank>Universidad Cat&oacutelica de C&oacuterdoba</a></footer>
    <!-- /.footer -->

    <!-- jQuery -->

    <!-- Bootstrap Core JavaScript -->
    <script src="resources/js/bootstrap.min.js"></script>
    
    <!-- Popover -->
    <script>
    $(document).ready(function(){
        $('[data-toggle="popover"]').popover();
    });
    </script>

</body>

</html>
