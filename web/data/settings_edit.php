<?php @include ('../data/blocks/token_assert.php'); ?>

<form action="" method="post" name="settings" id="settings" class="form-setup">

<?php

if (!isset($_POST["Save"])) {
	$username = CONFIG_USER;
	$password = CONFIG_PASS;
	
	$servername = SERVER_NAME;
	$clockMode = CLOCK24;
	
	$dbhost = DB_SERVER;
	$dbuser = DB_USER;
	$dbpass = DB_PASS;
	$dbname = DB_NAME;
	$dbport = DB_PORT;
} else {
	$username = $_POST["username"];
	$password = $_POST["password"];
	
	$servername = $_POST["servername"];
	$clockMode = $_POST["clockmode"];
	
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

<div class="row-fluid">
	<div class="span2" style="text-align:center;">
		<h2><i class="icon-user icon-3x" style="color:#ccc;"></i></h2>
	</div>
	<div class="span10 well">
		<div class="row-fluid">
			<div class="span6">
				<fieldset>
				<label><strong>Username</strong></label>
				<input type="text" name="username" id="username" class="input-block-level" <?php echo 'value="'.$username.'"'; ?> />
				</fieldset>
			</div>
			<div class="span6">
				<fieldset>
				<label><strong>Password</strong></label>
				<input type="password" name="password" id="password" class="input-block-level" <?php echo 'value="'.$password.'"'; ?> />
				</fieldset>
			</div>
		</div>
        </div>
</div>

<div class="row-fluid">
	<div class="span2" style="text-align:center;">
		<h2><i class="icon-pencil icon-3x" style="color:#ccc;"></i></h2>
	</div>
	<div class="span10 well">  
		<div class="row-fluid">
			<div class="span6">
				<fieldset>
				<label><strong>Server name</strong></label>
				<input type="text" name="servername" id="servername" class="input-block-level" <?php echo 'value="'.$servername.'"'; ?> />
				</fieldset> 
			</div>
			<div class="span6">
				<fieldset>
				<label><strong>Clock mode</strong></label>
				<label class="radio inline">
				<input type="radio" name="clockmode" id="clockmode" value="true"<?php if ($clockMode) echo " checked"; ?>> 24h
				</label>
				<label class="radio inline">
				<input type="radio" name="clockmode" id="clockmode" value="false"<?php if (!$clockMode) echo " checked"; ?>> 12h
				</label>
				</fieldset>
			</div>
		</div>  
        </div>
</div>

<div class="row-fluid">
	<div class="span2" style="text-align:center;">
		<h2><i class="icon-cog icon-3x" style="color:#ccc;"></i></h2>
	</div>
	<div class="span10 well">
		<div class="row-fluid">
			<div class="span6">
				<div class="row-fluid">
				<div class="span8">
				<fieldset>
				<label><strong>Database Host</strong></label>
				<input type="text" name="dbhost" id="dbhost" class="input-block-level"  <?php echo 'value="'.$dbhost.'"'; ?>
				</fieldset>
				</div>
				<div class="span4">
				<fieldset>
				<label><strong>Port</strong></label>
				<input type="text" name="dbport" id="dbport" class="input-block-level" value="3306" <?php echo 'value="'.$dbport.'"'; ?> />
				</fieldset>
				</div>
				</div>
				<fieldset>
				<label><strong>Database Username</strong></label>
				<input type="text" name="dbuser" id="dbuser" class="input-block-level" <?php echo 'value="'.$dbuser.'"'; ?> />
				</fieldset> 
			</div>
			<div class="span6">
				<fieldset>
				<label><strong>Database Name</strong></label>
				<input type="text" name="dbname" id="dbname" class="input-block-level" <?php echo 'value="'.$dbname.'"'; ?> />
				<label><strong>Database Password</strong></label>
				<input type="password" name="dbpass" id="dbpass" class="input-block-level" <?php echo 'value="'.$dbpass.'"'; ?> />
				</fieldset> 
				</fieldset> 
			</div>
		</div> 
       	</div>
</div>

<div class="row-fluid">
<div class="span10 offset2">
<button class="btn btn-large btn-primary" name="Save" id="Save"><i class="icon-save"></i> Save</button>
</div>

</form>