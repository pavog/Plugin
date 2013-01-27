<!DOCTYPE HTML>

<html>
<head>
	<title>Getting Started - Statistician</title>
	
	<link href="../src/css/style.css" rel="stylesheet" media="screen">	
	<link href="../src/css/bootstrap.min.css" rel="stylesheet" media="screen">
    	<link href="../src/css/bootstrap-responsive.css" rel="stylesheet">
	
	<style type="text/css">
	
	body {
		padding-top: 40px;
	        padding-bottom: 40px;
	        background-color: #f5f5f5;
	}
	
	.form-setup {
	        max-width: 800px;
	        padding: 19px 29px 29px;
	        margin: 0 auto 20px;
	        background-color: #fff;
	        border: 1px solid #e5e5e5;
	        -webkit-border-radius: 5px;
	           -moz-border-radius: 5px;
	                border-radius: 5px;
	        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
	           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
	                box-shadow: 0 1px 2px rgba(0,0,0,.05);
	}

    	</style>
    	<meta charset="utf-8">
    	<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<div class="container">
		<form action="" method="post" name="install" id="install" class="form-setup">
		
		<?php
		$dbhost = "localhost";
		$dbport = 3306;
	
		if (isset($_POST["submit"])) {
			
			$servername = $_POST["servername"];
			
			$dbhost = $_POST["dbhost"];
			$dbuser = $_POST["dbuser"];
			$dbpass = $_POST["dbpass"];
			$dbname = $_POST["dbname"];
			$dbport = $_POST["dbport"];
			
			$saveFile = false;
			
			$link = @mysql_connect($dbhost.":".$dbport, $dbuser, $dbpass);
			if ($link) {
				$db_selected = @mysql_select_db($dbname, $link);
				if ($db_selected) {
					echo "<div class='alert alert-success'>";
					echo "<button type='button' class='close' data-dismiss='alert'>&times;</button>";
					echo "<strong>Success!</strong> Data saved successfully. Do not forget to delete <em>install.php</em>!";
					echo "</div>";
					$saveFile = true;
				} else {
					echo "<div class='alert alert-error'>";
					echo "<button type='button' class='close' data-dismiss='alert'>&times;</button>";
					echo "<strong>Error!</strong> The database does not exist, or the user does not have the permission to connect to it";
					echo "</div>";
				}
			} else {
				echo "<div class='alert alert-error'>";
				echo "<button type='button' class='close' data-dismiss='alert'>&times;</button>";
				echo "<strong>Error!</strong> Could not establish connection to the database. Check the credentials!";
				echo "</div>";
			}
			
			if ($saveFile) {
			
				$string = '<?php 
				
				define("DB_SERVER"  , "'. $dbhost. '");
				define("DB_USER"    , "'. $dbuser. '");
				define("DB_PASSWORD", "'. $dbpass. '");
				define("DB_NAME"    , "'. $dbname. '");
				define("DB_PORT"    , "'. $dbport. '");
				
				define("SERVER_NAME", "'. $servername. '");
				define("CLOCK24", true);
				
				define("USE_MEGAMETERS", true);
				define("USE_SKINVIEWER", false);
				    
				define("LOCALE", "en");
				define("TIMEZONE", "");
				?>';
				
				$fp = fopen("./statistician/config.php", "w");
				
				fwrite($fp, $string);
				
				fclose($fp);
			}
				
		}
	
	?>
		
        		<h1>Statistician :: Getting Started</h1>
        		        		
        		<dl class="dl-horizontal">
        			<dt>Server Name</dt>
				<dd><input type="text" name="servername" id="servername" class="input-block-level" <?php echo 'value="'.$servername.'"'; ?> /></dd>
        		</dl>
        		
        		<h3>Database Setup</h3>
        		<dl class="dl-horizontal">
        			<dt>DB Host</dt>
				<dd><input type="text" name="dbhost" id="dbhost" class="input-block-level"  <?php echo 'value="'.$dbhost.'"'; ?> /></dd>
        		</dl>
        		<dl class="dl-horizontal">
        			<dt>DB Name</dt>
				<dd><input type="text" name="dbname" id="dbname" class="input-block-level" <?php echo 'value="'.$dbname.'"'; ?> /></dd>
        		</dl>
        		<dl class="dl-horizontal">
        			<dt>DB User</dt>
				<dd><input type="text" name="dbuser" id="dbuser" class="input-block-level" <?php echo 'value="'.$dbuser.'"'; ?> /></dd>
        		</dl>
        		<dl class="dl-horizontal">
        			<dt>DB Pass</dt>
				<dd><input type="password" name="dbpass" id="dbpass" class="input-block-level" <?php echo 'value="'.$dbpass.'"'; ?> /></dd>
        		</dl>
        		<dl class="dl-horizontal">
        			<dt>DB Port</dt>
				<dd><input type="text" name="dbport" id="dbport" class="input-block-level" value="3306" <?php echo 'value="'.$dbport.'"'; ?> /></dd>
        		</dl>
        		
       			<center><button class="btn btn-large btn-primary"  name="submit" id="submit">Submit</button></center>
      		</form>
	</div> <!-- /container -->


	<script src="../src/js/jquery.js"></script>
	<script src="../src/js/bootstrap.js"></script>
</body>
</html>