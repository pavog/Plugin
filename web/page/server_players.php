<div class="fixed-height">

<?php

$cnt = $serverObj->getAllPlayersOnlineCount();

if($cnt > 0) {
	$playerOnlineArray = $serverObj->getAllPlayersOnline();
	foreach($playerOnlineArray as $player) {
		echo "<a href='./?view=player&uuid=".$player->getUUID()."'><img src='../skin.php?headOnly=true&displayHairs=false&ratio=4&login=".$player->getName()."' /></a>";
	}
} else {
	echo "<en>No players online</en>";
}

?>
	
</div>