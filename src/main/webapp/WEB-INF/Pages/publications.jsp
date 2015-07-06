<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="resources/assets/css/bootstrap.min.css">
        <link type="text/css" rel="stylesheet" href="resources/assets/css/bootstrap-responsive.min.css">
        <link type="text/css" rel="stylesheet" href="resources/assets/css/style.css">
<title>Informe de Articulo</title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<script type="text/javascript">
/*var param = location.search.match(/[a-z_]\w*(?:=[^&]*)/?gi);*/
/*var param = document.location.href.split('$');*/
	var url= location.search.replace("?", "");
    var arrUrl = url.split("&");
    var urlObj={};   
    for(var i=0; i<arrUrl.length; i++){
        var x= arrUrl[i].split("=");
        urlObj[x[0]]=x[1];
    }
    /*alert(urlObj.var1.replace(/%20/g," "));*/

			function listPublications(){
				$.ajax({  
                    type : "POST",
                    url : "publications",
                    data : "search=" + urlObj.var1.replace(/%20/g," "),
            
                    success : function(objetojson) {
                    	
                    	var html = ""; //para devolver 
            			var datos = ""; //para guardar datos 
            			var obj = JSON.parse(objetojson);
            			//recorremos el json 
            			for(var i = 0; i < obj.length;i++){ 
            				datos=datos+"<tr align='center'><td><h3><a href='" + obj[i].permalink + "' target='_blank'>" + obj[i].title + "</a></h3></td><td><h4><strong>$" + obj[i].price + "</strong></h4></td>"; 
            			} 
            			datos="<table>"+datos+"</table>";
   
            			$("#divPublications").html(datos);
            			return datos; 
                    },
 					error : function(e){
                        
 					}
                    });
			} 
			
		</script>
</head>
<body onload="javascript:listPublications()">
<div id="divPublications"></div>
</body>
</html>