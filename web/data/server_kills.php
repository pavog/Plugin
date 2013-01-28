<?php @include ('../data/blocks/token_assert.php'); ?>

<?php
	$totalKills = $serverObj->getTotalPVPKills();
	$totalDeaths = $serverObj->getTotalKills();
	$mostDangerousWeapon = $serverObj->getMostDangerousWeapon();
	$weaponMostDangerous = QueryUtils::getResourceNameById($mostDangerousWeapon['name']);
	$weaponMostDangerousImg = str_replace(" ", "%20", $weaponMostDangerous);
	

	$totalPVEKills = $serverObj->getTotalPVEKills();
	$mostDangerousCreature = $serverObj->getMostDangerousPVECreature();
	$creatureMostDangerous = QueryUtils::getCreatureNameById($mostDangerousCreature['name']);
	$creatureMostDangerousImg = str_replace(" ", "%20", $creatureMostDangerous);
	
	$mostKilledCreature = $serverObj->getMostKilledPVECreature();
	$creatureMostKilled = QueryUtils::getCreatureNameById($mostKilledCreature['name']);
	$creatureMostKilledImg = str_replace(" ", "%20", $creatureMostKilled);
	
	$totalPVPKills = $serverObj->getTotalPVPKills();
	$mostKillerPlayer = $serverObj->getMostKillerPVP();
	$playerMostKiller = new Player($mostKillerPlayer['name']);
	$mostKilledPlayer = $serverObj->getMostKilledPVP();
	$playerMostKilled = new Player($mostKilledPlayer['name']);
	

?>

<div class="fluid-row">
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
			<dt style="width: 60px !important;"><span class="badge badge-important" style="padding: 10px 15px;"><?php echo "<img src='../data/util/block/?name=".$weaponMostDangerousImg."' style='width:20px;height:20px;' title='".$weaponMostDangerous."' alt='".$weaponMostDangerous."' />"; ?></span></dt>
			<dd style="margin-left: 80px !important;">Best Weapon</dd>
		</dl>
	</div>

	<div class="span4">
		<dl class="dl-horizontal">
			<dt style="width: 60px !important;"><span class="badge badge-success" style="padding: 10px 15px;"><?php echo $totalPVEKills; ?></span></dt>
			<dd style="margin-left: 80px !important;">PvE Kills</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 60px !important;"><span class="badge badge-important" style="padding: 9px 10px;"><?php echo "<img src='../data/util/mob/?name=".$creatureMostDangerousImg."' style='width:20px;height:20px;' title='".$creatureMostDangerous."' alt='".$creatureMostDangerous."' />"; ?></span></dt>
			<dd style="margin-left: 80px !important;">Most Dangrous</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 60px !important;"><span class="badge badge-important" style="padding: 9px 10px;"><?php echo "<img src='../data/util/mob/?name=".$creatureMostKilledImg."' style='width:20px;height:20px;' title='".$creatureMostKilled."' alt='".$creatureMostKilled."' />"; ?></span></dt>
			<dd style="margin-left: 80px !important;">Most Killed</dd>
		</dl>
		
	</div>
	
	<div class="span4">
		<dl class="dl-horizontal">
			<dt style="width: 80px !important;"><span class="badge badge-success" style="padding: 10px 15px;"><?php echo $totalPVPKills; ?></span></dt>
			<dd style="margin-left: 100px !important;">PvP Kills</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 80px !important;"><span class="badge badge-important" style="padding: 10px 10px;"><a href="./?view=player&amp;username=<?php echo $playerMostKiller->getName(); ?>"><img src="../data/util/player/head/?username=<?php echo $playerMostKiller->getName(); ?>" style='width:20px;height:20px;' alt="<?php echo $playerMostKiller->getName(); ?>" title="<?php echo $playerMostKiller->getName(); ?>"/></a></span></dt>
			<dd style="margin-left: 100px !important;">Most Kills</dd>
		</dl>
		<dl class="dl-horizontal">
			<dt style="width: 80px !important;"><span class="badge badge-important" style="padding: 10px 10px;"><a href="./?view=player&amp;username=<?php echo $playerMostKilled->getName(); ?>"><img src="../data/util/player/head/?username=<?php echo $playerMostKilled->getName(); ?>" style='width:20px;height:20px;' alt="<?php echo $playerMostKilled->getName(); ?>" title="<?php echo $playerMostKilled->getName(); ?>"/></a></span></dt>
			<dd style="margin-left: 100px !important;">Most Deaths</dd>
		</dl>
	</div>
</div>