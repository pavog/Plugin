<ul class="breadcrumb">
	<li class="active">Dashboard</li>
</ul>

<?php include ('./page/blocks/notifications.php'); ?>

<div class="row-fluid">
	<div class="span3">
	<div class="well" style="position: relative; padding: 10px; margin: 0px; background: #FFE0C2;">
		<h1 style="text-align:center;"><i class="icon-group icon-large"></i></h1>
		<h3 style="text-align:center;"><?php echo $serverObj->getAllPlayersOnlineCount(); ?> online</h3>
	</div>
	</div>
	
	
	<div class="span3">
	<div class="well" style="position: relative; padding: 10px; margin: 0px; background: #FFE0C2;">
		<h1 style="text-align:center;"><i class="icon-pencil icon-large"></i></h1>
		<h3 style="text-align:center;"><?php echo $serverObj->getAllPlayers(); ?> tracked</h3>
	</div>
	</div>
	
	
	<div class="span3">
	<div class="well" style="position: relative; padding: 10px; margin: 0px; background: #FFE0C2;">
		<h1 style="text-align:center;"><i class="icon-remove-sign icon-large"></i></h1>
		<h3 style="text-align:center;"><?php echo $serverObj->getTotalPVPKills(); ?> killed</h3>
	</div>
	</div>
	
	
	<div class="span3">
	<div class="well" style="position: relative; padding: 10px; margin: 0px; background: #FFE0C2;">
		<h1 style="text-align:center;"><i class="icon-tint icon-large"></i></h1>
		<h3 style="text-align:center;"><?php echo ($serverObj->getTotalPVPKills() + $serverObj->getTotalOtherKills()); ?> died</h3>
	</div>
	</div>
</div>
<br />
<div class="row-fluid">

	<div class="span4">
	<div class="well" style="position: relative; padding: 10px; margin: 0px;">
		<h3 style="text-align:center;">Server Statistics</h3>
		<p>
		<dl class="dl-horizontal">
			<dt><span class="label label-success"><i class="icon-bell"></i> <?php echo(QueryUtils::formatDate($serverObj->getStartupTime())); ?></span></dt>
			<dd>Startup</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt><span class="label label-warning"><i class="icon-lock"></i> <?php echo(QueryUtils::formatDate($serverObj->getLastShutdownTime())); ?></span></dt>
			<dd>Shutdown</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt><span class="label label-<?php if($online) {echo "success";} else {echo "important";} ?>"><i class="icon-calendar"></i> <?php echo $serverObj->getUptimeInSeconds(); ?></span></dt>
			<dd>Uptime</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt><span class="label label-info"><i class="icon-bullhorn"></i> <?php echo(QueryUtils::formatSecs($serverObj->getNumberOfSecondsLoggedOnTotal())); ?></span></dt>
			<dd>Gameplay</dd>
		</dl>
		</p>
	</div>
	</div>

	<div class="span4">
	<div class="well" style="position: relative; padding: 10px; margin: 0px;">
		<h3 style="text-align:center;">Player Statistics</h3>
		<p>
		<dl class="dl-horizontal">
			<dt style="width: 80px !important;"><span class="badge badge-info"><i class="icon-user"></i> <?php echo $serverObj->getAllPlayersOnlineCount(); ?></span></dt>
			<dd style="margin-left: 100px !important;">Currently online</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 80px !important;"><span class="badge badge-warning"><i class="icon-star"></i> <?php echo $serverObj->getAllPlayers(); ?></span></dt>
			<dd style="margin-left: 100px !important;">Tracked players</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 80px !important;"><span class="badge badge-success"><i class="icon-signal"></i> <?php echo $serverObj->getMaxPlayersEverOnline(); ?></span></dt>
			<dd style="margin-left: 100px !important;">Maximum players</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 80px !important;"><span class="badge badge-info"><i class="icon-fire"></i> <?php echo($serverObj->getNumberOfLoginsTotal()); ?></span></dt>
			<dd style="margin-left: 100px !important;">Number of logins</dd>
		</dl>
		</p>
	</div>
	</div>

	<div class="span4">
	<div class="well" style="position: relative; padding: 10px; margin: 0px;">
		<h3 style="text-align:center;">Distances</h3>
		<dl class="dl-horizontal">
			<dt style="width: 110px !important;"><span class="badge badge-info"><i class="icon-globe"></i> <?php echo(QueryUtils::formatDistance($serverObj->getDistanceTraveledTotal())); ?></span></dt>
			<dd style="margin-left: 130px !important;">Total distance</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 110px !important;"><span class="badge badge-info"><i class="icon-exchange"></i> <?php echo(QueryUtils::formatDistance($serverObj->getDistanceTraveledByFootTotal())); ?></span></dt>
			<dd style="margin-left: 130px !important;">By foot</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 110px !important;"><span class="badge badge-info"><i class="icon-truck"></i> <?php echo(QueryUtils::formatDistance($serverObj->getDistanceTraveledByMinecartTotal())); ?></span></dt>
			<dd style="margin-left: 130px !important;">By minecart</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 110px !important;"><span class="badge badge-info"><i class="icon-tablet"></i> <?php echo(QueryUtils::formatDistance($serverObj->getDistanceTraveledByBoatTotal())); ?></span></dt>
			<dd style="margin-left: 130px !important;">By boat</dd>
		</dl>
	</div>
	</div>

</div>
<br />
<div class="row-fluid">
	
	<div class="span4">
		<div class="well" style="position: relative; padding: 10px; margin: 0px; height: 250px !important;">
		<h3 style="text-align:center;">Blocks</h3>
		<?php include ("./page/server_blocks.php"); ?>
		</div>
		
		<br />
		
		<div class="well" style="position: relative; padding: 10px; margin: 0px;">
		<h3 style="text-align:center;">Items</h3>
		<?php include ("./page/server_items.php"); ?>
		</div>
	</div>
	
	<div class="span8">
		<div class="well" style="position: relative; padding: 10px; margin: 0px;">
		<h3 style="text-align:center;">Online Players</h3>
		<?php include ("./page/server_players.php"); ?>
		</div>
		
		<br />
		
		<div class="well" style="position: relative; padding: 10px; margin: 0px;">
		<h3 style="text-align:center;">Death Statistics</h3>
		<div style="height: 170px;"><?php include ("./page/server_kills.php"); ?></div>
		</div>
	</div>

</div>