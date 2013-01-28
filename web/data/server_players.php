<?php @include ('../data/blocks/token_assert.php'); ?>

<div class="fixed-height">

<?php

$cnt = $serverObj->getAllPlayersOnlineCount();

if($cnt > 0) {
	$playerOnlineArray = $serverObj->getAllPlayersOnline();
	foreach($playerOnlineArray as $player) {
		echo "<a href='./?view=player&uuid=".$player->getUUID()."'><img src='../data/util/player/head/?username=".$player->getName()."' /></a>";
	}
} else {
	echo "<div class='force-center'><em>No players online</em></div>";
}

?>
	
</div>