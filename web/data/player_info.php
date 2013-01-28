<?php @include ('../data/blocks/token_assert.php'); ?>

<?php
$_player = $serverObj->getPlayerByName($_GET['username']);
if (!isset($_player)) { $_player = $serverObj->getPlayer($_GET['uuid']); }

$_playerName = $_player->getName();
$_online = $_player->isOnline();
?>

<style type="text/css">
.player-stats {
        position: relative;
}

.player-stats:after {
    	content: "";
	background: url('../data/util/player/skin/?username=<?php echo $_playerName; ?>') no-repeat !important;
	opacity: 0.2;
	top: 0;
	left: 20px;
	bottom: 0;
	right: -20px;
	position: absolute;
	z-index: -1;  
}
</style>

<div class="row-fluid">

<div class="span12 player-stats">

	<div class="search pull-right">
		<form action="./" method="get" class="form-search" style="padding-top: 15px;">
			<input type="hidden" name="view" value="player" />
			
	    		<div class="input-append">
				<input type="text" name="username" placeholder="Search" class="span2" style="width: 150px;" />
				<input type="submit" value="Search" class="btn">
			</div>
		</form>
	</div>

	<h1>
	<?php 
	echo "<img src='../data/util/player/head/?username=".$_playerName."' /> ".$_playerName." ";
	if($_online) echo "<span class='label label-success'>In-Game</span>";
	else echo "<span class='label label-important'>Offline</span>";
	?>
	</h1>
	
	<p><strong>Joined on:</strong> <?php echo(QueryUtils::formatDate($_player->getFirstLogin())); ?></p>
	<p><strong>Last seen:</strong> <?php echo(QueryUtils::formatDate($_player->getLastLogin())); ?></p>
	<p><strong>Playtime:</strong> <?php echo(QueryUtils::formatSecs($_player->getNumberOfSecondsLoggedOn())); ?></p>
	
			
	<div class="row-fluid" style="width:100% !important;">
		<div class="span4" style="width: 30% !important;">
			<h3>Distances</h4>
			<p><strong>Travelled:</strong> <?php echo $_player->getDistanceTraveledTotal(); ?> meters</p>
			<p><strong>Walked:</strong> <?php echo $_player->getDistanceTraveledByFoot(); ?> meters</p>
			<p><strong>Minecarted:</strong> <?php echo $_player->getDistanceTraveledByMinecart(); ?> meters</p>
			<p><strong>Boated:</strong> <?php echo $_player->getDistanceTraveledByBoat(); ?> meters</p>
			<p><strong>Piggybacked:</strong> <?php echo $_player->getDistanceTraveledByPig(); ?> meters</p>
		</div>
		
		<div class="span4" style="width: 30% !important;">
			<h3>Blocks</h3>
			<p><strong>Total Blocks Placed:</strong> <?php echo($_player->getBlocksPlacedTotal()); ?> Blocks</p>
			<p><strong>Most Popular Block Placed:</strong>
			
			<?php
				$block = $_player->getBlocksMostPlaced();
				echo QueryUtils::getResourceNameById($block['block_id']);
				echo " (".$block['sum'].")";
			?>
			
			</p>
			<p><strong>Total Blocks Destroyed:</strong> <?php echo($_player->getBlocksDestroyedTotal()); ?> Blocks</p>
			<p><strong>Most Popular Block Destroyed:</strong>
			
			<?php
				$block = $_player->getBlocksMostDestroyed();
				echo QueryUtils::getResourceNameById($block['block_id']);
				echo " (".$block['sum'].")";
			?>
			
			</p>
		</div>
		
		<div class="span4" style="width: 30% !important;">
			<h3>Items</h3>
			<p><strong>Total Items Picked Up:</strong> <?php echo($_player->getPickedUpTotal()); ?> Items</p>
			<p><strong>Most Popular Item Picked Up:</strong>
			
			<?php
				$block = $_player->getMostPickedUp();
				echo QueryUtils::getResourceNameById($block['block_id']);
				echo " (".$block['sum'].")";
			?>
			
			</p>
			<p><strong>Total Items Dropped:</strong> <?php echo($_player->getDroppedTotal()); ?> Items</p>
			<p><strong>Most Popular Item Dropped:</strong>
			
			<?php
				$block = $_player->getMostDropped();
				echo QueryUtils::getResourceNameById($block['block_id']);
				echo " (".$block['sum'].")";
			?>
			
			</p>
		</div>
		
	</div>
	<div class="row-fluid" style="width:100% !important;">
		
		<div class="span4" style="width: 30% !important;">
			<h3>PvP Stats</h3>
			<p><strong>Total Kills:</strong> <?php echo($_player->getPlayerKillTable() ? count($_player->getPlayerKillTable()) : 0); ?></p>
			<p><strong>Total Deaths:</strong> <?php echo($_player->getPlayerDeathTable() ? count($_player->getPlayerDeathTable()) : 0); ?></p>
			<p><strong>Favorite Weapon:</strong> 
			
			<?php
			$weapon = $_player->getMostDangerousWeapon();
			echo(QueryUtils::getResourceNameById($weapon['name']));
			echo " (".$weapon['count'].")";
			?>
	
			</p>
			
			<br />
			<p><strong>PvP Kills:</strong> <?php echo($_player->getPlayerKillTablePVP() ? count($_player->getPlayerKillTablePVP()) : 0); ?></p>
			<p><strong>PvP Deaths:</strong> <?php echo($_player->getPlayerDeathTablePVP() ? count($_player->getPlayerDeathTablePVP()) : 0); ?></p>
			<p><strong>Most Killed Player:</strong> 
			
			<?php
			$ar = $_player->getMostKilledPVP();
			$player = $serverObj->getPlayer($ar['name']);
			if($ar['count'] > 0) {
				echo "<a href='?view=player&username=".$player->getName()."'>".$player->getName()."</a> (".$ar['count']." times)";
			} else echo "<em>None!</em>";
			?>
			
			</p>
			<p><strong>Sworn Enemy:</strong> 
			
			<?php
			$ar = $_player->getMostKilledByPVP();
			$player = $serverObj->getPlayer($ar['name']);
			if($ar['count'] > 0) {
				echo "<a href='?view=player&username=".$player->getName()."'>".$player->getName()."</a> (".$ar['count']." times)";
			} else echo "<em>None!</em>";
			?>
			
			</p>
		</div>
		
		<div class="span4" style="width: 30% !important;">
			<h3>PvE Stats</h3>
			<p><strong>PVE Kills:</strong> <?php echo($_player->getPlayerKillTablePVE() ? count($_player->getPlayerKillTablePVE()) : 0); ?></p>
			<p><strong>PVE Deaths:</strong> <?php echo($_player->getPlayerDeathTablePVE() ? count($_player->getPlayerDeathTablePVE()) : 0); ?></p>
			<p><strong>Most Killed Creature:</strong> 
			
			<?php
			echo(QueryUtils::getCreatureNameById($_player->getPlayerMostKilledPVECreature()));
			?>
			
			</p>
			<p><strong>Most Dangerous Creature:</strong> 
			
			<?php
			echo(QueryUtils::getCreatureNameById($_player->getPlayerMostDangerousPVECreature()));
			?>
			
			</p>
		</div>
		
		<div class="span4" style="width: 30% !important;">
			<h3>Other deaths</h3>
			<p><strong>Other Type Deaths:</strong> 
			<?php echo($_player->getPlayerDeathTableOther() ? count($_player->getPlayerDeathTableOther()) : 0); ?>
			</p>
			<p><strong>Falling Deaths:</strong> 
			<?php echo($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Fall")) ? count($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Fall"))) : 0); ?>
			</p>
			<p><strong>Drowning Deaths:</strong> 
			<?php echo($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Drowning")) ? count($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Drowning"))) : 0); ?>
			</p>
			<p><strong>Suffocation Deaths:</strong> 
			<?php echo($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Suffocation")) ? count($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Suffocation"))) : 0); ?>
			</p>
			<p><strong>Lightning Deaths:</strong> 
			<?php echo($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Lightening")) ? count($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Lightening"))) : 0); ?>
			</p>
			<p><strong>Lava Deaths:</strong> 
			<?php echo($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Lava")) ? count($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Lava"))) : 0); ?>
			</p>
			<p><strong>Fire Deaths:</strong> 
			<?php echo($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Fire")) ? count($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Fire"))) : 0); ?>
			</p>
			<p><strong>Fire Tick Deaths:</strong> 
			<?php echo($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Fire Tick")) ? count($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Fire Tick"))) : 0); ?>
			</p>
			<p><strong>Explosion Deaths:</strong> 
			<?php echo($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Entity Explosion")) ? count($_player->getPlayerDeathTableType(QueryUtils::getKillTypeIdByName("Entity Explosion"))) : 0); ?>
			</p>
		</div>
	</div>
</div>

</div>