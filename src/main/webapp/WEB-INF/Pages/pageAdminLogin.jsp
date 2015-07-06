<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Administrator</title>
	<script src="resources/js/bootstrap.min.js"></script>
	<link rel="stylesheet" href="resources/css/bootstrap.min.css" type="text/css" />
</head>

<body>
	<script src="http://pajhome.org.uk/crypt/md5/2.2/md5-min.js"></script>
	
	<script type="text/javascript">
		function validateForm(formulario) {
	  		if(formulario.mail.value.length==0) { 
	    		formulario.mail.focus();    
	    		$(".mail").show();
	    		return false; 
	   		}
	  		if(formulario.pass.value.length==0) { 
		    		formulario.pass.focus();    
		    		$(".pass").show();
		    		return false; 
		   	}
	  		
			formulario.pass.value = hex_md5(formulario.pass.value);
			return true;
		}
	</script>
	
	<div class="container" align="center" style="vertical-align:middle;">
      	<form action="adminLogin" class="form-signin" style="width:300px" method="post" onsubmit="return validateForm(this)">
        	<h2 class="form-signin-heading">Administrator</h2>
        	<input id = "mail" name="mail" type="text" class="input-block-level" placeholder="Email address" size="40">
        	<input id = "pass" name="pass" type="password" class="input-block-level" placeholder="Password" size="40">
			</br>
			<button class="btn btn-large btn-primary" type="submit">Ingresar</button>
		</form>
	</div>
	
</body>
</html>