<!DOCTYPE html PUBLIC>
<html xmlns="http://www.w3.org/1999/xhtml" >

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Bienvenidos a MeliDatos</title>
	<script src="http://code.jquery.com/jquery.js"></script>
    <script src="resources/js/bootstrap.min.js"></script>
	<link rel="stylesheet" href="resources/css/bootstrap.min.css" type="text/css" />
	
   <link rel="stylesheet" href="resources/css/jquery-jvectormap-1.2.2.css" type="text/css"/>
  <script src="resources/js/jquery-jvectormap-1.2.2.min.js"></script>
  <script src="resources/js/jquery-jvectormap-ar-mill-en.js"></script>
    <script>
    $(function(){
      $('#map').vectorMap({
      map: 'ar_mill_en',
      });
    });
  </script>		

</head>

<body>

<script src="http://pajhome.org.uk/crypt/md5/2.2/md5-min.js"></script>

<div class="container" align="center" style="vertical-align:middle;" >
      	<form action="login" class="form-signin" style="width:300px" method="post" onsubmit="return validarForm(this)">
        	<h2 class="form-signin-heading">Bienvenidos a MeliDatos</h2>
        	<input id = "mail" name="mail" type="text" class="input-block-level" placeholder="Email address" size="40">
        	<input id = "pass" name="pass" type="password" class="input-block-level" placeholder="Password" size="40">
			</br>
			<button class="btn btn-large btn-primary" type="submit">Ingresar</button>
		</form>
</div>
<!-- <div id="map"></div> -->

<script type="text/javascript">
function validarForm(formulario) {
	  if(formulario.mail.value.length==0) { 
	    formulario.mail.focus();    
	    $(".mail").show();
	    return false; 
	   }else{$(".mail").hide();}
	  if(formulario.pass.value.length==0) { 
		    formulario.pass.focus();    
		    $(".pass").show();
		    return false; 
		   }else{$(".pass").hide();}
	formulario.pass.value = hex_md5(formulario.pass.value);
	return true;
}
</script>

<div class="mail alert-error" style="display:none" align="center">
  <strong>Wrong!</br></strong> Input your email address</div>
  <div class="pass alert-error" style="display:none" align="center">
  <strong>Wrong!</br></strong> Input your password</div>

		<script type="text/javascript">
function validarForm2(formulario) {
  if(formulario.first.value.length==0) { //¿Tiene 0 caracteres?
    formulario.first.focus();    // Damos el foco al control
    $(".first").show();
    return false; //devolvemos el foco
   }else{$(".first").hide();}
    if(formulario.second.value.length==0) { //¿Tiene 0 caracteres?
    formulario.second.focus();    // Damos el foco al control
    $(".second").show();
    return false; //devolvemos el foco
  }else{$(".second").hide();}
  if(formulario.email.value.length==0) { //¿Tiene 0 caracteres?
    formulario.email.focus();    // Damos el foco al control
    $(".email").show();
    return false; //devolvemos el foco
  }else{$(".email").hide();}
    if(formulario.newpassword.value.length==0) { //¿Tiene 0 caracteres?
    formulario.newpassword.focus();    // Damos el foco al control
    $(".newpassword").show();
    return false; //devolvemos el foco
    }else{$(".newpassword").hide();}
    if(formulario.retrypassword.value.length==0) { //¿Tiene 0 caracteres?
    formulario.retrypassword.focus();    // Damos el foco al control
    $(".retrypassword").show();
    return false; //devolvemos el foco
    }else{$(".retrypassword").hide();}
    //Verifica que los dos password sean iguales
    formulario.newpassword.value = hex_md5(formulario.newpassword.value);
    formulario.retrypassword.value = hex_md5(formulario.retrypassword.value);
    var clave1 = formulario.newpassword.value; 
   	var clave2 = formulario.retrypassword.value; 
   	if (clave1 != clave2){
   		$(".claves").show();
   		formulario.retrypassword.value="";
   		formulario.retrypassword.focus();
   		return false;
   	}else{$(".claves").hide();};
   	$(".bienvenido").show();
    return true; //Si ha llegado hasta aquí, es que todo es correcto
}

</script>

<div class="first alert-error" style="display:none" align="center">
  <strong>Wrong!</br></strong> Input your first name</div>
<div class="second alert-error" style="display:none" align="center">
  <strong>Wrong!</br></strong> Input your lastname</div>
<div class="email alert-error" style="display:none" align="center">
  <strong>Wrong!</br></strong> Input your email</div>
  <div class="newpassword alert-error" style="display:none" align="center">
  <strong>Wrong!</br></strong> Input your password</div>
  <div class="retrypassword alert-error" style="display:none" align="center">
  <strong>Wrong!</br></strong> Retry password</div>
  <div class="claves alert-info" style="display:none" align="center">
  <strong>Warning!</br></strong> Retry valid password</div>
  <div class="bienvenido alert-success" style="display:none" align="center">
  <strong>Bienvenido a Melidatos!</br></strong> Sus datos estan siendo registrados.</div>


<div align="center">
<table><tr>
<th align="center" width="50%">     
	  	 
</th>

<th align="center" width="100%">
<div align="center" style="vertical-align:middle;">
<form id="newUser" action="onUserRegistration" method="post" onsubmit="return validarForm2(this)">
<h3 align="center">Registrate</h3>
<input type="text" id="first" name="first" placeholder="First Name" size="40"/></br>
<input type="text" id="second" name="second" placeholder="Last Name" size="40" /></br>
<input type="text" class="control-label" for="inputIcon" id="email" name="email" size="40" placeholder="Email address"/></br>
<input type="password" id="newpassword" name="newpassword" placeholder="Password" size="40"/></br>
<input type="password" id="retrypassword" name="retrypassword" placeholder="Retry password" size="40"/></br>
<p><button class="btn btn-large btn-success" type="submit">Registrarme</button></p>
</form>
</div>
</th>
</tr></table>
</div>
</body>
</html>

