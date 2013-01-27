<?php
define('__INC__', dirname(__FILE__) . '/');

require_once (__INC__ . './config.php');
require_once (__INC__ . './yasp/locale/' . LOCALE . '.php');
require_once (__INC__ . './yasp/_serverObj.php');
require_once (__INC__ . './yasp/_playerObj.php');
require_once (__INC__ . './yasp/query_utils.php');
require_once (__INC__ . './yasp/paginator.php');
require_once (__INC__ . './yasp/version.php');
require_once (__INC__ . './yasp/statistician.php');

$start = microtime(true);

$sObj = new STATISTICIAN();
$serverObj = $sObj->getServer();

$online = !($serverObj->getServerStatus() == 'Offline');

if(!isset($_GET['view'])) {
	$curPage = "main";
} else {
	$curPage = $_GET['view'];
}

?>


<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title><?php echo (SERVER_NAME); ?></title>
	
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="">
	<meta name="author" content="">

	<link href="./src/css/bootstrap.css" rel="stylesheet">
	<link href="./src/css/style.css" rel="stylesheet">
	<link href="./src/css/font-awesome.css" rel="stylesheet">
	<link href="./src/css/tablesorter/style.css" rel="stylesheet">

	<!--[if lt IE 9]>
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
</head>

<body>

<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
	<div class="container-fluid page-width force-center">
		<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		</a>
		<a class="brand" href="../"><img src="./src/img/logo.png" style="width:25px; height: 25px; margin-right: 15px;" /><?php echo (SERVER_NAME); ?></a>
	<div class="pull-right" style="padding-top: 10px;">
		<?php
			if ($online) echo "Server status: <span class='label label-success'>Online</span>";
			else echo "Server status: <span class='label label-important'>Offline</span>";
		?>
	</div>
	</div>
</div>

</div>

<div class="container page-width">
	<div class="row-fluid">
		<div class="span2">
			<div class="well sidebar-nav">
				<ul class="nav nav-list">
					<li class="nav-header">General</li>
					<li<?php if ($curPage == "main") echo " class='active'"; ?>><a href="../"><i class="icon-star"></i> Dashboard</a></li>
					<li class="divider"></li>
					<li class="nav-header">Statistics</li>
					<li<?php if ($curPage == "player") echo " class='active'"; ?>><a href="../?view=player"><i class="icon-group"></i> Players</a></li>
					<li<?php if ($curPage == "world") echo " class='active'"; ?>><a href="../?view=world"><i class="icon-book"></i> World</a></li>
					<li<?php if ($curPage == "kills") echo " class='active'"; ?>><a href="../?view=kills"><i class="icon-tint"></i> Death Log</a></li>
					<li class="divider"></li>
					<li class="nav-header">Other</li>
					<li<?php if ($curPage == "settings") echo " class='active'"; ?>><a href="../?view=settings""><i class="icon-cogs"></i> Settings</a></li>
				</ul>
			</div><!--/.well -->
		</div><!--/span-->
		<div class="span10">
			
		<?php
				
		switch ($curPage) {
				
			case 'player':
				include ('./page/player.php');
				break;
			case 'world':
				include ('./page/world.php');
				break;
			case 'kills':
				include ('./page/deaths.php');
				break;
			case 'settings':
				include ('./page/settings.php');
				break;
			case 'main':
			default:
                		include ('./page/server.php');
				break;
			
		}
		
		?>
				
		</div><!--/span-->
	</div><!--/row-->
	
	<hr>
	
	<footer>
		<p class="pull-right">&copy; 2013 Yet Another Statistics Plugin<br />Based on <a href="http://dev.bukkit.org/server-mods/statisticianv2/" target="_blank">Statistician 2</a> technology</p>
	</footer>

</div><!--/.fluid-container-->

<script src="./src/js/jquery.js" type="text/javascript"></script>
<script src="./src/js/bootstrap.js" type="text/javascript"></script>
<script src="./src/js/jquery.tablesorter.js" type="text/javascript"></script> 
<script type="text/javascript">
	$(document).ready(function() {
	$(".table").tablesorter(
		{sortList: [[0,0]]}
	);
	} 
); 
</script>

</body>
</html>