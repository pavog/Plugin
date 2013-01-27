<?php
	$totalKills = $serverObj->getTotalPVPKills();
	$totalDeaths = $serverObj->getTotalKills();
	$mostDangerousWeapon = $serverObj->getMostDangerousWeapon();
	$weaponMostDangerous = QueryUtils::getResourceNameById($mostDangerousWeapon['name']);
	$weaponMostDangerous = str_replace(" ", "", $weaponMostDangerous);
	$weaponMostDangerous = str_replace("'", "f", $weaponMostDangerous);
	$weaponMostDangerous = strtolower($weaponMostDangerous);
	

	$totalPVEKills = $serverObj->getTotalPVEKills();
	$mostDangerousCreature = $serverObj->getMostDangerousPVECreature();
	$creatureMostDangerous = QueryUtils::getCreatureNameById($mostDangerousCreature['name']);
	$creatureMostDangerous = str_replace(" ", "", $creatureMostDangerous);
	$creatureMostDangerous = str_replace("'", "f", $creatureMostDangerous);
	$creatureMostDangerous = strtolower($creatureMostDangerous);
	
	$mostKilledCreature = $serverObj->getMostKilledPVECreature();
	$creatureMostKilled = QueryUtils::getCreatureNameById($mostKilledCreature['name']);
	$creatureMostKilled = str_replace(" ", "", $creatureMostKilled);
	$creatureMostKilled = str_replace("'", "f", $creatureMostKilled);
	$creatureMostKilled = strtolower($creatureMostKilled);
	
	$totalPVPKills = $serverObj->getTotalPVPKills();
	$mostKillerPlayer = $serverObj->getMostKillerPVP();
	$playerMostKiller = new Player($mostKillerPlayer['name']);
	$mostKilledPlayer = $serverObj->getMostKilledPVP();
	$playerMostKilled = new Player($mostKilledPlayer['name']);
	

?>

<div classe="fluid-row">
	<div class="span4">
		<dl class="dl-horizontal">
			<dt style="width: 60px !important;"><span class="badge badge-success" style="padding: 10px 15px;"><?php echo $totalKills; ?></span></dt>
			<dd style="margin-left: 80px !important;">Total Kills</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 60px !important;"><span class="badge badge-success" style="padding: 10px 15px;"><?php echo $totalDeaths; ?></span></dt>
			<dd style="margin-left: 80px !important;">Total Deaths</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 60px !important;"><span class="badge badge-important" style="padding: 10px 15px;"><?php echo "<img src='../src/img/block/".$weaponMostDangerous.".png' style='width:20px;height:20px;' title='".$weaponMostDangerous."' />"; ?></span></dt>
			<dd style="margin-left: 80px !important;">Best Weapon</dd>
		</dl>
	</div>

	<div class="span4">
		<dl class="dl-horizontal">
			<dt style="width: 60px !important;"><span class="badge badge-success" style="padding: 10px 15px;"><?php echo $totalPVEKills; ?></span></dt>
			<dd style="margin-left: 80px !important;">PvE Kills</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 60px !important;"><span class="badge badge-important" style="padding: 9px 10px;"><?php echo "<img src='../src/img/mob/".$creatureMostDangerous.".png' style='width:20px;height:20px;' style='width:20px;height:20px;' title='".$creatureMostDangerous."' />"; ?></span></dt>
			<dd style="margin-left: 80px !important;">Most Dangrous</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 60px !important;"><span class="badge badge-important" style="padding: 9px 10px;"><?php echo "<img src='../src/img/mob/".$creatureMostKilled.".png' style='width:20px;height:20px;' style='width:20px;height:20px;' title='".$creatureMostKilled."' />"; ?></span></dt>
			<dd style="margin-left: 80px !important;">Most Killed</dd>
		</dl>
		
	</div>
	
	<div class="span4">
		<dl class="dl-horizontal">
			<dt style="width: 80px !important;"><span class="badge badge-success" style="padding: 10px 15px;"><?php echo $totalPVPKills; ?></span></dt>
			<dd style="margin-left: 100px !important;">PvP Kills</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 80px !important;"><span class="badge badge-important" style="padding: 10px 10px;"z><a href="./?view=player&username=<?php echo $playerMostKiller->getName(); ?>"><img src="./yasp/skin/head/?username=<?php echo $playerMostKiller->getName(); ?>" style='width:20px;height:20px;' alt="<?php echo $playerMostKiller->getName(); ?>" title="<?php echo $playerMostKiller->getName(); ?>"/></a></span></dt>
			<dd style="margin-left: 100px !important;">Most Kills</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 80px !important;"><span class="badge badge-important" style="padding: 10px 10px;"><a href="./?view=player&username=<?php echo $playerMostKilled->getName(); ?>"><img src="./yasp/skin/head/?username=<?php echo $playerMostKilled->getName(); ?>" style='width:20px;height:20px;' alt="<?php echo $playerMostKilled->getName(); ?>" title="<?php echo $playerMostKilled->getName(); ?>"/></a></span></dt>
			<dd style="margin-left: 100px !important;">Most Deaths</dd>
		</dl>
	</div>
</div>